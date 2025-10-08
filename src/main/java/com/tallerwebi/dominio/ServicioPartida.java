package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaExistenteException;

public interface ServicioPartida {

  public  void crearPartida();
  void borrarPartida(Partida partidaBorrada);
  void guardarPartida(Partida p);
  Partida buscarPartida() throws PartidaExistenteException;
  Partida getPartidaNueva();
}
