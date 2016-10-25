package es.unizar.es.foodnet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
