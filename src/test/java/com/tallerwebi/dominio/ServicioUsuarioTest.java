package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class ServicioUsuarioTest {

    RepositorioUsuarioImpl repositorioUsuario = mock(RepositorioUsuarioImpl.class);
    ServicioUsuarioImpl servicio= new ServicioUsuarioImpl(repositorioUsuario);
    Usuario usuario1 = new Usuario("user@gmail.com","user","userNombre","userApellido", LocalDate.now(), "UserName");


    @Test
    public void queSePuedaActualizarElNombreYApellidoDelUsuario() {

        Usuario usuario= givenExisteUsuarioConNombreYApellido(usuario1);
        whenActualizoLosDatos(usuario);
        thenAparecenModificadosEnElPerfil(usuario);
    }

    private Usuario givenExisteUsuarioConNombreYApellido(Usuario usuario) {
        assertEquals("userNombre", usuario.getNombre());
        assertEquals("userApellido", usuario.getApellido());

        return usuario;
    }

    private void whenActualizoLosDatos(Usuario usuario) {
        servicio.modificarNombre(usuario, "Mia Actualizada");
        servicio.modificarApellido(usuario, "Gomez Actualizado");

    }

    private void thenAparecenModificadosEnElPerfil(Usuario usuario) {
        assertEquals(usuario.getNombre(), "Mia Actualizada");
        assertEquals(usuario.getApellido(), "Gomez Actualizado");

    }






}
