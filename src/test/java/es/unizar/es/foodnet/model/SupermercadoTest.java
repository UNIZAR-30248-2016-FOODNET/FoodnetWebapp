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
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupermercadoTest {

    @Autowired
    private RepositorioSupermercado repositorioSupermercado;

    /**
     * Guarda los supermercados necesarios para realizar las pruebas
     */
    @Before
    public void inicializar () {
        repositorioSupermercado.save(new Supermercado("supermercado2"));
        repositorioSupermercado.save(new Supermercado("supermercado4"));
        repositorioSupermercado.save(new Supermercado("supermercado1"));
    }

    /**
     * Borra las categorias utilizadas para las pruebas.
     */
    @After
    public void finalizar () {
        Supermercado s2 = repositorioSupermercado.findByNombre("supermercado2");
        Supermercado s4 = repositorioSupermercado.findByNombre("supermercado4");
        Supermercado s1 = repositorioSupermercado.findByNombre("supermercado1");
        repositorioSupermercado.delete(s2);
        repositorioSupermercado.delete(s1);
        repositorioSupermercado.delete(s4);
    }

    /**
     * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
     */
    @Test
    public void repoNotNull(){
        assertNotNull(repositorioSupermercado);
    }


    /**
     * Test para comprobar que los supermercados almacenadas en la base de datos
     * son encontrados satisfactoriamente, comprobando que los nombres coinciden
     */
    /*@Test
    public void findAllTest () {
        boolean bien = true;
        ArrayList<String> esperada = new ArrayList<>();
        esperada.add("supermercado1");
        esperada.add("supermercado2");
        esperada.add("supermercado4");
        List<Supermercado> lista = repositorioSupermercado.findAll();
        if (lista.size() == esperada.size()) {
            for (Supermercado supermercado : lista) {
                for (String esperando : esperada) {
                    if (esperando.equals(supermercado.getNombre())) {
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
     * Test para comprobar que no se puede insertar un supermercado con el mismo nombre
     * que una ya almacenado en el sistema
     */
    @Test (expected = DuplicateKeyException.class)
    public void registrarCategoriaExistente(){
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
        assertEquals(repositorioSupermercado.findByNombre("supermercado1").getNombre(), "supermercado1");
    }

    /**
     * Test que comprueba que al buscar un supermercado que no esta
     * almacenado en la base de datos no lo encuentra, devolviendo null
     */
    @Test
    public void findByNombreNoEncontrado () {
        assertNull(repositorioSupermercado.findByNombre("supermercado3"));
    }
}
