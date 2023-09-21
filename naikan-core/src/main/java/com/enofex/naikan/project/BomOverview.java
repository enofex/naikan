package com.enofex.naikan.project;

import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Repository;
import com.enofex.naikan.model.Tags;
import java.time.LocalDateTime;
import java.util.List;

public record BomOverview(String id, LocalDateTime timestamp, Project project,
                          Organization organization, List<String> environmentNames,
                          List<String> teamNames, List<String> developerNames,
                          List<String> contactNames,
                          List<String> technologyNames, List<String> licenseNames,
                          List<String> documentationNames, List<String> integrationNames, Tags tags,
                          Repository repository, long deploymentsCount,
                          long deploymentsEnvironmentsCount, long deploymentsVersionsCount,
                          Deployment lastDeployment, DeploymentsPerMonth deploymentsPerMonth) {

  public BomOverview(String id, LocalDateTime timestamp, Project project,
      Organization organization, List<String> environments,
      List<String> teams, List<String> developers, List<String> contacts,
      List<String> technologies, List<String> licenses,
      List<String> documentations, List<String> integrations, Tags tags,
      Repository repository, int deploymentsCount, int deploymentsEnvironmentsCount,
      int deploymentsVersionsCount, Deployment lastDeployment) {
    this(id, timestamp, project, organization, environments, teams, developers, contacts,
        technologies, licenses, documentations, integrations, tags, repository, deploymentsCount,
        deploymentsEnvironmentsCount, deploymentsVersionsCount, lastDeployment, null);
  }

  public BomOverview {
    environmentNames = environmentNames != null ? environmentNames : List.of();
    teamNames = teamNames != null ? teamNames : List.of();
    developerNames = developerNames != null ? developerNames : List.of();
    contactNames = contactNames != null ? contactNames : List.of();
    technologyNames = technologyNames != null ? technologyNames : List.of();
    licenseNames = licenseNames != null ? licenseNames : List.of();
    documentationNames = documentationNames != null ? documentationNames : List.of();
    integrationNames = integrationNames != null ? integrationNames : List.of();
    tags = tags != null ? tags : Tags.empty();
  }

  public static BomOverview of(BomOverview bom, DeploymentsPerMonth deploymentsPerMonth) {
    return new BomOverview(
        bom.id(),
        bom.timestamp(),
        bom.project(),
        bom.organization(),
        bom.environmentNames(),
        bom.teamNames(),
        bom.developerNames(),
        bom.contactNames(),
        bom.technologyNames(),
        bom.licenseNames(),
        bom.documentationNames(),
        bom.integrationNames(),
        bom.tags(),
        bom.repository(),
        bom.deploymentsCount(),
        bom.deploymentsEnvironmentsCount(),
        bom.deploymentsVersionsCount(),
        bom.lastDeployment(),
        deploymentsPerMonth
    );
  }
}
