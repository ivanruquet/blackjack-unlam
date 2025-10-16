package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;

public interface ServicioPartida {

  public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException;
  void apostar(Partida partida, Integer apuesta, Integer monto);
  void comenzarPartida(Partida partida);
  void empezarPartida(Partida partida);

  void resetearPartida(Usuario usuario);

  void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente;

  void apostar(Usuario usuario, int monto);
}
