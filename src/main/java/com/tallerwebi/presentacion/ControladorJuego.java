package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Service
@Controller
public class ControladorJuego {

    private ServicioPartida servicioPartida;

    public ControladorJuego(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }


    @RequestMapping("/juego")
    public ModelAndView irAReglas() {
        ModelMap modelo = new ModelMap();
        return new ModelAndView("juego", modelo);
    }


    @PostMapping("/iniciar")
    public ModelAndView iniciarPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            try {
                servicioPartida.crearPartida(usuario);
            } catch (PartidaNoCreadaException e) {
                return new ModelAndView("redirect:/sala");
            }
        }
        return new ModelAndView("redirect:/juego");
    }



    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            servicioPartida.apostar(usuario, monto);
        }
        return new ModelAndView("redirect:/juego");
    }


    @PostMapping("/reset")
    public ModelAndView resetearPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            servicioPartida.resetearPartida(usuario);
        }
        return new ModelAndView("redirect:/juego");
    }




}
