package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioDeckOfCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorDeckTest {
    private String deckId;
    private List<Map<String, Object>> cartasJugador = new ArrayList<>();
    private List<Map<String, Object>> cartasDealer = new ArrayList<>();
    private int puntajeJugador;
    private int puntajeDealer;
    private boolean juegoTerminado = false;
    private Map<String, Object> ultimaCartaJugador;
    private final ServicioDeckOfCards servicioDeck;

    @Autowired
    public ControladorDeckTest(ServicioDeckOfCards servicioDeck) {
        this.servicioDeck = servicioDeck;
    }

    @GetMapping("/test-deck")
    public ModelAndView iniciarPartida() {
        //  Reiniciar estado completamente
        cartasJugador = new ArrayList<>();
        cartasDealer = new ArrayList<>();
        ultimaCartaJugador = null;
        puntajeJugador = 0;
        puntajeDealer = 0;
        juegoTerminado = false;
        deckId = null;

        //  Crear nuevo mazo
        var mazo = servicioDeck.crearMazo();
        deckId = (String) mazo.get("deck_id");

        cartasJugador = servicioDeck.sacarCartas(deckId, 2);
        cartasDealer = servicioDeck.sacarCartas(deckId, 2);

        puntajeJugador = calcularPuntaje(cartasJugador);
        puntajeDealer = calcularPuntaje(cartasDealer);

        ModelAndView mav = new ModelAndView("test-deck");
        mav.addObject("deckId", deckId);
        mav.addObject("cartasJugador", cartasJugador);
        mav.addObject("cartasDealer", cartasDealer);
        mav.addObject("puntajeJugador", puntajeJugador);
        mav.addObject("puntajeDealer", puntajeDealer);
        mav.addObject("mensaje", "");
        mav.addObject("juegoTerminado", false);
        return mav;
    }

    @PostMapping("/pedir")
    public ModelAndView pedirCarta() {
        //  si no hay mazo o ya se terminó el juego, reiniciamos
        if (deckId == null || juegoTerminado) return iniciarPartida();

        var nuevaCarta = servicioDeck.sacarCartas(deckId, 1).get(0);
        cartasJugador.add(nuevaCarta);
        ultimaCartaJugador = nuevaCarta;

        puntajeJugador = calcularPuntaje(cartasJugador);

        if (puntajeJugador > 21) {
            juegoTerminado = true;
        }

        return redibujarVista();
    }


    @PostMapping("/plantarse")
    public ModelAndView plantarse() {
        if (deckId == null || juegoTerminado) return iniciarPartida();

        while (puntajeDealer < 17) {
            var nueva = servicioDeck.sacarCartas(deckId, 1).get(0);
            cartasDealer.add(nueva);
            puntajeDealer = calcularPuntaje(cartasDealer);
        }

        juegoTerminado = true;
        return redibujarVista();
    }

    // posiblemente este metodo, lo tengamos que mover a la clase Partida
    private int calcularPuntaje(List<Map<String, Object>> cartas) {
        int total = 0;
        int ases = 0;

        for (Map<String, Object> carta : cartas) {
            String valor = (String) carta.get("value");
            switch (valor) {
                case "KING":
                case "QUEEN":
                case "JACK":
                    total += 10; break;
                case "ACE":
                    total += 11;
                    ases++;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }
        }
        // Si se pasa de 21, los Ases valen 1
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return total;
    }



    private ModelAndView redibujarVista() {
        String mensaje = "";

        if (juegoTerminado) {
            if (puntajeJugador > 21) {
                mensaje = "Te pasaste de 21. Perdiste";
            } else if (puntajeDealer > 21) {
                mensaje = "El dealer se pasó. ¡Ganaste!";
            } else if (puntajeJugador > puntajeDealer) {
                mensaje = "¡Ganaste! 🎉";
            } else if (puntajeJugador == puntajeDealer) {
                mensaje = "Empate";
            } else {
                mensaje = "Perdiste";
            }
        }

        ModelAndView mav = new ModelAndView("test-deck");
        mav.addObject("deckId", deckId);
        mav.addObject("cartasJugador", cartasJugador);
        mav.addObject("cartasDealer", cartasDealer);
        mav.addObject("puntajeJugador", puntajeJugador);
        mav.addObject("puntajeDealer", puntajeDealer);
        mav.addObject("mensaje", mensaje);
        mav.addObject("juegoTerminado", juegoTerminado);

        // enviar la última carta pedida para poder resaltarla si querés
        mav.addObject("ultimaCartaJugador", ultimaCartaJugador);

        return mav;
    }




}

