package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ServicioUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(repositorioUsuario);
    Usuario usuario1 = new Usuario("user@gmail.com", "user", "userNombre", "userApellido", LocalDate.now(), "UserName");

    @Test
    public void queSePuedaActualizarElNombreYApellidoDelUsuario() {
        // Given
        Usuario usuario = givenExisteUsuarioConSusDatos(usuario1);

        // When
        whenActualizoLosDatos(usuario);

        // Then
        thenAparecenModificadosEnElPerfil(usuario);
    }

    private Usuario givenExisteUsuarioConSusDatos(Usuario usuario) {
        assertEquals("userNombre", usuario.getNombre());
        assertEquals("userApellido", usuario.getApellido());
        assertEquals("UserName", usuario.getUsername());
        return usuario;
    }

    private void whenActualizoLosDatos(Usuario usuario) {
        servicioUsuario.modificarAtributos(usuario, "Mia Actualizada", "Gomez Actualizado", "UserName");
    }

    private void thenAparecenModificadosEnElPerfil(Usuario usuario) {
        assertEquals("Mia Actualizada", usuario.getNombre());
        assertEquals("Gomez Actualizado", usuario.getApellido());
        assertEquals("UserName", usuario.getUsername());
    }
}

