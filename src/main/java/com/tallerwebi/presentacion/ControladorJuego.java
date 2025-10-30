package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
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
@RequestMapping("/juego")
public class ControladorJuego {

    private ServicioPartida servicioPartida;

    public ControladorJuego(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }


    @PostMapping("/iniciar")
    public ModelAndView iniciarPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario != null) {
            try {
                servicioPartida.crearPartida(usuario);
                return new ModelAndView("juegoConCrupier");
            } catch (PartidaNoCreadaException e) {
                ModelMap modelo = new ModelMap();
                modelo.addAttribute("error", "No se pudo crear la partida.");
                return new ModelAndView("sala", modelo);
            }
        }

        ModelMap modelo = new ModelMap();
        modelo.addAttribute("error", "Debe iniciar sesión para jugar.");
        return new ModelAndView("sala", modelo);
    }


    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        try {
            servicioPartida.validarPartida(usuario, monto);
            servicioPartida.apostar(usuario, monto);


            request.getSession().setAttribute("usuario", usuario);

            modelo.addAttribute("saldo", usuario.getSaldo());
        } catch (ApuestaInvalidaException | SaldoInsuficiente e) {
            modelo.addAttribute("errorServidor", e.getMessage());
        }

        return new ModelAndView("juegoConCrupier", modelo);
    }



    @PostMapping("/reset")
    public ModelAndView resetearPartida(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario != null) {
            servicioPartida.resetearPartida(usuario);
        }
        return new ModelAndView("redirect:/juegoConCrupier");
    }

}
