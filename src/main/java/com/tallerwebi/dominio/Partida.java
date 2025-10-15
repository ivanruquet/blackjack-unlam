package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EstadoDeJuego estadoJuego;
    @Enumerated(EnumType.STRING)
    private EstadoPartida estadoPartida;
    private Integer apuesta;
    private boolean fichasHabilitadas;
    private boolean botonesDesicionHabilitados;

    @OneToOne(cascade = CascadeType.ALL)
    private Jugador jugador;


    public Partida(){}

    public void setFichasHabilitadas(boolean fichasHabilitadas) {this.fichasHabilitadas = fichasHabilitadas;}

    public void setBotonesDesicionHabilitados(boolean botonesDesicionHabilitados) {this.botonesDesicionHabilitados = botonesDesicionHabilitados;}

    public Boolean getFichasHabilitadas() {return fichasHabilitadas;}

    public Boolean getBotonesDesicionHabilitados() {return botonesDesicionHabilitados;}

    public EstadoDeJuego getEstadoJuego() {
        return estadoJuego;
    }

    public void cambiarEstadoDeJuego(EstadoDeJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
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

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Jugador getJugador(){
        return this.jugador;
    }

    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida=estadoPartida;
    }

    public EstadoPartida getEstadoPartida() {
        return this.estadoPartida;
    }
}
