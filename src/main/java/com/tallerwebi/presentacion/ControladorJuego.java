package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Controller
public class ControladorJuego {
    private List<Map<String, Object>> cartasJugador = new ArrayList<>();
    private List<Map<String, Object>> cartasDealer = new ArrayList<>();
    private ServicioDeckOfCards servicioDeck;
    private ServicioPartida servicioPartida;
    private ServicioUsuario servicioUsuario;
    private String deckId;

@Autowired
    public ControladorJuego(ServicioDeckOfCards servicioDeck,ServicioPartida servicioPartida, ServicioUsuario servicioUsuario) {
        this.servicioDeck = servicioDeck;
        this.servicioPartida = servicioPartida;
        this.servicioUsuario = servicioUsuario;

    }
    public ControladorJuego(ServicioPartida servicioPartida) {
        this.servicioPartida = servicioPartida;
    }
}

//    @RequestMapping("/juegoConCrupier")
//    public ModelAndView iraJuego() {
//        ModelMap modelo = new ModelMap();
//        return new ModelAndView("juegoConCrupier", modelo);
//    }
//
//

//
//    @PostMapping("/apostar")
//    public ModelAndView apostar(HttpServletRequest request, @RequestParam("monto") int monto) throws ApuestaInvalidaException, SaldoInsuficiente {
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//        ModelMap modelo = new ModelMap();
//
//        if (usuario != null) {
//           // servicioPartida.apostar(usuario, monto);
//            return new ModelAndView("redirect:/juegoConCrupier");
//        }
//
//        try {
//            servicioPartida.validarPartida(usuario, monto);
//            modelo.addAttribute("mensajeExito", "Apuesta realizada");
//            modelo.addAttribute("saldo", usuario.getSaldo());
//        }catch (ApuestaInvalidaException e) {
//            modelo.addAttribute("error", "El monto de la apuesta no es valido");
//        }catch (SaldoInsuficiente saldoIns){
//            modelo.addAttribute("erroSaldo", "Saldo insuficiente");
//        }
//
//        return new ModelAndView("juego", modelo);
//
//    }
//
//    @PostMapping("/reset")
//    public ModelAndView resetearPartida(HttpServletRequest request) {
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//        if (usuario != null) {
//            servicioPartida.resetearPartida(usuario);
//        }
//        return new ModelAndView("redirect:/juegoConCrupier");
//    }

//------------------------------------------------------------------

//
//    @PostMapping("/iniciar")
//    public ModelAndView comenzarPartida(HttpServletRequest request,  @RequestParam("monto") Integer monto) throws PartidaActivaNoEnApuestaException {
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//        cartasJugador = new ArrayList<>();
//        cartasDealer = new ArrayList<>();
//        //  se crea el mazo
//        var mazo = servicioDeck.crearMazo();
//        deckId = (String) mazo.get("deck_id");
//        try {
//            servicioPartida.consultarExistenciaDePartidaActiva(usuario);
//        } catch (PartidaExistenteActivaException e) {
//            List<Partida> activas = servicioPartida.buscarPartidaActiva(usuario);
//                servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(activas.get(0));
//                //que el saldo del usuario se actualice
//            //error 500 me sale violacion de restriccio - tabla usuario
//               // servicioPartida.setearApuesta(usuario, monto, activas.get(0));
//            //Se reparte cartas
//
//            cartasJugador = servicioDeck.sacarCartas(deckId, 2);
//            cartasDealer = servicioDeck.sacarCartas(deckId, 2);
////Calculo de puntaje basado en las cartas
//
//            int puntajeJugador = servicioPartida.calcularPuntaje(cartasJugador);
//            int puntajeDealer = servicioPartida.calcularPuntaje(cartasDealer);
////ahora tenemos que guardar ese puntaje en los jugadores
//
//// servicioPartida.cambiarEstadoDeJuegoAJuegoDeUnaPartida(partida);
//            ModelAndView mav = new ModelAndView("juegoConCrupier");
//            mav.addObject("usuario", usuario);
//            mav.addObject("deckId", deckId);
//            mav.addObject("cartasJugador", cartasJugador);
//            mav.addObject("cartasDealer", cartasDealer);
//            mav.addObject("puntajeJugador", puntajeJugador);
//            mav.addObject("puntajeDealer", puntajeDealer);
//            return mav;
//
//
//
//        }
//
//        return new ModelAndView();
//    }
//
//
//}
