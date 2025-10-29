package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ServicioUsuarioTest {
    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(repositorioUsuario);

    @Test
    public void queAlSePuedaActualizarElSaldoDelUsuario(){
        Usuario usuario = new Usuario();
        usuario.setSaldo(100.0);
        Integer montoApuesta = 100;
        servicioUsuario.actualizarSaldoDeUsuario(usuario, montoApuesta);
        assertEquals(00.0, usuario.getSaldo());
    }
}
