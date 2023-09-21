package com.enofex.naikan.administration.user;

import com.enofex.naikan.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrationUserRepository {

  Page<User> findAll(Filterable filterable, Pageable pageable);

  User findById(UserId id);

  User findByName(String name);

  User save(User user);

  long deleteId(UserId id);

  long count();


}
