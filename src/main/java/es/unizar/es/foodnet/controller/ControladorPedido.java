package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Pedido;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.RepositorioPedido;
import es.unizar.es.foodnet.model.service.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de aquello relacionado con los pedidos, ya sea historial, cancelacion de pedidos, etc.
 */
@Controller
public class ControladorPedido {

    private final RepositorioPedido repository;

    @Autowired
    public ControladorPedido(RepositorioPedido repository) {
        this.repository = repository;
    }

    /**
     * Carga el historial de pedidos del usuario y lo almacena para devolverselo al cliente.
     * Además elimina los pedidos que no están asignados a ningún usuario (usuario eliminado).
     *
     * @param model Contenedor para almacenar los pedidos devueltos al cliente
     * @param request request objeto request del usuario
     * @return pagina del historial
     */
    @RequestMapping("/historial")
    public String cargarPedidos(Model model, HttpServletRequest request){
        System.out.println("Detectada peticion para mostrar el historial de pedidos.");

        List<Pedido> listaPedidos = repository.findAll();
        List<Pedido> historial = new ArrayList<Pedido>();
        Usuario u =(Usuario)request.getSession().getAttribute("user");
        for(Pedido p :listaPedidos){
            if (p.getUsuario() == null) {
                repository.delete(p);
            } else if (p.getUsuario().getEmail().equals(u.getEmail())){
                historial.add(p);
            }
        }

        model.addAttribute("historial", historial);

        return "historial";
    }

    /**
     * Cancela el pedido cuyo id es idPedido
     * @param idPedido id del pedido a cancelar
     * @param ra apoyo para feedback
     * @return redireccion al historial con feedback
     */
    @RequestMapping(value = "/cancelarPedido", method = RequestMethod.POST)
    public String cancelarPedido(@RequestParam("idPedido") String idPedido,
                                 RedirectAttributes ra){
        System.out.println("Detectada peticion para cancelar el pedido cuyo id es " + idPedido);

        Pedido pedido = repository.findById(idPedido);

        if(pedido.cancelarPedido()){
            MessageHelper.addSuccessAttribute(ra,"exito.pedido.cancelar","");
        } else MessageHelper.addErrorAttribute(ra,"fallo.pedido.cancelar",idPedido);

        repository.save(pedido);

        return "redirect:/historial";
    }
}