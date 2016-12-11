package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorCarro;
import es.unizar.es.foodnet.controller.ControladorPedido;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PedidoTest {

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
    private ControladorPedido cp;
    @Autowired
    private ControladorCarro cc;
    @Autowired
    private ControladorUsuario cu;

    private static Usuario user;
    private static boolean inicializado;
    private static int testCompletados;
    private static ArrayList<ProductoCarro> carroProductos;

    @Before
    public void beforeTestClass() {
        if (!inicializado) {
            repositorioPedido.deleteAll();
            repositorioUsuario.deleteAll();
            repositorioProducto.deleteAll();
            repositorioCategoria.deleteAll();
            repositorioSupermercado.deleteAll();
            inicializado = true;

            user = new Usuario("test", "pedidos", "testPedidos@gmail.com", "pedidos", "zaragoza");
            cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));

            Categoria categoria = new Categoria("Lacteos");
            Supermercado supermercado = new Supermercado("supermercado1");
            repositorioCategoria.save(categoria);
            repositorioSupermercado.save(supermercado);
            repositorioProducto.save(new Producto(categoria, supermercado, "Yogurt", 0.80, "http://placehold.it/650x450"));

            carroProductos = new ArrayList<>();
            carroProductos.add(new ProductoCarro(repositorioProducto.findByNombre("Yogurt"), 1));
        }
    }

    @After
    public void finalizar () {
        if (testCompletados >= 5) {
            inicializado = false;
        }
    }

    /**
     * Test que comprueba que un nuevo usuario no debe devolver ningún pedido.
     */
    @Test
    public void noExistenPedidos () {
        testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        cp.cargarPedidos(model, request);
        assertEquals(0, ((List<Pedido>) model.get("historial")).size());
    }

    /**
     * Test que comprueba si se ha añadido sin errores un pedido.
     */
    @Test
    public void addPedido () {
        testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("carroProductos", carroProductos);
        assertEquals("redirect:/", cc.finCompra(request, Mockito.mock(RedirectAttributes.class)));

        ExtendedModelMap model = new ExtendedModelMap();
        cp.cargarPedidos(model, request);
        assertEquals("Yogurt",((List<Pedido>) model.get("historial")).get(0).getProductos().get(0).getProducto().getNombre());
    }

    /**
     * Test que añade un pedido y comprueba que ha aumentado el número de pedidos
     * del usuario.
     */
    @Test
    public void cargarPedidosTest () {
        testCompletados++;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ExtendedModelMap model = new ExtendedModelMap();
        request.getSession().setAttribute("user", user);
        cp.cargarPedidos(model, request);
        int pedidosInicial = ((List<Pedido>) model.get("historial")).size();

        request.getSession().setAttribute("carroProductos", carroProductos);
        cc.finCompra(request, Mockito.mock(RedirectAttributes.class));

        cp.cargarPedidos(model, request);
        assertEquals(pedidosInicial + 1, ((List<Pedido>) model.get("historial")).size());
    }

    /**
     * Crea un pedido y comprueba que el estado inicial es realizado.
     */
    @Test
    public void comprobarEstadoPedidoCreado() {
        testCompletados++;
        Pedido pedido = new Pedido(user, Calendar.getInstance().getTime(), carroProductos);
        assertEquals(pedido.getEstado(), Pedido.PEDIDO_REALIZADO);
    }

    /**
     * Crea un pedido y comprueba que al cancelarlo su estado pasa a estar cancelado.
     * Comprueba que al cancelar un pedido ya cancelado no hace nada y devuelve false.
     */
    @Test
    public void cancelarPedido() {
        testCompletados++;
        Pedido pedido = new Pedido(user, Calendar.getInstance().getTime(), carroProductos);
        assertTrue(pedido.cancelarPedido());
        assertEquals(pedido.getEstado(), Pedido.PEDIDO_CANCELADO);
        assertFalse(pedido.cancelarPedido());
    }
}
