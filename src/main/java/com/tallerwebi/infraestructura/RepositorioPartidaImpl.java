package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EstadoPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RepositorioPartida")
public class RepositorioPartidaImpl implements RepositorioPartida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Partida guardar(Partida p){
            sessionFactory.getCurrentSession().save(p);
            return p;

    }

    @Override
    public List<Partida> buscarPartidaActiva(Usuario usuario) {
       return sessionFactory.getCurrentSession()
                .createCriteria(Partida.class, "p")
                .createAlias("jugador", "j")
                .createAlias("j.usuario", "u")
                .add(Restrictions.eq("p.estadoPartida", EstadoPartida.ACTIVA))
                .add(Restrictions.eq("u.id", usuario.getId()))
                .list();
    }

    @Override
    public void borrarPartida(Partida p) {
        sessionFactory.getCurrentSession().delete(p);
    }

    @Override
    public void actualizar(Partida partida) {
        sessionFactory.getCurrentSession().update(partida);
    }

    @Override
    public Partida buscarPartidaPorId(Long id) {
        return (Partida)sessionFactory.getCurrentSession()
                .createCriteria(Partida.class, "p")
                .add(Restrictions.eq("p.id", id))
                .add(Restrictions.eq("p.estadoPartida", EstadoPartida.ACTIVA))
                .uniqueResult();
    }


}
