package com.tallerwebi.dominio;

public interface RepositorioJugador {
    void guardar(Jugador jugador);

    Jugador buscarJugador(Jugador jugador);
}
