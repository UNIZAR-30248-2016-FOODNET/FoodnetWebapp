package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductoTest {

    @Autowired
    private RepositorioCategoria repositorioCategoria;
    @Autowired
    private RepositorioSupermercado repositorioSupermercado;
    @Autowired
    private RepositorioProducto repositorioProducto;

    /**
     * Elimina los productos que hay inicialmente en la base de datos, junto a sus categorias
     * y supermercados e inserta los necesarios para realizar las pruebas
     */
    @Before
    public void inicializar () {
        repositorioCategoria.save(new Categoria("categoria1"));
        repositorioSupermercado.save(new Supermercado("supermercado1"));

        Categoria categoria = repositorioCategoria.findByNombre("categoria1");
        Supermercado supermercado = repositorioSupermercado.findByNombre("supermercado1");

        repositorioProducto.save(new Producto(categoria, supermercado, "producto1", 0.80, "http://placehold.it/650x450"));
        repositorioProducto.save(new Producto(categoria, supermercado, "producto3", 10, "http://placehold.it/650x450"));
    }

    /**
     * Borra las categorias, supermercados y productos
     * utilizados para las pruebas.
     */
    @After
    public void finalizar () {
        Producto p1 = repositorioProducto.findByNombre("producto1");
        Producto p3 = repositorioProducto.findByNombre("producto3");
        Categoria c1 = repositorioCategoria.findByNombre("categoria1");
        Supermercado s1 = repositorioSupermercado.findByNombre("supermercado1");
        repositorioProducto.delete(p1);
        repositorioProducto.delete(p3);
        repositorioCategoria.delete(c1);
        repositorioSupermercado.delete(s1);
    }

    /**
     * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
     */
    @Test
    public void repoNotNull(){
        assertNotNull(repositorioCategoria);
        assertNotNull(repositorioProducto);
        assertNotNull(repositorioSupermercado);
    }

    /**
     * Test para comprobar que los productos almacenados en la base de datos
     * son encontrados satisfactoriamente, comprobando que los nombres coinciden,
     * asi como su categoria y el supermercado al cual pertenecen.
     */
    /*@Test
    public void findAllTest () {
        boolean bien = true;
        ArrayList<String> esperada = new ArrayList<>();
        esperada.add("producto1");
        esperada.add("producto3");
        List<Producto> lista = repositorioProducto.findAll();
        if (lista.size() == esperada.size()) {
            for (Producto producto : lista) {
                for (String esperando : esperada) {
                    if (esperando.equals(producto.getNombre()) &&
                            producto.getCategoria().getNombre().equals("categoria1") &&
                            producto.getSupermercado().getNombre().equals("supermercado1")) {
                        bien = true;
                        break;
                    }
                    bien = false;
                }
            }
        } else {
            bien = false;
        }

        assertTrue(bien);
    }
*/
    /**
     * Test que comprueba que al realizar la busqueda de un producto existente
     * por su nombre, lo encuentra satisfactoriamente
     */
    @Test
    public void findByNombreEncontrado () {
        Producto producto = repositorioProducto.findByNombre("producto1");
        assertEquals(producto.getNombre(), "producto1");
        assertEquals(producto.getCategoria().getNombre(), "categoria1");
        assertEquals(producto.getSupermercado().getNombre(), "supermercado1");
    }

    /**
     * Test que comprueba que al realizar la busqueda de un producto no existente
     * por su nombre, no es devuelto, devuelve null en su caso
     */
    @Test
    public void findByNombreNoEncontrado () {
        assertNull(repositorioProducto.findByNombre("producto2"));
    }
}
