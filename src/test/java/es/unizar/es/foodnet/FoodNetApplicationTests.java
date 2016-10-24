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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

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
			cu.registrarUsuario(new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza"));
			Usuario usuario = repositorioUsuario.findByEmail("pepe@gmail.com");
			HttpServletRequest hsr = new MockHttpServletRequest();
			Model m = new ExtendedModelMap();
			assertTrue(cu.autenticarUsuario("pepe@gmail.com","Zaragoza-1",hsr,m).equals("redirect:/"));
			assertTrue(((Usuario)m.asMap().get("currentUser")).equals(usuario));
			repositorioUsuario.delete(usuario);
		}else{
			assertTrue(false);
		}
	}


	@Test
	public void contextLoads() {
	}



}
