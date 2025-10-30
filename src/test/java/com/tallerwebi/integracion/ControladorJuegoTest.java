package com.tallerwebi.integracion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.presentacion.ControladorJuego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class ControladorJuegoTest {

    private ControladorJuego controladorJuego;

    @Mock
    ServicioPartida mockServicioPartida;


    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this); // Esto inicializa @Mock
        controladorJuego = new ControladorJuego(mockServicioPartida);
    }

    @Test
    public void SeVerificaIrAVistaJuegoCuandoSeAccedeAJuego() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView mv = controladorJuego.iniciarPartida(request);
        assertEquals("juegoConCrupier", mv.getViewName());

    }



    @Test
    public void deberiaIrAlJuegoAlResetear() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView modelAndView = controladorJuego.resetearPartida(request);

        assertEquals("redirect:/juegoConCrupier", modelAndView.getViewName());
    }

    @Test
    public void apostarMontoValidoDeberiaRegistrarApuesta() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Usuario usuario = new Usuario();
        usuario.setSaldo(100.0);
        request.getSession().setAttribute("usuario", usuario);

        ModelAndView mv = controladorJuego.apostar(request, 50);
        assertEquals("juegoConCrupier", mv.getViewName());
        assertEquals(usuario.getSaldo(), mv.getModel().get("saldo"));
    }






















}
