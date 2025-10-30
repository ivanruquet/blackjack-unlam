package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

@Override
public void registrar(Usuario usuario) throws UsuarioExistente {
    Usuario usuarioBuscado = repositorioUsuario.buscar(usuario.getEmail());
    if (usuarioBuscado != null) {
        throw new UsuarioExistente();
    }

    repositorioUsuario.guardar(usuario);
}

    public void guardar(Usuario usuario) {
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repositorioUsuario.buscar(email);
    }
}



