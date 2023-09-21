package com.enofex.naikan.administration.token.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.AbstractRepository;
import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.administration.token.AdministrationTokenRepository;
import com.enofex.naikan.administration.token.Token;
import com.enofex.naikan.administration.token.TokenId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
class AdministrationTokenMongoRepository extends AbstractRepository implements
    AdministrationTokenRepository {

  private static final String TOKENS_COLLECTION = "tokens";

  AdministrationTokenMongoRepository(MongoTemplate template) {
    super(template, TOKENS_COLLECTION);
  }

  @Override
  public Page<Token> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(match(builder.toSearch(List.of("token", "createdBy", "description"))));
    operations.add(match(builder.toFilters()));

    return findAll(Token.class, operations, pageable);
  }

  @Override
  public long updateLastUsed(String token) {
    Query query = new Query(where("token").is(token));
    Update update = new Update().currentDate("lastUsed");

    return template().updateFirst(query, update, collectionName()).getModifiedCount();
  }

  @Override
  public Token save(Token token) {
    return template().save(token, collectionName());
  }

  @Override
  public long deleteById(TokenId id) {
    return template()
        .remove(new Query(where("id").is(id.id())), Token.class, collectionName())
        .getDeletedCount();
  }

  @Override
  public boolean exists(String token) {
    return template().exists(new Query(where("token").is(token)), collectionName());
  }
}
