package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorPartida {
    private List<Map<String, Object>> cartasJugador = new ArrayList<>();
    private List<Map<String, Object>> cartasDealer = new ArrayList<>();
    private ServicioDeckOfCards servicioDeck;
    private ServicioPartida servicioPartida;
    private ServicioUsuario servicioUsuario;
    private String deckId;

    @Autowired
    public ControladorPartida(ServicioDeckOfCards servicioDeck,ServicioPartida servicioPartida, ServicioUsuario servicioUsuario) {
        this.servicioDeck = servicioDeck;
        this.servicioPartida = servicioPartida;
        this.servicioUsuario = servicioUsuario;

    }
    public ControladorPartida(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }


    @RequestMapping("/juegoConCrupier")
    public ModelAndView iraJuego() {
        ModelMap modelo = new ModelMap();
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


    @PostMapping("/iniciar")
    public ModelAndView comenzarPartida(HttpServletRequest request,  @RequestParam("monto") Integer monto)
            throws PartidaActivaNoEnApuestaException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        cartasJugador = new ArrayList<>();
        cartasDealer = new ArrayList<>();
        //  se crea el mazo
        var mazo = servicioDeck.crearMazo();
        deckId = (String) mazo.get("deck_id");
        try {
            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
        } catch (PartidaExistenteActivaException e) {
            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
            servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(activas.get(0));
            //que el saldo del usuario se actualice
            //error 500 me sale violacion de restriccio - tabla usuario
            // servicioPartida.setearApuesta(usuario, monto, activas.get(0));
            //Se reparte cartas

            cartasJugador = servicioDeck.sacarCartas(deckId, 2);
            cartasDealer = servicioDeck.sacarCartas(deckId, 2);
//Calculo de puntaje basado en las cartas

            int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
            int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);
//ahora tenemos que guardar ese puntaje en los jugadores

// servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
            ModelAndView mav = new ModelAndView("juegoConCrupier");
            mav.addObject("usuario", usuario);
            mav.addObject("deckId", deckId);
            mav.addObject("cartasJugador", cartasJugador);
            mav.addObject("cartasDealer", cartasDealer);
            mav.addObject("puntajeJugador", puntajeJugador);
            mav.addObject("puntajeDealer", puntajeDealer);
            return mav;

        }
        return new ModelAndView();
    }



    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto) throws PartidaNoCreadaException, ApuestaInvalidaException, SaldoInsuficiente{

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Partida partida = servicioPartida.crearPartida(usuario);
        request.getSession().setAttribute("partida", partida);

        ModelMap modelo = new ModelMap();

        try {
            servicioPartida.apostar(partida, partida.getApuesta(), monto);

        } catch (ApuestaInvalidaException e) {
            modelo.addAttribute("error", "El monto de la apuesta no es válido");
        } catch (SaldoInsuficiente e) {
            modelo.addAttribute("errorSaldo", "Saldo insuficiente");
        }

        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/mostrarEstrategia")
    public ModelAndView mostrarEstrategia(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        //servicio y excep
        if (usuario == null) {
            modelo.addAttribute("error", "Inicia sesión para ver la estrategia");
            return new ModelAndView("redirect:/login", modelo);
        }

        Partida partidaActiva = servicioPartida.obtenerPartidaActiva(usuario);
        if (partidaActiva == null) {
            modelo.addAttribute("error", "No hay partida activa.");
            return new ModelAndView("sala", modelo);
        }
//-----
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

    @ExceptionHandler(PartidaNoEncontradaException.class)
    public ModelAndView manejarPartidaNoEncontrada(PartidaNoEncontradaException e) {
        ModelAndView mav = new ModelAndView("redirect:/inicio");
        mav.addObject("error", e.getMessage());
        return mav;
    }

}