package com.tiagosune.qrcode.qrart.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/create-checkout")
    public ResponseEntity<Map<String, String>> createCheckout(
            @RequestParam("qrCodeId") Long qrCodeId,
            Authentication auth
    ) {
        try {
            Users user = (Users) auth.getPrincipal();
            String checkoutUrl = paymentService.createCheckoutSession(qrCodeId, user);

            Map<String, String> response = new HashMap<>();
            response.put("checkoutUrl", checkoutUrl);
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao criar checkout: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> stripeWebhook(@RequestBody String payload) {
        try {
            // Parseia o JSON com Jackson
            JsonNode eventJson = objectMapper.readTree(payload);
            String eventType = eventJson.get("type").asText();

            if ("checkout.session.completed".equals(eventType)) {
                String sessionId = eventJson.get("data").get("object").get("id").asText();

                paymentService.confirmPayment(sessionId);

                return ResponseEntity.ok("Pagamento confirmado com sucesso!");
            }

            return ResponseEntity.ok("Evento processado: " + eventType);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
