package com.enofex.naikan.administration.user;

import static com.enofex.naikan.administration.user.AdministrationUserController.REQUEST_PATH;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.AdministrationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Void> updateAuthorities(@PathVariable UserId id,
      @RequestBody String[] authorities) {
    User user = this.administrationUserService.findById(id);

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    this.administrationUserService.save(User.ofAuthorities(user, authorities));

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UserId id) {
    return this.administrationUserService.delete(id) > 0
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
