package com.enofex.naikan.security;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractRepository {

  private static final String USERS_COLLECTION = "users";

  UserRepository(MongoTemplate template) {
    super(template, USERS_COLLECTION);
  }

  public User findByName(String name) {
    Query query = new Query(where("name").is(name));
    return template().findOne(query, User.class, collectionName());
  }

  public User save(User user) {
    return template().save(user, collectionName());
  }

  public long count() {
    return template().count(new Query(), collectionName());
  }
}
