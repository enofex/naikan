package com.enofex.naikan.security;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthenticationController {

  private final UserRepository userRepository;

  AuthenticationController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/api/authenticated")
  public ResponseEntity authenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      User user = this.userRepository.findByName(authentication.getName());

      return ResponseEntity.ok(new UserResource(authentication, user));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  private static final class UserResource {

    private final Authentication authentication;
    private final User user;

    UserResource(Authentication authentication, User user) {
      this.authentication = Objects.requireNonNull(authentication);
      this.user = user;
    }

    public String getUsername() {
      return this.authentication.getName();
    }

    public List<String> getFavorites() {
      return this.user != null ? this.user.favorites() : Collections.emptyList();
    }

    public Object getDetails() {
      if (this.authentication.getPrincipal() instanceof InetOrgPerson principal) {
        return new InetOrgPersonResource(principal);
      }

      return null;
    }

    public List<String> getAuthorities() {
      return this.authentication.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .toList();
    }
  }

  private static final class InetOrgPersonResource {

    private final InetOrgPerson principal;

    InetOrgPersonResource(InetOrgPerson principal) {
      this.principal = principal;
    }

    public String getTitle() {
      return this.principal.getTitle();
    }

    public String getDisplayName() {
      return this.principal.getDisplayName();
    }

    public String getGivenName() {
      return this.principal.getGivenName();
    }

    public String getMail() {
      return this.principal.getMail();
    }

    public String getMobile() {
      return this.principal.getMobile();
    }

    public String getTelephoneNumber() {
      return this.principal.getTelephoneNumber();
    }
  }
}




