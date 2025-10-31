//package com.tallerwebi.dominio;
//
//import com.tallerwebi.dominio.excepcion.ApuestaInvalidaException;
//import com.tallerwebi.dominio.excepcion.PartidaExistenteActivaException;
//import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;
//import com.tallerwebi.dominio.excepcion.PartidaActivaNoEnApuestaException;
//
//import com.tallerwebi.dominio.excepcion.SaldoInsuficiente;
//import com.tallerwebi.infraestructura.RepositorioJugadorImpl;
//import com.tallerwebi.infraestructura.RepositorioPartidaImpl;
//import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Map;
//
//import static java.util.Objects.isNull;
//
//@Service
//@Transactional
//public abstract class ServicioPartidaImpl implements ServicioPartida {
//
//
//    private RepositorioPartida respositorioPartida;
//
//    private RepositorioPartida repositorioPartida;
//    private ServicioUsuarioImpl servicioUsuario;
//    private RepositorioUsuario repositorioUsuario;
//    private RepositorioJugador repositorioJugador;
//
//    public void setRepositorioPartida(RepositorioPartida repositorioPartida) {
//        this.repositorioPartida = repositorioPartida;
//    }
//    public ServicioPartidaImpl(ServicioUsuarioImpl servicioUsuario) {
//        this.servicioUsuario = servicioUsuario;
//    }
//
//    @Autowired
//    public ServicioPartidaImpl(RepositorioPartida respositorioPartida, RepositorioUsuario repositorioUsuario, RepositorioJugadorImpl repositorioJugador){
//        this.repositorioPartida=respositorioPartida;
//        this.repositorioUsuario = repositorioUsuario;
//        this.repositorioJugador = repositorioJugador;
//        this.servicioUsuario=servicioUsuario;
//    }
//
//    public ServicioPartidaImpl(RepositorioPartida repositorioPartida) {
//        this.repositorioPartida= repositorioPartida;
//    }
//
//    public ServicioPartidaImpl(RepositorioPartida repositorioPartida, RepositorioJugador repositorioJugador) {
//        this.repositorioJugador = repositorioJugador;
//        this.repositorioPartida = repositorioPartida;
//    }
//
//
//
//    @Override
//    public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException {
//        corroborarExistenciaDePartidaActiva(usuario);
//        Jugador jugador = crearJugador(usuario);
//        Partida partida = instanciarPartida(jugador);
//        if(isNull(partida)){
//            throw new PartidaNoCreadaException();
//        }
//        return partida;
//    }
//
//    @Override
//    public void consultarExistenciaDePartidaActiva(Usuario usuario) throws PartidaExistenteActivaException {
//        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
//        if(!partidasActivas.isEmpty()){
//            throw new PartidaExistenteActivaException();
//        }
//
//    }
//
//    @Override
//    public void inactivarPartidas(List<Partida> partidasActivas) {
//        for(Partida partidaActiva: partidasActivas){
//            partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
//            partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
//            repositorioPartida.guardar(partidaActiva);
//        }
//    }
//
//    @Override
//    public Jugador crearJugador(Usuario usuario) {
//        Jugador jugador = new Jugador();
//        jugador.setUsuario(usuario);
//        repositorioJugador.guardar(jugador);
//        return jugador;
//    }
//
//    @Override
//    public void cambiarEstadoDeJuegoAJuegoDeUnaPartida(Partida p) throws PartidaActivaNoEnApuestaException {
//        if(p.getEstadoJuego().equals(EstadoDeJuego.APUESTA)){
//            p.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
//        }else{
//            throw new PartidaActivaNoEnApuestaException();
//        }
//
//    }
//
//    @Override
//    public List<Partida> buscarPartidaActiva(Usuario usuario) {
//        return repositorioPartida.buscarPartidaActiva(usuario);
//    }
//
//    @Override
//    public void seleccionBotonEstrategia(Partida partidaActiva) {
//        partidaActiva.setBotonEstrategia(true);
//    }
//
//    @Override
//    public String mandarEstrategia(Partida partidaActiva, Jugador jugador) {
//        Crupier crupier = partidaActiva.getCrupier();
//
//        if (crupier == null) {
//            crupier = new Crupier();
//            partidaActiva.setCrupier(crupier);
//        }
//
//        if (jugador.getPuntaje() == null) {
//            //de prueba hasta que unamos las cartas y obtengamos el puntaje de ahí
//            jugador.setPuntaje(17);
//        }
//        if (crupier.getPuntaje() == null) {
//            crupier.setPuntaje(12);
//        }
//
//        Integer jugadorPuntaje = jugador.getPuntaje();
//        Integer crupierPuntaje = crupier.getPuntaje();
//
//        String mensajeRecibido = "Pedi una carta";
//
//        if (jugadorPuntaje <= 8) {
//            mensajeRecibido = "Pedi una carta, no hay riesgo";
//        }
//        if (jugadorPuntaje == 9 && crupierPuntaje >= 3 && crupierPuntaje <= 6) {
//            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
//        }
//        if (jugadorPuntaje == 10 && crupierPuntaje <= 9) {
//            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
//        }
//        if (jugadorPuntaje == 11 && crupierPuntaje <= 10) {
//            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
//        }
//
//        if (jugadorPuntaje == 12 && crupierPuntaje >= 4 && crupierPuntaje <= 6) {
//            mensajeRecibido = "Plantate.";
//        }
//        if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje <= 6) {
//            mensajeRecibido = "Plantate.";
//        }
//        if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje >= 7) {
//            mensajeRecibido = "Pedi una carta.";
//        }
//        if (jugadorPuntaje >= 17) {
//            mensajeRecibido = "Plantate.";
//        }
//
//        jugador.setMensajeEstrategia(mensajeRecibido);
//        return mensajeRecibido;
//    }
//
//    @Override
//    public Partida obtenerPartidaActiva(Usuario usuario) {
//        List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
//        if (!partidas.isEmpty()) {
//            return partidas.get(0);
//        }
//        return null;
//    }
//
//    @Override
//    public Double doblarApuesta(Partida partidaActiva, Jugador jugador) {
//        Integer apuestaOriginal = partidaActiva.getApuesta();
//        Integer nuevaApuesta = (apuestaOriginal * 2);
//
//        partidaActiva.setApuesta(nuevaApuesta);
//        jugador.setSaldo(jugador.getSaldo() - apuestaOriginal);
//
//        return jugador.getSaldo();
//    }
//
//    @Override
//    public String resultadoDeLaPartida(Integer puntosCrupier, Integer puntosJugador) {
//        String resul= "No hay resultado";
//        if (puntosJugador > 21 && puntosCrupier <= 21) {
//            resul="Crupier gana";
//        }
//        if (puntosCrupier > 21 && puntosJugador <= 21) {
//            resul="Jugador gana";
//        }
//        if (puntosCrupier > 21 && puntosJugador > 21) {
//            resul="Nadie gana";
//        }
//        if (puntosJugador > puntosCrupier) {
//            resul="Jugador gana";
//
//        } else if (puntosCrupier > puntosJugador) {
//            resul="Crupier gana";
//        } else {
//            resul="empate";
//        }
//        return "Resultado: " + resul;
//    }
//
//
//    @Override
//    public void rendirse(Partida partidaActiva, Jugador jugador) {
//        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
//    }
//
//
//    @Override
//    public void apostar(Partida partida, Integer apuesta, Integer monto) {
//        if(apuesta == null){
//            apuesta = 0;
//        }
//        partida.setApuesta(apuesta + monto);
//    }
//
//    @Override
//    public void setBotonesAlCrearPartida(Partida partida) {
//       // partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
//        partida.setBotonesDesicionHabilitados(false);
//        partida.setFichasHabilitadas(true);
//    }
//
//    @Override
//    public void setBotonesAlComenzarPartida(Partida partida) {
//      //  partida.cambiarEstadoDeJuego(EstadoDeJuego.JUEGO);
//        partida.setBotonesDesicionHabilitados(true);
//        partida.setFichasHabilitadas(false);
//        if(partida.getJugador() != null && partida.getApuesta() != null) {
//            partida.getJugador().restarSaldo((double) partida.getApuesta());
//        }
//    }
//
//
//
//    @Override
//    public Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException {
//        Partida partida =  new Partida();
//        Crupier crupier= new Crupier();
//        partida.setCrupier(crupier);
//        jugador.setPuntaje(5);
//        crupier.setPuntaje(5);
//        partida.setApuesta(0);
//        partida.setJugador(jugador);
//        partida.setEstadoPartida(EstadoPartida.ACTIVA);
//        partida.cambiarEstadoDeJuego(EstadoDeJuego.APUESTA);
//        if(isNull(partida)){
//            throw new PartidaNoCreadaException();
//        }
//        return repositorioPartida.guardar(partida);
//    }
//
//
//
//
//
//    @Override
//    public void setearApuesta(Usuario usuario, Integer monto, Partida partida) {
//
//        //List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
////        if (!partidas.isEmpty()) {
////            Partida partida = partidas.get(0);
//            partida.setApuesta(monto);
//            servicioUsuario.actualizarSaldoDeUsuario(usuario, monto);
//            repositorioUsuario.actualizar(usuario);
//            repositorioPartida.actualizar(partida);
//       // }
//
//    }
//
//
//    private void corroborarExistenciaDePartidaActiva(Usuario usuario) {
//        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
//        if(!partidasActivas.isEmpty()){
//            for(Partida partidaActiva: partidasActivas){
//                partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
//                partidaActiva.setEstadoPartida(EstadoPartida.INACTIVA);
//            }
//        }
//    }
//    @Override
//    public int calcularPuntaje(List<Map<String, Object>> cartas) {
//        int total = 0;
//        int ases = 0;
//
//        for (Map<String, Object> carta : cartas) {
//            String valor = (String) carta.get("value");
//            switch (valor) {
//                case "KING":
//                case "QUEEN":
//                case "JACK":
//                    total += 10; break;
//                case "ACE":
//                    total += 11;
//                    ases++;
//                    break;
//                default:
//                    total += Integer.parseInt(valor);
//            }
//        }
//        // Si se pasa de 21, los Ases valen 1
//        while (total > 21 && ases > 0) {
//            total -= 10;
//            ases--;
//        }
//        return total;
//    }
//
//    @Override
//    public void apostar(Usuario usuario, int monto) {
//
//        List<Partida> partidas = repositorioPartida.buscarPartidaActiva(usuario);
//        if (!partidas.isEmpty()) {
//            Partida partida = partidas.get(0);
//            partida.setApuesta(partida.getApuesta() + monto);
//            repositorioPartida.guardar(partida);
//        }
//
//    }
//
//
//    @Override
//    public void resetearPartida(Usuario usuario) {
//
//        List<Partida> partidasActivas = repositorioPartida.buscarPartidaActiva(usuario);
//
//        if (!partidasActivas.isEmpty()) {
//
//            Partida partida = partidasActivas.get(0);
//
//            partida.setApuesta(0);
//            partida.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
//            partida.setEstadoPartida(EstadoPartida.INACTIVA);
//
//            repositorioPartida.guardar(partida);
//
//        }
//
//    }
//
//    @Override
//    public void validarPartida(Usuario usuario, int monto) throws ApuestaInvalidaException, SaldoInsuficiente {
////modificar nombre de metodo(confuso) ->-> validar saldo o algo asi
//        if (monto <= 0){
//            throw new ApuestaInvalidaException("El monto debe ser mayor a 0");
//        }
//
//        if (usuario.getSaldo() < monto){
//            throw new SaldoInsuficiente("El saldo debe ser mayor a 0");
//        }
//
//        usuario.setSaldo(usuario.getSaldo() - monto);
//
//        apostar(usuario, monto);
//
//    }
//
//
//
//}

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

    public ServicioPartidaImpl(RepositorioPartidaImpl respositorioPartida, RepositorioUsuarioImpl repositorioUsuario, RepositorioJugadorImpl repositorioJugador, ServicioUsuario servicioUsuario){
        this.repositorioPartida=respositorioPartida;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioJugador = repositorioJugador;
        this.servicioUsuario=servicioUsuario;
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
    public String mandarEstrategia(Partida partidaActiva, Jugador jugador) {
        Crupier crupier = partidaActiva.getCrupier();

        if (crupier == null) {
            crupier = new Crupier();
            partidaActiva.setCrupier(crupier);
        }

        if (jugador.getPuntaje() == null) {
            //de prueba hasta que unamos las cartas y obtengamos el puntaje de ahí
            jugador.setPuntaje(17);
        }
        if (crupier.getPuntaje() == null) {
            crupier.setPuntaje(12);
        }

        Integer jugadorPuntaje = jugador.getPuntaje();
        Integer crupierPuntaje = crupier.getPuntaje();

        String mensajeRecibido = "Pedi una carta";

        if (jugadorPuntaje <= 8) {
            mensajeRecibido = "Pedi una carta, no hay riesgo";
        }
        if (jugadorPuntaje == 9 && crupierPuntaje >= 3 && crupierPuntaje <= 6) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }
        if (jugadorPuntaje == 10 && crupierPuntaje <= 9) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }
        if (jugadorPuntaje == 11 && crupierPuntaje <= 10) {
            mensajeRecibido = "Dobla si podes, sino pedi una carta.";
        }

        if (jugadorPuntaje == 12 && crupierPuntaje >= 4 && crupierPuntaje <= 6) {
            mensajeRecibido = "Plantate.";
        }
        if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje <= 6) {
            mensajeRecibido = "Plantate.";
        }
        if (jugadorPuntaje >= 13 && jugadorPuntaje <= 16 && crupierPuntaje >= 7) {
            mensajeRecibido = "Pedi una carta.";
        }
        if (jugadorPuntaje >= 17) {
            mensajeRecibido = "Plantate.";
        }

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
    public Double doblarApuesta(Partida partidaActiva, Jugador jugador) {
        Integer apuestaOriginal = partidaActiva.getApuesta();
        Integer nuevaApuesta = (apuestaOriginal * 2);

        partidaActiva.setApuesta(nuevaApuesta);
        jugador.setSaldo(jugador.getSaldo() - apuestaOriginal);

        return jugador.getSaldo();
    }

    @Override
    public String resultadoDeLaPartida(Integer puntosCrupier, Integer puntosJugador) {
        String resul= "No hay resultado";
        if (puntosJugador > 21 && puntosCrupier <= 21) {
            resul="Crupier gana";
        }
        if (puntosCrupier > 21 && puntosJugador <= 21) {
            resul="Jugador gana";
        }
        if (puntosCrupier > 21 && puntosJugador > 21) {
            resul="Nadie gana";
        }
        if (puntosJugador > puntosCrupier) {
            resul="Jugador gana";

        } else if (puntosCrupier > puntosJugador) {
            resul="Crupier gana";
        } else {
            resul="empate";
        }
        return "Resultado: " + resul;
    }


    @Override
    public void rendirse(Partida partidaActiva, Jugador jugador) {
        partidaActiva.cambiarEstadoDeJuego(EstadoDeJuego.ABANDONADO);
    }


    @Override
    public void apostar(Partida partida, Integer apuesta, Integer monto)
            throws ApuestaInvalidaException, SaldoInsuficiente {

        List<Integer> fichasValidas = List.of(10, 25, 50, 100);
        if (!fichasValidas.contains(monto)) {
            throw new ApuestaInvalidaException("El monto no corresponde a una ficha válida.");
        }

        Jugador jugador = partida.getJugador();
        Double saldoActual = jugador.getSaldo();

        if (saldoActual == null || saldoActual < monto) {
            throw new SaldoInsuficiente("Saldo insuficiente para realizar la apuesta.");
        }

        jugador.setSaldo(saldoActual - monto);

        if (apuesta == null) {
            apuesta = 0;
        }
        partida.setApuesta(apuesta + monto);
    }

    @Override
    public void setBotonesAlCrearPartida(Partida partida) {
        partida.setBotonesDesicionHabilitados(false);
        partida.setFichasHabilitadas(true);
    }

    @Override
    public void setBotonesAlComenzarPartida(Partida partida) {
        partida.setBotonesDesicionHabilitados(true);
        partida.setFichasHabilitadas(false);
        if(partida.getJugador() != null && partida.getApuesta() != null) {
            partida.getJugador().restarSaldo((double) partida.getApuesta());
        }
    }




    public Partida instanciarPartida(Jugador jugador) throws PartidaNoCreadaException {
        Partida partida =  new Partida();
        Crupier crupier= new Crupier();
        partida.setCrupier(crupier);
        jugador.setPuntaje(5);
        crupier.setPuntaje(5);
        partida.setApuesta(0);
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
//modificar nombre de metodo(confuso) ->-> validar saldo o algo asi
        if (monto <= 0){
            throw new ApuestaInvalidaException("El monto debe ser mayor a 0");
        }

        if (usuario.getSaldo() < monto){
            throw new SaldoInsuficiente("El saldo debe ser mayor a 0");
        }



    }



}