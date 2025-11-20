package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioJugador;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Controller
public class ControladorSaldo {

    private final ServicioJugador servicioJugador;
    private String publicUrl;

    private ServicioUsuario servicioUsuario;


    public ControladorSaldo(ServicioUsuario servicioUsuario, ServicioJugador servicioJugador) {
        MercadoPagoConfig.setAccessToken("APP_USR-4020960842682085-111914-ac4db46720d715c4a2d3bcf151d6959c-3002141881");
        Dotenv dotenv = Dotenv.load();
        this.publicUrl = dotenv.get("PUBLIC_URL");
        this.servicioUsuario = servicioUsuario;
        this.servicioJugador = servicioJugador;
    }

    @GetMapping("/cargarSaldo")
    public ModelAndView irACargarSaldo() {
        return new ModelAndView("cargarSaldo", new ModelMap());
    }

    @PostMapping("/irAMP")
    public ModelAndView irAMP(@RequestParam("monto") Double monto, HttpSession session) {
        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {

            modelo.put("error", "No se encontró el jugador en sesión.");
            return new ModelAndView("cargarSaldo", modelo);
        }

        try {

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Carga de saldo")
                    .description("Compra de moneda en Black jack")
                    .categoryId("digital_goods")
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(monto))
                    .currencyId("ARS")
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(publicUrl + "/spring/api/pago/confirmacion")
                    .failure(publicUrl + "/spring/api/pago/fallo")
                    .pending(publicUrl + "/spring/api/pago/pendiente")
                    .build();



            PreferenceRequest requestMP = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(usuario.getEmail())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(requestMP);

            session.setAttribute("montoRecarga", monto);

            modelo.put("initPoint", preference.getInitPoint());

            return new ModelAndView("redirectToMP", modelo);

        } catch (MPApiException ex) {
            modelo.put("error", "Error generando preferencia: " + ex.getApiResponse().getContent());
            return new ModelAndView("cargarSaldo", modelo);

        } catch (MPException ex) {
            modelo.put("error", "Error generando preferencia: " + ex.getMessage());
            return new ModelAndView("cargarSaldo", modelo);
        }
    }
    @GetMapping("/api/pago/confirmacion")
    public ModelAndView pagoConfirmado(@RequestParam Map<String, String> allRequestParams) {
        ModelMap modelo = new ModelMap();

        String paymentId = allRequestParams.get("payment_id");
        String status = allRequestParams.get("status");
        String preferenceId = allRequestParams.get("preference_id");

        if ("approved".equalsIgnoreCase(status)) {
            String email = allRequestParams.get("external_reference");
            Usuario usuario = servicioUsuario.buscarUsuario(email);
            Jugador jugador = servicioJugador.buscarJugadorPorUsuario(usuario);

            String transactionAmountStr = allRequestParams.get("transaction_amount");
            Double monto = transactionAmountStr != null ? Double.valueOf(transactionAmountStr) : 0.0;

        }

        modelo.put("mensaje", "Pago aprobado correctamente!");
        modelo.put("paymentId", paymentId);
        modelo.put("status", status);
        modelo.put("preferenceId", preferenceId);

        return new ModelAndView("resultadoPago", modelo);
    }




    @GetMapping("/api/pago/fallo")
    public ModelAndView pagoFallido() {
        ModelMap modelo = new ModelMap();
        modelo.put("mensaje", "El pago falló. Intenta nuevamente.");
        return new ModelAndView("resultadoPago", modelo);
    }

    @GetMapping("/api/pago/pendiente")
    public ModelAndView pagoPendiente() {
        ModelMap modelo = new ModelMap();
        modelo.put("mensaje", "El pago quedó pendiente.");
        return new ModelAndView("resultadoPago", modelo);
    }
}
