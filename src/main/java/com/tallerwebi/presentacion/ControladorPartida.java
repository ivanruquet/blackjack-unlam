package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView comenzarPartida(HttpServletRequest request)
            throws PartidaActivaNoEnApuestaException, PartidaNoCreadaException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Jugador jugador= servicioPartida.crearJugador(usuario);
        Partida partida =  servicioPartida.instanciarPartida(jugador);
//        Partida partida = (Partida) request.getSession().getAttribute("partida");
        try {
            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
        } catch (PartidaExistenteActivaException e) {
            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
            partida = activas.get(0);
            servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
            request.getSession().setAttribute("partida", partida);
            //que el saldo del usuario se actualice
            //error 500 me sale violacion de restriccio - tabla usuario
            // servicioPartida.setearApuesta(usuario, monto, activas.get(0));
            //Se reparte cartas
        }

        partida.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        servicioPartida.setBotonesAlCrearPartida(partida);
        request.getSession().setAttribute("partida", partida);

//        servicioPartida.setearApuesta(usuario,  partida.getApuesta(), partida);

        cartasJugador = new ArrayList<>();
        cartasDealer = new ArrayList<>();
        //  se crea el mazo

        var mazo = servicioDeck.crearMazo();
        deckId = (String) mazo.get("deck_id");

        cartasJugador = servicioDeck.sacarCartas(deckId, 2);
        cartasDealer = servicioDeck.sacarCartas(deckId, 2);
//Calculo de puntaje basado en las cartas
        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);

//ahora tenemos que guardar ese puntaje en los jugadores
        partida.getJugador().setPuntaje(puntajeJugador);
        partida.getCrupier().setPuntaje(puntajeDealer);


        request.getSession().setAttribute("cartasJugador", cartasJugador);
        request.getSession().setAttribute("cartasDealer", cartasDealer);
// servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
        request.getSession().setAttribute("partida", partida);
        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("partida", partida);
        mav.addObject("usuario", usuario);
        mav.addObject("deckId", deckId);
        mav.addObject("cartasJugador", cartasJugador);
        mav.addObject("cartasDealer", cartasDealer);
        mav.addObject("puntajeJugador", puntajeJugador);
        mav.addObject("puntajeDealer", puntajeDealer);

        return mav;
    }



    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto)
            throws PartidaNoCreadaException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) session.getAttribute("partida");

        if (partida == null) {
            partida = servicioPartida.crearPartida(usuario);
            partida.setBotonesDesicionHabilitados(false);
            partida.setFichasHabilitadas(true);
            partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
            session.setAttribute("partida", partida);
        }

        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        partida.setJugador(jugador);

        ModelMap modelo = new ModelMap();
        modelo.addAttribute("usuario", usuario);

        try {
            servicioPartida.apostar(partida, monto);
            session.setAttribute("partida", partida);

            modelo.addAttribute("partida", partida);
            modelo.addAttribute("apuesta", partida.getApuesta());


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
        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);

        List<Map<String, Object>> cartasJugador =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");

        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        servicioPartida.setBotonesAlCrearPartida(partidaActiva);
        request.getSession().setAttribute("partida", partidaActiva);
        String mensajeEstrategia = servicioPartida.mandarEstrategia(partidaActiva, puntajeJugador,  puntajeDealer);

        modelo.put("mensajeEstrategia", mensajeEstrategia);
        modelo.addAttribute("partida", partidaActiva);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", puntajeJugador);
        modelo.addAttribute("puntajeDealer", puntajeDealer);
        modelo.addAttribute("cartasJugador", cartasJugador);
        modelo.addAttribute("cartasDealer", cartasDealer);
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
        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);

        List<Map<String, Object>> cartasJugador =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");

        String mensajeResultado= servicioPartida.resultadoDeLaPartida(puntajeDealer, puntajeJugador);

        modelo.put("mensajeResultado", mensajeResultado);
        modelo.addAttribute("partida", partidaActiva);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", puntajeJugador);
        modelo.addAttribute("puntajeDealer", puntajeDealer);
        modelo.addAttribute("cartasJugador", cartasJugador);
        modelo.addAttribute("cartasDealer", cartasDealer);
        request.getSession().setAttribute("partidaActiva", partidaActiva);

        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/rendirse")
    public ModelAndView rendirse(HttpServletRequest request){
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partidaActiva = servicioPartida.obtenerPartidaActiva(usuario);
        ModelMap modelo = new ModelMap();
        servicioPartida.rendirse(partidaActiva, partidaActiva.getJugador());
        request.getSession().removeAttribute("partidaActiva");
        return new ModelAndView("redirect:/sala", modelo);
    }


}