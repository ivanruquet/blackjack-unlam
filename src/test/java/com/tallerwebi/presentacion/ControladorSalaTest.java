package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ControladorSalaTest {

    ServicioPartida servicioPartida = mock(ServicioPartida.class);
    ControladorSala controladorSala = new ControladorSala(servicioPartida);


    @Test
    public void queAlCrearLaPartidaSePuedaIrAElJuego(){
        servicioPartida.crearPartida();
        assertThat(controladorSala.irAlJuego().getViewName(), equalToIgnoringCase("juego"));

    }




}
