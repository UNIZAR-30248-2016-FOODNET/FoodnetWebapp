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

import java.util.List;

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
    private int cantidad;

    /**
     * Elimina los productos que hay inicialmente en la base de datos, junto a sus categorias
     * y supermercados e inserta los necesarios para realizar las pruebas
     */
    @Before
    public void inicializar () {
        this.cantidad = repositorioProducto.findAll().size();
        repositorioCategoria.save(new Categoria("categoria1"));
        repositorioSupermercado.save(new Supermercado("supermercado1"));

        Categoria categoria = repositorioCategoria.findByNombre("categoria1");
        Supermercado supermercado = repositorioSupermercado.findByNombre("supermercado1");

        repositorioProducto.save(new Producto(categoria, supermercado, "producto1", 0.80, "http://placehold.it/650x450"));
        repositorioProducto.save(new Producto(categoria, supermercado, "producto3", 10, "http://placehold.it/650x450"));
        this.cantidad += 2;
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
    @Test
    public void findAllTest () {
        List<Producto> lista = repositorioProducto.findAll();

        assertEquals(this.cantidad, lista.size());
    }

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
