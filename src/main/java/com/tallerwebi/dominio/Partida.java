package com.tallerwebi.dominio;

public class Partida {

    private EstadoPartida estadoPartida;
    private Integer apuesta;

    public Partida(){
        this.estadoPartida = EstadoPartida.APUESTA;
        this.apuesta = 0;
    }


    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    public void cambiarEstadoDeLaPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public void setApuesta(Integer apuesta) {
        this.apuesta = apuesta;
    }

    public Integer getApuesta() {
        return this.apuesta;
    }
}
