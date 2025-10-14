package com.tallerwebi.dominio;

import java.util.List;
import java.util.Map;

public interface ServicioDeckOfCards {
    Map<String, Object> crearMazo();
    List<Map<String, Object>> sacarCartas(String deckId, int cantidad);
}