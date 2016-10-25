package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.Password;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {

	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private ControladorUsuario cu;
	@Autowired
	WebApplicationContext context;
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
	public void registrarUsuarioNuevo(){
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		Usuario usuario = repositorioUsuario.findByEmail("pepe@gmail.com");
		assertNotNull(usuario);
		assertNotNull(repositorioUsuario.findById(usuario.getId()));
	}

    /**
     * Test para comprobar que al buscar un usuario que no existe no devuelve
	 * ningun usuario
	 */
	@Test
	public void buscarUsuarioInexistente() {
		assertNull(repositorioUsuario.findByEmail("noExisto@gmail.com"));
	}

	/**
	 * Test para comprobar que no se puede registrar a un usuario con un email ya existente en el sistema
	 */
	@Test (expected = DuplicateKeyException.class)
	public void registrarUsuarioExistente(){
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		Usuario u2 = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(u2);
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

	/**
	 * Test para comprobar que al introducir un email que todavia no existe en el
	 * registro no se produce un error y indicando "no encontrado"
	 */
	@Test
	public void comprobarEmailNoDuplicado() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		mockMvc.perform(post("/comprobarEmail").param("email", "noExiste@gmail.com"))
				.andExpect(status().isOk());

		MockHttpServletResponse response = new MockHttpServletResponse();
		cu.comprobarEmailRegistro("noExiste@gmail.com", response);
		assertEquals("noencontrado", response.getContentAsString().trim());
	}

	/**
	 * Test para comprobar que al introducir un email que existe en el registro
	 * se produce un error indicando "encontrado"
	 */
	@Test
	public void comprobarEmailDuplicado() throws Exception {

		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		mockMvc.perform(post("/comprobarEmail").param("email", user.getEmail()))
				.andExpect(status().isOk());

		MockHttpServletResponse response = new MockHttpServletResponse();
		cu.comprobarEmailRegistro(user.getEmail(), response);
		assertEquals("encontrado", response.getContentAsString().trim());


	}

	/**
	 * Modifica los datos de un usuario existente y comprueba que los datos corresponden
	 */
	@Test
	public void modificarDatosUsuario() {
		Password pw = new Password();
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		user.setNombre("pepe2");
		user.setApellidos("Sanchez2");
		user.setDireccion("Zaragoza-2");
		user.setEmail("pepe2@pepe2@gmail.com");
		user.setPassword("zaragozaaaa");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",user);
		cu.modificarDatosUsuario(user, request);

		Usuario userRepo = repositorioUsuario.findById(user.getId());
		assertEquals(user.getNombre(), userRepo.getNombre());
		assertEquals(user.getApellidos(), userRepo.getApellidos());
		assertEquals(user.getDireccion(), userRepo.getDireccion());
		assertEquals(user.getEmail(), userRepo.getEmail());
		try {
			if (pw.isPasswordValid(user.getPassword(), userRepo.getPassword())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifica los datos de un usuario con un email de otro usuario que existia
	 * previamente y comprueba que se produce un error
	 */
	@Test (expected = DuplicateKeyException.class)
	public void modificarEmailAUnoExistente() {
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);

		Usuario user2 = new Usuario("manolo", "Navarro", "manolo@gmail.com", "Zaragoza-2", "zaragoza");
		cu.registrarUsuario(user2);
		user2.setEmail("pepe@gmail.com");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",user2);
		cu.modificarDatosUsuario(user2,request);
	}

	/**
	 * Modifica los datos de un usuario con una password igual a la cadena vacia
	 * para comprobar que se conserva la antigua en caso de que el usuario no quiera
	 * modificar su password actual
	 */
	@Test
	public void NoModificarPassword() {
		user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(user);
		String passAntigua = repositorioUsuario.findById(user.getId()).getPassword();
		user.setPassword("");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",user);
		cu.modificarDatosUsuario(user, request);

		String passActual = repositorioUsuario.findById(user.getId()).getPassword();
		assertEquals(passAntigua, passActual);
	}

}
