package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Pedido;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioPedido;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.service.MessageHelper;
import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlador encargado de lo relacionado con los carros
 */
@Controller
public class ControladorCarro {

    private final RepositorioProducto rp;

    private final RepositorioPedido repoPedidos;

    @Autowired
    public ControladorCarro(RepositorioProducto rp,RepositorioPedido repoPedidos) {
        this.rp = rp;
        this.repoPedidos=repoPedidos;
    }

    /**
     * Devuelve una vista ampliada del carro cuando se solicita
     */
    @RequestMapping(value = "/verCarro")
    public String verCarro(){
        System.out.println("Me ha llegado peticion para ver el carro al completo");
        return "carro";
    }

    /**
     * Mapeo para la pantalla de finalizar el pago
     */
    @RequestMapping(value="/finalizarPago")
    public String finalizarPago(HttpServletRequest request){
        System.out.println("Me ha llegado peticion para finalizar el pago");
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if(user!=null){
            return "finalizarCompra";
        } else{
            return "redirect:/panelLogin";
        }
    }

    /**
     * Mapeo para finalizar el pago utilizando un metodo de pago y guardar el pedido en el repositorio
     */
    @RequestMapping(value="/compraFinalizada", method = RequestMethod.POST)
    public String finCompra(HttpServletRequest request,RedirectAttributes ra){
        System.out.println("Me ha llegado peticion para pagar una compra ");
        ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");
        Usuario u =(Usuario)request.getSession().getAttribute("user");
        repoPedidos.save(new Pedido(u,new Date(),carroSesion));
        MessageHelper.addSuccessAttribute(ra,"exito.pago","");
        return "redirect:/";
    }

    /**
     * Mapeo para agregar un nuevo producto al carro de la compra
     * @param id id del producto a agregar
     */
    @RequestMapping(value="/addProductoCarro", method = RequestMethod.POST)
    public void addProductoCarro(@RequestParam("id")String id,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        System.out.println("Me ha llegado peticion para agregar un nuevo producto al carro de la compra");

        try {
            PrintWriter out = response.getWriter();
            Producto p = rp.findById(id);
            ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");
            Integer numProductos = (Integer) request.getSession().getAttribute("productosCarro");
            if(carroSesion!=null){
                //Ya existe el carro
                System.out.println("Carro existe, add");
                numProductos++;
                ArrayList<ProductoCarro> carroActualizado = actualizar(carroSesion,p);
                request.getSession().setAttribute("carroProductos",carroActualizado);
                request.getSession().setAttribute("productosCarro",numProductos);
                request.getSession().setAttribute("subtotal",actualizarSubtotal(carroActualizado));
                int indice = indiceExistencia(carroSesion, p);
                if(indice == -1){
                    out.print("nuevo, 1," + (carroActualizado.size()-1));
                } else out.print("existe," + carroActualizado.get(indice).getCantidadProducto() + "," + indice);
            } else{
                //No existe el carro
                System.out.println("No existe carro, creo uno nuevo");
                ArrayList<ProductoCarro> listaP = new ArrayList<>();
                listaP.add(new ProductoCarro(p,1));
                request.getSession().setAttribute("carroProductos",listaP);
                request.getSession().setAttribute("productosCarro",1);
                request.getSession().setAttribute("subtotal",p.getPrecio());
                out.print("nuevo, 1, 0");
            }
        } catch (IOException e) {
            System.err.println("Error al obtener la salida hacia el cliente");
        }
    }

    /**
     * Mapeo para actualizar las cantidades habiendolas modificado en el panel de la compra
     * @param request request del usuario
     * @param response response hacia el usuario
     * @param id id del producto a actualizar
     * @param cantidad nueva cantidad del producto
     */
    @RequestMapping(value="/actualizarCantidades", method = RequestMethod.POST)
    public void actualizarCantidades(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam("id")String id,
                                     @RequestParam("cantidad")int cantidad){
        System.out.println("Me ha llegado peticion para actualizar el carro de la compra");

        try {
            PrintWriter out = response.getWriter();
            ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");
            Producto p = rp.findById(id);

            ArrayList<ProductoCarro> carroNuevo = actualizar(carroSesion,p,cantidad);
            double subtotal = actualizarSubtotal(carroNuevo);
            int numProductos = numProductosCarro(carroNuevo);

            request.getSession().setAttribute("carroProductos",carroNuevo);
            request.getSession().setAttribute("productosCarro",numProductos);
            request.getSession().setAttribute("subtotal",subtotal);

            out.print(subtotal+","+numProductos);

        } catch (IOException e) {
            System.err.println("Error al obtener la salida hacia el cliente");
        }
    }

    /**
     * Mapeo para eliminar un producto del carro de la compra
     * @param id id del producto a eliminar
     */
    @RequestMapping(value="/eliminarProducto", method = RequestMethod.POST)
    public void eliminarProductoDelCarro(@RequestParam("id") String id,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        System.out.println("Me ha llegado peticion para eliminar un producto del carro de la compra");

        try {
            PrintWriter out = response.getWriter();
            Producto p = rp.findById(id);
            ArrayList<ProductoCarro> carroSesion = (ArrayList<ProductoCarro>) request.getSession().getAttribute("carroProductos");

            int indiceProducto = indiceExistencia(carroSesion,p);
            carroSesion.remove(indiceProducto);
            int numProductos = numProductosCarro(carroSesion);
            double subtotal = actualizarSubtotal(carroSesion);

            request.getSession().setAttribute("carroProductos",carroSesion);
            request.getSession().setAttribute("productosCarro",numProductos);
            request.getSession().setAttribute("subtotal",subtotal);

            out.print(subtotal+","+numProductos);

        } catch (IOException e) {
            System.err.println("Error al obtener la salida hacia el cliente");
        }
    }

    /**
     * Si existe el producto p en la lista pc, se actualizan sus cantidades con 1 mas, sino se agrega con cantidad 1.
     * @param pc carro de productos
     * @param p producto a agregar
     * @return nuevo carro
     */
    private ArrayList<ProductoCarro> actualizar(ArrayList<ProductoCarro> pc, Producto p){
        ArrayList<ProductoCarro> pc2 = (ArrayList<ProductoCarro>) pc.clone();
        int indice = indiceExistencia(pc,p);
        if(indice != -1){
            pc2.get(indice).setCantidadProducto(pc.get(indice).getCantidadProducto() + 1);
        } else{
            pc2.add(new ProductoCarro(p,1));
        }

        return pc2;
    }

    /**
     * Actualiza el producto p del carro pc con cantidad cantidades
     * @param pc carro de productos
     * @param p producto a agregar
     * @param cantidad nueva cantidad del producto
     * @return nuevo carro
     */
    private ArrayList<ProductoCarro> actualizar(ArrayList<ProductoCarro> pc, Producto p, Integer cantidad){
        ArrayList<ProductoCarro> pc2 = (ArrayList<ProductoCarro>) pc.clone();
        pc2.get(indiceExistencia(pc,p)).setCantidadProducto(cantidad);
        return pc2;
    }

    /**
     * Devuelve el indice de la lista pc donde se encuentra el objeto p
     * @param pc carro de compra
     * @param p producto
     * @return -1 si no esta, el indice en caso contrario
     */
    private int indiceExistencia(ArrayList<ProductoCarro> pc, Producto p){
        int indice = -1;
        for(int i = 0; i < pc.size(); i++){
            if(pc.get(i).getProducto().getId().equals(p.getId())){
                indice = i;
                break;
            }
        }
        return indice;
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
