package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
import com.tallerwebi.infraestructura.RepositorioJugadorImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {


    private RepositorioPartida respositorioPartida;

    private RepositorioPartida repositorioPartida;
    private ServicioUsuarioImpl servicioUsuario;
    private RepositorioUsuarioImpl repositorioUsuario;
    private RepositorioJugador repositorioJugador;

    public ServicioPartidaImpl(){
    }

    public ServicioPartidaImpl(ServicioUsuarioImpl servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Autowired
    public ServicioPartidaImpl(RepositorioPartida respositorioPartida, RepositorioUsuarioImpl repositorioUsuario, RepositorioJugadorImpl repositorioJugador){
        this.repositorioPartida=respositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioJugador = repositorioJugador;
    }

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida) {
        this.repositorioPartida= repositorioPartida;
    }

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioJugador repositorioJugador) {
        this.repositorioJugador = repositorioJugador;
        this.repositorioPartida = repositorioPartida;
    }


    public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException {
        corroborarExistenciaDePartidaActiva(usuario);
        Jugador jugador = crearJugador(usuario);
        Partida partida = instanciarPartida(jugador);
        if(isNull(partida)){
            throw new PartidaNoCreadaException();
        }
        return partida;
    }

    @Override
    public void apostar(Partida partida, Integer apuesta, Integer monto) {
        if(apuesta == null){
            apuesta = 0;
        }
        partida.setApuesta(apuesta + monto);
    }

    @Override
    public void comenzarPartida(Partida partida) {
        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        partida.setBotonesDesicionHabilitados(false);
        partida.setFichasHabilitadas(true);
    }

    @Override
    public void empezarPartida(Partida partida) {
        partida.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        partida.setBotonesDesicionHabilitados(true);
        partida.setFichasHabilitadas(false);
        if(partida.getJugador() != null && partida.getApuesta() != null) {
            partida.getJugador().restarSaldo((double) partida.getApuesta());
        }
    }


    private Partida instanciarPartida(Jugador jugador) {
        Partida partida =  new Partida();
        partida.setJugador(jugador);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);

        return repositorioPartida.guardar(partida);
    }

    private Jugador crearJugador(Usuario usuario) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        repositorioJugador.guardar(jugador);
        return jugador;
    }

    private void corroborarExistenciaDePartidaActiva(Usuario usuario) {
        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
        if(!partidasActivas.isEmpty()){
            for(Partida partidaActiva: partidasActivas){
                partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
                partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
            }
        }
    }

    @Override
    public void apostar(Usuario usuario, int monto) {

        List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);

        if (!partidas.isEmpty()) {
            Partida partida = partidas.get(0);
            partida.setApuesta(partida.getApuesta() + monto);
            repositorioPartida.guardar(partida);
        }

    }

    @Override
    public void resetearPartida(Usuario usuario) {

        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);

        if (!partidasActivas.isEmpty()) {

            Partida partida = partidasActivas.get(0);

            partida.setApuesta(0);
            partida.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
            partida.setEstadoPartida(EstadoPartida.INACTIVA);

            repositorioPartida.guardar(partida);

        }

    }

    @Override
    public void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente {

        if (monto <= 0){
            throw new ApuestaInvalidaException("El monto debe ser mayor a 0");
        }

        if (usuario.getSaldo() < monto){
            throw new SaldoInsuficiente("El saldo debe ser mayor a 0");
        }

        usuario.setSaldo(usuario.getSaldo() - monto);

        apostar(usuario, monto);

    }



}
