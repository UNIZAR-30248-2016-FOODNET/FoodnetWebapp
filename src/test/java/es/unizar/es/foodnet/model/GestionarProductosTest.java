package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorGestionarProductos;
import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.Password;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class GestionarProductosTest {
    @Autowired
    private RepositorioProducto repositorioProducto;
    @Autowired
    private RepositorioCategoria repositorioCategoria;
    @Autowired
    private RepositorioSupermercado repositorioSupermercado;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private ControladorGestionarProductos cgp;
    @Autowired
    private ControladorUsuario cu;

    private static boolean inicializado;
    private static int testCompletados;


    @Before
    public void inicializar () throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (!inicializado) {
            repositorioUsuario.deleteAll();
            repositorioCategoria.deleteAll();
            repositorioSupermercado.deleteAll();
            inicializado = true;

            repositorioCategoria.save(new Categoria("categoria1"));
            repositorioSupermercado.save(new Supermercado("supermercado1"));
            repositorioProducto.save(new Producto(repositorioCategoria.findByNombre("categoria1"),
                    repositorioSupermercado.findByNombre("supermercado1"), "producto6", 10, "http://placehold.it/650x450",
                    "El mismo producto pero mas caro."));
            repositorioProducto.save(new Producto(repositorioCategoria.findByNombre("categoria1"),
                    repositorioSupermercado.findByNombre("supermercado1"), "producto8", 10, "http://placehold.it/650x450",
                    "El mismo producto pero mas caro."));


            Usuario user = new Usuario("Administrador","Foodnet","admin@admin.com","admin","Direccion Admin",true);
            cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));

            user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
            cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));
        }
    }

    @After
    public void finalizar () {
        if (testCompletados >= 8) {
            inicializado = false;
        }
    }


    /**
     * Test para comprobar que gestionar productos para el usuario administrador inserta todos los productos en Model
     * Y devuele la página de gestión de productos
     */
    @Test
    public void gestionarProductosAdminTest(){
        testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        int numProductos = repositorioProducto.findAll().size();
        Usuario user = repositorioUsuario.findByEmail("admin@admin.com");
        request.getSession().setAttribute("user", user);
        String page = cgp.gestionarProductos(request, Mockito.mock(RedirectAttributes.class),model);
        assertTrue(numProductos == ((List<Producto>) model.get("listaProductos")).size());
        assertEquals(page,"gestionarProductos");

    }

    /**
     * Test para comprobar que gestionar productos para el usuario comun no inserta nada en Model
     * y redirije al catalogo
     */
    @Test
    public void gestionarProductosUserTest(){
        testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        int numProductos = repositorioProducto.findAll().size();
        Usuario user = repositorioUsuario.findByEmail("pepe@gmail.com");
        request.getSession().setAttribute("user", user);
        String page = cgp.gestionarProductos(request, Mockito.mock(RedirectAttributes.class),model);
        assertNull(model.get("listaProductos"));
        assertEquals(page,"redirect:/");
    }

    /**
     * Test para comprobar que getSupermercados imprime una lista con todos los supermercados
     */
    @Test
    public void getSupermercadosTest() throws UnsupportedEncodingException {
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        cgp.getSupermercados(response);
        assertEquals(1 + repositorioSupermercado.findAll().size(),response.getContentAsString().split("\n").length);
    }


    /**
     * Test para comprobar que getCategorias imprime una lista con todas las categorias
     */
    @Test
    public void getCategoriasTest() throws UnsupportedEncodingException {
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        cgp.getCategorias(response);
        assertEquals(1 + repositorioCategoria.findAll().size(),response.getContentAsString().split("\n").length);
    }



    /**
     * Test para comprobar que addNuevoProducto añade un nuevo producto a la base de datos
     */
    @Test
    public void addNuevoProductoTest(){
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        int producosIniciales = repositorioProducto.findAll().size();
        cgp.addNuevoProducto(response,"producto5","supermercado1","categoria1","2,5€","descripcion");
        assertEquals(producosIniciales+1,repositorioProducto.findAll().size());
    }


    /**
     * Test para comprobar que addNuevoProducto no añade un nuevo producto a la base de datos,
     *  si no existe la categoría o el supermercado de ese producto
     *
     */
    @Test
    public void addNuevoProductoErroneoTest(){
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        int producosIniciales = repositorioProducto.findAll().size();
        //supermercado 12 no existe
        cgp.addNuevoProducto(response,"producto5","supermercado12","categoria1","2,5€","descripcion");
        assertEquals(producosIniciales,repositorioProducto.findAll().size());
        cgp.addNuevoProducto(response,"producto5","supermercado1","categoria12","2,5€","descripcion");
        assertEquals(producosIniciales,repositorioProducto.findAll().size());
    }



    /**
     * Test para comprobar que actualizarProducto actualiza un producto de la base de datos
     */
    @Test
    public void actualizarProductoTest(){
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        Producto p = repositorioProducto.findByNombre("producto6");

        cgp.actualizarProducto(response,p.getId(),"producto7","supermercado1","categoria1","2,5€");
        Producto p2 = repositorioProducto.findByNombre("producto7");
        assertEquals(p2.getNombre(), "producto7");
        assertEquals(p2.getCategoria().getNombre(), "categoria1");
        assertEquals(p2.getSupermercado().getNombre(), "supermercado1");
        assertTrue(p2.getPrecio() == 2.5);
    }

    /**
     * Test para comprobar que actualizarProducto actualiza un producto de la base de datos
     */
    @Test
    public void borrarProductoTest(){
        testCompletados++;
        MockHttpServletResponse response = new MockHttpServletResponse();
        Producto p = repositorioProducto.findByNombre("producto8");
        int producosIniciales = repositorioProducto.findAll().size();
        cgp.borrarProducto(response,p.getId());
        assertEquals(producosIniciales-1,repositorioProducto.findAll().size());
    }

















}
