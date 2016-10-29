package es.unizar.es.foodnet.controller;

import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.service.MessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controlador encargado de lo relacionado con los carros
 */
@Controller
public class ControladorCarro {

    /**
     * Devuelve una vista ampliada del carro cuando se solicita
     */
    @RequestMapping(value = "/verCarro")
    public String verCarro(){
        System.out.println("Me ha llegado peticion para ver el carro al completo");
        return "carro";
    }

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

    @RequestMapping(value="/compraFinalizada", method = RequestMethod.POST)
    public String finCompra(RedirectAttributes ra){
        System.out.println("Me ha llegado peticion para pagar la compra");
        MessageHelper.addSuccessAttribute(ra,"exito.pago","");
        return "redirect:/";
    }
}
