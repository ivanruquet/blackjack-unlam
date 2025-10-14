package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioDeckOfCards;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ControladorHome {


    @RequestMapping("/home")
    public ModelAndView irHome() {
        ModelMap modelo = new ModelMap();

        return new ModelAndView("home", modelo);
    }


}
