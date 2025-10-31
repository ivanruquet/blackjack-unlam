package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.presentacion.ControladorPartida;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Part;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPartidaTest {

    RepositorioPartida repositorioPartida = mock(RepositorioPartida.class);
    ServicioPartidaImpl servicioPartida = new ServicioPartidaImpl(repositorioPartida);
    ControladorPartida controladorPartida;

    @BeforeEach
    public void init() {
        servicioPartida.setRepositorioPartida(repositorioPartida);
        controladorPartida = new ControladorPartida(servicioPartida);
    }

    @Test
    public void queAlConsultarSiExistePartidasActivasSeLanceUnaPartidaActivaException(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenExisteUnaPartidaActiva();
        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaActiva));

        assertThrows(PartidaExistenteActivaException.class, ()-> servicioPartida.consultarExistenciaDePartidaActiva(usuario));
    }
    @Test
    public void queSeSeteenEstadosParaPartidasActivasExistentes(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenExisteUnaPartidaActiva();
        servicioPartida.inactivarPartidas(List.of(partidaActiva));
        assertEquals(EstadoPartida.INACTIVA ,partidaActiva.getEstadoPartida());
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
    }

//    @Test
//    public void queSePuedaCreearUnJugador() {
//        Usuario usuario = givenExisteUnUsuario();
//        assertNotNull(servicioPartida.crearJugador(usuario));
//    }

    @Test
    public void queSePuedaIntanciarUnaPartida() throws PartidaNoCreadaException {
        Usuario usuario = givenExisteUnUsuario();
        Jugador j = new Jugador();
        when(repositorioPartida.guardar(any(Partida.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(servicioPartida.instanciarPartida(j));
    }
    @Test
    public void queSePuedaCambiarElEstadoDeJuegoDeUnaPartida() throws PartidaActivaNoEnApuestaException {
    Partida p = new Partida();
    p.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
    servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(p);
    assertEquals(EstadoDeJuego.JUEGO, p.getEstadoJuego());
    }

    @Test
    public void queCalculeCorrectamenteElPuntajeDeCartasNumericas() {
        List<Map<String, Object>> cartas = new ArrayList<>();
        cartas.add(Map.of("value", "5"));
        cartas.add(Map.of("value", "9"));

        int puntaje = servicioPartida.calcularPuntaje(cartas);

        assertEquals(14, puntaje);
    }
    @Test
    public void queCalculeCorrectamenteElPuntajeConCartasEspeciales() {
        List<Map<String, Object>> cartas = new ArrayList<>();
        cartas.add(Map.of("value", "KING"));
        cartas.add(Map.of("value", "QUEEN"));

        int puntaje = servicioPartida.calcularPuntaje(cartas);

        assertEquals(20, puntaje);
    }


    @Test
    public void queAlComenzarLaPartidaEstenHabilitadasSoloLasFichasYNoLosBotonesDeDecision() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        whenComienzaLaPartida(partidaActiva);
        thenBotonesHabilitadosYDesicionesDesabilitado(partidaActiva);
    }
    private void thenBotonesHabilitadosYDesicionesDesabilitado(Partida partidaActiva) {
        assertTrue(partidaActiva.getFichasHabilitadas());
        assertFalse(partidaActiva.getBotonesDesicionHabilitados());
    }

    private void whenComienzaLaPartida(Partida partidaActiva) {
        servicioPartida.setBotonesAlCrearPartida(partidaActiva);
    }
    @Test
    public void queAlSeleccionarFichasSuValorSeSumeEnElPozoTotal() throws ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        whenSeleccionoFichas(partidaActiva);
        thenSeSumaElPozo(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeDescuenteElSaldoDelJugador() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        thenSeDescuentaElsaldo(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeHabilitenLosBotonesDeDecision() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        thenSeHabilitanLosBotonesDeDecision(partidaActiva);
    }

    @Test
    public void queAlSeleccionarElBotonEstrategiaElUsuarioRecibaUnaAyuda(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        String mensajeEsperado= whenSeleccionoBotonEstrategia(partidaActiva, jugador);
        thenElUsuarioRecibeUnaAyuda(jugador, mensajeEsperado);
    }

    private void thenElUsuarioRecibeUnaAyuda(Jugador jugador, String mensajeEsperado) {
        assertEquals(mensajeEsperado, "Dobla si podes, sino pedi una carta.");
    }

    private String whenSeleccionoBotonEstrategia(Partida partidaActiva, Jugador jugador) {
        servicioPartida.seleccionBotonEstrategia(partidaActiva);
        String mensajeEsperado= servicioPartida.mandarEstrategia(partidaActiva, jugador);
        return mensajeEsperado;
    }

    @Test
    public void queAlSeleccionarElBotonDoblarApuestaSeDobleLaApuesta(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        Double apuestaDoblada= whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(partidaActiva, jugador);
        thenApuestaDoblada(partidaActiva, jugador, apuestaDoblada);
    }

    private void thenApuestaDoblada(Partida partidaActiva, Jugador jugador, Double apuestaDoblada) {
        assertEquals(Integer.valueOf(400), partidaActiva.getApuesta());
        assertEquals(600.0, jugador.getSaldo(), 0.01);
    }

    private Double whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(Partida partidaActiva, Jugador jugador) {
        Double resultado= servicioPartida.doblarApuesta(partidaActiva, jugador);
        return resultado;
    }

    @Test
    public void queAlSeleccionarElBotonPararseSeComparenLosPuntosYSeDefinaUnGanador(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeleccionoBotonPararseSeComparanLosPuntosYSeDefineUnGanador(partidaActiva);
        thenResultadoDeLaPartida(partidaActiva);
    }

    private void thenResultadoDeLaPartida(Partida partidaActiva) {
        assertEquals("Resultado: Jugador gana", partidaActiva.getGanador());
    }

    private String whenSeleccionoBotonPararseSeComparanLosPuntosYSeDefineUnGanador(Partida partidaActiva) {
        Integer puntosCrupier= partidaActiva.getCrupier().getPuntaje();
        Integer puntosJugador= partidaActiva.getJugador().getPuntaje();
        String resultado= servicioPartida.resultadoDeLaPartida(puntosCrupier, puntosJugador);
        partidaActiva.setGanador(resultado);
        return resultado;
    }

    @Test
    public void queAlSeleccionarElBotonRendirseSeLeResteLaApuestaAlJugadorYLaPartidaPasaAEstadoAbandonado(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(partidaActiva, jugador);
        thenEstadoAbandonadoYSaldoRestado(partidaActiva, jugador);
    }

    private void thenEstadoAbandonadoYSaldoRestado(Partida partidaActiva, Jugador jugador) {
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
        assertEquals(800, jugador.getSaldo() );
    }

    private void whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(Partida partidaActiva, Jugador jugador) {
        servicioPartida.rendirse(partidaActiva, jugador);
    }


    //------------------------------------------------------------------------------


    private @NotNull Partida givenExisteUnaPartidaActiva() {
        Partida partidaActiva = new Partida();
        partidaActiva.setEstadoPartida(EstadoPartida.ACTIVA);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        return partidaActiva;
    }

    private static @NotNull Usuario givenExisteUnUsuario() {
        Usuario usuario = new Usuario();
        return usuario;
    }


    private Partida givenComienzaUnaPartida(Partida partidaComenzada, Usuario usuario) {
        Crupier crupier= new Crupier();
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        jugador.setSaldo(1000.0);

        crupier.setPuntaje(7);
        jugador.setPuntaje(10);
        partidaComenzada.setJugador(jugador);
        partidaComenzada.setCrupier(crupier);
        return partidaComenzada;
    }



    private void whenSeleccionoFichas(Partida partidaActiva) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.apostar(partidaActiva, partidaActiva.getApuesta(), 100);
    }

    private void thenSeSumaElPozo(Partida partidaActiva) {
        assertEquals(100, partidaActiva.getApuesta());
    }

    private void thenSeDescuentaElsaldo(Partida partidaActiva) {
        assertEquals(800.0, partidaActiva.getJugador().getSaldo());
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva) {
        partidaActiva.setApuesta(200);
        servicioPartida.setBotonesAlComenzarPartida(partidaActiva);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
    }

    private void thenSeHabilitanLosBotonesDeDecision(Partida partidaActiva) {
        assertFalse(partidaActiva.getFichasHabilitadas());
        assertTrue(partidaActiva.getBotonesDesicionHabilitados());
    }









}



