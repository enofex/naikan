package com.enofex.naikan.security.token;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

public final class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

  private final BearerTokenResolver bearerTokenResolver = new BearerTokenResolver();
  private final AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
  private final TokenRepository tokenRepository;

  public BearerTokenAuthenticationFilter(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    try {
      String token = this.bearerTokenResolver.resolve(request);

      if (StringUtils.isBlank(token)) {
        this.logger.warn("Failed to parse token!");
        throw new InsufficientAuthenticationException(
            "Did not process request since did not find bearer token on request");
      }

      if (this.logger.isDebugEnabled()) {
        this.logger.debug(
            "Bearer Authentication Authorization header found for token '%s'".formatted(token));
      }

      if (!this.tokenRepository.exists(token)) {
        this.logger.warn("Token '%s' is not configured in database!".formatted(token));
        throw new InsufficientAuthenticationException(
            "Did not process request since did not find bearer token in database");
      }

      BearerTokenAuthenticationToken bearerTokenAuthentication = new BearerTokenAuthenticationToken(
          token);

      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(bearerTokenAuthentication);
      SecurityContextHolder.setContext(context);

      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Set SecurityContextHolder to '%s'".formatted(bearerTokenAuthentication));
      }

      this.tokenRepository.updateLastUsed(token);

      chain.doFilter(request, response);
    } catch (AuthenticationException failed) {
      SecurityContextHolder.clearContext();

      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Authentication request for failed: " + failed);
      }

      this.authenticationEntryPoint.commence(request, response, failed);
    }
  }

}
