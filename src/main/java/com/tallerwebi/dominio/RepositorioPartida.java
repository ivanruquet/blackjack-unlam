package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartida {
    Partida guardar(Partida p)  ;
    List<Partida> buscarPartidaActiva(Usuario usuario);
    void borrarPartida(Partida p);

    void actualizar(Partida partida);
}
