package com.certificator.patron_ms.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class IngressLogFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger("INGRESS");

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String method = req.getMethod();
    String url    = req.getRequestURL().toString();
    String qs     = req.getQueryString();
    String path   = req.getServletPath();
    String host   = req.getHeader("Host");
    String xfwd   = req.getHeader("X-Forwarded-For");
    String xproto = req.getHeader("X-Forwarded-Proto");
    String auth   = req.getHeader("Authorization");

    // opcional: dump de headers (cuidado con ruido)
    StringBuilder headers = new StringBuilder();
    for (String h : Collections.list(req.getHeaderNames())) {
      headers.append(h).append("=").append(req.getHeader(h)).append("; ");
    }

    log.debug("INGRESS -> {} {} qs={} path={} host={} xfwd={} xproto={} auth={} headers=[{}]",
        method, url, qs, path, host, xfwd, xproto, auth, headers);

    chain.doFilter(req, res);
  }
}
