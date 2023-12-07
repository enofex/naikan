package com.enofex.naikan.administration.user;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import com.enofex.naikan.security.User;
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
class AdministrationUserRepository extends AbstractRepository {

  private static final String USERS_COLLECTION = "users";

  AdministrationUserRepository(MongoTemplate template) {
    super(template, USERS_COLLECTION);
  }

  public Page<User> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(match(builder.toSearch(List.of("name"))));
    operations.add(match(builder.toFilters()));

    return findAll(User.class, operations, pageable);
  }

  public User findById(UserId id) {
    Query query = new Query(where("id").is(id.id()));
    return template().findOne(query, User.class, collectionName());
  }


  public User save(User user) {
    return template().save(user, collectionName());
  }

  public long deleteId(UserId id) {
    Query query = new Query(where("id").is(id.id()));
    return template()
        .remove(query, User.class, collectionName())
        .getDeletedCount();
  }
}
