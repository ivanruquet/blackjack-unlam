package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EstadoPartida;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("RepositorioPartida")
public class RepositorioPartidaImpl implements RepositorioPartida {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Partida p) {
       sessionFactory.getCurrentSession().save(p);
    }

    @Override
    public Partida buscarPartida() {
        String hql = "FROM Partida p WHERE p.estadoPartida = :estadoBuscado";
        Query<Partida> query = sessionFactory.getCurrentSession().createQuery(hql, Partida.class);
        query.setParameter("estadoBuscado", EstadoPartida.APUESTA);
        return query.uniqueResult();
    }

    @Override
    public void borrarPartida(Partida p) {
        sessionFactory.getCurrentSession().delete(p);
    }


}
