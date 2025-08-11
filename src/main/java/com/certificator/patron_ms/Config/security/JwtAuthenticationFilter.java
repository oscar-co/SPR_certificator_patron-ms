package com.certificator.patron_ms.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Excluye del filtro:
     *  - Preflight OPTIONS
     *  - Endpoints públicos: auth, registro de usuario, actuator/health
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String path = request.getServletPath();
        final String method = request.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        return path.startsWith("/certificator/api/auth/")
            || path.equals("/certificator/api/users/register")
            || path.startsWith("/certificator/api/actuator/health");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String path   = request.getServletPath();
        final String method = request.getMethod();
        final String authHeader = request.getHeader("Authorization");

        log.debug("JWT FILTER -> path={}, method={}, Authorization={}", path, method, authHeader);

        // Si no hay Bearer, no autenticamos y dejamos seguir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.extractUsername(jwt);
            log.debug("JWT FILTER -> token presente, username extraído={}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("JWT FILTER -> token válido, autenticación establecida para {}", username);
                } else {
                    log.warn("JWT FILTER -> token inválido para {}", username);
                }
            }
        } catch (Exception e) {
            // No cortar la cadena con 403/401 desde el filtro; deja que la autorización decida
            log.error("JWT FILTER -> excepción procesando token: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
