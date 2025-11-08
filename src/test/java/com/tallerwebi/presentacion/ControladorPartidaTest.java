package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ControladorPartidaTest {

    RepositorioPartida repositorioPartida = mock(RepositorioPartida.class);
    ServicioPartidaImpl servicioPartida = new ServicioPartidaImpl(repositorioPartida);
    ControladorPartida controladorPartida;

    @BeforeEach
    public void init() {
        servicioPartida.setRepositorioPartida(repositorioPartida);
        controladorPartida = new ControladorPartida(servicioPartida);
    }

    @Test
    public void deberiaIrAlJuegoAlResetear() throws PartidaNoCreadaException {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView modelAndView = controladorPartida.resetearPartida(request);

        assertEquals("redirect:/juegoConCrupier", modelAndView.getViewName());

}

    @Test
    public void queAlSeleccionarFichasSuValorSeSumeEnElPozoTotal() throws ApuestaInvalidaException, SaldoInsuficiente, PartidaNoCreadaException {
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva = givenComienzaUnaPartida(usuario);
        ModelAndView mav = whenSeleccionoFichas(partidaActiva, usuario);
        thenSeSumaElPozo(mav);
    }

    private ModelAndView whenSeleccionoFichas(Partida partidaActiva, Usuario usuario) throws PartidaNoCreadaException, ApuestaInvalidaException, SaldoInsuficiente {
        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
        return controladorPartida.apostar(request, 100);
    }

    private void thenSeSumaElPozo(ModelAndView mav) {
        assertEquals("juegoConCrupier", mav.getViewName());

        Partida partida = (Partida) mav.getModel().get("partida");
        assertNotNull(partida);
        assertEquals(100, partida.getApuesta());
    }

    @Test
    public void queAlSeleccionarEstrategiaMuestreLaAyudaParaElUsuario(){
        Usuario usuario = givenExisteUnUsuario();
        Partida partidaActiva= givenComienzaUnaPartida(usuario);
        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
        ModelAndView modelo = whenGenerarAyuda(request);
        thenMostrarAyuda(modelo, "Pedi una carta, no hay riesgo.");
    }

    private MockHttpServletRequest givenExisteUnaSesionConUsuarioYPartida(Usuario usuario, Partida partida) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession();
        request.getSession().setAttribute("usuario", usuario);
        request.getSession().setAttribute("partida", partida);
        ComienzoCartasDTO dto = new ComienzoCartasDTO();
        dto.setPuntajeJugador(0);
        dto.setPuntajeDealer(0);
        dto.setCartasJugador(new ArrayList<>());
        dto.setCartasDealer(new ArrayList<>());

        session.setAttribute("dto", dto);
        return request;
    }

    private void thenMostrarAyuda(ModelAndView modelo, String mensaje) {
        assertEquals("juegoConCrupier", modelo.getViewName());
        assertTrue(modelo.getModel().containsKey("mensajeEstrategia"));
        String mensajeObtenido = modelo.getModel().get("mensajeEstrategia").toString();
        assertEquals(mensaje, mensajeObtenido);
    }

    private ModelAndView whenGenerarAyuda( MockHttpServletRequest request) {
        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partida");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Map<String, Object>> cartasJugador = new ArrayList<>();
        Map<String, Object> carta = new HashMap<>();
        carta.put("value", "5");
        carta.put("suit", "HEARTS");
        carta.put("image", "urlCarta.jpg");
        cartasJugador.add(carta);
        request.getSession().setAttribute("cartasJugador", cartasJugador);

        List<Map<String, Object>> cartasDealer = new ArrayList<>();
        Map<String, Object> cartaDealer = new HashMap<>();
        cartaDealer.put("value", "9");
        cartaDealer.put("suit", "SPADES");
        cartaDealer.put("image", "urlDealer.jpg");
        cartasDealer.add(cartaDealer);
        request.getSession().setAttribute("cartasDealer", cartasDealer);

        when(repositorioPartida.buscarPartidaActiva(usuario))
                .thenReturn(List.of(partidaDeSesion));

        return controladorPartida.mostrarEstrategia(request);
    }

