package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorCarro;
import es.unizar.es.foodnet.controller.ControladorPedido;
import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Pedido;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioPedido;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PedidoTest {

    @Autowired
    RepositorioPedido repositorioPedido;
    @Autowired
    RepositorioProducto repositorioProducto;
    @Autowired
    RepositorioUsuario repositorioUsuario;
    @Autowired
    ControladorPedido cp;
    @Autowired
    ControladorCarro cc;
    @Autowired
    ControladorUsuario cu;
    @Autowired
    WebApplicationContext context;

    /**
     * Test que comprueba que un nuevo usuario no debe devolver ningún pedido.
     */
    @Test
    public void noExistenPedidos () {
        Usuario user = repositorioUsuario.findByEmail("testPedidos@gmail.com");
        if (user == null) {
            user = new Usuario("test", "pedidos", "testPedidos@gmail.com", "pedidos", "zaragoza");
            cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));
        }

        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        cp.cargarPedidos(model, request);
        assertEquals(0, ((List<Pedido>) model.get("historial")).size());

        repositorioUsuario.delete(user);
    }

    /**
     * Test que comprueba si se ha añadido sin errores un pedido.
     */
    @Test
    public void addPedido () {
        Usuario user = repositorioUsuario.findByEmail("testPedidos2@gmail.com");
        if (user == null) {
            user = new Usuario("test2", "pedidos", "testPedidos2@gmail.com", "pedidos", "zaragoza");
            cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));
        }

        ArrayList<ProductoCarro> carroProductos = new ArrayList<>();
        carroProductos.add(new ProductoCarro(repositorioProducto.findByNombre("Yogurt"), 1));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("carroProductos", carroProductos);
        assertEquals("redirect:/", cc.finCompra(request, Mockito.mock(RedirectAttributes.class)));

        ExtendedModelMap model = new ExtendedModelMap();
        cp.cargarPedidos(model, request);
        assertEquals("Yogurt",((List<Pedido>) model.get("historial")).get(0).getProductos().get(0).getProducto().getNombre());

        repositorioUsuario.delete(user);
    }

    /**
     * Test que añade un pedido y comprueba que ha aumentado el número de pedidos
     * del usuario.
     */
    @Test
    public void cargarPedidosTest () {
        Usuario user = repositorioUsuario.findByEmail("pablo@gmail.com");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        cp.cargarPedidos(model, request);
        int pedidosInicial = ((List<Pedido>) model.get("historial")).size();

        ArrayList<ProductoCarro> carroProductos = new ArrayList<>();
        carroProductos.add(new ProductoCarro(repositorioProducto.findByNombre("Yogurt"), 1));
        request.getSession().setAttribute("carroProductos", carroProductos);
        cc.finCompra(request, Mockito.mock(RedirectAttributes.class));

        cp.cargarPedidos(model, request);
        assertEquals(pedidosInicial + 1, ((List<Pedido>) model.get("historial")).size());
    }
}
