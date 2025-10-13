package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioDeckOfCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorDeckTest {

    private final ServicioDeckOfCards servicioDeck;

    @Autowired
    public ControladorDeckTest(ServicioDeckOfCards servicioDeck) {
        this.servicioDeck = servicioDeck;
    }

    @GetMapping("/test-deck")
    public ModelAndView testDeck() {
        var mazo = servicioDeck.crearMazo();
        String deckId = (String) mazo.get("deck_id");

        var cartas = servicioDeck.sacarCartas(deckId, 2);

        ModelAndView mav = new ModelAndView("test-deck");
        mav.addObject("deckId", deckId);
        mav.addObject("cartas", cartas);
        return mav;
    }
}
