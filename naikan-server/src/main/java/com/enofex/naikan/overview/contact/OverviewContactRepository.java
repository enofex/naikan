package com.enofex.naikan.overview.contact;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewContactRepository extends OverviewRepository {

  OverviewContactRepository(MongoTemplate template) {
    super(template);
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);

    List<AggregationOperation> operations = defaultOverviewGroupOperations(
        "contacts",
        List.of("contacts.name"),
        builder.toSearch(List.of("group.name")),
        builder.toFilters());

    return findAll(OverviewGroup.class, operations, pageable);
  }

}
