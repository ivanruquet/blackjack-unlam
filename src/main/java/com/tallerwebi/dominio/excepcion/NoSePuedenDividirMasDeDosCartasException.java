package com.tallerwebi.dominio.excepcion;

public class NoSePuedenDividirMasDeDosCartasException extends RuntimeException {
    public NoSePuedenDividirMasDeDosCartasException(String message) {
        super(message);
    }
}
