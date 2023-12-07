package com.enofex.naikan.security.token;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository extends AbstractRepository {

  private static final String TOKENS_COLLECTION = "tokens";

  TokenRepository(MongoTemplate template) {
    super(template, TOKENS_COLLECTION);
  }

  public boolean exists(String token) {
    return template().exists(new Query(where("token").is(token)), collectionName());
  }

  public long updateLastUsed(String token) {
    Query query = new Query(where("token").is(token));
    Update update = new Update().currentDate("lastUsed");

    return template().updateFirst(query, update, collectionName()).getModifiedCount();
  }
}
