package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CategoriaTest {

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    private static int cantidad;
    private static boolean inicializado;

    /**
     * Inicializa las categorias añadiendo las necesarias
     * para realizar las pruebas
     */
    @Before
    public void inicializar () {
        if (!inicializado) {
            repositorioCategoria.deleteAll();
            inicializado = true;

            cantidad = repositorioCategoria.findAll().size();
            repositorioCategoria.save(new Categoria("categoria2"));
            repositorioCategoria.save(new Categoria("categoria4"));
            repositorioCategoria.save(new Categoria("categoria1"));
            cantidad += 3;
        }
    }

    /**
     * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
     */
    @Test
    public void repoNotNull(){
        assertNotNull(repositorioCategoria);
    }

    /**
     * Test para comprobar que las categorias almacenadas en la base de datos
     * son encontradas satisfactoriamente, comprobando que los nombres coinciden
     */
    @Test
    public void findAllTest () {
        List<Categoria> lista = repositorioCategoria.findAll();
        assertEquals(cantidad, lista.size());
    }

    /**
     * Test para comprobar que no se puede insertar una categoria con el mismo nombre
     * que una ya almacenada en el sistema
     */
    @Test (expected = DuplicateKeyException.class)
    public void registrarCategoriaExistente(){
        Categoria categoria = new Categoria("categoriaDuplicada");
        Categoria categoria2 = new Categoria("categoriaDuplicada");
        repositorioCategoria.save(categoria);
        repositorioCategoria.save(categoria2);
    }

    /**
     * Test que verifica que al realizar una búsqueda por nombre de una categoria
     * existente en la base de datos la encuentra satisfactoriamente.
     */
    @Test
    public void findByNombreEncontrado () {
        assertEquals(repositorioCategoria.findByNombre("categoria1").getNombre(),"categoria1");
    }

    /**
     * Test que verifica que al realizar una busqueda de una categoria que no existe
     * en la base de datos no la devuelve, devolviendo "null".
     */
    @Test
    public void findByNombreNoEncontrado () {
        assertNull(repositorioCategoria.findByNombre("categoria3"));
    }
}
