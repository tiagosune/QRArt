package com.tiagosune.qrcode.qrart.exception;


import com.tiagosune.qrcode.qrart.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity <ApiError> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setTimestamp(java.time.Instant.now().toString());
        apiError.setError("Bad Request");
        apiError.setStatus(400);
        apiError.setMessage(e.getMessage());
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(QrNotFoundException.class)
    public ResponseEntity<ApiError> handleQrNotFound(QrNotFoundException e, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setTimestamp(java.time.Instant.now().toString());
        apiError.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setMessage(e.getMessage());
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}
