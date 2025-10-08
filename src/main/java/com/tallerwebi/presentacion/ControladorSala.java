package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorSala {

    private ServicioPartida servicioPartida;

    @Autowired
    public ControladorSala(ServicioPartida servicioPartida){
        this.servicioPartida = servicioPartida;
    }


    @RequestMapping("/sala")
    public ModelAndView irASala() {


        return new ModelAndView("sala");
    }
    @RequestMapping(path = "/juego", method = RequestMethod.POST)
    public ModelAndView irAlJuego() {
        servicioPartida.crearPartida();
        return new ModelAndView("juego");

    }

}
