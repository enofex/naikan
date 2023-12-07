package com.enofex.naikan.administration.token;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import com.enofex.naikan.security.token.Token;
import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class AdministrationTokenRepository extends AbstractRepository {

  private static final String TOKENS_COLLECTION = "tokens";

  AdministrationTokenRepository(MongoTemplate template) {
    super(template, TOKENS_COLLECTION);
  }

  public Page<Token> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(match(builder.toSearch(List.of("token", "createdBy", "description"))));
    operations.add(match(builder.toFilters()));

    return findAll(Token.class, operations, pageable);
  }

  public Token save(Token token) {
    return template().save(token, collectionName());
  }

  public long deleteById(TokenId id) {
    return template()
        .remove(new Query(where("id").is(id.id())), Token.class, collectionName())
        .getDeletedCount();
  }
}
