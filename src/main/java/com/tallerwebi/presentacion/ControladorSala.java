package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaExistenteActivaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorSala {

    private ServicioPartida servicioPartida;
    private RepositorioPartida repositorioPartida;
    private ServicioUsuario servicioUsuario;

    public ControladorSala(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }

    @Autowired
    public ControladorSala(ServicioPartida servicioPartida, RepositorioPartida repositorioPartida, ServicioUsuario servicioUsuario) {
        this.servicioPartida = servicioPartida;
        this.repositorioPartida = repositorioPartida;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/sala")
    public ModelAndView irASala(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView mav = new ModelAndView("sala");
        mav.addObject("usuario", usuario);
        return mav;
    }


    @RequestMapping(path = "/juegoConCrupier", method = RequestMethod.POST)
    public ModelAndView irAlJuegoConCrupier(HttpServletRequest request) throws PartidaNoCreadaException {
        HttpSession session = request.getSession();

        Usuario usuario = (Usuario) session.getAttribute("usuario");


        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }
        Usuario usuarioActualizado = servicioUsuario.buscarUsuario(usuario.getEmail());
        session.setAttribute("usuario", usuarioActualizado);

        if (crearPartida(request)) return new ModelAndView("sala");
        Partida partida = (Partida) session.getAttribute("partida");

        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("partida", partida);
         mav.addObject("usuario", usuarioActualizado);
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("dto", new ComienzoCartasDTO());
        return mav;
    }


    private boolean crearPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        try {
            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
        } catch (PartidaExistenteActivaException e) {
            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
            servicioPartida.inactivarPartidas(activas);
            request.getSession().removeAttribute("partida");
          
        }
        try {
            Partida p = servicioPartida.instanciarPartida(servicioPartida.crearJugador(usuario));
            servicioPartida.setBotonesAlCrearPartida(p);
            request.getSession().setAttribute("partida", p);
        } catch (PartidaNoCreadaException e) {
            return true;
        }
        return false;
    }


}