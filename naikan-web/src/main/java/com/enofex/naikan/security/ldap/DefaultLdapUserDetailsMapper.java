package com.enofex.naikan.security.ldap;

import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.security.AuthorityType;
import java.util.Collection;
import java.util.List;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

public final class DefaultLdapUserDetailsMapper extends LdapUserDetailsMapper {

  private final AdministrationUserService userService;

  public DefaultLdapUserDetailsMapper(AdministrationUserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
      Collection<? extends GrantedAuthority> authorities) {
    com.enofex.naikan.administration.user.User savedUser = this.userService.findByName(username);

    return super.mapUserFromContext(
        ctx,
        username,
        hasAdminAuthority(savedUser) ? List.of(AuthorityType.ROLE_ADMIN) : List.of());
  }

  private boolean hasAdminAuthority(com.enofex.naikan.administration.user.User user) {
    return isFirstUserAndShouldBeAdmin() || user != null
        && user.authorities() != null
        && user.authorities().contains(AuthorityType.ROLE_ADMIN.getAuthority());
  }

  private boolean isFirstUserAndShouldBeAdmin() {
    return this.userService.count() == 0;
  }
}
