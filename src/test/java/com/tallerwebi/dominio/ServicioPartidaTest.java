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
    Partida partida = new Partida();
    Usuario usuario= new Usuario();

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

    @Test
    public void queAlComenzarLaPartidaEstenHabilitadasSoloLasFichasYNoLosBotonesDeDesicion() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva(usuario);
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada);
        whenComienzaLaPartida(partidaActiva);
        thenBotonesHabilitadosYDesicionesDesabilitado(partidaActiva);
    }

    @Test
    public void queAlSeleccionarFichasSuValorSeSumeEnElPozoTotal() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva(usuario);
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada);
        whenSeleccionoFichas(partidaActiva);
        thenSeSumaElPozo(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeDescuenteElSaldoDelJugador() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva(usuario);
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        thenSeDescuentaElsaldo(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeHabilitenLosBotonesDeDesicion() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva(usuario);
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        thenSeHabilitanLosBotonesDeDesicion(partidaActiva);
    }

    private void thenSeHabilitanLosBotonesDeDesicion(Partida partidaActiva) {
        assertFalse(partidaActiva.getFichasHabilitadas());
        assertTrue(partidaActiva.getBotonesDesicionHabilitados());
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

    private Partida givenComienzaUnaPartida(Partida partidaComenzada) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        jugador.setSaldo(1000.0);

        partidaComenzada.setJugador(jugador);
        partidaComenzada.setApuesta(0);
        return partidaComenzada;
    }

    private void thenBotonesHabilitadosYDesicionesDesabilitado(Partida partidaActiva) {
        assertTrue(partidaActiva.getFichasHabilitadas());
        assertFalse(partidaActiva.getBotonesDesicionHabilitados());
    }

    private void whenComienzaLaPartida(Partida partidaActiva) {
        servicioPartida.comenzarPartida(partidaActiva);
    }

    private void whenSeleccionoFichas(Partida partidaActiva) {
        servicioPartida.apostar(partidaActiva, partidaActiva.getApuesta(), 200);
    }

    private void thenSeSumaElPozo(Partida partidaActiva) {
        assertEquals(200, partidaActiva.getApuesta());
    }

    private void thenSeDescuentaElsaldo(Partida partidaActiva) {
        assertEquals(800.0, partidaActiva.getJugador().getSaldo());
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva) {
        partidaActiva.setApuesta(200);
        servicioPartida.empezarPartida(partidaActiva);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);

    }
}



