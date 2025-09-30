package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PartidaTest {

    Partida partida = new Partida();
    EstadoPartida estadoPartida;


    @Test
    public void queExistaUnaPartida(){
        assertNotNull(partida);
    }
    @Test
    public void queElEstadoInicialDeUnaPartidaSeaDeApuesta(){
        estadoPartida = EstadoPartida.APUESTA;
        thenEstadoDeLaPartida(estadoPartida);
    }
    @Test
    public void queUnaPartidaPuedaCambiarSuEstadoAJuego(){
        estadoPartida = EstadoPartida.JUEGO;
        whenCambioElEstadoDeLaPartida(estadoPartida);
        thenEstadoDeLaPartida(estadoPartida);
    }
    @Test
    public void queUnaPartidaPuedaCambiarSuEstadoAFinalizado(){
        estadoPartida = EstadoPartida.FINALIZADA;
        whenCambioElEstadoDeLaPartida(estadoPartida);
        thenEstadoDeLaPartida(estadoPartida);
    }
    @Test
    public void queSePuedaAsignarUnValorDeApuestaAUnaPartida(){
        Integer apuesta = 100;
        whenSeAsignaUnValorALaApuesta(apuesta);
        thenLaApuestaTieneMismoValorQueElEsperado(apuesta);
    }


//----------------------------------------------------------------

    private void whenCambioElEstadoDeLaPartida(EstadoPartida estadoPartida) {
        partida.cambiarEstadoDeLaPartida(this.estadoPartida);
    }


    private void thenEstadoDeLaPartida(EstadoPartida estadoPartida) {
        assertEquals(estadoPartida, partida.getEstadoPartida());
    }

    private void whenSeAsignaUnValorALaApuesta(Integer apuesta) {
        partida.setApuesta(apuesta);
    }
    private void thenLaApuestaTieneMismoValorQueElEsperado(Integer apuesta) {
        assertEquals(apuesta, partida.getApuesta());
    }


}

