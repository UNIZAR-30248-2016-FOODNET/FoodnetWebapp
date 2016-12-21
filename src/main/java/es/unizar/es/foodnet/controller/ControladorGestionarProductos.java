package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import es.unizar.es.foodnet.model.service.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lo relacionado con la gestion de los productos del sistema
 */

@Controller
public class ControladorGestionarProductos {

    @Autowired private RepositorioProducto repositorioProducto;
    @Autowired private RepositorioCategoria repositorioCategoria;
    @Autowired private RepositorioSupermercado repositorioSupermercado;

    /**
     * Devuelve todos los productos del sistema
     * @return html de la gestion de productos
     */
    @RequestMapping(value = "/gestionarProductos")
    public String gestionarProductos(HttpServletRequest request, RedirectAttributes ra, Model model){
        System.out.println("Me ha llegado peticion para cargar el panel de gestion de productos");
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if(user != null && user.isAdmin()){
            model.addAttribute("listaProductos",repositorioProducto.findAll());
            return "gestionarProductos";
        } else{
            MessageHelper.addErrorAttribute(ra,"fallo.gestionarProductos.admin","");
            return "redirect:/";
        }
    }

    /**
     * Devuelve todos los supermercados existentes en el sistema
     */
    @RequestMapping(value = "/supermercado", method = RequestMethod.POST)
    public void getSupermercados(HttpServletResponse response){

        System.out.println("Me ha llegado peticion para cargar los supermercados");
        List<String> supermercados = repositorioSupermercado.findAll().stream()
                                        .map(Supermercado::getNombre)
                                        .collect(Collectors.toList());

        try {
            PrintWriter out = response.getWriter();
            out.println("<option value = \"\" selected disabled>Elija un supermercado</option>");

            for (String supermercado : supermercados) {
                out.println("<option value=\"" + supermercado + "\">" + supermercado + "</option>");
            }
            out.close();
        } catch (Exception e) {
            System.err.println("Error al operar con los supermercados");
        }
    }

    /**
     * Devuelve todas las categorias existentes en el sistema
     */
    @RequestMapping(value = "/categoria", method = RequestMethod.POST)
    public void getCategorias(HttpServletResponse response){

        System.out.println("Me ha llegado peticion para cargar las categorias");
        List<String> categorias = repositorioCategoria.findAll().stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toList());

        try {
            PrintWriter out = response.getWriter();
            out.println("<option value = \"\" selected disabled>Elija una categoria</option>");

            for (String categoria : categorias) {
                out.println("<option value=\"" + categoria + "\">" + categoria + "</option>");
            }
            out.close();
        } catch (Exception e) {
            System.err.println("Error al operar con las categorias");
        }
    }

    /**
     * Inserta un nuevo producto en el sistema
     */
    @RequestMapping(value = "/agregarNuevoProducto", method = RequestMethod.POST)
    public String addNuevoProducto(HttpServletResponse response,
                                 @RequestParam("nombre")String nombre,
                                 @RequestParam("supermercado")String supermercado,
                                 @RequestParam("categoria")String categoria,
                                 @RequestParam("precio")String precio,
                                 @RequestParam("descripcion")String descripcion){

        precio = precio.replace(",",".");
        precio = precio.replace("€","");

        System.out.println("Me ha llegado peticion para insertar un nuevo producto en la bbdd: "
                            + nombre + " " + supermercado + " " + categoria + " " + precio + " " + descripcion);

        Supermercado s = repositorioSupermercado.findByNombre(supermercado);
        Categoria c = repositorioCategoria.findByNombre(categoria);

        if(s!= null && c != null){
            Producto p = new Producto(c,s,nombre,Double.parseDouble(precio),
                    "http://placehold.it/650x450&text="+nombre,descripcion);
            repositorioProducto.save(p);

            return "redirect:/gestionarProductos";

        } else{
            System.err.println("Error: O la categoria o el supermercado o ambos son nulos");
            return "redirect:/gestionarProductos";
        }
    }

    /**
     * Actualiza un producto existente en el sistema
     */
    @RequestMapping(value = "/actualizarProducto", method = RequestMethod.POST)
    public void actualizarProducto(HttpServletResponse response,
                                    @RequestParam("id")String idProducto,
                                    @RequestParam("nombre")String nombre,
                                    @RequestParam("supermercado")String supermercado,
                                    @RequestParam("categoria")String categoria,
                                    @RequestParam("precio")String precio){
        precio = precio.replace(",",".");
        precio = precio.replace("€","");

        System.out.println("Me ha llegado peticion para actualizar el producto cuyo id es "
                + idProducto + " con los campos : " + nombre + " " + supermercado + " " + categoria + " " + precio);

        Producto p = repositorioProducto.findById(idProducto);
        if(!supermercado.equals("")){
            p.setSupermercado(repositorioSupermercado.findByNombre(supermercado));
        }
        if(!categoria.equals("")){
            p.setCategoria(repositorioCategoria.findByNombre(categoria));
        }
        p.setNombre(nombre);
        p.setPrecio(Double.parseDouble(precio));
        p.setImagen("http://placehold.it/650x450&text="+nombre);
        repositorioProducto.save(p);
    }

    /**
     * Borra un producto existente en el sistema
     */
    @RequestMapping(value = "/borrarProducto", method = RequestMethod.POST)
    public void borrarProducto(HttpServletResponse response,
                               @RequestParam("id") String idProducto){
        System.out.println("Me ha llegado peticion para borrar el producto cuyo id es " + idProducto);
        repositorioProducto.delete(idProducto);
    }
}
