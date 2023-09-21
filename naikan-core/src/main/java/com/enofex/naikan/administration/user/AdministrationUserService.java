package com.enofex.naikan.administration.user;

import com.enofex.naikan.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrationUserService {

  Page<User> findAll(Filterable filterable, Pageable pageable);

  User findById(UserId id);

  User findByName(String name);

  User save(User user);

  long deleteById(UserId id);

  long count();
}
