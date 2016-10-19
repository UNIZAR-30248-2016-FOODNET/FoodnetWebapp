package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
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
public class CategoriaTest {

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @Before
    public void inicializar () {
        repositorioCategoria.deleteAll();
        repositorioCategoria.save(new Categoria("categoria2"));
        repositorioCategoria.save(new Categoria("categoria4"));
        //repositorioCategoria.save(new Categoria(null));
        repositorioCategoria.save(new Categoria("categoria1"));
    }

    @Test
    public void findAllTest () {
        boolean bien = true;
        ArrayList<String> esperada = new ArrayList<>();
        esperada.add(new String("categoria1"));
        esperada.add(new String("categoria2"));
        esperada.add(new String("categoria4"));
        List<Categoria> lista = repositorioCategoria.findAll();
        if (lista.size() == esperada.size()) {
            for (Categoria categoria : lista) {
                for (String esperando : esperada) {
                    if (esperando.toString().equals(categoria.getNombre())) {
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
        assertTrue(repositorioCategoria.findByNombre("categoria1").getNombre().equals("categoria1"));
    }

    @Test
    public void findByNombreNoEncontrado () {
        assertNull(repositorioCategoria.findByNombre("categoria3"));
    }
}
