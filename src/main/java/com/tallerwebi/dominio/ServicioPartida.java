package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

import java.util.List;
import java.util.Map;

public interface ServicioPartida {

    void apostar(Partida partida, Integer apuesta, Integer monto);
    void setBotonesAlCrearPartida(Partida partida);
  void setBotonesAlComenzarPartida(Partida partida);


  void apostar(Usuario usuario, int monto);

  void resetearPartida(Usuario usuario);

  void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente;



    void setearApuesta(Usuario usuario, Integer monto, Partida partida);



    int calcularPuntaje(List<Map<String, Object>> cartas);

 void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException;

  void comenzarPartida(Partida partida);

  void empezarPartida(Partida partida);

  Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException;
  void inactivarPartidas(List<Partida> partidaActiva);

  Jugador crearJugador(Usuario usuario);
  void seleccionBotonEstrategia(Partida partidaActiva);
  String mandarEstrategia(Partida partidaActiva, Jugador jugador);
  Partida obtenerPartidaActiva(Usuario usuario);

  Double doblarApuesta(Partida partidaActiva, Jugador jugador);

  String resultadoDeLaPartida(Integer puntosCrupier, Integer puntosCrupier1);

  void rendirse(Partida partidaActiva, Jugador jugador);

    void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException;

    List<Partida> buscarPartidaActiva(Usuario usuario);

  Partida crearPartida(Usuario usuario)throws PartidaNoCreadaException;
}
