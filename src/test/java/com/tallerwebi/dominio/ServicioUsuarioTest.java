package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(repositorioUsuario);
    Usuario usuario1 = new Usuario("user@gmail.com", "user", "userNombre", "userApellido", LocalDate.now(), "UserName");
    private static final double RECOMPENSA_DIARIA = 500;

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


    @Test
    public void queSeOtorgueRecompensaSiUsuarioNoIngresoHoy() {
        Usuario usuario = givenUsuarioQueNoIngresoHoy();
        whenRegistroSuIngreso(usuario);
        thenSeOtorgaRecompensaYSeActualizaFecha(usuario);
    }

    private Usuario givenUsuarioQueNoIngresoHoy() {
        usuario1.setSaldo(0);
        usuario1.setRacha(0);
        usuario1.setUltimoIngreso(LocalDate.now().minusDays(1));
        return usuario1;
    }

    private void whenRegistroSuIngreso(Usuario usuario) {
        when(repositorioUsuario.buscarPorId(usuario.getId())).thenReturn(usuario);
        servicioUsuario.registrarIngreso(usuario.getId());
    }

    private void thenSeOtorgaRecompensaYSeActualizaFecha(Usuario usuario) {
        assertEquals((int)RECOMPENSA_DIARIA, usuario.getSaldo());
        assertEquals(LocalDate.now(), usuario.getUltimoIngreso());
        assertEquals(1, usuario.getRacha());
    }

    @Test
    public void queNoSeOtorgueRecompensaSiYaIngresoHoy() {
        Usuario usuario = givenUsuarioQueYaIngresoHoy();
        whenRegistroSuIngreso(usuario);
        thenNoSeOtorgaRecompensa(usuario);
    }

    private Usuario givenUsuarioQueYaIngresoHoy() {
        usuario1.setSaldo(500);
        usuario1.setRacha(1);
        usuario1.setUltimoIngreso(LocalDate.now());
        when(repositorioUsuario.buscarPorId(usuario1.getId())).thenReturn(usuario1);
        return usuario1;
    }

    private void thenNoSeOtorgaRecompensa(Usuario usuario) {
        assertEquals(500, usuario.getSaldo());
        assertEquals(1, usuario.getRacha());
    }
}

