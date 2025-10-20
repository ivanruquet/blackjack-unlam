package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaExistenteActivaException;
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
    public void queAlConsultarSiExistePartidasActivasSeLanceUnaPartidaActivaException(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenExisteUnaPartidaActiva(usuario);
        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaActiva));

        assertThrows(PartidaExistenteActivaException.class, ()-> servicioPartida.consultarExistenciaDePartidaActiva(usuario));
    }
    @Test
    public void queSeSeteenEstadosParaPartidasActivasExistentes(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenExisteUnaPartidaActiva(usuario);
        servicioPartida.inactivarPartidas(List.of(partidaActiva));
        assertEquals(EstadoPartida.INACTIVA ,partidaActiva.getEstadoPartida());
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
    }
    @Test
    public void queSePuedaCreearUnJugador() {
        Usuario usuario = givenExisteUnUsuario();
        assertNotNull(servicioPartida.crearJugador(usuario));
    }

    @Test
    public void queSePuedaIntanciarUnaPartida() throws PartidaNoCreadaException {
        Usuario usuario = givenExisteUnUsuario();
        Jugador j = new Jugador();
        when(repositorioPartida.guardar(any(Partida.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(servicioPartida.instanciarPartida(j));
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
        servicioPartida.setBotonesAlCrearPartida(partidaActiva);
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
        servicioPartida.setBotonesAlComenzarPartida(partidaActiva);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);

    }
}



