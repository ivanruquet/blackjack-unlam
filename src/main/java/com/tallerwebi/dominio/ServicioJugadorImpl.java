package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioJugadorImpl implements ServicioJugador {

    public RepositorioJugador repositorioJugador;
@Autowired
public ServicioJugadorImpl( RepositorioJugador repositorioJugador) {
    this.repositorioJugador = repositorioJugador;
}

    @Override
    public Jugador buscarJugadorPorUsuario(Usuario usuario) {
        return repositorioJugador.buscarJugadorPorUsuario(usuario);
    }

    @Override
    public void modificarJugador(Jugador jugador) {
        repositorioJugador.modificarJugador(jugador);
    }
}
