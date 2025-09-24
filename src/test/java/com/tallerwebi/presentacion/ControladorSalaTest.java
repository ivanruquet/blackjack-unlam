package com.tallerwebi.presentacion;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;


public class ControladorSalaTest {


    @Test
    public void queSePuedaIrAlJuego(){
        ControladorSala controladorSala = new ControladorSala();
        assertThat(controladorSala.irAlJuego().getViewName(), equalToIgnoringCase("juego"));
    }




}
