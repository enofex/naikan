package com.enofex.naikan.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

class HttpStatusReturningAuthenticationFailureHandlerTest {

  private HttpStatusReturningAuthenticationFailureHandler failureHandler;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.failureHandler = new HttpStatusReturningAuthenticationFailureHandler();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  void shouldReturnCorrectValuesAuthenticationFailure() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    AuthenticationException exception = mock(AuthenticationException.class);
    PrintWriter writer = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(writer);

    this.failureHandler.onAuthenticationFailure(request, response, exception);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.name());
    verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    verify(writer).write(this.objectMapper.writeValueAsString(Mockito.any()));
    verify(writer).flush();
  }
}
