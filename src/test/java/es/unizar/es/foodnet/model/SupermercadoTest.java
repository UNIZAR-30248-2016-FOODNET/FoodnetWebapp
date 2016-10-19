package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Supermercado;
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
public class SupermercadoTest {

    @Autowired
    private RepositorioSupermercado repositorioSupermercado;

    @Before
    public void inicializar () {
        repositorioSupermercado.deleteAll();
        repositorioSupermercado.save(new Supermercado("supermercado2"));
        repositorioSupermercado.save(new Supermercado("supermercado4"));
        //repositorioSupermercado.save(new Supermercado(null));
        repositorioSupermercado.save(new Supermercado("supermercado1"));
    }

    @Test
    public void findAllTest () {
        boolean bien = true;
        ArrayList<String> esperada = new ArrayList<>();
        esperada.add(new String("supermercado1"));
        esperada.add(new String("supermercado2"));
        esperada.add(new String("supermercado4"));
        List<Supermercado> lista = repositorioSupermercado.findAll();
        if (lista.size() == esperada.size()) {
            for (Supermercado supermercado : lista) {
                for (String esperando : esperada) {
                    if (esperando.toString().equals(supermercado.getNombre())) {
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
        assertTrue(repositorioSupermercado.findByNombre("supermercado1").getNombre().equals("supermercado1"));
    }

    @Test
    public void findByNombreNoEncontrado () {
        assertNull(repositorioSupermercado.findByNombre("supermercado3"));
    }
}
