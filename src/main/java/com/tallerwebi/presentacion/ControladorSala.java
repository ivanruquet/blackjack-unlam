package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaExistenteActivaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (crearPartida(request)) return new ModelAndView("sala");
        ModelAndView mav =  new ModelAndView("juegoConCrupier");
        mav.addObject("usuario", usuario);
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
            List<Partida> activas = repositorioPartida.buscarPartidaActiva(usuario);
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