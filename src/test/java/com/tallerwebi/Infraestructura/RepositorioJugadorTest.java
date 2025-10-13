package com.tallerwebi.Infraestructura;

import com.tallerwebi.config.HibernateConfig;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.RepositorioJugador;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateConfig.class})
public class RepositorioJugadorTest {

    @Autowired
    RepositorioJugador repositorioJugador;
    @Autowired
    SessionFactory sessionFactory;


    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnJugador(){
        Jugador jugador = givenQueExisteUnJugador();
        whenGuardoEseJugador(jugador);
        thenSeObtieneConExito(jugador);
    }

    private void thenSeObtieneConExito(Jugador jugador) {
        assertEquals(jugador.getId(), repositorioJugador.buscarJugador(jugador).getId());
    }

    private void whenGuardoEseJugador(Jugador jugador) {
        repositorioJugador.guardar(jugador);
    }

    private Jugador givenQueExisteUnJugador() {
        Usuario usuario = new Usuario("","","","", LocalDate.now(), "");
        sessionFactory.getCurrentSession().save(usuario);
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        return jugador;
    }
}
