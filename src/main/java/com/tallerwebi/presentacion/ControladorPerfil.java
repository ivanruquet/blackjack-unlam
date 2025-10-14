package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {
    private final ServicioLogin servicioLogin;

    @Autowired
    public ControladorPerfil(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/perfil")
    public ModelAndView irAPerfil(HttpSession session) {
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        if (usuarioSesion == null) {
            return new ModelAndView("redirect:/login");
        }

        Usuario usuarioActualizado = servicioLogin.buscarPorEmail(usuarioSesion.getEmail());
        if (usuarioActualizado == null) {
            session.invalidate();
            return new ModelAndView("redirect:/login");
        }

        session.setAttribute("usuario", usuarioActualizado);

        ModelAndView mav = new ModelAndView("perfil");
        mav.addObject("usuario", usuarioActualizado);
        return mav;
    }

    @PostMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
