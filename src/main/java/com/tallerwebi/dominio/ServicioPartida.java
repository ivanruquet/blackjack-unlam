package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;

public interface ServicioPartida {

  public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException;


    void resetearPartida(Usuario usuario);

  void apostar(Usuario usuario, int monto);
}
