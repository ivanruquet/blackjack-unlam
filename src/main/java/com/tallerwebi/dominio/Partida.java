package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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
    private boolean botonEstrategia;
    private String ganador;
    @Transient
    private List<Map<String, Object>> mano1;
    @Transient
    private List<Map<String, Object>> mano2;
    private Integer puntajeMano1;
    private Integer puntajeMano2;
    private boolean manoDividida;
    @OneToOne(cascade = CascadeType.ALL)
    private Jugador jugador;
    private Boolean botonEmpezar;
    @OneToOne(cascade = CascadeType.ALL)
    private Crupier crupier;


    public Partida(){}

    public void setGanador(String ganador) {this.ganador = ganador;}

    public String getGanador() {return ganador;}

    public Boolean getBotonEstrategia(){ return botonEstrategia;}

    public void setBotonEstrategia(boolean botonEstrategia){ this.botonEstrategia = botonEstrategia; }

    public void setFichasHabilitadas(boolean fichasHabilitadas) {this.fichasHabilitadas = fichasHabilitadas;}

    public void setBotonesDesicionHabilitados(boolean botonesDesicionHabilitados) {this.botonesDesicionHabilitados = botonesDesicionHabilitados;}

    public Boolean getFichasHabilitadas() {return fichasHabilitadas;}

    public Boolean getBotonesDesicionHabilitados() {return botonesDesicionHabilitados;}

    public Boolean getBotonEmpezar() {return botonEmpezar;}

    public void setBotonEmpezar(boolean botonEmpezar) {this.botonEmpezar = botonEmpezar;}

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

    public void setCrupier(Crupier crupier) {
        this.crupier = crupier;
    }

    public Crupier getCrupier(){
        return this.crupier;
    }

    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida=estadoPartida;
    }

    public EstadoPartida getEstadoPartida() {
        return this.estadoPartida;
    }

    public void setMano1(List<Map<String, Object>> mano1) {
        this.mano1=mano1;
    }

    public List<Map<String, Object>> getMano1(){
        return this.mano1;
    }

    public List<Map<String, Object>> getMano2(){
        return this.mano2;
    }

    public void setMano2(List<Map<String, Object>> mano2) {
        this.mano2=mano2;
    }

    public void setPuntajeMano1(Integer puntajeMano1) {
        this.puntajeMano1 = puntajeMano1;
    }

    public Integer getPuntajeMano1(){
        return this.puntajeMano1;
    }

    public Integer getPuntajeMano2(){
        return this.puntajeMano2;
    }

    public void setPuntajeMano2(Integer puntajeMano2) {
        this.puntajeMano2 = puntajeMano2;
    }

    public void setManoDividida(Boolean manoDividida) {
        this.manoDividida = manoDividida;
    }

    public Boolean getManoDividida(){return this.manoDividida;}
}
