package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {
    private final ServicioLogin servicioLogin;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPerfil(ServicioLogin servicioLogin, ServicioUsuario servicioUsuario) {
        this.servicioLogin = servicioLogin;
        this.servicioUsuario = servicioUsuario;
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

    @PostMapping("/editar-Atributos")
    public String editarAtributos(@RequestParam String nombre, @RequestParam String apellido,
                                  @RequestParam String username,
                               HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setUsername(username);
            servicioUsuario.modificarAtributos(usuario, nombre, apellido, username);
            session.setAttribute("usuario", usuario);
        }
        return "redirect:/perfil";
    }


    @PostMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
