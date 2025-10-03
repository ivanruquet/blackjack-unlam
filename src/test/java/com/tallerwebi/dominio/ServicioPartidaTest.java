package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServicioPartidaTest {
    ServicioPartida servicioPartida = new ServicioPartidaImpl();

    @Test
    public void queElServicioPartidaCreeUnaPartida(){
     assertTrue(servicioPartida.crearPartida());
    }
    @Test
    public void queElServicioPartidaAlCrearUnaPartidaSeteeSuEstadoAApuesta(){
        servicioPartida.crearPartida();
        assertEquals(EstadoPartida.APUESTA, servicioPartida.getPartida().getEstadoPartida());
    }
    @Test
    public void queElServicioPartidaAlCrearUnaPartidaSeteeSuApuestaACero(){
        servicioPartida.crearPartida();
        assertEquals(0, servicioPartida.getPartida().getApuesta());
    }



}
