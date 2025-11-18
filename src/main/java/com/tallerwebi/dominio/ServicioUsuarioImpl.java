package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private RepositorioUsuario repositorioUsuario;
    private final double RECOMPENSA_DIARIA = 500.0;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    public void modificarAtributos(Usuario usuario, String nombreActualizado, String apellidoActualizado, String username) {
        usuario.setNombre(nombreActualizado);
        usuario.setApellido(apellidoActualizado);
        usuario.setUsername(username);
        repositorioUsuario.guardarModificaciones(usuario);
    }

    @Override
    public void incrementarSaldoDeGanador(Integer saldo, Usuario usuario) {
        if (usuario != null) {
            usuario.setSaldo(saldo);
            repositorioUsuario.actualizar(usuario);
        }
    }

    @Override
    public Usuario buscarUsuario(String email) {
        return repositorioUsuario.buscar(email);
    }

    @Override
    public void actualizarSaldoDeUsuario(Usuario usuario, Integer montoApuesta) {

        usuario.setSaldo(usuario.getSaldo() - montoApuesta);
        repositorioUsuario.actualizar(usuario);
    }

    @Override
    public void registrarResultado(Usuario usuario, String resultado) {
        if (usuario.getPartidasTotales() == null) usuario.setPartidasTotales(0);
        if (usuario.getPartidasGanadas() == null) usuario.setPartidasGanadas(0);
        if (usuario.getPartidasPerdidas() == null) usuario.setPartidasPerdidas(0);
        usuario.setPartidasTotales(usuario.getPartidasTotales() + 1);

        if (resultado.equalsIgnoreCase("Resultado: Jugador gana") || resultado.equalsIgnoreCase("Resultado: El crupier se paso de 21, Jugador gana") || resultado.equalsIgnoreCase("Ganó mano 1. ") || resultado.equalsIgnoreCase("Ganó mano 2. ")) {
            usuario.setPartidasGanadas(usuario.getPartidasGanadas() + 1);

        } else if (resultado.equalsIgnoreCase("Resultado: Crupier gana") || resultado.equalsIgnoreCase("Resultado: Superaste los 21, Crupier gana") || resultado.equalsIgnoreCase("Perdió mano 1. ") || resultado.equalsIgnoreCase("Perdió mano 2. ")) {
            usuario.setPartidasPerdidas(usuario.getPartidasPerdidas() + 1);
        }
    }


    @Override
    public void actualizarLogros(Usuario usuario) {

        if (usuario.getPartidasJugadas() == null) {
            usuario.setPartidasJugadas(0);
        }

        usuario.setPartidasJugadas(usuario.getPartidasJugadas() + 1);


        if (usuario.getPartidasJugadas() >= usuario.getPartidasMeta()) {
            usuario.setLogro5partidas(true);
            usuario.setRecompensaReclamada(false);

        }

        repositorioUsuario.actualizar(usuario);
    }


    @Override
    public void registrarIngreso(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);
        if (usuario != null) {
            otorgarRecompensaDiaria(usuario);
        }
    }

    @Override
    public void otorgarRecompensaDiaria(Usuario usuario) {
        LocalDate hoy = LocalDate.now();
        LocalDate ultimoIngreso = usuario.getUltimoIngreso();

        if (ultimoIngreso == null || !ultimoIngreso.isEqual(hoy)) {
            usuario.setSaldo((int) (usuario.getSaldo() + RECOMPENSA_DIARIA));
            usuario.setUltimoIngreso(hoy);
        }

        if (ultimoIngreso != null && ultimoIngreso.plusDays(1).isEqual(hoy)) {
            usuario.setRacha(usuario.getRacha() + 1);
        } else {
            usuario.setRacha(1);
        }
        repositorioUsuario.actualizar(usuario);
    }


    @Override
    public void actualizarManosGanadas(Usuario usuario, Boolean gano) {

        Usuario usuarioDB = repositorioUsuario.buscar(usuario.getEmail());

        if (gano) {
            if (usuarioDB.getManosGanadas() == null) {
                usuarioDB.setManosGanadas(0);
            }

            usuarioDB.setManosGanadas(usuarioDB.getManosGanadas() + 1);


            if (usuarioDB.getManosGanadas() >= usuarioDB.getManosMeta()) {
                usuarioDB.setLogroGanar2Manos(true);
                usuarioDB.setRecompensaReclamada(false);
            }
        }

        repositorioUsuario.actualizar(usuarioDB);

        usuario.setManosGanadas(usuarioDB.getManosGanadas());
        usuario.setLogroGanar2Manos(usuarioDB.getLogroGanar2Manos());
    }

}






