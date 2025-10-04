package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
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
        ModelMap modelo = new ModelMap();

        return new ModelAndView("sala", modelo);
    }
    @RequestMapping(path = "/juego", method = RequestMethod.POST)
    public ModelAndView irAlJuego() {
        ModelMap modelo = new ModelMap();
        if(servicioPartida.crearPartida()){
            return new ModelAndView("juego", modelo);
        }
        return new ModelAndView("sala");
    }

}
