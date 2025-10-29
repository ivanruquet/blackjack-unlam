package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.infraestructura.RepositorioJugadorImpl;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {


    private RepositorioPartida respositorioPartida;

    private RepositorioPartida repositorioPartida;
    private ServicioUsuario servicioUsuario;
    private RepositorioUsuarioImpl repositorioUsuario;
    private RepositorioJugador repositorioJugador;

    public ServicioPartidaImpl(){
    }

    public ServicioPartidaImpl(ServicioUsuarioImpl servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Autowired

    public ServicioPartidaImpl(RepositorioPartidaImpl respositorioPartida, RepositorioUsuarioImpl repositorioUsuario, RepositorioJugadorImpl repositorioJugador, ServicioUsuario servicioUsuario){
        this.repositorioPartida=respositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioJugador = repositorioJugador;
        this.servicioUsuario=servicioUsuario;
    }

    public ServicioPartidaImpl(RepositorioPartidaImpl repositorioPartida) {
        this.repositorioPartida= repositorioPartida;
    }

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioJugador repositorioJugador) {
        this.repositorioJugador = repositorioJugador;
        this.repositorioPartida = repositorioPartida;
    }




    @Override
    public void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException {
        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
        if(!partidasActivas.isEmpty()){
            throw new PartidaExistenteActivaException();
        }

    }

    @Override
    public void inactivarPartidas(List<Partida> partidasActivas) {
        for(Partida partidaActiva: partidasActivas){
            partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
            partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
            repositorioPartida.guardar(partidaActiva);
        }
    }

    @Override
    public Jugador crearJugador(Usuario usuario) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);
        repositorioJugador.guardar(jugador);
        return jugador;
    }

    @Override
    public void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException {
        if(p.getEstadoJuego().equals(EstadoDeJuego.APUESTA)){
            p.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        }else{
            throw new PartidaActivaNoEnApuestaException();
        }

    }

    @Override
    public List<Partida> buscarPartidaActiva(Usuario usuario) {
        return repositorioPartida.buscarPartidaActiva(usuario);
    }


    @Override
    public void apostar(Partida partida, Integer apuesta, Integer monto) {
        if(apuesta == null){
            apuesta = 0;
        }
        partida.setApuesta(apuesta + monto);
    }

    @Override
    public void setBotonesAlCrearPartida(Partida partida) {
       // partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        partida.setBotonesDesicionHabilitados(false);
        partida.setFichasHabilitadas(true);
    }

    @Override
    public void setBotonesAlComenzarPartida(Partida partida) {
      //  partida.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
        partida.setBotonesDesicionHabilitados(true);
        partida.setFichasHabilitadas(false);
        if(partida.getJugador() != null && partida.getApuesta() != null) {
            partida.getJugador().restarSaldo((double) partida.getApuesta());
        }
    }




    public Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException {
        Partida partida =  new Partida();
        partida.setJugador(jugador);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        if(isNull(partida)){
            throw new PartidaNoCreadaException();
        }
        return repositorioPartida.guardar(partida);
    }





    @Override
    public void setearApuesta(Usuario usuario, Integer monto, Partida partida) {

        //List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
//        if (!partidas.isEmpty()) {
//            Partida partida = partidas.get(0);
            partida.setApuesta(monto);
            servicioUsuario.actualizarSaldoDeUsuario(usuario, monto);
            repositorioUsuario.actualizar(usuario);
            repositorioPartida.actualizar(partida);
       // }

    }

    @Override
    public int calcularPuntaje(List<Map<String, Object>> cartas) {
        int total = 0;
        int ases = 0;

        for (Map<String, Object> carta : cartas) {
            String valor = (String) carta.get("value");
            switch (valor) {
                case "KING":
                case "QUEEN":
                case "JACK":
                    total += 10; break;
                case "ACE":
                    total += 11;
                    ases++;
                    break;
                default:
                    total += Integer.parseInt(valor);
            }
        }
        // Si se pasa de 21, los Ases valen 1
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return total;
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
//modificar nombre de metodo(confuso) ->-> validar saldo o algo asi
        if (monto <= 0){
            throw new ApuestaInvalidaException("El monto debe ser mayor a 0");
        }

        if (usuario.getSaldo() < monto){
            throw new SaldoInsuficiente("El saldo debe ser mayor a 0");
        }



    }



}
