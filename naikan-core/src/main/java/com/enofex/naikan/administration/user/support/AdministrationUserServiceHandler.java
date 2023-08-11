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
    return administrationUserRepository.findAll(filterable, pageable);
  }

  @Override
  public User findById(UserId id) {
    return administrationUserRepository.findById(id);
  }

  @Override
  public User findByName(String name) {
    return administrationUserRepository.findByName(name);
  }

  @Override
  public User save(User user) {
    return administrationUserRepository.save(user);
  }

  @Override
  public long delete(UserId id) {
    return administrationUserRepository.delete(id);
  }

  @Override
  public long count() {
    return administrationUserRepository.count();
  }
}
