package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorCarroFavorito;
import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.*;
import es.unizar.es.foodnet.model.repository.*;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CarroFavoritoTest {

    @Autowired
    private RepositorioCarroFavorito repositorioCarroFavorito;
    @Autowired
    private RepositorioPedido repositorioPedido;
    @Autowired
    private RepositorioSupermercado repositorioSupermercado;
    @Autowired
    private RepositorioCategoria repositorioCategoria;
    @Autowired
    private RepositorioProducto repositorioProducto;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private ControladorUsuario cu;
    @Autowired
    private ControladorCarroFavorito ccf;

    private static Usuario user;
    private static ArrayList<ProductoCarro> carroProductos;

    @Before
    public void before(){
        repositorioCarroFavorito.deleteAll();
        repositorioPedido.deleteAll();
        repositorioUsuario.deleteAll();
        repositorioProducto.deleteAll();
        repositorioCategoria.deleteAll();
        repositorioSupermercado.deleteAll();

        user = new Usuario("test", "pedidos", "testPedidos@gmail.com", "pedidos", "zaragoza");
        cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));

        Categoria categoria = new Categoria("Lacteos");
        Supermercado supermercado = new Supermercado("supermercado1");
        repositorioCategoria.save(categoria);
        repositorioSupermercado.save(supermercado);
        repositorioProducto.save(new Producto(categoria, supermercado, "Yogurt", 0.80, "http://placehold.it/650x450",""));

        carroProductos = new ArrayList<>();
        carroProductos.add(new ProductoCarro(repositorioProducto.findByNombre("Yogurt"), 1));

        Usuario user2 = new Usuario("test2", "pedidos", "test2Pedidos@gmail.com", "pedidos", "zaragoza");
        cu.registrarUsuario(user2, Mockito.mock(RedirectAttributes.class));

        CarroFavorito cf = new CarroFavorito("Carro",user2,carroProductos);
        repositorioCarroFavorito.save(cf);
    }

    @After
    public void after(){

    }

    /**
     * Test contra la BBDD de la creacion de un nuevo carro favorito
     */
    @Test
    public void carroFavoritoNuevo(){
        int numCarrosFavoritos = repositorioCarroFavorito.findAll().size();
        CarroFavorito cf = new CarroFavorito("NombreCarro",user,carroProductos);
        repositorioCarroFavorito.save(cf);

        assertEquals(repositorioCarroFavorito.findAll().size(),numCarrosFavoritos+1);

        CarroFavorito cf2 = repositorioCarroFavorito.findByNombre("NombreCarro");

        assertNotNull(cf2);
        assertEquals(cf2.getNombre(),cf.getNombre());
        //No comprobamos item a item del carro, damos por hecho de que en esta sub-comprobacion si tiene el mismo
        //numero de items, es la misma lista
        assertEquals(cf2.getProductos().size(),cf.getProductos().size());
        assertEquals(cf2.getUsuario().getEmail(),cf.getUsuario().getEmail());

    }

    /**
     * Test contra la BBDD de intentar insertar un nuevo carro favorito con el mismo nombre que uno ya existente
     */
    @Test (expected = DuplicateKeyException.class)
    public void carroFavoritoRepetido(){
        CarroFavorito cf = new CarroFavorito("NombreCarro",user,carroProductos);
        repositorioCarroFavorito.save(cf);

        CarroFavorito cf2 = new CarroFavorito("NombreCarro",user,carroProductos);
        repositorioCarroFavorito.save(cf2);
    }

    /**
     * Test contra el controlador encargado de crear un nuevo carro favorito
     */
    @Test
    public void carroFavoritoNuevoControlador(){
        assertNull(repositorioCarroFavorito.findByNombre("NombreCarro"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("carroProductos", carroProductos);
        ccf.anadirCarroFavorito("NombreCarro", request, new MockHttpServletResponse());

        assertNotNull(repositorioCarroFavorito.findByNombre("NombreCarro"));
    }



    /**
     * Test que comprueba que un nuevo usuario no debe devolver ningún carroFavorito.
     */
    @Test
    public void noExistenCarrosFavoritos () {
        //testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        ccf.cargarFavoritos(model,request);
        assertEquals(0, ((List<CarroFavorito>) model.get("carrosFavoritos")).size());
    }

    /**
     * Test que añade un carroFavorito y comprueba que ha aumentado el número de carros favoritos del
     * del usuario.
     */
    @Test
    public void cargarFavoritosTest () {

        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        ccf.cargarFavoritos(model, request);
        int favoritosInicial = ((List<CarroFavorito>) model.get("carrosFavoritos")).size();


        request.getSession().setAttribute("carroProductos", carroProductos);

        ccf.anadirCarroFavorito("NombreCarro", request, new MockHttpServletResponse());

        ccf.cargarFavoritos(model, request);
        assertEquals(favoritosInicial + 1, ((List<CarroFavorito>) model.get("carrosFavoritos")).size());
    }

    /**
     * Test que elimina un carroFavorito y comprueba que el número de carros favoritos del
     * del usuario ha disminuido.
     */
    @Test
    public void eliminarFavoritosTest() {
        CarroFavorito cf = repositorioCarroFavorito.findByNombre("Carro");
        assertNotNull(cf);
        int favoritosInicial = repositorioCarroFavorito.findAll().size();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("user", cf.getUsuario());
        ccf.borrarCarroFavorito(response,request,cf.getNombre());
        assertEquals(favoritosInicial, repositorioCarroFavorito.findAll().size() + 1);
    }


    /**
     * Test que elimina un carroFavorito desde un usuario al que no le pertenece y comprueba que el número de carros favoritos del
     * del usuario no ha disminuido.
     */
    @Test
    public void eliminarFavoritosOtroUsuarioTest() {
        CarroFavorito cf = repositorioCarroFavorito.findByNombre("Carro");
        assertNotNull(cf);
        int favoritosInicial = repositorioCarroFavorito.findAll().size();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Usuario usuario = repositorioUsuario.findByEmail("testPedidos@gmail.com");
        assertNotNull(usuario);
        request.getSession().setAttribute("user", usuario);
        ccf.borrarCarroFavorito(response,request,cf.getNombre());
        assertEquals(favoritosInicial, repositorioCarroFavorito.findAll().size());
    }


}
