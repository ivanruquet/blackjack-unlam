package com.tallerwebi.dominio;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer saldo = 150;
    private String nombre;
    private String apellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String username;
    private Integer partidasTotales=0;
    private Integer partidasGanadas=0;
    private Integer partidasPerdidas=0;
    private LocalDate ultimoIngreso;
    private Integer racha;
    private String rol;
    private Boolean aceptoTerminos;
    private Integer partidasJugadas = 0;
    private Boolean logro5partidas = false;

    private Integer manosGanadas = 0;
    private Boolean logroGanar2Manos = false;

    private Boolean recompensaReclamada = false;
    private Integer partidasMeta = 5;
    private Integer manosMeta = 2;


    public Usuario(){

    }
    public Usuario(String email, String password, String nombre, String apellido, LocalDate fechaNacimiento, String username) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.username = username;
        this.partidasTotales = 0;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
    }

    public Boolean getAceptoTerminos() { return aceptoTerminos; }
    public void setAceptoTerminos(Boolean aceptoTerminos) { this.aceptoTerminos = aceptoTerminos; }
    public Integer getRacha() { return racha; }
    public void setRacha(Integer racha) { this.racha = racha; }
    public LocalDate getUltimoIngreso() { return ultimoIngreso; }
    public void setUltimoIngreso(LocalDate ultimoIngreso) { this.ultimoIngreso = ultimoIngreso; }
    public Integer getSaldo() { return saldo; }
    public void setSaldo(Integer saldo) { this.saldo = saldo; }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public  String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre; }
    public  String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido; }
    public  LocalDate getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fechaNacimiento ) {this.fechaNacimiento = fechaNacimiento;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public Integer getPartidasTotales() {return partidasTotales;}
    public void setPartidasTotales(Integer partidasTotales) {this.partidasTotales = partidasTotales;}
    public Integer getPartidasGanadas() {return partidasGanadas;}
    public void setPartidasGanadas(Integer partidasGanadas) {this.partidasGanadas = partidasGanadas;}
    public Integer getPartidasPerdidas() {return partidasPerdidas;}
    public void setPartidasPerdidas(Integer partidasPerdidas) {this.partidasPerdidas = partidasPerdidas;}
    public Integer getPartidasJugadas() {
        return partidasJugadas;
    }
    public void setPartidasJugadas(Integer partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }
    public Boolean getLogro5partidas() {
        return logro5partidas;
    }
    public void setLogro5partidas(Boolean logro5partidas) {
        this.logro5partidas = logro5partidas;
    }


    public Boolean getLogroGanar2Manos() {
        return logroGanar2Manos;
    }

    public void setLogroGanar2Manos(Boolean logroGanar2Manos) {
        this.logroGanar2Manos = logroGanar2Manos;
    }

    public Integer getManosGanadas() {
        return manosGanadas;
    }

    public void setManosGanadas(Integer manosGanadas) {
        this.manosGanadas = manosGanadas;
    }

    public Boolean getRecompensaReclamada() {
        return recompensaReclamada != null ? recompensaReclamada : false;
    }

    public void setRecompensaReclamada(Boolean recompensaReclamada) {
        this.recompensaReclamada = recompensaReclamada;
    }

    public Integer getPartidasMeta() {
        return partidasMeta != null ? partidasMeta : 5;
    }

    public Integer getManosMeta() {
        return manosMeta != null ? manosMeta : 2;
    }


    public void setPartidasMeta(Integer partidasMeta) {
        this.partidasMeta = partidasMeta;
    }

    public void setManosMeta(Integer manosMeta) {
        this.manosMeta = manosMeta;
    }

}
