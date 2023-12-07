package com.enofex.naikan.administration.project;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import com.enofex.naikan.web.ProjectId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class AdministrationProjectRepository extends AbstractRepository {

  AdministrationProjectRepository(MongoTemplate template) {
    super(template);
  }

  public Page<Project> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(project()
        .and("id").as("id")
        .and("project.name").as("name")
        .and("project.repository").as("repository")
        .and("timestamp").as("timestamp")
    );

    operations.add(match(builder.toSearch(List.of("id", "name", "repository", "timestamp"))));
    operations.add(match(builder.toFilters()));

    return findAll(Project.class, operations, pageable);
  }

  public long deleteById(ProjectId id) {
    return template()
        .remove(new Query(where("id").is(id.id())), Bom.class, collectionName())
        .getDeletedCount();
  }
}
