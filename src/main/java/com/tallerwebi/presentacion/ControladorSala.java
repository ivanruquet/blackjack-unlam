package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorSala {

    private ServicioPartida servicioPartida;

    @Autowired
    public ControladorSala(ServicioPartida servicioPartida){
        this.servicioPartida = servicioPartida;
    }

    @RequestMapping("/sala")
    public ModelAndView irASala() {


        return new ModelAndView("sala");
    }

    @RequestMapping(path = "/juegoConCrupier", method = RequestMethod.POST)
    public ModelAndView irAlJuegoConCrupier(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if(usuario != null){
            try{
                servicioPartida.crearPartida(usuario);
                modelo.addAttribute("saldo", usuario.getSaldo());
                return new ModelAndView("juegoConCrupier", modelo);
            } catch(PartidaNoCreadaException e){
                modelo.addAttribute("error", "No se pudo crear la partida.");
                return new ModelAndView("sala", modelo);
            }
        }

        modelo.addAttribute("error", "Debe iniciar sesión para jugar.");
        return new ModelAndView("sala", modelo);
    }


    @RequestMapping(path = "/juegoOnline", method = RequestMethod.POST)
    public ModelAndView irAlJuegoOnline(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (usuario != null) {
            try {
                servicioPartida.crearPartida(usuario);
                modelo.addAttribute("saldo", usuario.getSaldo());
                return new ModelAndView("juegoOnline", modelo);
            } catch (PartidaNoCreadaException e) {
                modelo.addAttribute("error", "No se pudo crear la partida.");
                return new ModelAndView("sala", modelo);
            }
        }

        modelo.addAttribute("error", "Debe iniciar sesión para jugar online.");
        return new ModelAndView("sala", modelo);
    }



}
