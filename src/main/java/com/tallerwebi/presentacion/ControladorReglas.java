package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorReglas {

    @RequestMapping("/reglas")
    public ModelAndView irAReglas() {
        ModelMap modelo = new ModelMap();
        return new ModelAndView("reglas", modelo);
    }



}
