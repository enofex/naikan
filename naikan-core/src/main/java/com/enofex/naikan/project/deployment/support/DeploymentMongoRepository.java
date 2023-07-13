package com.enofex.naikan.project.deployment.support;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.AbstractRepository;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.deployment.DeploymentRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
class DeploymentMongoRepository extends AbstractRepository implements DeploymentRepository {

  DeploymentMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Bom save(ProjectId id, Deployment deployment) {
    Query query = new Query(where("id").is(id.id()));
    Update update = new Update().addToSet("deployments" , deployment);

    if (deployment.timestamp() == null) {
      update.currentDate("timestamp");
    }

    FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

    return template().findAndModify(query, update, options, Bom.class, collectionName());
  }
}
