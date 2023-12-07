package com.enofex.naikan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class HttpStatusReturningAuthenticationFailureHandler extends
    SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    ProblemDetail problem = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
    problem.setDetail("Authentication failed, please check your credentials and try again.");

    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(new ObjectMapper().writeValueAsString(problem));
    response.getWriter().flush();
  }
}