package com.enofex.naikan.security;


import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.administration.user.User;
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

  private final AdministrationUserService administrationUserService;

  AuthenticationController(AdministrationUserService administrationUserService) {
    this.administrationUserService = administrationUserService;
  }

  @GetMapping("/api/authenticated")
  public ResponseEntity authenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      User user = administrationUserService.findByName(authentication.getName());

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
      return authentication.getName();
    }

    public List<String> getFavorites() {
      return user != null ? user.favorites() : Collections.emptyList();
    }

    public Object getDetails() {
      if (authentication.getPrincipal() instanceof InetOrgPerson principal) {
        return new InetOrgPersonResource(principal);
      }

      return null;
    }

    public List<String> getAuthorities() {
      return authentication.getAuthorities()
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
      return principal.getTitle();
    }

    public String getDisplayName() {
      return principal.getDisplayName();
    }

    public String getGivenName() {
      return principal.getGivenName();
    }

    public String getMail() {
      return principal.getMail();
    }

    public String getMobile() {
      return principal.getMobile();
    }

    public String getTelephoneNumber() {
      return principal.getTelephoneNumber();
    }
  }
}




