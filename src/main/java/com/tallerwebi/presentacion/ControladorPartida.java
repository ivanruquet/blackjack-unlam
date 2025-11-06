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

        String deckId = dto.getDeckId();
        session.setAttribute("deckId", deckId);
        session.setAttribute("dto", dto);
        session.setAttribute("cartasJugador", dto.getCartasJugador());
        session.setAttribute("cartasDealer", dto.getCartasDealer());

        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("partida", partida);
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("usuario", usuario);
        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        mav.addObject("dto", dto);
        return mav ;
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
            session.setAttribute("partida", partida);
            session.setAttribute("usuario", partida.getJugador().getUsuario());


            modelo.addAttribute("usuario", partida.getJugador().getUsuario());
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
        List<Map<String, Object>> cartasJugador = (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer = (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");
        ComienzoCartasDTO dto = (ComienzoCartasDTO) session.getAttribute("dto");

        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);
        String mensajeEstrategia = servicioPartida.mandarEstrategia(partida, puntajeJugador, puntajeDealer);

        modelo.put("mensajeEstrategia", mensajeEstrategia);
        modelo.addAttribute("dto", dto);
        modelo.addAttribute("partida", partida);
        modelo.addAttribute("jugador", partida.getJugador());
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", puntajeJugador);
        modelo.addAttribute("puntajeDealer", puntajeDealer);
        modelo.addAttribute("cartasJugador", cartasJugador);
        modelo.addAttribute("cartasDealer", cartasDealer);
        modelo.addAttribute("apuesta", partida.getApuesta());

        return new ModelAndView("juegoConCrupier", modelo);
    }



    @PostMapping("/doblarApuesta")
    public ModelAndView doblarApuesta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        List<Map<String, Object>> cartasJugador = (List<Map<String, Object>>) session.getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer = (List<Map<String, Object>>) session.getAttribute("cartasDealer");
        ModelMap modelo = new ModelMap();
        ComienzoCartasDTO dto = (ComienzoCartasDTO) session.getAttribute("dto");

        Integer apuesta = servicioPartida.doblarApuesta(partida, usuario);

        modelo.addAttribute("dto", dto);
        modelo.put("apuesta", apuesta);
        modelo.put("partida", partida);
        modelo.put("usuario", usuario);
        modelo.put("jugador", partida.getJugador());
        modelo.addAttribute("puntajeJugador", partida.getJugador().getPuntaje());
        modelo.addAttribute("puntajeDealer", partida.getCrupier().getPuntaje());
        modelo.put("cartasJugador", cartasJugador);
        modelo.put("cartasDealer", cartasDealer);
        return new ModelAndView("juegoConCrupier", modelo);
    }

    @PostMapping("/pararse")
    public ModelAndView pararse(HttpServletRequest request){
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        ModelMap modelo = new ModelMap();
        List<Map<String, Object>> cartasJugador = (List<Map<String, Object>>) request.getSession().getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer = (List<Map<String, Object>>) request.getSession().getAttribute("cartasDealer");
        ComienzoCartasDTO dto = (ComienzoCartasDTO) session.getAttribute("dto");


        String mensajeResultado = servicioPartida.determinarResultado(partida);

        modelo.put("mensajeResultado", mensajeResultado);
        modelo.addAttribute("dto", dto);
        modelo.addAttribute("partida", partida);
        modelo.addAttribute("jugador", partida.getJugador());
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("puntajeJugador", partida.getJugador().getPuntaje());
        modelo.addAttribute("puntajeDealer", partida.getCrupier().getPuntaje());
        modelo.addAttribute("cartasJugador", partida);
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
        ComienzoCartasDTO dto = (ComienzoCartasDTO) session.getAttribute("dto");

        Map<String, Object> cartaNueva = servicioPartida.pedirCarta(partida.getJugador(), cartasJugador, deckId);

        int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
        dto.setPuntajeJugador(puntajeJugador);
        partida.getJugador().setPuntaje(puntajeJugador);


        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("dto", dto);
        mav.addObject("cartasJugador", dto.getCartasJugador());
        mav.addObject("cartasDealer", dto.getCartasDealer());
        mav.addObject("usuario", usuario);
        mav.addObject("puntajeJugador", partida.getJugador().getPuntaje());
        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        mav.addObject("puntajeDealer", partida.getCrupier().getPuntaje());
        mav.addObject("cartaNueva", cartaNueva);
        mav.addObject("partida", partida);

        return mav;

    }


    @PostMapping("dividirPartida")
    public ModelAndView dividirPartida(HttpServletRequest request) throws SaldoInsuficiente {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Partida partida = (Partida) request.getSession().getAttribute("partida");
        List<Map<String, Object>> cartasJugador = (List<Map<String, Object>>) session.getAttribute("cartasJugador");
        List<Map<String, Object>> cartasDealer = (List<Map<String, Object>>) session.getAttribute("cartasDealer");
        String deckId = (String) session.getAttribute("deckId");
        ComienzoCartasDTO dto = (ComienzoCartasDTO) session.getAttribute("dto");

        servicioPartida.dividirPartida(partida, cartasJugador);

        List<Map<String, Object>> mano1 = partida.getMano1();
        List<Map<String, Object>> mano2 = partida.getMano2();

        Map<String, Object> cartaMano1 = servicioPartida.pedirCarta(partida.getJugador(), mano1, deckId);
        Map<String, Object> cartaMano2 = servicioPartida.pedirCarta(partida.getJugador(), mano2, deckId);

        partida.setPuntajeMano1(servicioPartida.calcularPuntaje(mano1));
        partida.setPuntajeMano2(servicioPartida.calcularPuntaje(mano2));

        ModelAndView mav = new ModelAndView("juegoConCrupier");
        mav.addObject("dto", dto);
        mav.addObject("jugador", partida.getJugador());
        mav.addObject("cartasJugador", cartasJugador);
        mav.addObject("cartasDealer", cartasDealer);
        mav.addObject("usuario", usuario);
        mav.addObject("puntajeJugador", partida.getJugador().getPuntaje());
        mav.addObject("apuesta", ((Partida) session.getAttribute("partida")).getApuesta());
        mav.addObject("puntajeDealer", partida.getCrupier().getPuntaje());
        mav.addObject("cartaMano1", cartaMano1);
        mav.addObject("cartaMano2", cartaMano2);

        mav.addObject("partida", partida);

        return mav;

    }
}