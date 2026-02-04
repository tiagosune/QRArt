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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${stripe.api.key}")
    private String stripeApiKey;
    private static final long PRICE_AMOUNT = 500L; // R$ 5,00 em centavos

    @Override
    public void afterPropertiesSet() throws Exception {
        Stripe.apiKey = stripeApiKey;
    }

    public String createCheckoutSession(Long qrCodeId, Users user) throws StripeException {
        QRCode qrCode = qrCodeRepository.findByIdAndUser(qrCodeId, user)
                .orElseThrow(() -> new RuntimeException("QR Code não encontrado"));

        if (qrCode.isPaid()) {
            throw new RuntimeException("QR Code já está pago");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/success?qrCodeId=" + qrCodeId)
                .setCancelUrl("http://localhost:5173/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("brl")
                                                .setUnitAmount(PRICE_AMOUNT)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("QR Code Custom - " + qrCode.getTitle())
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

        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(5.00))
                .stripeSessionId(session.getId())
                .status("PENDING")
                .user(user)
                .qrCode(qrCode)
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

        QRCode qrCode = payment.getQrCode();
        if (qrCode != null) {
            qrCode.setPaid(true);
            qrCodeRepository.save(qrCode);
        }

        paymentRepository.save(payment);
        log.info(" pagamento confirmado: sessionId={}", sessionId);
    }



}
