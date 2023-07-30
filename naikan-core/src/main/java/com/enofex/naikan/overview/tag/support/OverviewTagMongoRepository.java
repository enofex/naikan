package com.enofex.naikan.overview.tag.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.tag.OverviewTagRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewTagMongoRepository extends OverviewRepository implements
    OverviewTagRepository {

  OverviewTagMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = defaultOverviewGroupOperations(
        "tags",
        List.of("tags"),
        builder.toSearch(List.of("group")),
        builder.toFilters());

    return findAll(OverviewGroup.class, operations, pageable);
  }

  @Override
  public OverviewTopGroups findTopTags(long topN) {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind("tags"),
        group("tags").count().as("count"),
        sort(Direction.DESC, "count").and(Direction.ASC, "_id"),
        limit(topN),
        group()
            .push("_id").as("names")
            .push("count").as("counts"),
        project().andExclude("_id")
            .and("names").as("names")
            .and("counts").as("counts")
    );

    return template()
        .aggregate(aggregation, collectionName(), OverviewTopGroups.class)
        .getUniqueMappedResult();
  }
}
