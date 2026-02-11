package com.tiagosune.qrcode.qrart.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.exception.StripeException;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

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
    public ResponseEntity<String> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        try {

            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeWebhookSecret
            );

            if ("checkout.session.completed".equals(event.getType())) {

                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (session != null) {
                    paymentService.confirmPayment(session.getId());
                }
            }

            return ResponseEntity.ok("Webhook processado com sucesso");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Invalid signature");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno");
        }
    }
}
