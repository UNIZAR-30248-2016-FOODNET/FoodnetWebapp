package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Product;
import es.unizar.es.foodnet.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.WebContext;

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
    private ProductRepository repository;

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

        Product p1 = new Product("Chope","Alcampo");
        Product p2 = new Product("Salchipapa","Carrefour");
        List<Product> products = repository.findAll();

        //WebContext ctx = new WebContext(request, response, servletContext);
        //ctx.setVariable("listica", products);
        //ctx.setVariable("producttype","new");

        model.addAttribute("listica", products);
        model.addAttribute("producttype","new");
        model.addAttribute("productObject",new Product());

        return "sample2";
    }

    @RequestMapping(value="/products/create", produces="application/json", method= RequestMethod.POST)
    public String create(Product product) {

        System.out.println("Petition to add product");
        System.out.println("Product name: " + product.getName());
        System.out.println("Product brand: " + product.getBrand());

        repository.save(product);
        return "redirect:/sample2";
    }

}
