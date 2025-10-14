package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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


    @RequestMapping(path = "/juego", method = RequestMethod.POST)
    public ModelAndView irAlJuego(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if(usuario != null){
            try{
                servicioPartida.crearPartida(usuario);
                return new ModelAndView("juego");
            }catch(PartidaNoCreadaException e){
                return new ModelAndView("sala");
            }
        }
        return new ModelAndView("sala");

    }

}
