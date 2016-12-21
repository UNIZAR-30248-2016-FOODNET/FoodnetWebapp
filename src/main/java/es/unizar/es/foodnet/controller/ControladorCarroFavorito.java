package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.CarroFavorito;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioCarroFavorito;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
     * @param nombreFavorito cadena de caracteres con el nombre deseado para el carro
     * @param request objeto request del usuario
     * @param response objeto response del usuario
     */
    @RequestMapping(value="/addFavorito", method = RequestMethod.POST)
    public void anadirCarroFavorito(@RequestParam("name") String nombreFavorito,
                                    HttpServletRequest request, HttpServletResponse response){
        System.out.println("Me ha llegado peticion para añadir un carro favorito");

        ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");
        Usuario usuario = (Usuario) request.getSession().getAttribute("user");

        try{
            PrintWriter out;
            out = response.getWriter();

            if (repository.findByNombre(nombreFavorito) == null) {
                repository.save(new CarroFavorito(nombreFavorito, usuario, carroSesion));
                System.out.println("Carro favorito añadido");
                out.println("ok");
            } else {
                System.err.println("Carro favorito no añadido: ya existe un carro con ese nombre.");
                out.println("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Carga los carros favoritos de un usuario y lo almacena para devolverselo al cliente.
     *
     * @param model Contenedor para almacenar los carros favoritos devueltos al cliente
     * @param request request objeto request del usuario
     * @return pagina de carros favoritos
     */
    @RequestMapping(value="/carrosFavoritos")
    public String cargarFavoritos(Model model, HttpServletRequest request){
        System.out.println("Me ha llegado peticion para mostrar un carro favorito");


        List<CarroFavorito> listaFavoritos = repository.findAll();
        List<CarroFavorito> carrosFavoritos = new ArrayList<>();
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
