package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaExistenteException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPartidaTest {
    RepositorioPartida repositorioPartida = mock(RepositorioPartida.class);
    ServicioPartida servicioPartida = new ServicioPartidaImpl(repositorioPartida);


    @Test
    public void queElServicioPartidaAlInstanciarUnaPartidaSeteeSuEstadoAApuesta() throws PartidaExistenteException {
        assertEquals(EstadoPartida.APUESTA, servicioPartida.getPartidaNueva().getEstadoPartida());
    }
    @Test
    public void queElServicioPartidaAlCrearUnaPartidaSeteeSuApuestaACero() throws PartidaExistenteException {
        servicioPartida.crearPartida();
        assertEquals(0, servicioPartida.getPartidaNueva().getApuesta());
    }
    @Test
    public void queElServicioPuedaGuardarUnaPartida() {
        Partida p = new Partida();
        servicioPartida.guardarPartida(p);
        //se verifica que la llamada del metodo guardarPartdia() sea de una
        //unica vez con verify, ya que es un metodo void
        Mockito.verify(repositorioPartida, Mockito.times(1)).guardar(p);
    }

    @Test
    public void queAlBuscarUnaPartidaConElEstadoEnApuestaSeLancePartidaExistenteException() {
        Partida p = new Partida();
        p.cambiarEstadoDeLaPartida(EstadoPartida.APUESTA);
        when(repositorioPartida.buscarPartida()).thenReturn(p);
        assertThrows(PartidaExistenteException.class, () ->
            servicioPartida.buscarPartida());
    }

    @Test
    public void queElServicioPuedaBorrarUnaPartida() {
        Partida p = new Partida();
        servicioPartida.borrarPartida(p);
        Mockito.verify(repositorioPartida, Mockito.times(1)).borrarPartida(p);
    }





}



