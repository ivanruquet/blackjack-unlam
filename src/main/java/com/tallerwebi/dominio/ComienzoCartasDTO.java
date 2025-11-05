package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComienzoCartasDTO {

    private Partida partida;
    private String deckId;
    private List<Map<String, Object>> cartasJugador = new ArrayList<>();
    private List<Map<String, Object>> cartasDealer = new ArrayList<>();
    private int puntajeJugador;
    private int puntajeDealer;

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public void setDeckId(String deckId) {
        this.deckId=deckId;
    }

    public void setCartasJugador(List<Map<String, Object>> cartasJugador) {
        this.cartasJugador = cartasJugador;
    }

    public void setCartasDealer(List<Map<String, Object>> cartasDealer) {
        this.cartasDealer = cartasDealer;
    }

    public void setPuntajeJugador(int puntajeJugador) {
        this.puntajeJugador = puntajeJugador;
    }

    public void setPuntajeDealer(int puntajeDealer) {
        this.puntajeDealer = puntajeDealer;
    }


    public String getDeckId() {
        return this.deckId;
    }
    public List<Map<String, Object>> getCartasJugador() {
        return this.cartasJugador;
    }
    public List<Map<String, Object>> getCartasDealer() {
        return this.cartasDealer;
    }
    public int getPuntajeJugador() {
        return this.puntajeJugador;
    }
    public int getPuntajeDealer() {
        return this.puntajeDealer;
    }



}
