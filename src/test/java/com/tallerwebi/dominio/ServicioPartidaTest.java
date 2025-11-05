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
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaActiva));

        assertThrows(PartidaExistenteActivaException.class, ()-> servicioPartida.consultarExistenciaDePartidaActiva(usuario));
    }
    @Test
    public void queSeSeteenEstadosParaPartidasActivasExistentes(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
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

//    @Test
//    public void queSePuedaCambiarElEstadoDeJuegoDeUnaPartida() throws PartidaActivaNoEnApuestaException {
//        Partida p = new Partida();
//        p.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
//        Long idDePrueba = 2L;
//        when(repositorioPartida.buscarPartidaPorId(any(Long.class)))
//                .thenReturn(p);
//        servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(idDePrueba);
//        assertEquals(EstadoDeJuego.JUEGO, p.getEstadoJuego());
//    }

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
    public void queAlIniciarEstenHabilitadasSoloLasFichasYNoLosBotonesDeDecision() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenComienzaLaPartida(partidaActiva);
        thenBotonesHabilitadosYDesicionesDesabilitado(partidaActiva);
    }
    private void thenBotonesHabilitadosYDesicionesDesabilitado(Partida partidaActiva) {
        assertEquals(partidaActiva.getEstadoJuego(), EstadoDeJuego.APUESTA);
        assertTrue(partidaActiva.getFichasHabilitadas());
        assertFalse(partidaActiva.getBotonesDesicionHabilitados());
    }

    private void whenComienzaLaPartida(Partida partidaActiva) {
        servicioPartida.setBotonesAlCrearPartida(partidaActiva);
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeHabilitenLosBotonesDeDecision() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenComienzaLaPartida(partidaActiva);
        thenSeHabilitanLosBotonesDeDecision(partidaActiva);
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva)throws PartidaActivaNoEnApuestaException  {
        servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partidaActiva);
    }

    private void thenSeHabilitanLosBotonesDeDecision(Partida partidaActiva) {
        assertEquals(partidaActiva.getEstadoJuego(), EstadoDeJuego.JUEGO);
        assertFalse(partidaActiva.getFichasHabilitadas());
        assertTrue(partidaActiva.getBotonesDesicionHabilitados());
    }
    @Test
    public void queAlSeleccionarFichasSuValorSeSumeEnElPozoTotal() throws ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoFichas(partidaActiva);
        thenSeSumaElPozo(partidaActiva);
    }

    private void whenSeleccionoFichas(Partida partidaActiva) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.apostar(partidaActiva, 100);
    }

    private void thenSeSumaElPozo(Partida partidaActiva) {
        assertEquals(200, partidaActiva.getApuesta());
    }

    @Test
    public void queAlSeleccionarElBotonEstrategiaElUsuarioRecibaUnaAyuda() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        String mensajeEsperado= whenSeleccionoBotonEstrategia(partidaActiva, partidaActiva.getJugador());
        thenElUsuarioRecibeUnaAyuda(partidaActiva.getJugador(), mensajeEsperado);
    }

    private void thenElUsuarioRecibeUnaAyuda(Jugador jugador, String mensajeEsperado) {
        assertEquals(mensajeEsperado, "Dobla si podes, sino pedi una carta.");
    }

    private String whenSeleccionoBotonEstrategia(Partida partidaActiva, Jugador jugador) {
        servicioPartida.seleccionBotonEstrategia(partidaActiva);
        String mensajeEsperado= servicioPartida.mandarEstrategia(partidaActiva, partidaActiva.getJugador().getPuntaje(), partidaActiva.getCrupier().getPuntaje());
        return mensajeEsperado;
    }


    @Test
    public void queAlSeleccionarElBotonEmpezarPartidaSeDescuenteElSaldoDelJugador() throws PartidaActivaNoEnApuestaException, ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeDescuentaElSaldoDeLaApuesta(partidaActiva);
        thenSeDescuentaElsaldo(partidaActiva);
    }

    private void whenSeDescuentaElSaldoDeLaApuesta(Partida partidaActiva) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.apostar(partidaActiva, partidaActiva.getApuesta());
    }

    private void thenSeDescuentaElsaldo(Partida partidaActiva) {
        assertEquals(50, partidaActiva.getJugador().getUsuario().getSaldo());
    }

    @Test
    public void queAlSeleccionarElBotonDoblarApuestaSeDobleLaApuesta() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        Integer apuestaDoblada= whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(partidaActiva, partidaActiva.getJugador());
        thenApuestaDoblada(partidaActiva, partidaActiva.getJugador(), apuestaDoblada);
    }

    private void thenApuestaDoblada(Partida partidaActiva, Jugador jugador, Integer apuestaDoblada) {
        assertEquals(Integer.valueOf(200), partidaActiva.getApuesta());
        assertEquals(800.0, jugador.getSaldo(), 0.01);
    }

    private Integer whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(Partida partidaActiva, Jugador jugador) {
        Integer resultado= servicioPartida.doblarApuesta(partidaActiva, jugador);
        return resultado;
    }

    @Test
    public void queAlSeleccionarElBotonPararseSeComparenLosPuntosYSeDefinaUnGanador() throws PartidaActivaNoEnApuestaException{
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
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
    public void queAlSeleccionarElBotonRendirseSeLeResteLaApuestaAlJugadorYLaPartidaPasaAEstadoAbandonado() throws PartidaActivaNoEnApuestaException, ApuestaInvalidaException, SaldoInsuficiente {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(partidaActiva, partidaActiva.getJugador());
        thenEstadoAbandonadoYSaldoRestado(partidaActiva, partidaActiva.getJugador());
    }

    private void thenEstadoAbandonadoYSaldoRestado(Partida partidaActiva, Jugador jugador) {
        assertEquals(EstadoDeJuego.ABANDONADO ,partidaActiva.getEstadoJuego());
       // assertEquals(900, jugador.getSaldo());
    }

    private void whenSeleccionoBotonRendirseSeLeResteLaApuestaAlJugadorYYLaPartidaPasaAEstadoAbandonado(Partida partidaActiva, Jugador jugador) throws ApuestaInvalidaException, SaldoInsuficiente {
        servicioPartida.rendirse(partidaActiva, jugador);
     //   servicioPartida.apostar(partidaActiva, partidaActiva.getApuesta());
    }


    //------------------------------------------------------------------------------


    private static @NotNull Usuario givenExisteUnUsuario() {
        Usuario usuario = new Usuario();
        return usuario;
    }


    private @NotNull Partida givenComienzaUnaPartida(Usuario usuario) {
        Partida partidaActiva = new Partida();
        partidaActiva.setEstadoPartida(EstadoPartida.ACTIVA);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        Crupier crupier= new Crupier();
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);

        crupier.setPuntaje(7);
        jugador.setPuntaje(10);
        partidaActiva.setApuesta(100);
        partidaActiva.setJugador(jugador);
        partidaActiva.setCrupier(crupier);
        return partidaActiva;
    }












}