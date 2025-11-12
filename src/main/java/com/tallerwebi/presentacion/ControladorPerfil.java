package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Controller
public class ControladorPerfil {
    private final ServicioLogin servicioLogin;
    private final ServicioUsuario servicioUsuario;
    @Autowired
    private RepositorioUsuarioImpl repositorioUsuario;

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

        Usuario usuarioActualizado = servicioUsuario.buscarUsuario(usuarioSesion.getEmail());
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



    @PostMapping("/reclamar-recompensa")
    @Transactional
    public String reclamarRecompensa(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null &&
                Boolean.TRUE.equals(usuario.getLogro5partidas()) &&
                Boolean.TRUE.equals(usuario.getLogroGanar2Manos())) {

            usuario.setSaldo(usuario.getSaldo() + 1000);

            usuario.setPartidasMeta(usuario.getPartidasMeta() * 2);
            usuario.setManosMeta(usuario.getManosMeta() * 2);

            usuario.setLogro5partidas(false);
            usuario.setLogroGanar2Manos(false);

            usuario.setRecompensaReclamada(true);

            repositorioUsuario.actualizar(usuario);
        }

        session.setAttribute("usuario", usuario);
        return "redirect:/perfil";
    }










}
