package com.enofex.naikan.administration.user.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.user.AdministrationUserRepository;
import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.administration.user.User;
import com.enofex.naikan.administration.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AdministrationUserServiceHandler implements AdministrationUserService {

  private final AdministrationUserRepository administrationUserRepository;

  AdministrationUserServiceHandler(AdministrationUserRepository administrationUserRepository) {
    this.administrationUserRepository = administrationUserRepository;
  }

  @Override
  public Page<User> findAll(Filterable filterable, Pageable pageable) {
    return this.administrationUserRepository.findAll(filterable, pageable);
  }

  @Override
  public User findById(UserId id) {
    return this.administrationUserRepository.findById(id);
  }

  @Override
  public User findByName(String name) {
    return this.administrationUserRepository.findByName(name);
  }

  @Override
  public User save(User user) {
    return this.administrationUserRepository.save(user);
  }

  @Override
  public long deleteById(UserId id) {
    return this.administrationUserRepository.deleteId(id);
  }

  @Override
  public long count() {
    return this.administrationUserRepository.count();
  }
}
