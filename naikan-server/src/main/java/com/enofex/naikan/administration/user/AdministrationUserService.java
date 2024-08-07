package com.enofex.naikan.administration.user;

import com.enofex.naikan.security.User;
import com.enofex.naikan.web.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AdministrationUserService {

  private final AdministrationUserRepository administrationUserRepository;

  AdministrationUserService(AdministrationUserRepository administrationUserRepository) {
    this.administrationUserRepository = administrationUserRepository;
  }

  public Page<User> findAll(Filterable filterable, Pageable pageable) {
    return this.administrationUserRepository.findAll(filterable, pageable);
  }

  public User findById(UserId id) {
    return this.administrationUserRepository.findById(id);
  }

  @Transactional
  public User save(User user) {
    return this.administrationUserRepository.save(user);
  }

  @Transactional
  public long deleteById(UserId id) {
    return this.administrationUserRepository.deleteById(id);
  }
}
