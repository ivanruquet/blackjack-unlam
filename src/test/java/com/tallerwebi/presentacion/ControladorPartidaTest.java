package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ControladorPartidaTest {

    RepositorioPartida repositorioPartida = mock(RepositorioPartida.class);
    ServicioPartidaImpl servicioPartida = new ServicioPartidaImpl();
    ControladorPartida controladorPartida;

    @BeforeEach
    public void init() {
        servicioPartida.setRepositorioPartida(repositorioPartida);
        controladorPartida = new ControladorPartida(servicioPartida);
    }

    @Test
    public void SeVerificaIrAVistaJuegoCuandoSeAccedeAJuego() {
        ModelAndView mv = controladorPartida.iraJuego();
        assertEquals("juegoConCrupier", mv.getViewName());
    }

    @Test
    public void deberiaIrAlJuegoAlResetear() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView modelAndView = controladorPartida.resetearPartida(request);

        assertEquals("redirect:/juegoConCrupier", modelAndView.getViewName());

}
    @Test
    public void queAlSeleccionarEstrategiaMuestreLaAyudaParaElUsuario(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
        ModelAndView modelo = whenGenerarAyuda(request);
        thenMostrarAyuda(modelo, "Pedi una carta, no hay riesgo");
    }

    private MockHttpServletRequest givenExisteUnaSesionConUsuarioYPartida(Usuario usuario, Partida partidaActiva) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("usuario", usuario);
        request.getSession().setAttribute("partidaActiva", partidaActiva);
        return request;
    }

    private void thenMostrarAyuda(ModelAndView modelo, String mensaje) {
        assertEquals("juegoConCrupier", modelo.getViewName());
        assertTrue(modelo.getModel().containsKey("mensajeEstrategia"));
        String mensajeObtenido = modelo.getModel().get("mensajeEstrategia").toString();
        assertEquals(mensaje, mensajeObtenido);
    }

    private ModelAndView whenGenerarAyuda( MockHttpServletRequest request) {
        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partidaActiva");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaDeSesion));

        return controladorPartida.mostrarEstrategia(request);
    }

    @Test
    public void queAlSeleccionarElBotonDoblarApuestaSeDobleLaApuestaEnElPozo(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
        ModelAndView mav= whenSelecionoElBotonDoblarApuestaSeDoblaYRestaElSaldoDelUsuario(request);
        thenApuestaFinal(mav);
    }

    private void thenApuestaFinal(ModelAndView mav) {
        assertEquals("juegoConCrupier", mav.getViewName());
        assertTrue(mav.getModel().containsKey("resultado"));
        Double resultadoObtenido = (Double) mav.getModel().get("resultado");
        Double apuestaEsperada = 600.0;
        assertEquals(apuestaEsperada, resultadoObtenido);
    }

    private ModelAndView whenSelecionoElBotonDoblarApuestaSeDoblaYRestaElSaldoDelUsuario(MockHttpServletRequest request) {
        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partidaActiva");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaDeSesion));

        return controladorPartida.doblarApuesta(request);
    }

    @Test
    public void queAlSeleccionarElBotonPararseSeDefinaElResultadoDeLaPartida(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
        ModelAndView mav= whenSelecionoElBotonPararseObtengoElResultado(request);
        thenResultadoFinal(mav);
    }

    private void thenResultadoFinal(ModelAndView mav) {
        String mensaje = (String) mav.getModel().get("mensajeResultado");
        assertTrue(mensaje.contains("Jugador gana"));
    }

    private ModelAndView whenSelecionoElBotonPararseObtengoElResultado( MockHttpServletRequest request) {
        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partidaActiva");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaDeSesion));

        return controladorPartida.pararse(request);
    }

    @Test
    public void queAlSeleccionarElBotonRendirseEnvieAlUsuarioALaVistaSala(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaComenzada= givenExisteUnaPartidaActiva();
        Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
        Jugador jugador = partidaActiva.getJugador();
        whenSeleccionoBotonEmpezarPartida(partidaActiva);
        ModelAndView vista= whenSeleccionoElBotonRendirseSeTerminaLaPartidaYcambiaDeVista();
        thenVistaAlcual(vista);
    }

    private void thenVistaAlcual(ModelAndView vista) {
        assertEquals("redirect:/sala", vista.getViewName());
    }

    private ModelAndView whenSeleccionoElBotonRendirseSeTerminaLaPartidaYcambiaDeVista() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        return controladorPartida.rendirse(request);
    }





    //-------------------------
    private @NotNull Partida givenExisteUnaPartidaActiva() {
        Partida partidaActiva = new Partida();
        partidaActiva.setEstadoPartida(EstadoPartida.ACTIVA);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        return partidaActiva;
    }

    private static @NotNull Usuario givenExisteUnUsuario() {
        return new Usuario();
    }

    private Partida givenComienzaUnaPartida(Partida partidaComenzada, Usuario usuario) {
        Crupier crupier = new Crupier();
        crupier.setPuntaje(5);

        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        jugador.setSaldo(1000.0);
        jugador.setPuntaje(7);

        partidaComenzada.setCrupier(crupier);
        partidaComenzada.setJugador(jugador);
        partidaComenzada.setApuesta(0);
        partidaComenzada.setEstadoPartida(EstadoPartida.ACTIVA);

        return partidaComenzada;
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva) {
        partidaActiva.setApuesta(200);
        servicioPartida.setBotonesAlComenzarPartida(partidaActiva);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
    }

}
