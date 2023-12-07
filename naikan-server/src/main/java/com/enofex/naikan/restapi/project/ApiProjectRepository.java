package com.enofex.naikan.restapi.project;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.web.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
class ApiProjectRepository extends AbstractRepository {

  ApiProjectRepository(MongoTemplate template) {
    super(template);
  }

  public boolean existsByProjectName(String projectName) {
    Query query = new Query(where("project.name").is(projectName));
    return template().exists(query, Bom.class, collectionName());
  }

  public Bom updateById(ProjectId id, Bom bom) {
    Query query = new Query(where("id").is(id.id()));
    return findAndModify(query, false, bom);
  }

  public Bom upsertByProjectName(Bom bom) {
    Query query = new Query(where("project.name").is(bom.project().name()));
    return findAndModify(query, true, bom);
  }

  private Bom findAndModify(Query query, boolean upsert, Bom bom) {
    Update update = Update.fromDocument(toDocument(bom), "deployments")
        .addToSet("deployments")
        .each(bom.deployments().all());

    FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(upsert);

    return template().findAndModify(query, update, options, Bom.class, collectionName());
  }

  private Document toDocument(Bom bom) {
    Document document = new Document();
    template().getConverter().write(bom, document);

    return document;
  }
}

