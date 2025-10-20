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
public class ControladorJuego {

    private ServicioPartida servicioPartida;

    public ControladorJuego(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }


    @RequestMapping("/juegoConCrupier")
    public ModelAndView iraJuego() {
        ModelMap modelo = new ModelMap();
        return new ModelAndView("juegoConCrupier", modelo);
    }




    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto) throws ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (usuario != null) {
            servicioPartida.apostar(usuario, monto);
            return new ModelAndView("redirect:/juegoConCrupier");
        }

        try {
            servicioPartida.validarPartida(usuario, monto);
            modelo.addAttribute("mensajeExito", "Apuesta realizada");
            modelo.addAttribute("saldo", usuario.getSaldo());
        }catch (ApuestaInvalidaException e) {
            modelo.addAttribute("error", "El monto de la apuesta no es valido");
        }catch (SaldoInsuficiente saldoIns){
            modelo.addAttribute("erroSaldo", "Saldo insuficiente");
        }

        return new ModelAndView("juego", modelo);

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
