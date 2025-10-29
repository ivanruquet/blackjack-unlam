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

    public void modificarAtributos(Usuario usuario, String nombreActualizado,  String apellidoActualizado, String username) {
        usuario.setNombre(nombreActualizado);
        usuario.setApellido(apellidoActualizado);
        usuario.setUsername(username);
        repositorioUsuario.guardarModificaciones(usuario);
    }



}
