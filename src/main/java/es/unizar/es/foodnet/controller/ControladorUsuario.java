package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.MessageHelper;
import es.unizar.es.foodnet.model.service.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
public class ControladorUsuario {

    private final RepositorioUsuario repoUsuario;

    @Autowired
    public ControladorUsuario(RepositorioUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
    }

    /**
     * Devuelve la pagina de registro a quien la solicite
     * @return pagina de registro
     */
    @RequestMapping(value="/panelLogin")
    public String paginaRegistro(){
        System.out.println("Detectada peticion para cargar la pagina de registro/log");
        return "registro";
    }

    /**
     * Devuelve la pagina encargada de modificar los datos de un usuario
     * @return html para modificar datos de un usuario
     */
    @RequestMapping(value = "/modificarUsuario")
    public String modificarUsuario(HttpServletRequest request){
        System.out.println("Me ha llegado la peticion de cargar el panel de modificar datos de un usuario");
        if(request.getSession().getAttribute("user")!=null){
            return "modificarDatosUsuario";
        } else return "redirect:/";

    }

    /**
     * Obtiene un usuario del formulario html e intenta registrarlo
     * @param user usuario a registrar
     * @return redireccion a index
     */
    @RequestMapping(value="/registrarse", method = RequestMethod.POST)
    public String registrarUsuario(Usuario user, RedirectAttributes ra){
        System.out.println("Detectada peticion para registrar al usuario " + user.getEmail());
        Password pw = new Password();

        //ciframos la password del usuario
        try {
            user.setPassword(pw.generatePassword(user.getPassword()));
            repoUsuario.save(user);
            MessageHelper.addSuccessAttribute(ra, "exito.registro", user.getEmail());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Error al generar password cifrada del usuario " + user.getEmail());
            MessageHelper.addErrorAttribute(ra,"fallo.password.cifrar",user.getEmail());
        }

        return "redirect:/";
    }

    /**
     * Autentica un usuario si existe e ingresa la password adecuada
     * @param email email con el que se quiere hacer login
     * @param password password con el que se quiere hacer login
     * @param request objeto request del usuario
     * @return redireccion a catalogo si ha ido bien, redireccion a index si ha habido algun fallo
     */
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String autenticarUsuario(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    HttpServletRequest request,
                                    Model model, RedirectAttributes ra){
        System.out.println("Detectada peticion para que el usuario " + email + " haga login");

        Password pw = new Password();

        Usuario user = repoUsuario.findByEmail(email);

        if(user != null){
            try {
                if(pw.isPasswordValid(password,user.getPassword())){
                    System.out.println("Password valida, redirigiendo a catalogo");
                    request.getSession().setAttribute("user", user);
                    model.addAttribute("currentUser",user);
                    return "redirect:/";
                } else{
                    System.out.println("Password no valida");
                    MessageHelper.addErrorAttribute(ra, "fallo.login.password", "");
                    return "redirect:/panelLogin";
                }
            } catch (Exception e) {
                System.err.println("Error al comprobar la password del usuario " + user.getEmail());
                MessageHelper.addErrorAttribute(ra, "fallo.password.descifrar", user.getEmail());
                return "redirect:/panelLogin";
            }
        } else{
            System.err.println("Error al obtener el usuario cuyo email es " + email);
            MessageHelper.addErrorAttribute(ra, "fallo.login.email", email);
            return "redirect:/panelLogin";
        }

    }

    /**
     * Comprueba si un email existe ya en la bbdd
     * @param email email a comprobar
     * @param response 'noencontrado' si no existe, 'encontrado' en caso contrario
     */
    @RequestMapping(value="/comprobarEmail", method= RequestMethod.POST)
    public void comprobarEmailRegistro(@RequestParam("email") String email,
                                   HttpServletResponse response){
        System.out.println("Me ha llegado peticion para comprobar si " + email +  " existe");

        Usuario user = repoUsuario.findByEmail(email);
        PrintWriter out;
        try {
            out = response.getWriter();

            if(user != null){
                out.println("encontrado");
                System.out.println(email + " existe.");
            } else{
                out.println("noencontrado");
                System.out.println(email + " no existe.");
            }
        } catch (IOException e) {
            System.err.println("Error al obtener la salida del response del usuario");
        }
    }

    /**
     * Tratamiento para cuando se pide hacer un logout
     * @param request request del usuario que hace la peticion
     * @return redireccion a index invalidando la sesion
     */
    @RequestMapping(value="/logout")
    public String logout(HttpServletRequest request){
        System.out.println("Me ha llegado peticion para logout");

        request.getSession().invalidate();

        return "redirect:/";
    }

    /**
     * Obtiene los datos del usuario al cual modificar sus datos personales
     * y lo cambia en la base de datos
     * @param user usuario con sus campos a cambiar
     * @return redireccion a index si es correcto o al panel de login si
     * no se encuentra el usuario del que modificar datos
     */
    @RequestMapping(value="/modificarDatos", method = RequestMethod.POST)
    public String modificarDatosUsuario(Usuario user, HttpServletRequest request, RedirectAttributes ra){
        System.out.println("Detectada peticion para modificar datos del usuario " + user.getEmail());
        Usuario logueado = (Usuario) request.getSession().getAttribute("user");
        Usuario userRepo = repoUsuario.findById(logueado.getId());

        if(userRepo != null) {  //Obtenemos el usuario correctamente de la bbdd
            // Comprueba si ha cambiado la contraseña
            Password pw = new Password();
            try {
                String password = user.getPassword();
                if (!password.equals("") && !pw.isPasswordValid(password, userRepo.getPassword())) {
                    // La contraseña ha cambiado
                    userRepo.setPassword(pw.generatePassword(user.getPassword()));
                } else if (password.equals("")) {
                    // La contraseña no se quiere modificar
                    userRepo.setPassword(userRepo.getPassword());
                }

                userRepo.setEmail(user.getEmail());
                userRepo.setNombre(user.getNombre());
                userRepo.setApellidos(user.getApellidos());
                userRepo.setDireccion(user.getDireccion());
                repoUsuario.save(userRepo);

                //Actualizamos el usuario de la sesion con los nuevos datos
                request.getSession().setAttribute("user",userRepo);

                //Alertas
                MessageHelper.addSuccessAttribute(ra,"exito.usuario.modificar","");

                return "redirect:/modificarUsuario";
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                System.err.println("Error al generar password cifrada del usuario " + user.getEmail());
                MessageHelper.addErrorAttribute(ra,"fallo.password.cifrar",user.getEmail());
                return "redirect:/modificarUsuario";
            }
        } else{
            System.err.println("No se han podido obtener los datos del usuario " + logueado.getEmail());
            request.getSession().invalidate();
            MessageHelper.addErrorAttribute(ra,"fallo.interno.sesion",logueado.getEmail());
            return "redirect:/";
        }
    }


    /**
     * Elimina al usuario de la base de datos y redirige a la pagina principal
     * @param request request del usuario que hace la peticion
     */
    @RequestMapping(value = "/eliminarUsuario", method = RequestMethod.POST)
    public String eliminacionUsuario(HttpServletRequest request, RedirectAttributes ra){
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if(user!=null){
            System.out.println("Detectada peticion para eliminar al usuario " + user.getEmail());
            MessageHelper.addSuccessAttribute(ra,"exito.usuario.borrar",user.getEmail());
            repoUsuario.delete(user);
            request.getSession().invalidate();
        }
        return "redirect:/";
    }
}
