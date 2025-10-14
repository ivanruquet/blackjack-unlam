package com.tallerwebi.Infraestructura;

import com.tallerwebi.config.HibernateConfig;
import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;


import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateConfig.class})
public class RepositorioPartidaTest {
   @Autowired
    RepositorioPartida repositorioPartida;
   @Autowired
    SessionFactory sessionFactory;


   @Test
   @Transactional
   @Rollback
    public void queSePuedanObtenerLasPartidasActivas() {

       Usuario usuario = givenQueExisteUnUsuarioConUnaPartidaActiva(EstadoPartida.ACTIVA);
       givenQueExisteUnUsuarioConUnaPartidaInactiva(EstadoPartida.INACTIVA);

       List<Partida> partidasBuscadas = whenBuscoPartidasActivas(usuario);

       thenEncuentroUnaSolaPartida(partidasBuscadas);

   }



   //-------------------------------------------------------------------------------
    private void thenEncuentroUnaSolaPartida(List<Partida> partidasBuscadas){
        assertEquals(1, partidasBuscadas.size());
    }
    private List<Partida> whenBuscoPartidasActivas(Usuario usuario) {
       List<Partida> partidasBuscadas = repositorioPartida.buscarPartidaActiva(usuario);
       return partidasBuscadas;
    }

    private void givenQueExisteUnUsuarioConUnaPartidaInactiva(EstadoPartida estadoPartida) {
        Usuario usuario2 = new Usuario("email1","pass1","nombre1","apellido1", LocalDate.now(), "user1");
        sessionFactory.getCurrentSession().save(usuario2);
        Jugador j2 = new Jugador();
        j2.setUsuario(usuario2);
        Partida p1 = new Partida();
        p1.setEstadoPartida(EstadoPartida.INACTIVA);
        p1.setJugador(j2);
        sessionFactory.getCurrentSession().save(p1);
   }

    private Usuario givenQueExisteUnUsuarioConUnaPartidaActiva(EstadoPartida e) {
        Usuario usuario = new Usuario("email2","pass2","nombre2","apellido2", LocalDate.now(), "user2");
        sessionFactory.getCurrentSession().save(usuario);
        Jugador j = new Jugador();
        j.setUsuario(usuario);
        Partida p2 = new Partida();
        p2.setEstadoPartida(EstadoPartida.ACTIVA);
        p2.setJugador(j);
        sessionFactory.getCurrentSession().save(p2);
        return usuario;
    }


}
