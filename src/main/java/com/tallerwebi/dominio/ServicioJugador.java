package com.tallerwebi.dominio;

public interface ServicioJugador {
    Jugador buscarJugadorPorUsuario(Usuario usuario);

    void modificarJugador(Jugador jugador);
}
