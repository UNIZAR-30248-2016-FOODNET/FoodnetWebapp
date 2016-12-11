package es.unizar.es.foodnet.model;

import es.unizar.es.foodnet.controller.ControladorUsuario;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.Password;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UsuarioTest {

	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private ControladorUsuario cu;
	@Autowired
	WebApplicationContext context;

	private static Usuario user;
	private static boolean inicializado;
	private static int testCompletados;

	@Before
	public void inicializar () {
		if (!inicializado) {
			repositorioUsuario.deleteAll();
			inicializado = true;

			user = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
			cu.registrarUsuario(user, Mockito.mock(RedirectAttributes.class));
		}
	}

	@After
	public void finalizar () {
		if (testCompletados >= 10) {
			inicializado = false;
		}
	}

	/**
	 * Test para comprobar que la inyeccion de campos se ha realizado de manera correcta
	 */
	@Test
	public void repoNotNull(){
		testCompletados++;
		assertNotNull(repositorioUsuario);
	}

	/**
	 * Test para comprobar que se puede registrar correctamente a un usuario
	 */
	@Test
	public void registrarUsuarioNuevo(){
		testCompletados++;
		Usuario usuario = new Usuario("pedro", "Jiménez", "pedro@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(usuario, Mockito.mock(RedirectAttributes.class));
		usuario = repositorioUsuario.findByEmail("pedro@gmail.com");

		assertNotNull(usuario);
		assertNotNull(repositorioUsuario.findById(usuario.getId()));
	}

    /**
     * Test para comprobar que al buscar un usuario que no existe no devuelve
	 * ningun usuario
	 */
	@Test
	public void buscarUsuarioInexistente() {
		testCompletados++;
		assertNull(repositorioUsuario.findByEmail("noExisto@gmail.com"));
	}

	/**
	 * Test para comprobar que no se puede registrar a un usuario con un email ya existente en el sistema
	 */
	@Test (expected = DuplicateKeyException.class)
	public void registrarUsuarioExistente(){
		testCompletados++;
		Usuario usuario = new Usuario("pepe", "Sanchez", "pepe@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(usuario, Mockito.mock(RedirectAttributes.class));
	}

	/**
	 * Test para comprobar que funciona la autentificacion de un usuario
	 */
	@Test
	public void autenticarUsuario(){
		testCompletados++;
		assertEquals("redirect:/",cu.autenticarUsuario("pepe@gmail.com","Zaragoza-1",
				new MockHttpServletRequest(), new ExtendedModelMap(), Mockito.mock(RedirectAttributes.class)));
		assertEquals("redirect:/panelLogin",cu.autenticarUsuario("pepe@gmail.com","falla",
				new MockHttpServletRequest(), new ExtendedModelMap(), Mockito.mock(RedirectAttributes.class)));
		assertEquals("redirect:/panelLogin",cu.autenticarUsuario("noExisto@gmail.com","daIgualPass",
				new MockHttpServletRequest(),new ExtendedModelMap(), Mockito.mock(RedirectAttributes.class)));
	}

	/**
	 * Test para comprobar que al introducir un email que todavia no existe en el
	 * registro no se produce un error y indicando "no encontrado"
	 */
	@Test
	public void comprobarEmailNoDuplicado() throws Exception {
		testCompletados++;
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
		testCompletados++;
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
		testCompletados++;
		Password pw = new Password();
		Usuario usuario = new Usuario("Señor", "Prueba", "senorPrueba@gmail.com", "Zaragoza-1", "zaragoza");
		cu.registrarUsuario(usuario, Mockito.mock(RedirectAttributes.class));

		usuario.setNombre("pepe2");
		usuario.setApellidos("Sanchez2");
		usuario.setDireccion("Zaragoza-2");
		usuario.setEmail("pepe2@gmail.com");
		usuario.setPassword("zaragozaaaa");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",usuario);
		cu.modificarDatosUsuario(usuario, request, Mockito.mock(RedirectAttributes.class));
		Usuario userRepo = repositorioUsuario.findById(usuario.getId());

		assertEquals(usuario.getNombre(), userRepo.getNombre());
		assertEquals(usuario.getApellidos(), userRepo.getApellidos());
		assertEquals(usuario.getDireccion(), userRepo.getDireccion());
		assertEquals(usuario.getEmail(), userRepo.getEmail());
		try {
			if (pw.isPasswordValid(usuario.getPassword(), userRepo.getPassword())) {
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
		testCompletados++;
		Usuario usuario = new Usuario("manolo", "Navarro", "manolo@gmail.com", "Zaragoza-2", "zaragoza");
		cu.registrarUsuario(usuario, Mockito.mock(RedirectAttributes.class));
		usuario.setEmail("pepe@gmail.com");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",usuario);
		cu.modificarDatosUsuario(usuario,request, Mockito.mock(RedirectAttributes.class));
	}

	/**
	 * Modifica los datos de un usuario con una password igual a la cadena vacia
	 * para comprobar que se conserva la antigua en caso de que el usuario no quiera
	 * modificar su password actual
	 */
	@Test
	public void NoModificarPassword() {
		testCompletados++;
		String passAntigua = repositorioUsuario.findById(user.getId()).getPassword();
		user.setPassword("");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.getSession().setAttribute("user",user);
		cu.modificarDatosUsuario(user, request, Mockito.mock(RedirectAttributes.class));

		String passActual = repositorioUsuario.findById(user.getId()).getPassword();
		assertEquals(passAntigua, passActual);
	}

}
