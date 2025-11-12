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
    private LocalDate lastLoginDate;
    private Integer racha;

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

    public Integer getRacha() { return racha; }
    public void setRacha(Integer racha) { this.racha = racha; }
    public LocalDate getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(LocalDate lastLoginDate) { this.lastLoginDate = lastLoginDate; }
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
}
