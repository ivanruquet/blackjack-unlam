package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControladorRegistro {

    @GetMapping("/registro")
    public ModelAndView registro() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("usuario", new Usuario());
        return new ModelAndView("registro", modelo);
    }

    }
