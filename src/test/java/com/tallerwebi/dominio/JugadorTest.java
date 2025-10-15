package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JugadorTest {
    Jugador jugador = new Jugador();
    @Test
    public void queAUnJugadorSeLePuedaSetearUnUsuario(){
        Usuario usuario = new Usuario("","","","",LocalDate.now(), "");
        jugador.setUsuario(usuario);
        assertEquals(usuario, jugador.getUsuario());
    }



}
