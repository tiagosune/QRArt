package com.tiagosune.qrcode.qrart.security;

import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        try {
            if ("OPTIONS".equals(request.getMethod())) {
                log.info("Liberando requisição OPTIONS");
                filterChain.doFilter(request, response);
                return;
            }

            String path = request.getRequestURI();
            if (path.startsWith("/api/auth/") ||
                    path.startsWith("/api/webhook/") ||
                    path.startsWith("/api/payments/webhook") ||
                    path.startsWith("/uploads/")) {
                log.info("rota publica, pulando JWT");
                filterChain.doFilter(request, response);
                return;
            }

            String authorization = request.getHeader("Authorization");
            log.info("Authorization header: {}", authorization);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorization.substring(7);

            String email = jwtService.extractUsername(token);

            if (email != null) {
                var userOptional = usersRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    Users user = userOptional.get();
                    boolean isValid = jwtService.isTokenValid(token, email);

                    if (isValid && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } else {
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("erro no filtro jwt: ", e);
            filterChain.doFilter(request, response);
        }
    }
}
