package com.enofex.naikan.administration.token;

import static com.enofex.naikan.administration.token.AdministrationTokenController.REQUEST_PATH;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.AdministrationRequest;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(REQUEST_PATH)
class AdministrationTokenController {

  static final String REQUEST_PATH = AdministrationRequest.PATH + "/tokens";

  private final AdministrationTokenService administrationTokenService;

  AdministrationTokenController(AdministrationTokenService administrationTokenService) {
    this.administrationTokenService = administrationTokenService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Token>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.administrationTokenService.findAll(filterable, pageable));
  }

  @PostMapping
  public ResponseEntity<String> save(@RequestBody(required = false) String description) {
    Token newToken = this.administrationTokenService.save(description);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .replacePath(REQUEST_PATH + "/{id}")
        .buildAndExpand(newToken.id())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable TokenId id) {
    return this.administrationTokenService.deleteById(id) > 0
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
