package com.tiagosune.qrcode.qrart.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tiagosune.qrcode.qrart.model.Payment;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.repository.PaymentRepository;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService implements InitializingBean {

    private final QrCodeRepository qrCodeRepository;
    private final PaymentRepository paymentRepository;

    private static final String STRIPE_SECRET_KEY = "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";
    private static final long PRICE_AMOUNT = 500L; // R$ 5,00 em centavos

    @Override
    public void afterPropertiesSet() throws Exception {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    public String createCheckoutSession(Long qrCodeId, Users user) throws StripeException {
        QRCode qrCode = qrCodeRepository.findByIdAndUser(qrCodeId, user)
                .orElseThrow(() -> new RuntimeException("QR Code não encontrado"));

        if (qrCode.isPaid()) {
            throw new RuntimeException("QR Code já está pago");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success?qrCodeId=" + qrCodeId)
                .setCancelUrl("http://localhost:3000/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("brl")
                                                .setUnitAmount(PRICE_AMOUNT)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("QR Code Premium - " + qrCode.getTitle())
                                                                .setDescription("QR Code personalizado")
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .putMetadata("qrCodeId", qrCodeId.toString())
                .putMetadata("userId", user.getId().toString())
                .build();

        Session session = Session.create(params);

        // 🔥 CORRIGIDO: Adiciona user e qrCode
        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(5.00))
                .stripeSessionId(session.getId())
                .status("PENDING")
                .user(user)           // 🔥 ADICIONA USER
                .qrCode(qrCode)       // 🔥 ADICIONA QRCODE
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        log.info("Checkout criado: sessionId={}, qrCodeId={}", session.getId(), qrCodeId);

        return session.getUrl();
    }


    public void confirmPayment(String sessionId) {
        log.info("🔍 Buscando pagamento com sessionId: {}", sessionId);

        Payment payment = paymentRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        payment.setStatus("COMPLETED");

        // 🔥 MARCA O QR CODE COMO PAGO
        QRCode qrCode = payment.getQrCode();
        if (qrCode != null) {
            qrCode.setPaid(true);
            qrCodeRepository.save(qrCode);
            log.info("✅ QR Code #{} marcado como pago!", qrCode.getId());
        }

        paymentRepository.save(payment);
        log.info("✅ Pagamento confirmado: sessionId={}", sessionId);
    }



}
