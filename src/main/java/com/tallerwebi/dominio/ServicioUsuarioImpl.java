package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{
    private RepositorioUsuario repositorioUsuario;

    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void actualizarSaldoDeUsuario(Usuario usuario, Integer montoApuesta) {
        usuario.setSaldo(usuario.getSaldo()-montoApuesta);
        repositorioUsuario.guardar(usuario);
    }
}
