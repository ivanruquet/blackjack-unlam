package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Partida {

    private EstadoPartida estadoPartida;
    private Integer apuesta;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Partida(){
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
