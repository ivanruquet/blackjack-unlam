package com.tallerwebi.infraestructura;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.ServicioDeckOfCards;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ServicioDeckOfCardsImpl implements ServicioDeckOfCards {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Object> crearMazo() {
        String url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";
        String json = restTemplate.getForObject(url, String.class);

        try {
            JsonNode obj = mapper.readTree(json);
            return Map.of("deck_id", obj.get("deck_id").asText());
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear JSON", e);
        }
    }


    @Override
    public List<Map<String, Object>> sacarCartas(String deckId, int cantidad) {
        String url = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=" + cantidad;
        String json = restTemplate.getForObject(url, String.class);

        try {
            JsonNode obj = mapper.readTree(json);
            List<Map<String, Object>> lista = new ArrayList<>();
            for (JsonNode carta : obj.get("cards")) {
                lista.add(Map.of(
                        "image", carta.get("image").asText(),
                        "value", carta.get("value").asText(),
                        "suit", carta.get("suit").asText()
                ));
            }
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear JSON", e);
        }
    }
















}
