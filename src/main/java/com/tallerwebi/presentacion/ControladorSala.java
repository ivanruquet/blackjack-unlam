package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorSala {

    @RequestMapping("/sala")
    public ModelAndView irASala() {
        ModelMap modelo = new ModelMap();

        return new ModelAndView("sala", modelo);
    }
    @RequestMapping(path = "/juego", method = RequestMethod.POST)
    public ModelAndView irAlJuego() {
        ModelMap modelo = new ModelMap();
        return new ModelAndView("juego", modelo);
    }

}
