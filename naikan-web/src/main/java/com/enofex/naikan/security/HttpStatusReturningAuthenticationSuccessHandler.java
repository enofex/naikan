package com.enofex.naikan.security;

import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.administration.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class HttpStatusReturningAuthenticationSuccessHandler extends
    SimpleUrlAuthenticationSuccessHandler {

  private final AdministrationUserService userService;

  public HttpStatusReturningAuthenticationSuccessHandler(AdministrationUserService userService) {
    this.userService = userService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    if (isFirstUserAndShouldBeAdmin()) {
      this.userService.save(new User(
          authentication.getName(),
          List.of(AuthorityType.ROLE_ADMIN.getAuthority()))
      );
    } else {
      if (this.userService.findByName(authentication.getName()) == null) {
        this.userService.save(new User(authentication.getName()));
      }
    }

    response.setStatus(HttpStatus.OK.value());
  }

  private boolean isFirstUserAndShouldBeAdmin() {
    return this.userService.count() == 0;
  }
}