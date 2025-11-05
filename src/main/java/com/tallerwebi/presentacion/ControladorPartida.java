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
    public ModelAndView iraJuego(HttpServletRequest request) {
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
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Partida partida = (Partida) session.getAttribute("partida");
        servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
        servicioPartida.setBotonesAlCrearPartida(partida);
        ComienzoCartasDTO dto = servicioPartida.repartoInicial(partida.getId());



        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("partida", partida);
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("usuario", usuario);
        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        mav.addObject("dto", dto);
      //  mav.addObject("cartasJugador", dto.getCartasJugador());
//        mav.addObject("cartasDealer", dto.getCartasDealer());
//        mav.addObject("puntajeJugador", dto.getPuntajeJugador());
//        mav.addObject("puntajeDealer", dto.getPuntajeDealer());



        return mav ;
//---------
//        try {
//            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
//        } catch (PartidaExistenteActivaException e) {
//            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
//            partida = activas.get(0);
//            servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
//            request.getSession().setAttribute("partida", partida);
//            //que el saldo del usuario se actualice
//            //error 500 me sale violacion de restriccio - tabla usuario
//            // servicioPartida.setearApuesta(usuario, monto, activas.get(0));
//            //Se reparte cartas
//        }


        //-----------------Con todo esto funcionan los botones (me faltan doblar y dividir)
//        Partida partida = (Partida) request.getSession().getAttribute("partida");
//
 //

//        request.getSession().setAttribute("partida", partida);
//
//        cartasJugador = new ArrayList<>();
//        cartasDealer = new ArrayList<>();
//
//        var mazo = servicioDeck.crearMazo();
//        deckId = (String) mazo.get("deck_id");
//        request.getSession().setAttribute("deckId", deckId);
//
//        cartasJugador = servicioDeck.sacarCartas(deckId, 2);
//        cartasDealer = servicioDeck.sacarCartas(deckId, 2);
//        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
//        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);
//
//        partida.getJugador().setPuntaje(puntajeJugador);
//        partida.getCrupier().setPuntaje(puntajeDealer);
//
//
//        request.getSession().setAttribute("cartasJugador", cartasJugador);
//        request.getSession().setAttribute("cartasDealer", cartasDealer);
//        ModelAndView mav = new ModelAndView("juegoConCrupier");
//        mav.addObject("partida", partida);
//        mav.addObject("usuario", usuario);
//        mav.addObject("deckId", deckId);
//        mav.addObject("jugador", partida.getJugador());
//        mav.addObject("cartasJugador", cartasJugador);
//        mav.addObject("cartasDealer", cartasDealer);
//        mav.addObject("puntajeJugador", puntajeJugador);
//        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
//        mav.addObject("puntajeDealer", puntajeDealer);
//
 //        return mav;
        //-------------------
    }



    @PostMapping("/apostar")
    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto)
            throws PartidaNoCreadaException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");

        ModelMap modelo = new ModelMap();
        try {
            servicioPartida.apostar(partida, monto);
            //se agrega el jugador al modelo para que muestre su saldo a la vista -valentina
            modelo.addAttribute("jugador", partida.getJugador());
            modelo.addAttribute("usuario", usuario);

           // session.setAttribute("partida", partida);
            modelo.addAttribute("partida", partida);
            modelo.addAttribute("apuesta", partida.getApuesta());
            modelo.addAttribute("dto", new ComienzoCartasDTO());

        } catch (ApuestaInvalidaException e) {
            modelo.addAttribute("error", "El monto de la apuesta no es válido");
        } catch (SaldoInsuficiente e) {
            modelo.addAttribute("errorSaldo", "Saldo insuficiente");
        }

        return new ModelAndView("juegoConCrupier", modelo);
    }



    @PostMapping("/mostrarEstrategia")
    public ModelAndView mostrarEstrategia(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");

        ModelMap modelo = new ModelMap();

        Jugador jugador = partida.getJugador();
        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);

        List<Map<String, Object>> cartasJugador =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");

        partida.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        servicioPartida.setBotonesAlCrearPartida(partida);
        request.getSession().setAttribute("partida", partida);
        String mensajeEstrategia = servicioPartida.mandarEstrategia(partida, puntajeJugador,  puntajeDealer);

        modelo.put("mensajeEstrategia", mensajeEstrategia);
        modelo.addAttribute("partida", partida);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", puntajeJugador);
        modelo.addAttribute("puntajeDealer", puntajeDealer);
        modelo.addAttribute("cartasJugador", cartasJugador);
        modelo.addAttribute("cartasDealer", cartasDealer);
        modelo.addAttribute("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        request.getSession().setAttribute("partidaActiva", partida);


        return new ModelAndView("juegoConCrupier", modelo);
    }



    @PostMapping("/doblarApuesta")
    public ModelAndView doblarApuesta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        ModelMap modelo = new ModelMap();


        Jugador jugador = partida.getJugador();
        Integer resultado = servicioPartida.doblarApuesta(partida, jugador);
        modelo.put("resultado", resultado);
        modelo.addAttribute("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());

        request.getSession().setAttribute("partidaActiva", partida);
        return new ModelAndView("juegoConCrupier", modelo);

    }

    @PostMapping("/pararse")
    public ModelAndView pararse(HttpServletRequest request){
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        ModelMap modelo = new ModelMap();

        Jugador jugador = partida.getJugador();
        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);

        List<Map<String, Object>> cartasJugador =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer =
                (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");

        String mensajeResultado= servicioPartida.resultadoDeLaPartida(puntajeDealer, puntajeJugador);

        modelo.put("mensajeResultado", mensajeResultado);
        modelo.addAttribute("partida", partida);
        modelo.addAttribute("jugador", jugador);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", puntajeJugador);
        modelo.addAttribute("puntajeDealer", puntajeDealer);
        modelo.addAttribute("cartasJugador", cartasJugador);
        modelo.addAttribute("cartasDealer", cartasDealer);
        modelo.addAttribute("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        request.getSession().setAttribute("partidaActiva", partida);

        servicioPartida.bloquearBotones(partida);
        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/rendirse")
    public ModelAndView rendirse(HttpServletRequest request){
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        servicioPartida.rendirse(partida, partida.getJugador());
        request.getSession().removeAttribute("partidaActiva");
        return new ModelAndView("redirect:/sala");
    }

    @PostMapping("/pedirCarta")
    public ModelAndView pedirCarta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        List<Map<String, Object>> cartasJugador = (List<Map<String, Object>>) session.getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer = (List<Map<String, Object>>) session.getAttribute("cartasDealer");
        String deckId = (String) session.getAttribute("deckId");

        Map<String, Object> cartaNueva = servicioPartida.pedirCarta(
                partida.getJugador(), cartasJugador, deckId
        );

        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("partida", partida);

        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        partida.getJugador().setPuntaje(puntajeJugador);


        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("cartasJugador", cartasJugador);
        mav.addObject("cartasDealer", cartasDealer);
        mav.addObject("usuario", usuario);
        mav.addObject("puntajeJugador", puntajeJugador);
        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        mav.addObject("puntajeDealer", partida.getCrupier().getPuntaje());
        mav.addObject("ultimaCartaJugador", cartaNueva);
        mav.addObject("partida", partida);

        return mav;

    }

}