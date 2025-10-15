package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;

public interface ServicioPartida {

  public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException;
  void apostar(Partida partida, Integer apuesta, Integer monto);
  void comenzarPartida(Partida partida);
  void empezarPartida(Partida partida);

}
