package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.PartidaExistenteActivaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;

import java.util.List;

public interface ServicioPartida {

//  public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException;
  void apostar(Partida partida, Integer apuesta, Integer monto);
  void setBotonesAlCrearPartida(Partida partida);
  void setBotonesAlComenzarPartida(Partida partida);

  void resetearPartida(Usuario usuario);

  void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente;

  void apostar(Usuario usuario, int monto);

 void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException;
  Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException;
  void inactivarPartidas(List<Partida> partidaActiva);

  Jugador crearJugador(Usuario usuario);
}
