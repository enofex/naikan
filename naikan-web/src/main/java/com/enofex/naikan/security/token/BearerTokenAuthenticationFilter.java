package com.enofex.naikan.security.token;


import com.enofex.naikan.administration.token.AdministrationTokenService;
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

public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

  private final BearerTokenResolver bearerTokenResolver = new BearerTokenResolver();
  private final AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
  private final AdministrationTokenService administrationTokenService;

  public BearerTokenAuthenticationFilter(AdministrationTokenService administrationTokenService) {
    this.administrationTokenService = administrationTokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    try {
      String token = bearerTokenResolver.resolve(request);

      if (StringUtils.isBlank(token)) {
        logger.warn("Failed to parse token!");
        throw new InsufficientAuthenticationException(
            "Did not process request since did not find bearer token on request");
      }

      if (logger.isDebugEnabled()) {
        logger.debug(
            "Bearer Authentication Authorization header found for token '%s'".formatted(token));
      }

      if (!administrationTokenService.exists(token)) {
        logger.warn("Token '%s' is not configured in database!".formatted(token));
        throw new InsufficientAuthenticationException(
            "Did not process request since did not find bearer token in database");
      }

      BearerTokenAuthenticationToken bearerTokenAuthentication = new BearerTokenAuthenticationToken(
          token);

      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(bearerTokenAuthentication);
      SecurityContextHolder.setContext(context);

      if (logger.isDebugEnabled()) {
        logger.debug("Set SecurityContextHolder to '%s'".formatted(bearerTokenAuthentication));
      }

      administrationTokenService.updateLastUsed(token);

      chain.doFilter(request, response);
    } catch (AuthenticationException failed) {
      SecurityContextHolder.clearContext();

      if (logger.isDebugEnabled()) {
        logger.debug("Authentication request for failed: " + failed);
      }

      authenticationEntryPoint.commence(request, response, failed);
    }
  }

}
