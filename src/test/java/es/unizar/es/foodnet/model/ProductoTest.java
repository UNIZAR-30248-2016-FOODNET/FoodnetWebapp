package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pablo on 19/10/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductoTest {

    @Autowired
    private RepositorioCategoria repositorioCategoria;
    @Autowired
    private RepositorioSupermercado repositorioSupermercado;
    @Autowired
    private RepositorioProducto repositorioProducto;

    @Before
    public void inicializar () {
        repositorioCategoria.deleteAll();
        repositorioSupermercado.deleteAll();
        repositorioProducto.deleteAll();

        repositorioCategoria.save(new Categoria("categoria1"));
        repositorioSupermercado.save(new Supermercado("supermercado1"));

        Categoria categoria = repositorioCategoria.findByNombre("categoria1");
        Supermercado supermercado = repositorioSupermercado.findByNombre("supermercado1");

        repositorioProducto.save(new Producto(categoria, supermercado, "producto1", 0.80, "http://placehold.it/650x450"));
        repositorioProducto.save(new Producto(categoria, supermercado, "producto3", 10, "http://placehold.it/650x450"));
    }

    @Test
    public void findAllTest () {
        boolean bien = true;
        ArrayList<String> esperada = new ArrayList<>();
        esperada.add(new String("producto1"));
        esperada.add(new String("producto3"));
        List<Producto> lista = repositorioProducto.findAll();
        if (lista.size() == esperada.size()) {
            for (Producto producto : lista) {
                for (String esperando : esperada) {
                    if (esperando.toString().equals(producto.getNombre()) &&
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

    @Test
    public void findByNombreEncontrado () {
        Producto producto = repositorioProducto.findByNombre("producto3");
        boolean bien = producto.getNombre().equals("producto3") &&
                producto.getSupermercado().getNombre().equals("supermercado1") &&
                producto.getCategoria().getNombre().equals("categoria1");
        assertTrue(bien);
    }

    @Test
    public void findByNombreNoEncontrado1 () {
        assertNull(repositorioProducto.findByNombre("producto2"));
    }
}
