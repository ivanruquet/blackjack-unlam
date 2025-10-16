package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private RepositorioUsuarioImpl repositorioUsuario;


    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuarioImpl repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public void modificarNombre(Usuario usuario, String nombreActualizado) {
        usuario.setNombre(nombreActualizado);
        repositorioUsuario.guardarModificaciones(usuario);
    }

    public void modificarApellido(Usuario usuario, String apellidoActualizado) {
        usuario.setApellido(apellidoActualizado);
        repositorioUsuario.guardarModificaciones(usuario);
    }

}
