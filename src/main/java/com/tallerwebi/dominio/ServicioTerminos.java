package com.tallerwebi.dominio;

public interface ServicioTerminos {
    boolean verificarTerminosAceptados(String email);
    void aceptarTerminos(String email);
}
