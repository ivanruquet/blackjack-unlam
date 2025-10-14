package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ControladorSalaTest {

    ServicioPartida servicioPartida = mock(ServicioPartida.class);
    ControladorSala controladorSala = new ControladorSala(servicioPartida);

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);

    @Test
    public void queAlCrearLaPartidaSePuedaIrAElJuego() throws PartidaNoCreadaException {
        Usuario usuario = new Usuario("valenvdz7@gmail.com", "1234", "", "", LocalDate.now(), "");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuario")).thenReturn(usuario);
        servicioPartida.crearPartida(usuario);
        assertThat(controladorSala.irAlJuego(request).getViewName(), equalToIgnoringCase("juego"));

    }




}
