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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPartidaTest {

    private RepositorioPartida repositorioPartida;
    private ServicioDeckOfCards servicioDeckOfCards;
    private ServicioPartidaImpl servicioPartida;
    private ControladorPartida controladorPartida;
    List<Map<String, Object>> cartasJugador = new ArrayList<>();

    @BeforeEach
    public void init() {
        repositorioPartida = mock(RepositorioPartida.class);
        servicioDeckOfCards = mock(ServicioDeckOfCards.class);
        servicioPartida = new ServicioPartidaImpl(repositorioPartida);
        servicioPartida.setServicioDeckOfCards(servicioDeckOfCards);
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
        Integer apuestaDoblada= whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(partidaActiva, usuario);
        thenApuestaDoblada(partidaActiva, partidaActiva.getJugador(), apuestaDoblada);
    }

    private void thenApuestaDoblada(Partida partidaActiva, Jugador jugador, Integer apuestaDoblada) {
        assertEquals(Integer.valueOf(200), partidaActiva.getApuesta());
        assertEquals(800.0, jugador.getSaldo(), 0.01);
    }

    private Integer whenSeleccionoBotonDoblarApuestaSeDoblaLaApuesta(Partida partidaActiva, Usuario jugador) {
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

    @Test
    public void queAlSeleccionarElBotonPedirCartaSeLeAgregueUnaCartaAlJugadorYSeActualiceElPuntaje() {
        Usuario usuario = givenExisteUnUsuario();
        Partida partida = givenComienzaUnaPartida(usuario);
        String deckId = "abc123";

        Map<String, Object> cartaMock = new HashMap<>();
        cartaMock.put("value", "10");
        cartaMock.put("suit", "HEARTS");
        cartaMock.put("image", "urlCarta.png");

        whenElUsuarioPideUnaCarta(partida, deckId, cartaMock);
        thenSeSumaUnaCartaYSuPuntaje(partida);
    }

    private void whenElUsuarioPideUnaCarta(Partida partida, String deckId, Map<String, Object> cartaMock) {
        when(servicioDeckOfCards.sacarCartas(deckId, 1)).thenReturn(Collections.singletonList(cartaMock));
        servicioPartida.pedirCarta(partida.getJugador(), cartasJugador, deckId);
    }

    private void thenSeSumaUnaCartaYSuPuntaje(Partida partida) {
        assertEquals(1, cartasJugador.size());
        assertTrue(partida.getJugador().getPuntaje() > 0);
    }



    @Test
    public void queAlSeleccionarElBotonDividirPartidaSeCreeMano1YMano2YSeResteSaldo() throws Exception {
        Usuario usuario = givenExisteUnUsuario();
        Partida partida = givenComienzaUnaPartida(usuario);

        Map<String, Object> carta1 = new HashMap<>();
        carta1.put("value", "8");
        carta1.put("suit", "HEARTS");
        carta1.put("image", "carta1.png");

        Map<String, Object> carta2 = new HashMap<>();
        carta2.put("value", "8");
        carta2.put("suit", "SPADES");
        carta2.put("image", "carta2.png");

        List<Map<String, Object>> cartasJugador = Arrays.asList(carta1, carta2);
        whenElUsuarioDivideLaPartida(partida, cartasJugador);
        thenLaPartidaSeDivideYSeRestaSaldo(partida, cartasJugador);
    }

    private void whenElUsuarioDivideLaPartida(Partida partida, List<Map<String, Object>> cartasJugador) throws Exception {
        servicioPartida.dividirPartida(partida, cartasJugador);
    }

    private void thenLaPartidaSeDivideYSeRestaSaldo(Partida partida, List<Map<String, Object>> cartasJugador) {
        assertTrue(partida.getManoDividida());
        assertEquals(1, partida.getMano1().size());
        assertEquals(1, partida.getMano2().size());
        assertEquals(900, partida.getJugador().getSaldo());
        assertNotEquals(partida.getPuntajeMano1(), 0);
        assertNotEquals(partida.getPuntajeMano2(), 0);
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