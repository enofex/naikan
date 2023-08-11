package com.enofex.naikan;

import static org.springframework.security.config.Customizer.withDefaults;

import com.enofex.naikan.administration.token.AdministrationTokenService;
import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.security.AuthorityType;
import com.enofex.naikan.security.CookieCsrfFilter;
import com.enofex.naikan.security.HttpStatusReturningAuthenticationFailureHandler;
import com.enofex.naikan.security.HttpStatusReturningAuthenticationSuccessHandler;
import com.enofex.naikan.security.ldap.DefaultLdapUserDetailsMapper;
import com.enofex.naikan.security.token.BearerTokenAuthenticationFilter;
import com.enofex.naikan.web.SpaWebFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticator;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@Configuration
class SecurityConfiguration {

  private static final String API_URLS = "/api/public/**";

  @EnableWebSecurity
  @Configuration
  @ConditionalOnProperty(prefix = "naikan", name = "security.ldap.enable", havingValue = "true", matchIfMissing = true)
  static class LdapSecurityConfiguration {

    private static final String AUTHENTICATE_URL = "/api/authenticate";
    private static final String LOGOUT_URL = "/api/logout";
    private static final String ADMINISTRATION_URLS = "/api/administration/**";
    private static final String[] RESOURCES = {"/", "/*.js", "/*.html", "/*.css", "/*.png",
        "/*.ico", "/*.svg", "/*.woff", "/*.woff2", "/assets/**",
    };

    private final AdministrationUserService userService;
    private final NaikanProperties properties;

    LdapSecurityConfiguration(AdministrationUserService userService, NaikanProperties properties) {
      this.userService = userService;
      this.properties = properties;
    }

    @Bean
    SecurityFilterChain ldapSecurityFilterChain(HttpSecurity http, AuthenticationProvider provider)
        throws Exception {
      http
          .securityMatchers(matchers ->
              matchers.requestMatchers(new NegatedRequestMatcher(
                  new AntPathRequestMatcher(API_URLS)))
          )
          .csrf(csrf -> csrf
              .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
              .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
              .ignoringRequestMatchers(AUTHENTICATE_URL, LOGOUT_URL))
          .cors(withDefaults())
          .anonymous(AbstractHttpConfigurer::disable)
          .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
          .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
          .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
              .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
          )
          .authorizeHttpRequests(authorizeRequests -> authorizeRequests
              .requestMatchers(RESOURCES).permitAll()
              .requestMatchers(AUTHENTICATE_URL).permitAll()
              .requestMatchers(ADMINISTRATION_URLS)
                .hasAuthority(AuthorityType.ROLE_ADMIN.getAuthority())
              .requestMatchers("/api/**").authenticated()
          )
          .formLogin(login -> login
              .loginProcessingUrl(AUTHENTICATE_URL)
              .successHandler(authenticationSuccessHandler())
              .failureHandler(authenticationFailureHandler())
              .loginPage("/index.html")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl(LOGOUT_URL)
              .logoutSuccessHandler(logoutSuccessHandler())
              .permitAll()
          )
          .authenticationProvider(provider);

      return http.build();
    }

    @Bean
    AuthenticationProvider ldapAuthenticationProvider(LdapContextSource contextSource) {
      AbstractLdapAuthenticationProvider provider;

      if (StringUtils.isBlank(properties.security().ldap().activeDirectoryDomain())) {
        provider = new LdapAuthenticationProvider(authenticator(contextSource));
      } else {
        provider = new ActiveDirectoryLdapAuthenticationProvider(
            properties.security().ldap().activeDirectoryDomain(),
            contextSource.getUrls()[0]
        );

        ((ActiveDirectoryLdapAuthenticationProvider) provider).setSearchFilter(
            properties.security().ldap().userSearchFilter());
      }

      provider.setUserDetailsContextMapper(new DefaultLdapUserDetailsMapper(userService));

      return provider;
    }

    private LdapAuthenticator authenticator(BaseLdapPathContextSource contextSource) {
      AbstractLdapAuthenticator authenticator = new BindAuthenticator(contextSource);

      if (StringUtils.isNotEmpty(properties.security().ldap().userDnPatterns())) {
        authenticator.setUserDnPatterns(
            new String[]{properties.security().ldap().userDnPatterns()}
        );
      } else if (StringUtils.isNotEmpty(properties.security().ldap().userSearchFilter())) {
        authenticator.setUserSearch(
            new FilterBasedLdapUserSearch(
                StringUtils.isNotBlank(properties.security().ldap().userSearchBase())
                    ? properties.security().ldap().userSearchBase() : "",
                properties.security().ldap().userSearchFilter(),
                contextSource)
        );
      }

      return authenticator;
    }


    @Bean
    LogoutSuccessHandler logoutSuccessHandler() {
      return new HttpStatusReturningLogoutSuccessHandler();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
      return new HttpStatusReturningAuthenticationSuccessHandler(userService);
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
      return new HttpStatusReturningAuthenticationFailureHandler();
    }
  }

  @EnableWebSecurity
  @Configuration
  static class ApiSecurityConfiguration {

    private final AdministrationTokenService administrationTokenService;

    ApiSecurityConfiguration(AdministrationTokenService administrationTokenService) {
      this.administrationTokenService = administrationTokenService;
    }

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatchers(matchers -> matchers.requestMatchers(
              new AntPathRequestMatcher(API_URLS))
          )
          .csrf(AbstractHttpConfigurer::disable)
          .cors(AbstractHttpConfigurer::disable)
          .anonymous(AbstractHttpConfigurer::disable)
          .sessionManagement(session -> session
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(authorizeRequests -> authorizeRequests
              .anyRequest().permitAll()
          )
          .addFilterBefore(
              new BearerTokenAuthenticationFilter(administrationTokenService),
              UsernamePasswordAuthenticationFilter.class
          );

      return http.build();
    }
  }
}
