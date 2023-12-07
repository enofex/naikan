package com.enofex.naikan.security.ldap;

import com.enofex.naikan.security.AuthorityType;
import com.enofex.naikan.security.User;
import com.enofex.naikan.security.UserRepository;
import java.util.Collection;
import java.util.List;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

public final class DefaultLdapUserDetailsMapper extends LdapUserDetailsMapper {

  private final UserRepository userRepository;

  public DefaultLdapUserDetailsMapper(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
      Collection<? extends GrantedAuthority> authorities) {
    User savedUser = this.userRepository.findByName(username);

    return super.mapUserFromContext(
        ctx,
        username,
        hasAdminAuthority(savedUser) ? List.of(AuthorityType.ROLE_ADMIN) : List.of());
  }

  private boolean hasAdminAuthority(User user) {
    return isFirstUserAndShouldBeAdmin() || user != null
        && user.authorities() != null
        && user.authorities().contains(AuthorityType.ROLE_ADMIN.getAuthority());
  }

  private boolean isFirstUserAndShouldBeAdmin() {
    return this.userRepository.count() == 0L;
  }
}
