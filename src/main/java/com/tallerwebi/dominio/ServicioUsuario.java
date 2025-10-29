package com.tallerwebi.dominio;

public interface ServicioUsuario {


    void actualizarSaldoDeUsuario(Usuario usuario, Integer montoApuesta);
    void modificarAtributos(Usuario usuario, String nombreActualizado, String apellidoActualizado, String nomUsuario);
}
