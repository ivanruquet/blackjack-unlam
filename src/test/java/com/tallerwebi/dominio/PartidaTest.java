package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PartidaTest {

    Partida partida = new Partida();
    EstadoDeJuego estadoJuego;


    @Test
    public void queExistaUnaPartida(){
        assertNotNull(partida);
    }

    @Test
    public void queUnaPartidaPuedaCambiarSuEstadoDeJuegoAJuego(){
        estadoJuego = EstadoDeJuego.JUEGO;
        whenCambioElEstadoDeLaPartida(estadoJuego);
        thenEstadoDeLaPartida(estadoJuego);
    }
    @Test
    public void queUnaPartidaPuedaCambiarSuEstadoDeJuegoAFinalizado(){
        estadoJuego = EstadoDeJuego.FINALIZADA;
        whenCambioElEstadoDeLaPartida(estadoJuego);
        thenEstadoDeLaPartida(estadoJuego);
    }
    @Test
    public void queSePuedaAsignarUnValorDeApuestaAUnaPartida(){
        Integer apuesta = 100;
        whenSeAsignaUnValorALaApuesta(apuesta);
        thenLaApuestaTieneMismoValorQueElEsperado(apuesta);
    }

    @Test
    public void queSePuedaSetearUnJugador(){
        Jugador jugador = new Jugador();
        partida.setJugador(jugador);
        assertEquals(jugador, partida.getJugador());
    }

    @Test
    public void queSePuedaSetearElEstadoDeLaPartida(){
      partida.setEstadoPartida(EstadoPartida.ACTIVA);
      assertEquals(EstadoPartida.ACTIVA, partida.getEstadoPartida());
    }





//----------------------------------------------------------------

    private void whenCambioElEstadoDeLaPartida(EstadoDeJuego estadoJuego) {
        partida.cambiarEstadoDeJuego(this.estadoJuego);
    }


    private void thenEstadoDeLaPartida(EstadoDeJuego estadoDeJuego) {
        assertEquals(estadoDeJuego, partida.getEstadoJuego());
    }

    private void whenSeAsignaUnValorALaApuesta(Integer apuesta) {
        partida.setApuesta(apuesta);
    }
    private void thenLaApuestaTieneMismoValorQueElEsperado(Integer apuesta) {
        assertEquals(apuesta, partida.getApuesta());
    }


}

