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


    public ControladorSala(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }

    @Autowired
    public ControladorSala(ServicioPartida servicioPartida, RepositorioPartida repositorioPartida) {
        this.servicioPartida = servicioPartida;
        this.repositorioPartida = repositorioPartida;
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
        Partida partida = (Partida) session.getAttribute("partida");

        if (partida == null) {
            partida = new Partida();
            partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
            servicioPartida.setBotonesAlCrearPartida(partida);
            session.setAttribute("partida", partida);
        }

        if (crearPartida(request)) return new ModelAndView("sala");

        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("usuario", usuario);
        mav.addObject("partida", partida);
        return mav;
    }




    @RequestMapping(path = "/juegoOnline", method = RequestMethod.POST)
    public ModelAndView irAlJuegoOnline(HttpServletRequest request) {
        if (crearPartida(request)) return new ModelAndView("sala");
        return new ModelAndView("juegoOnline");
    }

    private boolean crearPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        try {
            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
        } catch (PartidaExistenteActivaException e) {
            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
            servicioPartida.inactivarPartidas(activas);
        }
        try {
            servicioPartida.instanciarPartida(servicioPartida.crearJugador(usuario));
        } catch (PartidaNoCreadaException e) {
            return true;
        }
        return false;
    }


}