package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador que se encarga de recibir las peticiones de los clientes y redirige
 * a la pagina concreta con los atributos necesarios obtenidos del repositorio.
 */
@Controller
public class ControladorProducto {

    @Autowired
    private RepositorioProducto repository;

    /**
     * Carga el catalogo de productos disponibles del repositorio y los almacena
     * para devolverselos al cliente
     * @param model Contenedor para almacenar los productos devueltos al cliente
     * @return pagina de catalogo
     */
    @RequestMapping("/catalogo")
    public String cargarCatalogo(Model model){
        System.out.println("Detectada peticion para mostrar el catalogo de " +
                "productos.");

        List<Producto> listProductos = repository.findAll();
        model.addAttribute("listaProductos", listProductos);

        return "catalogo";
    }
}