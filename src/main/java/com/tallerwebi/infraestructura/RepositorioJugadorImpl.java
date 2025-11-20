package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.RepositorioJugador;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("RepositorioJugador")

public class RepositorioJugadorImpl implements RepositorioJugador {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioJugadorImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(Jugador jugador) {
        sessionFactory.getCurrentSession().save(jugador);
    }

    @Override
    public Jugador buscarJugador(Jugador jugador) {
        return sessionFactory.getCurrentSession().get(Jugador.class, jugador.getId());
    }

    @Override
    public Jugador buscarJugadorPorUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Jugador j WHERE j.usuario = :usuario", Jugador.class)
                .setParameter("usuario", usuario)
                .uniqueResult();
    }

    @Override
    public void modificarJugador(Jugador jugador) {
        sessionFactory.getCurrentSession().saveOrUpdate(jugador);
    }
}
