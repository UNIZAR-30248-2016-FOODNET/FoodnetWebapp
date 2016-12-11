package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class SupermercadoTest {

    @Autowired
    private RepositorioSupermercado repositorioSupermercado;

    private static int cantidad;
    private static boolean inicializado;
    private static int testCompletados;

    /**
     * Guarda los supermercados necesarios para realizar las pruebas y calcula
     * el número de productos actualmente del carro.
     */
    @Before
    public void inicializar () {
        if (!inicializado) {
            repositorioSupermercado.deleteAll();
            inicializado = true;
            testCompletados = 0;

            repositorioSupermercado.save(new Supermercado("supermercado2"));
            repositorioSupermercado.save(new Supermercado("supermercado4"));
            repositorioSupermercado.save(new Supermercado("supermercado1"));
            cantidad += 3;
        }
    }

    @After
    public void finalizar () {
        if (testCompletados >= 5) {
            inicializado = false;
        }
    }

    /**
     * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
     */
    @Test
    public void repoNotNull(){
        testCompletados++;
        assertNotNull(repositorioSupermercado);
    }


    /**
     * Test para comprobar que el número de supermercados almacenados en la base de datos
     * coincide con el número esperado.
     */
    @Test
    public void findAllTest () {
        testCompletados++;
        List<Supermercado> lista = repositorioSupermercado.findAll();

        assertEquals(cantidad, lista.size());
    }

    /**
     * Test para comprobar que no se puede insertar un supermercado con el mismo nombre
     * que una ya almacenado en el sistema
     */
    @Test (expected = DuplicateKeyException.class)
    public void registrarCategoriaExistente(){
        testCompletados++;
        Supermercado sup = new Supermercado("supermercadoDuplicado");
        Supermercado sup2 = new Supermercado("supermercadoDuplicado");
        repositorioSupermercado.save(sup);
        repositorioSupermercado.save(sup2);
    }

    /**
     * Test que comprueba que al buscar un supermercado por nombre almacenado
     * previamente en la base de datos lo encuentra satisfactoriamente
     */
    @Test
    public void findByNombreEncontrado () {
        testCompletados++;
        assertEquals(repositorioSupermercado.findByNombre("supermercado1").getNombre(), "supermercado1");
    }

    /**
     * Test que comprueba que al buscar un supermercado que no esta
     * almacenado en la base de datos no lo encuentra, devolviendo null
     */
    @Test
    public void findByNombreNoEncontrado () {
        testCompletados++;
        assertNull(repositorioSupermercado.findByNombre("supermercado3"));
    }
}
