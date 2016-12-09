package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

/**
 * Controlador que se encarga de recibir las peticiones de los clientes y redirige
 * a la pagina concreta con los atributos necesarios obtenidos del repositorio.
 */
@Controller
public class ControladorProducto {

    private final RepositorioProducto repository;

    @Autowired
    public ControladorProducto(RepositorioProducto repository) {
        this.repository = repository;
    }

    /**
     * Carga el catalogo de productos disponibles del repositorio, los ordena según
     * el parámetro obtenido de la reques y los almacena para devolverselos al cliente
     * @param model Contenedor para almacenar los productos devueltos al cliente
     * @param request objeto request del usuario
     * @return pagina de catalogo
     */
    @RequestMapping("/")
    public String cargarCatalogo(Model model, HttpServletRequest request){
        System.out.println("Detectada peticion para mostrar el catalogo de " +
                "productos.");

        List<Producto> listProductos = repository.findAll();
        if (request.getParameter("ordenacion") != null) {
            String tipoOrdenacion = request.getParameter("ordenacion");
            if (tipoOrdenacion.trim().equals("preciomenormayor")) {
                ordenarPrecioMenorMayor(listProductos);
            } else if (tipoOrdenacion.trim().equals("preciomayormenor")) {
                ordenarPrecioMayorMenor(listProductos);
            } else if (tipoOrdenacion.trim().equals("nombremayormenor")) {
                ordenarNombreMayorMenor(listProductos);
            }
        } else{
            ordenarNombreMenorMayor(listProductos);
        }

        model.addAttribute("listaProductos", listProductos);

        return "catalogo";
    }

    /**
     * Ordena la lista de productos por precio de menor a mayor.
     * @param lista lista de productos
     * @return lista ordenada por precio de menor a mayor
     */
    private List<Producto> ordenarPrecioMenorMayor (List<Producto> lista) {
        Collections.sort(lista, (p1, p2) ->{
            double precio1 = p1.getPrecio();
            double precio2 = p2.getPrecio();

            if (precio1 <= precio2) {
                return -1;
            } else {
                return 1;
            }
        });
        return lista;
    }

    /**
     * Ordena la lista de productos por precio de mayor a menor.
     * @param lista lista de productos
     * @return lista ordenada por precio de mayor a menor
     */
    private List<Producto> ordenarPrecioMayorMenor (List<Producto> lista) {
        Collections.sort(lista, (p1, p2) ->{
            double precio1 = p1.getPrecio();
            double precio2 = p2.getPrecio();

            if (precio1 <= precio2) {
                return 1;
            } else {
                return -1;
            }
        });
        return lista;
    }

    /**
     * Ordena la lista de productos alfabeticamente según el nombre de
     * mayor a menor.
     * @param lista lista de productos
     * @return lista ordenada por nombre de mayor a menor
     */
    private List<Producto> ordenarNombreMayorMenor (List<Producto> lista) {
        Collections.sort(lista, (p1, p2) ->{
            String nombre1 = p1.getNombre();
            String nombre2 = p2.getNombre();

            return nombre2.compareTo(nombre1);
        });
        return lista;
    }

    /**
     * Ordena la lista de productos alfabeticamente según el nombre de
     * menor a mayor.
     * @param lista lista de productos
     * @return lista ordenada por nombre de menor a mayor
     */
    private List<Producto> ordenarNombreMenorMayor (List<Producto> lista) {
        Collections.sort(lista, (p1, p2) ->{
            String nombre1 = p1.getNombre();
            String nombre2 = p2.getNombre();

            return nombre1.compareTo(nombre2);
        });
        return lista;
    }
}