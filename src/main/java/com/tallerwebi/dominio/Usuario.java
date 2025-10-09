package com.tallerwebi.dominio;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Double saldo;
    private String nombre;
    private String apellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String username;
    private String rol;
    public Usuario(){

    }
    public Usuario(String email, String password, String nombre, String apellido, LocalDate fechaNacimiento, String username) {
    this.email = email;
    this.password = password;
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.username = username;
    }

    public String getRol() {return rol;}
    public void setRol(String rol) {this.rol = rol;}
    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }
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
}
