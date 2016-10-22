package es.unizar.es.foodnet;

import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodNetApplicationTests {



	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private ControladorUsuario cu;

	@Before
	public void inicializar(){
		//repositorioUsuario.deleteAll();
	}

	@Test
	public void registrarUsuario(){
		if(repositorioUsuario.findByEmail("pepe@gmail.com")==null){
			cu.registrarUsuario(new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza"));
			Usuario usuario = repositorioUsuario.findByEmail("pepe@gmail.com");
			assertTrue(usuario!=null);
			repositorioUsuario.delete(usuario);
		}else{
			assertTrue(false);
		}
	}

	@Test
	public void autenticarUsuario(){
		if(repositorioUsuario.findByEmail("pepe@gmail.com")==null){
			Usuario usuario = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
			cu.registrarUsuario(usuario);
			HttpServletRequest hsr = new MockHttpServletRequest();
			assertTrue(cu.autenticarUsuario("pepe@gmail.com","Zaragoza-1",hsr).equals("redirect:/catalogo"));
			repositorioUsuario.delete(usuario);
		}else{
			assertTrue(false);
		}
	}


	@Test
	public void contextLoads() {
	}



}
