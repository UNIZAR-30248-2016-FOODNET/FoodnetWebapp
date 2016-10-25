package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioUsuario;
import es.unizar.es.foodnet.model.service.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * Obtiene un usuario del formulario html e intenta registrarlo
     * @param user usuario a registrar
     * @return redireccion a index
     */
    @RequestMapping(value="/registrarse", method = RequestMethod.POST)
    public String registrarUsuario(Usuario user){
        System.out.println("Detectada peticion para registrar al usuario " + user.getEmail());
        Password pw = new Password();

        //ciframos la password del usuario
        try {
            user.setPassword(pw.generatePassword(user.getPassword()));
            repoUsuario.save(user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Error al generar password cifrada del usuario " + user.getEmail());
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
                                    Model model){
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
                    return "redirect:/panelLogin";
                }
            } catch (Exception e) {
                System.err.println("Error al comprobar la password del usuario " + user.getEmail());
                return "redirect:/panelLogin";
            }
        } else{
            System.err.println("Error al obtener el usuario cuyo email es " + email);
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
    public String modificarDatosUsuario(Usuario user){
        System.out.println("Detectada peticion para modificar datos del usuario " + user.getEmail());
        Usuario userRepo = repoUsuario.findById(user.getId());
        if(userRepo != null) {
            // Comprueba si ha cambiado la contraseña
            Password pw = new Password();
            try {
                // La contraseña ha cambiado
                if (!pw.isPasswordValid(userRepo.getPassword(), user.getPassword()))
                    user.setPassword(pw.generatePassword(user.getPassword()));
                repoUsuario.save(user);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                System.err.println("Error al generar password cifrada del usuario " + user.getEmail());
                return "redirect:/modificarDatosUsuario";
            }
        } else{
            System.err.println("Error al intentar modificar los datos del usuario.");
            return "redirect:/panelLogin";
        }
        return "redirect:/modificarDatosUsuario";
    }

    /**
     * Elimina al usuario de la base de datos y redirige a la pagina principal
     * @param user usuario a eliminar
     * @return redireccion a la pagina de login/registro
     */
    @RequestMapping(value="/eliminarUsuario", method = RequestMethod.POST)
    public String eliminarUsuario(Usuario user){
        System.out.println("Detectada peticion para eliminar al usuario " + user.getEmail());
        Usuario userRepo = repoUsuario.findById(user.getId());
        if(userRepo != null) {
            repoUsuario.delete(user);
        } else{
            System.err.println("Error al intentar eliminar al usuario.");
        }
        return "redirect:/panelLogin";
    }
}
