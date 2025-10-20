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

    @Autowired
    public ControladorSala(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }

    public ControladorSala(ServicioPartida servicioPartida, RepositorioPartida repositorioPartida) {
        this.servicioPartida = servicioPartida;
        this.repositorioPartida = repositorioPartida;
    }

    @RequestMapping("/sala")
    public ModelAndView irASala() {
        return new ModelAndView("sala");
    }


    @RequestMapping(path = "/juegoConCrupier", method = RequestMethod.POST)
    public ModelAndView irAlJuegoConCrupier(HttpServletRequest request) throws PartidaNoCreadaException {
        if (crearPartida(request)) return new ModelAndView("sala");
        return new ModelAndView("juegoConCrupier");
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