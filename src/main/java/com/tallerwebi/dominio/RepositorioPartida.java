package com.tallerwebi.dominio;

public interface RepositorioPartida {
    void guardar(Partida p);


    Partida buscarPartida();

    void borrarPartida(Partida p);
}
