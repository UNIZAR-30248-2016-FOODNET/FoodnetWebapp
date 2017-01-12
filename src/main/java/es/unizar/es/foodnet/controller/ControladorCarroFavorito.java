package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.CarroFavorito;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioCarroFavorito;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        System.out.println("hola1");
        for(CarroFavorito c :listaFavoritos){
            System.out.println("hola1");
            if (c.getUsuario().getEmail().equals(u.getEmail())){
                System.out.println("hola1");
                carrosFavoritos.add(c);
            }
        }

        model.addAttribute("carrosFavoritos", carrosFavoritos);

        return "carrosFavoritos";

    }


    /**
     * Borra un carro favorito existente en el sistema
     */
    @RequestMapping(value = "/borrarCarroFavorito", method = RequestMethod.POST)
    public void borrarCarroFavorito(HttpServletResponse response,HttpServletRequest request,
                                    @RequestParam("nombre") String nombreCarroFavorito){
        System.out.println("Me ha llegado peticion para borrar el carro favorito cuyo nombre es " + nombreCarroFavorito);

        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if(user != null){
            CarroFavorito cf = repository.findByNombre(nombreCarroFavorito);
            if(cf.getUsuario().getEmail().equals(user.getEmail())){
                repository.delete(cf);
            }
        }
    }

    /**
     * Mapeo para agregar los productos de un carro favorito al carro actual
     * @param nombre nombre del carro favorito a agregar
     */
    @RequestMapping(value="/addFavoritoCarro", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<ProductoCarro> addFavoritoACarro(@RequestParam("name") String nombre,
                                          HttpServletRequest request) {
        System.out.println("Petición para agregar carro favorito a carrito actual.");

        //PrintWriter out = response.getWriter();
        CarroFavorito carroFavorito = repository.findByNombre(nombre);
        ArrayList<ProductoCarro> carroF = (ArrayList<ProductoCarro>) carroFavorito.getProductos();
        ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");

        if(carroSesion != null) {
            System.out.println("Carro existe, add");
            for(ProductoCarro productoCarro : carroF) {
                boolean repe = false;
                for(ProductoCarro productoCarroS : carroSesion) {
                    //Si el producto a añadir ya se encuentra en el carrito de la sesión, añade la cantidad
                    if(productoCarro.getProducto().getId().equals(productoCarroS.getProducto().getId())) {
                        productoCarroS.setCantidadProducto(productoCarroS.getCantidadProducto()
                                + productoCarro.getCantidadProducto());
                        repe = true;
                    }
                }
                if (!repe) {
                    carroSesion.add(productoCarro);
                }
            }
            int numProductos = numProductosCarro(carroSesion);
            request.getSession().setAttribute("carroProductos", carroSesion);
            request.getSession().setAttribute("productosCarro", numProductos);
            request.getSession().setAttribute("subtotal", actualizarSubtotal(carroSesion));
            return carroSesion;
        } else {
            System.out.println("Carro no existe, creandolo");
            int numProductos = numProductosCarro(carroF);
            request.getSession().setAttribute("carroProductos", carroF);
            request.getSession().setAttribute("productosCarro", numProductos);
            request.getSession().setAttribute("subtotal", actualizarSubtotal(carroF));

            return carroF;
        }
    }

    /**
     * Devuelve el total de la compra contenida en carroCompra
     * @param carroCompra carro de la compra
     * @return total de la compra
     */
    private double actualizarSubtotal(ArrayList<ProductoCarro> carroCompra){
        double cuentaIntermedia = 0.0;
        for(ProductoCarro p : carroCompra){
            cuentaIntermedia = cuentaIntermedia + (((double) p.getCantidadProducto()) * p.getProducto().getPrecio());
        }
        return cuentaIntermedia;
    }

    /**
     * Devuelve el numero de productos en el carro
     * @param carro carro de la compra
     * @return numero de productos en el carro
     */
    private int numProductosCarro(ArrayList<ProductoCarro> carro){
        int cuenta = 0;
        for(ProductoCarro pc: carro){
            cuenta+=pc.getCantidadProducto();
        }
        return cuenta;
    }
}
