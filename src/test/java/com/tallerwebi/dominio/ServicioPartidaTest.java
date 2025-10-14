package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPartidaTest {
    RepositorioPartida repositorioPartida = mock(RepositorioPartida.class);
    RepositorioJugador repositorioJugador = mock(RepositorioJugador.class);
    ServicioPartida servicioPartida = new ServicioPartidaImpl(repositorioPartida, repositorioJugador);


    @Test
    public void queSePuedaCrearUnaPartidaCorrectamenteConSusAtrubutos() throws PartidaNoCreadaException {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenExisteUnaPartidaActiva(usuario);
        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaActiva));
        Partida nuevaPartida = whenSeCreaNuevaPartida(usuario);
        thenComprobarAtributos(partidaActiva, nuevaPartida, usuario);
    }
    @Test
    public void queLanceUnaExceptionAlNoPoderCrearLaPartida(){
        Usuario u = givenExisteUnUsuario();
        whenLaPartidaGuardadaIsNull();
        thenSeLanzaUnaExeption(u);
    }
//------------------------------------------------------------------------------
    private Partida whenSeCreaNuevaPartida(Usuario usuario) throws PartidaNoCreadaException {
     //   when(repositorioJugador.guardar(any(Jugador.class))).thenAnswer(invoc -> invoc.getArgument(0));
       when(repositorioPartida.guardar(any(Partida.class))).thenAnswer(invoc -> invoc.getArgument(0));

        Partida nuevaPartida = servicioPartida.crearPartida(usuario);
        return nuevaPartida;
    }

    private @NotNull Partida givenExisteUnaPartidaActiva(Usuario usuario) {
        Partida partidaActiva = new Partida();
        partidaActiva.setEstadoPartida(EstadoPartida.ACTIVA);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);


        return partidaActiva;
    }

    private static @NotNull Usuario givenExisteUnUsuario() {
        Usuario usuario = new Usuario();
        return usuario;
    }

    private static void thenComprobarAtributos(Partida partidaActiva, Partida nuevaPartida, Usuario usuario) {
        assertEquals(EstadoPartida.INACTIVA, partidaActiva.getEstadoPartida());
        assertEquals(EstadoDeJuego.ABANDONADO, partidaActiva.getEstadoJuego());
        assertEquals(EstadoPartida.ACTIVA, nuevaPartida.getEstadoPartida());
        assertEquals(EstadoDeJuego.APUESTA, nuevaPartida.getEstadoJuego());
        assertNotNull(nuevaPartida.getJugador());
        assertEquals(usuario, nuevaPartida.getJugador().getUsuario());
    }



    private void thenSeLanzaUnaExeption(Usuario u) {
        assertThrows(PartidaNoCreadaException.class, () -> {
            servicioPartida.crearPartida(u);
        });
    }

    private void whenLaPartidaGuardadaIsNull() {
        when(repositorioPartida.buscarPartidaActiva(any(Usuario.class)))
                .thenReturn(Collections.emptyList());
        when(repositorioPartida.guardar(any(Partida.class))).thenReturn(null);
    }


}



