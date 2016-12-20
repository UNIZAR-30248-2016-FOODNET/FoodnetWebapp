package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.CarroFavorito;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioCarroFavorito;
import es.unizar.es.foodnet.model.service.MessageHelper;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de recibir las peticiones de los clientes que estén relacionadas
 * con la gestión de carros favoritos.
 */
@Controller
public class ControladorCarroFavorito {

    private final RepositorioCarroFavorito repository;

    @Autowired
    public ControladorCarroFavorito(RepositorioCarroFavorito repository) {
        this.repository = repository;
    }

    /**
     * Almacena el carro en el repositorio de carros favoritos.
     *
     * @param request request objeto request del usuario
     * @param ra apoyo para el feedback
     * @return pagina del carro
     */
    @RequestMapping(value="/addFavorito", method = RequestMethod.POST)
    public String anadirCarroFavorito(HttpServletRequest request, RedirectAttributes ra){
        System.out.println("Me ha llegado peticion para añadir un carro favorito");

        ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");
        Usuario usuario = (Usuario) request.getSession().getAttribute("user");
        String nombre = (String) request.getSession().getAttribute("nombreFavorito");

        if (repository.findByNombre(nombre) == null) {
            repository.save(new CarroFavorito(nombre, usuario, carroSesion));
            System.out.println("Carro favorito añadido");
            MessageHelper.addSuccessAttribute(ra,"exito.favorito","");
        } else {
            System.err.println("Carro favorito no añadido: ya existe un carro con ese nombre.");
            MessageHelper.addErrorAttribute(ra,"fallo.favorito", "");
        }

        return "redirect:/carro";
    }


    /**
     * Carga los carros favoritos de un usuario y lo almacena para devolverselo al cliente.
     *
     * @param model Contenedor para almacenar los carros favoritos devueltos al cliente
     * @param request request objeto request del usuario
     * @return pagina de carros favoritos
     */
    @RequestMapping(value="/carrosFavoritos", method = RequestMethod.POST)
    public String cargarFavoritos(Model model, HttpServletRequest request){
        System.out.println("Me ha llegado peticion para mostrar un carro favorito");


        List<CarroFavorito> listaFavoritos = repository.findAll();
        List<CarroFavorito> carrosFavoritos = new ArrayList<CarroFavorito>();
        Usuario u =(Usuario)request.getSession().getAttribute("user");
        for(CarroFavorito c :listaFavoritos){
            if (c.getUsuario().getEmail().equals(u.getEmail())){
                carrosFavoritos.add(c);
            }
        }

        model.addAttribute("carrosFavoritos", carrosFavoritos);

        return "carrosFavoritos";

    }


}
