package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.ServicioTerminos;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


@Controller
public class ControladorTerminosYCondiciones {

    private final ServicioTerminos servicioTerminos;


    @RequestMapping ("/terminosYCondiciones")
    public ModelAndView mostrarTerminos(HttpSession session) {
        ModelAndView mav = new ModelAndView("terminosYCondiciones");

        Usuario email = (Usuario) session.getAttribute("email");

        boolean aceptado = false;
        if (email != null) {
            aceptado = servicioTerminos.verificarTerminosAceptados("email");
        }

        mav.addObject("aceptado", aceptado);
        return mav;
    }

    @Autowired
    public ControladorTerminosYCondiciones(ServicioTerminos servicioTerminos) {
        this.servicioTerminos = servicioTerminos;
    }

    @PostMapping("/aceptarTerminos")
    public ModelAndView aceptarTerminos(HttpSession session) {
        Usuario email = (Usuario) session.getAttribute("email");
        servicioTerminos.aceptarTerminos("email");

        return new ModelAndView("redirect:/sala");
    }

}



