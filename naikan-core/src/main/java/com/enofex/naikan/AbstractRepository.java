package com.enofex.naikan;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    return template;
  }

  public String collectionName() {
    return collectionName;
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
            .getMappedResults().get(0).get("total").toString()));
  }

  public <S> Page<S> findAll(Class<S> clazz, Criteria criteria, Pageable pageable) {
    Query query = new Query().with(pageable);

    if (criteria != null) {
      query.addCriteria(criteria);
    }

    List<S> result = template.find(query, clazz, collectionName);

    return PageableExecutionUtils.getPage(result, pageable,
        () -> template.count(Query.of(query).limit(-1).skip(-1), clazz, collectionName));
  }
}
