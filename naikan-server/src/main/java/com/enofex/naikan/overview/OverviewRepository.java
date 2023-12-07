package com.enofex.naikan.overview;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.repository.AbstractRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class OverviewRepository extends AbstractRepository {

  protected OverviewRepository(MongoTemplate template) {
    super(template);
  }

  public List<AggregationOperation> defaultOverviewGroupOperations(String overviewGroupName,
      List<String> fields, Criteria searchCriteria, Criteria filters) {
    Objects.requireNonNull(overviewGroupName);
    Objects.requireNonNull(fields);

    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(unwind(overviewGroupName));
    operations.add(group(fields.toArray(String[]::new))
        .first(overviewGroupName).as("group")
        .push(Aggregation.ROOT).as("boms"));
    operations.add(project("group" , "boms")
        .and("boms").size().as("count"));

    if (searchCriteria != null) {
      operations.add(match(searchCriteria));
    }

    if (filters != null) {
      operations.add(match(filters));
    }

    return operations;
  }
}
