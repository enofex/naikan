package com.enofex.naikan.administration.user.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.AbstractRepository;
import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.administration.user.AdministrationUserRepository;
import com.enofex.naikan.administration.user.User;
import com.enofex.naikan.administration.user.UserId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class AdministrationUserMongoRepository extends AbstractRepository implements
    AdministrationUserRepository {

  private static final String USERS_COLLECTION = "users";

  AdministrationUserMongoRepository(MongoTemplate template) {
    super(template, USERS_COLLECTION);
  }

  @Override
  public Page<User> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(match(builder.toSearch(List.of("name"))));
    operations.add(match(builder.toFilters()));

    return findAll(User.class, operations, pageable);
  }

  @Override
  public User findById(UserId id) {
    Query query = new Query(where("id").is(id.id()));
    return template().findOne(query, User.class, collectionName());
  }

  @Override
  public User findByName(String name) {
    Query query = new Query(where("name").is(name));
    return template().findOne(query, User.class, collectionName());
  }

  @Override
  public User save(User user) {
    return template().save(user, collectionName());
  }

  @Override
  public long delete(UserId id) {
    Query query = new Query(where("id").is(id.id()));
    return template()
        .remove(query, User.class, collectionName())
        .getDeletedCount();
  }

  @Override
  public long count() {
    return template().count(new Query(), collectionName());
  }
}
