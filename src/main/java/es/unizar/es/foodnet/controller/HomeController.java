package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Fran Menendez Moya on 5/10/16.
 */
@Controller
public class HomeController {

    @Autowired
    private RepositorioProducto repository;

    @RequestMapping("/")
    public String root(){
        System.out.println("Detectada peticion para ir a /");
        return "index";
    }

    @RequestMapping("/sample1")
    public String sample1(){
        System.out.println("Detectada peticion para ir a sample1");
        return "sample1";
    }

    @RequestMapping("/sample2")
    public String sample2(Model model, HttpServletRequest request, HttpServletResponse response,
                          ServletContext servletContext){
        System.out.println("Detectada peticion para ir a sample2");

        //Producto p1 = new Producto("Chope","Alcampo");
        //Producto p2 = new Producto("Salchipapa","Carrefour");
        List<Producto> productos = repository.findAll();

        //WebContext ctx = new WebContext(request, response, servletContext);
        //ctx.setVariable("listica", productos);
        //ctx.setVariable("producttype","new");

        model.addAttribute("listica", productos);
        model.addAttribute("producttype","new");
        model.addAttribute("productObject",new Producto());

        return "sample2";
    }

    @RequestMapping(value="/products/create", produces="application/json", method= RequestMethod.POST)
    public String create(Producto producto) {

        System.out.println("Petition to add producto");
        System.out.println("Producto name: " + producto.getNombre());
        System.out.println("Producto brand: " + producto.getSupermercado().getNombre());

        repository.save(producto);
        return "redirect:/sample2";
    }

}
