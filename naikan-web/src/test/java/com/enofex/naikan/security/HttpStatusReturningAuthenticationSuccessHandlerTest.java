package com.enofex.naikan.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.administration.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

class HttpStatusReturningAuthenticationSuccessHandlerTest {

  private HttpStatusReturningAuthenticationSuccessHandler successHandler;
  private AdministrationUserService userService;

  @BeforeEach
  void setUp() {
    this.userService = mock(AdministrationUserService.class);
    this.successHandler = new HttpStatusReturningAuthenticationSuccessHandler(this.userService);
  }

  @Test
  void shouldAuthenticateFirstUserAndShouldBeAdmin() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Authentication authentication = mock(Authentication.class);

    when(this.userService.count()).thenReturn(0L);
    when(authentication.getName()).thenReturn("firstUser");

    this.successHandler.onAuthenticationSuccess(request, response, authentication);

    verify(this.userService).save(new User("firstUser", List.of("ROLE_ADMIN")));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void shouldnAuthenticateIfUserNotFound() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Authentication authentication = mock(Authentication.class);

    when(this.userService.count()).thenReturn(1L);
    when(authentication.getName()).thenReturn("newUser");
    when(this.userService.findByName("newUser")).thenReturn(null);

    this.successHandler.onAuthenticationSuccess(request, response, authentication);

    verify(this.userService).save(new User("newUser"));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void shouldnAuthenticateIfUserAlreadyExists() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Authentication authentication = mock(Authentication.class);

    when(this.userService.count()).thenReturn(1L);
    when(authentication.getName()).thenReturn("existingUser");
    when(this.userService.findByName("existingUser")).thenReturn(new User("existingUser"));

    this.successHandler.onAuthenticationSuccess(request, response, authentication);

    verify(this.userService, never()).save(Mockito.any(User.class));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}
