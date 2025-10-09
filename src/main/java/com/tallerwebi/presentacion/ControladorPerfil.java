package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/perfil")
    public ModelAndView irAPerfil(HttpSession session) {
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        if (usuarioSesion == null) {
            return new ModelAndView("redirect:/login");
        }

        Usuario usuarioActualizado = servicioUsuario.buscarPorEmail(usuarioSesion.getEmail());
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
