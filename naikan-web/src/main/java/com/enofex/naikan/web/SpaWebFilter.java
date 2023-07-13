package com.enofex.naikan.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public final class SpaWebFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String path = request.getRequestURI();

    if (!path.startsWith("/api") && !path.contains(".") && path.matches("/(.*)")) {
      request.getRequestDispatcher("/index.html").forward(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }
}