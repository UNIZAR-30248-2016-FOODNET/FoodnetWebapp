package es.unizar.es.foodnet;

import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodNetApplicationTests {

	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private ControladorUsuario cu;
	private Usuario user;

	@After
	public void borrarPepe(){
		if(user != null){
			repositorioUsuario.delete(user);
			user = null;
		}
	}

	/**
	 * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
	 */
	@Test
	public void repoNotNull(){
		assertNotNull(repositorioUsuario);
	}

	/**
	 * Test para comprobar que se puede registrar correctamente a un usuario
	 */
	@Test
	public void registrarUsuario(){
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		Usuario usuario = repositorioUsuario.findByEmail("pepe@gmail.com");
		assertNotNull(usuario);
	}

	/**
	 * Test para comprobar que funciona la autentificacion de un usuario
	 */
	@Test
	public void autenticarUsuario(){
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		assertEquals("redirect:/",cu.autenticarUsuario("pepe@gmail.com","Zaragoza-1",new MockHttpServletRequest(),new ExtendedModelMap()));
		assertEquals("redirect:/panelLogin",cu.autenticarUsuario("pepe@gmail.com","falla",new MockHttpServletRequest(),new ExtendedModelMap()));
		assertEquals("redirect:/panelLogin",cu.autenticarUsuario("noExisto@gmail.com","daIgualPass",new MockHttpServletRequest(),new ExtendedModelMap()));
	}


	@Test
	public void contextLoads() {
	}



}
