package com.tallerwebi.dominio;
import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
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


    private RepositorioPartida repositorioPartida;
    private ServicioUsuario servicioUsuario;
    private RepositorioUsuarioImpl repositorioUsuario;
    private RepositorioJugador repositorioJugador;
    private ServicioDeckOfCards servicioDeckOfCards;

    public ServicioPartidaImpl(RepositorioPartida repositorioPartida) {
        this.repositorioPartida= repositorioPartida;
    }

    public ServicioPartidaImpl(){
    }
    public void setRepositorioPartida(RepositorioPartida repositorioPartida) {
        this.repositorioPartida = repositorioPartida;
    }
    public ServicioPartidaImpl(ServicioUsuarioImpl servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Autowired

    public ServicioPartidaImpl(ServicioDeckOfCards servicioDeckOfCards, RepositorioPartidaImpl respositorioPartida, RepositorioUsuarioImpl repositorioUsuario, RepositorioJugadorImpl repositorioJugador, ServicioUsuario servicioUsuario){
        this.repositorioPartida=respositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioJugador = repositorioJugador;
        this.servicioUsuario=servicioUsuario;
        this.servicioDeckOfCards= servicioDeckOfCards;

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
    public void bloquearBotones(Partida partida) {
        partida.setEstadoPartida(EstadoPartida.INACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.FINALIZADA);
        partida.setBotonesDesicionHabilitados(false);
        partida.setFichasHabilitadas(false);
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
    public void seleccionBotonEstrategia(Partida partidaActiva) {
        partidaActiva.setBotonEstrategia(true);
    }

    @Override
    public String mandarEstrategia(Partida partidaActiva, Integer jugadorPuntaje, Integer  crupierPuntaje) {

    //Mejorar valores según el juego
        String mensajeRecibido;

        if (jugadorPuntaje <= 8) {
            mensajeRecibido = "Pedi una carta, no hay riesgo.";
        }
        else if (jugadorPuntaje == 9 && crupierPuntaje >= 3 && crupierPuntaje <= 6) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }
        else if (jugadorPuntaje == 10 && crupierPuntaje <= 9) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }
        else if (jugadorPuntaje == 11 && crupierPuntaje <= 10) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }
        else if (jugadorPuntaje == 12 && crupierPuntaje >= 4 && crupierPuntaje <= 6) {
            mensajeRecibido = "Plantate.";
        }
        else if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje <= 6) {
            mensajeRecibido = "Plantate.";
        }
        else if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje >= 7) {
            mensajeRecibido = "Pedi una carta.";
        }
        else if (jugadorPuntaje >= 17) {
            mensajeRecibido = "Plantate.";
        }
        else {
            mensajeRecibido = "Pedi una carta.";
        }

        Jugador jugador= partidaActiva.getJugador();
        jugador.setMensajeEstrategia(mensajeRecibido);
        return mensajeRecibido;
    }


    @Override
    public Partida obtenerPartidaActiva(Usuario usuario) {
        List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
        if (!partidas.isEmpty()) {
            return partidas.get(0);
        }
        return null;
    }

    @Override
    public Integer doblarApuesta(Partida partidaActiva, Jugador jugador) {
        Integer apuestaOriginal = partidaActiva.getApuesta();
        Integer nuevaApuesta = (apuestaOriginal * 2);

        partidaActiva.setApuesta(nuevaApuesta);
        jugador.setSaldo(jugador.getSaldo() - nuevaApuesta);

        return nuevaApuesta;
    }

    @Override
    public String resultadoDeLaPartida(Integer puntosCrupier, Integer puntosJugador) {
        String resul= "No hay resultado";
        if (puntosJugador > 21 && puntosCrupier <= 21) {
            return "Resultado: Superaste los 21, Crupier gana";
        } else if (puntosCrupier > 21 && puntosJugador <= 21) {
            return "Resultado: El crupier se paso de 21, Jugador gana";
        } else if (puntosCrupier > 21 && puntosJugador > 21) {
            return "Resultado: Ambos superaron los 21, nadie gana";
        } else if (puntosJugador > puntosCrupier) {
            return "Resultado: Jugador gana";
        } else if (puntosCrupier > puntosJugador) {
            resul="Crupier gana";
            return "Resultado: Crupier gana";
        } else {
            resul="empate";
        }
        return "Resultado: " + resul;
    }

    @Override
    public void rendirse(Partida partidaActiva, Jugador jugador) {
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
        partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
        repositorioPartida.actualizar(partidaActiva);
    }


    @Override
    public void apostar(Partida partida, Integer monto)
            throws ApuestaInvalidaException, SaldoInsuficiente {

        Jugador jugador = partida.getJugador();
        Double saldoActual = jugador.getSaldo();

        if (saldoActual == null || saldoActual < monto) {
            throw new SaldoInsuficiente("Saldo insuficiente para realizar la apuesta.");
        }

        jugador.setSaldo(saldoActual - monto);

        if (partida.getApuesta() == null) {
            partida.setApuesta(0);
        }
        partida.setApuesta(partida.getApuesta() + monto);
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
    public void setBotonesAlCrearPartida(Partida partida) {
        if(partida.getEstadoJuego().equals(EstadoDeJuego.APUESTA)){
            partida.setBotonesDesicionHabilitados(false);
            partida.setFichasHabilitadas(true);
        }else if(partida.getEstadoJuego().equals(EstadoDeJuego.JUEGO)){
            partida.setBotonesDesicionHabilitados(true);
            partida.setFichasHabilitadas(false);
        } else if (partida.getEstadoJuego().equals(EstadoDeJuego.FINALIZADA) || partida.getEstadoJuego().equals(EstadoDeJuego.ABANDONADO)) {
            partida.setBotonesDesicionHabilitados(false);
            partida.setFichasHabilitadas(false);
        }
    }

    public Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException {
        Partida partida =  new Partida();
        Crupier crupier= new Crupier();
        partida.setCrupier(crupier);
        partida.setJugador(jugador);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
        if(isNull(partida)){
            throw new PartidaNoCreadaException();
        }
        return repositorioPartida.guardar(partida);
    }




    @Override
    public void setearApuesta(Usuario usuario, Partida partida) {

        //List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
//        if (!partidas.isEmpty()) {
//            Partida partida = partidas.get(0);
        servicioUsuario.actualizarSaldoDeUsuario(usuario, partida.getApuesta());
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


    @Override
    public Map<String, Object> pedirCarta(Jugador jugador, List<Map<String, Object>> cartasJugador, String deckId) {
        if (deckId == null || deckId.isEmpty()) {
            return null;
        }

        if(jugador.getPuntaje()<21){
            List<Map<String, Object>> nuevaCarta = servicioDeckOfCards.sacarCartas(deckId, 1);
            cartasJugador.add(nuevaCarta.get(0));

            int puntajeJugador = calcularPuntaje(cartasJugador);
            jugador.setPuntaje(puntajeJugador);

            return nuevaCarta.get(0);
        }
        return null;
    }

    public void setServicioDeckOfCards(ServicioDeckOfCards servicioDeckOfCards) {
        this.servicioDeckOfCards = servicioDeckOfCards;
    }

}