package com.enofex.naikan.overview.contact.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.contact.OverviewContactRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewContactMongoRepository extends OverviewRepository implements
    OverviewContactRepository {

  OverviewContactMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);

    List<AggregationOperation> operations = defaultOverviewGroupOperations("contacts",
        builder.toSearch(List.of("group.name", "group.description", "group.email",
            "group.phone", "group.title", "group.roles")));

    operations.add(match(builder.toFilters()));

    return findAll(OverviewGroup.class, operations, pageable);
  }

}
