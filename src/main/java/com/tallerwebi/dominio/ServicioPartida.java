package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

import java.util.List;
import java.util.Map;

public interface ServicioPartida {


 void apostar(Partida partida, Integer apuesta, Integer monto);
  void setBotonesAlCrearPartida(Partida partida);
  void setBotonesAlComenzarPartida(Partida partida);



    void resetearPartida(Usuario usuario);

  void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente;



    void setearApuesta(Usuario usuario, Integer monto, Partida partida);



    int calcularPuntaje(List<Map<String, Object>> cartas);

 void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException;
  Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException;
  void inactivarPartidas(List<Partida> partidaActiva);

  Jugador crearJugador(Usuario usuario);

    void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException;

    List<Partida> buscarPartidaActiva(Usuario usuario);
}
