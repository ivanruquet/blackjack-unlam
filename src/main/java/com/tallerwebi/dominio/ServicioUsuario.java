package com.tallerwebi.dominio;

public interface ServicioUsuario {

    Usuario buscarUsuario(String email);
    void actualizarSaldoDeUsuario(Usuario usuario, Integer montoApuesta);
    void modificarAtributos(Usuario usuario, String nombreActualizado, String apellidoActualizado, String nomUsuario);
    void incrementarSaldoDeGanador(Integer saldo, Usuario usuario);
    void registrarResultado(Usuario usuario, String resultado);
    void actualizarLogros(Usuario usuario);
    void actualizarManosGanadas (Usuario usuario, Boolean gano);
    void registrarIngreso(Long idUsuario);
    void otorgarRecompensaDiaria(Usuario usuario);

}
