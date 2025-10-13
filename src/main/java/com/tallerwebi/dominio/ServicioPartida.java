package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaNoCreadaException;

public interface ServicioPartida {

  public Partida crearPartida(Usuario usuario) throws PartidaNoCreadaException;




}
