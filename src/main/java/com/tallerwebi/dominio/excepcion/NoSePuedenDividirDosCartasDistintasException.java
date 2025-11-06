package com.tallerwebi.dominio.excepcion;

public class NoSePuedenDividirDosCartasDistintasException extends RuntimeException {
    public NoSePuedenDividirDosCartasDistintasException(String message) {
        super(message);
    }
}
