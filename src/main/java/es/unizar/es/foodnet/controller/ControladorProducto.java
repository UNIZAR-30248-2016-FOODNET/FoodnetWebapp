package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ControladorProducto {

    @Autowired
    private RepositorioProducto repository;

    @RequestMapping("/catalogo")
    public String cargarCatalogo(Model model){
        System.out.println("Detectada peticion para mostrar el catalogo de " +
                "productos.");

        List<Producto> listProductos = repository.findAll();

        model.addAttribute("listaProductos", listProductos);

        return "catalogo";
    }
}
