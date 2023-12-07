package com.enofex.naikan.administration.user;

import static com.enofex.naikan.administration.user.AdministrationUserController.REQUEST_PATH;

import com.enofex.naikan.administration.AdministrationRequest;
import com.enofex.naikan.security.User;
import com.enofex.naikan.web.Filterable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(REQUEST_PATH)
class AdministrationUserController {

  static final String REQUEST_PATH = AdministrationRequest.PATH + "/users";

  private final AdministrationUserService administrationUserService;

  AdministrationUserController(AdministrationUserService administrationUserService) {
    this.administrationUserService = administrationUserService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<User>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.administrationUserService.findAll(filterable, pageable));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateAuthoritiesById(@PathVariable UserId id,
      @RequestBody String[] authorities) {
    User user = this.administrationUserService.findById(id);

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    this.administrationUserService.save(User.ofAuthorities(user, authorities));

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable UserId id) {
    return this.administrationUserService.deleteById(id) > 0L
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @Component
  static class UserIdConverter implements Converter<String, UserId> {

    @Override
    public UserId convert(String source) {
      return UserId.of(source);
    }
  }
}
