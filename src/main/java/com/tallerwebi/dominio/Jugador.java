package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Jugador {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    private Double saldo = 1000.0;


    public void restarSaldo(Double monto) { this.saldo -= monto; }

    public Double getSaldo() { return saldo;}

    public void setSaldo(Double saldo) { this.saldo = saldo;}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }
}
