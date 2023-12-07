package com.enofex.naikan.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.web.ProjectId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.support.PageableExecutionUtils;

public abstract class AbstractRepository {

  private static final String DEFAULT_COLLECTION = "projects";

  private final MongoTemplate template;
  private final String collectionName;

  protected AbstractRepository(MongoTemplate template) {
    this(template, DEFAULT_COLLECTION);
  }

  protected AbstractRepository(MongoTemplate template, String collectionName) {
    this.template = Objects.requireNonNull(template);
    this.collectionName = Objects.requireNonNull(collectionName);
  }

  public MongoTemplate template() {
    return this.template;
  }

  public String collectionName() {
    return this.collectionName;
  }

  public Optional<Bom> findById(ProjectId id) {
    return Optional.ofNullable(template().findById(id.id(), Bom.class, collectionName()));
  }

  public <S> Page<S> findAll(Class<S> clazz, List<AggregationOperation> operations,
      Pageable pageable) {
    List<AggregationOperation> totalOperations = new ArrayList<>(operations);
    totalOperations.add(group().count().as("total"));

    if (pageable.getSort().isSorted()) {
      operations.add(sort(pageable.getSort()));
    }

    if (pageable.isPaged()) {
      operations.add(skip((long) pageable.getPageNumber() * pageable.getPageSize()));
      operations.add(limit(pageable.getPageSize()));
    }

    List<S> result = template()
        .aggregate(Aggregation.newAggregation(operations), collectionName(), clazz)
        .getMappedResults();

    return PageableExecutionUtils.getPage(result, pageable,
        () -> Long.parseLong(template()
            .aggregate(Aggregation.newAggregation(totalOperations), collectionName(),
                Document.class)
            .getMappedResults().getFirst().get("total").toString()));
  }
}
