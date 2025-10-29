package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPartida {

    private ServicioPartida servicioPartida;

    public ControladorPartida(ServicioPartida servicioPartida) {
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

    @PostMapping("/mostrarEstrategia")
    public ModelAndView mostrarEstrategia(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (usuario == null) {
            modelo.addAttribute("error", "Inicia sesión para ver la estrategia");
            return new ModelAndView("redirect:/login", modelo);
        }

        Partida partidaActiva = servicioPartida.obtenerPartidaActiva(usuario);
        if (partidaActiva == null) {
            modelo.addAttribute("error", "No hay partida activa.");
            return new ModelAndView("sala", modelo);
        }

        Jugador jugador = partidaActiva.getJugador();
        String mensajeEstrategia = servicioPartida.mandarEstrategia(partidaActiva, jugador);


        modelo.put("mensajeEstrategia", mensajeEstrategia);
        modelo.addAttribute("partida", partidaActiva);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);

        request.getSession().setAttribute("partidaActiva", partidaActiva);

        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/doblarApuesta")
    public ModelAndView doblarApuesta(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (usuario == null) {
            modelo.addAttribute("error", "Inicia sesión para ver la estrategia");
            return new ModelAndView("redirect:/login", modelo);
        }

        Partida partidaActiva = servicioPartida.obtenerPartidaActiva(usuario);
        if (partidaActiva == null) {
            modelo.addAttribute("error", "No hay partida activa.");
            return new ModelAndView("sala", modelo);
        }

        Jugador jugador = partidaActiva.getJugador();
        Double resultado = servicioPartida.doblarApuesta(partidaActiva, jugador);
        modelo.put("resultado", resultado);

        request.getSession().setAttribute("partidaActiva", partidaActiva);
        return new ModelAndView("juegoConCrupier", modelo);

    }
    @PostMapping("/pararse")
    public ModelAndView pararse(HttpServletRequest request){
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (usuario == null) {
            modelo.addAttribute("error", "Inicia sesión para ver la estrategia");
            return new ModelAndView("redirect:/login", modelo);
        }

        Partida partidaActiva = servicioPartida.obtenerPartidaActiva(usuario);
        if (partidaActiva == null) {
            modelo.addAttribute("error", "No hay partida activa.");
            return new ModelAndView("sala", modelo);
        }

        Jugador jugador = partidaActiva.getJugador();
        String mensajeEstrategia = servicioPartida.resultadoDeLaPartida(partidaActiva.getCrupier().getPuntaje(), jugador.getPuntaje());

        modelo.put("mensajeResultado", mensajeEstrategia);
        modelo.addAttribute("partida", partidaActiva);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);

        request.getSession().setAttribute("partidaActiva", partidaActiva);

        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/rendirse")
    public ModelAndView rendirse(HttpServletRequest request){
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelAndView modelo = new ModelAndView();
        modelo.setViewName("redirect:/sala");
        return modelo;
    }

}
