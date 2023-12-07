package com.enofex.naikan.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class HttpStatusReturningAuthenticationSuccessHandler extends
    SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;

  public HttpStatusReturningAuthenticationSuccessHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    if (isFirstUserAndShouldBeAdmin()) {
      this.userRepository.save(new User(
          authentication.getName(),
          List.of(AuthorityType.ROLE_ADMIN.getAuthority()))
      );
    } else if (this.userRepository.findByName(authentication.getName()) == null) {
      this.userRepository.save(new User(authentication.getName()));
    }

    response.setStatus(HttpServletResponse.SC_OK);
  }

  private boolean isFirstUserAndShouldBeAdmin() {
    return this.userRepository.count() == 0L;
  }
}