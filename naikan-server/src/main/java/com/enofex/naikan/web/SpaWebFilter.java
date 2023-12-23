package com.enofex.naikan.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.web.filter.OncePerRequestFilter;

public final class SpaWebFilter extends OncePerRequestFilter {

  private static final Pattern PATTERN = Pattern.compile("/(.*)");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String path = request.getRequestURI();

    if (!path.startsWith("/api") && !path.contains(".") && PATTERN.matcher(path).matches()) {
      request.getRequestDispatcher("/index.html").forward(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }
}