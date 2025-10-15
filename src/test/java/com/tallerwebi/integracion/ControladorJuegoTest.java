package com.tallerwebi.integracion;

import com.tallerwebi.dominio.ServicioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.ControladorJuego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControladorJuegoTest {

    private ControladorJuego controladorJuego;

    @Mock
    ServicioPartida mockServicioPartida;



@BeforeEach
public void init(){

    controladorJuego = new ControladorJuego(mockServicioPartida);

}

    @Test
    public void SeVerificaIrAVistaJuegoCuandoSeAccedeAJuego() {
        ModelAndView mv = controladorJuego.iraJuego();
        assertEquals("juegoConCrupier", mv.getViewName());
    }



    @Test
    public void deberiaIrAlJuegoAlResetear() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ModelAndView modelAndView = controladorJuego.resetearPartida(request);

        assertEquals("redirect:/juegoConCrupier", modelAndView.getViewName());
    }




















}