//    @Test
//    public void queAlSeleccionarElBotonDoblarApuestaSeDobleLaApuestaEnElPozo(){
//        Usuario usuario = givenExisteUnUsuario();
//        Partida partidaActiva= givenComienzaUnaPartida(usuario);
//        whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
//        ModelAndView mav= whenSelecionoElBotonDoblarApuestaSeDoblaYRestaElSaldoDelUsuario(request);
//        thenApuestaFinal(mav);
//    }
//
//    private void thenApuestaFinal(ModelAndView mav) {
//        assertEquals("juegoConCrupier", mav.getViewName());
//        assertTrue(mav.getModel().containsKey("resultado"));
//        Double resultadoObtenido = (Double) mav.getModel().get("resultado");
//        Double apuestaEsperada = 600.0;
//        assertEquals(apuestaEsperada, resultadoObtenido);
//    }
//
//    private ModelAndView whenSelecionoElBotonDoblarApuestaSeDoblaYRestaElSaldoDelUsuario(MockHttpServletRequest request) {
//        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partida");
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//
//        when(repositorioPartida.buscarPartidaActiva(usuario))
//                .thenReturn(List.of(partidaDeSesion));
//
//        return controladorPartida.doblarApuesta(request);
//    }
//
//    @Test
//    public void queAlSeleccionarElBotonPararseSeDefinaElResultadoDeLaPartida(){
//        Usuario usuario = givenExisteUnUsuario();
//        Partida partidaActiva= givenComienzaUnaPartida(usuario);
//        whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        MockHttpServletRequest request = givenExisteUnaSesionConUsuarioYPartida(usuario, partidaActiva);
//        ModelAndView mav= whenSelecionoElBotonPararseObtengoElResultado(request);
//        thenResultadoFinal(mav);
//    }
//
//    private void thenResultadoFinal(ModelAndView mav) {
//        String mensaje = (String) mav.getModel().get("mensajeResultado");
//        assertEquals(mensaje, "Resultado: Jugador gana");
//    }
//
//    private ModelAndView whenSelecionoElBotonPararseObtengoElResultado( MockHttpServletRequest request) {
//        Partida partidaDeSesion = (Partida) request.getSession().getAttribute("partida");
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//
//        when(repositorioPartida.buscarPartidaActiva(usuario))
//                .thenReturn(List.of(partidaDeSesion));
//
//        return controladorPartida.pararse(request);
//    }


//        @Test
//    public void apostarMontoValidoDeberiaRegistrarApuesta() throws Exception {
//            Usuario usuario = givenExisteUnUsuario();
//            Partida partidaComenzada= givenExisteUnaPartidaActiva();
//            Partida partidaActiva= givenComienzaUnaPartida(partidaComenzada, usuario);
//            whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.getSession().setAttribute("usuario", usuario);
//        request.getSession().setAttribute("partida", partidaActiva);
//        ModelAndView mv = controladorPartida.apostar(request, 500);
//        assertEquals("juegoConCrupier", mv.getViewName());
//        assertEquals(usuario.getSaldo(), mv.getModel().get("saldo"));
//    }

//    @Test
//    public void queAlSeleccionarElBotonRendirseEnvieAlUsuarioALaVistaSala(){
//        Usuario usuario = givenExisteUnUsuario();
//        Partida partidaActiva= givenComienzaUnaPartida(usuario);
//        whenSeleccionoBotonEmpezarPartida(partidaActiva);
//        ModelAndView vista= whenSeleccionoElBotonRendirseSeTerminaLaPartidaYcambiaDeVista();
//        thenVistaAlcual(vista);
//    }
//
//    private void thenVistaAlcual(ModelAndView vista) {
//        assertEquals("redirect:/sala", vista.getViewName());
//    }
//
//    private ModelAndView whenSeleccionoElBotonRendirseSeTerminaLaPartidaYcambiaDeVista() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        return controladorPartida.rendirse(request);
//    }





    //-------------------------
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
        jugador.getSaldo();

        crupier.setPuntaje(7);
        jugador.setPuntaje(10);
        partidaActiva.setJugador(jugador);
        partidaActiva.setCrupier(crupier);
        return partidaActiva;
    }

    private void whenSeleccionoBotonEmpezarPartida(Partida partidaActiva) {
        partidaActiva.setApuesta(200);
//        servicioPartida.setBotonesAlComenzarPartida(partidaActiva);
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
    }

}
