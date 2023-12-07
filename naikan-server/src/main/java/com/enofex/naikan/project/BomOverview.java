package com.enofex.naikan.project;

import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Tags;
import java.time.LocalDateTime;
import java.util.List;

record BomOverview(String id, LocalDateTime timestamp, Project project,
                          Organization organization, List<String> environmentNames,
                          List<String> teamNames, List<String> developerNames,
                          List<String> contactNames, List<String> technologyNames,
                          List<String> licenseNames, List<String> documentationNames,
                          List<String> integrationNames, Tags tags, long deploymentsCount,
                          long deploymentsEnvironmentsCount, long deploymentsVersionsCount,
                          Deployment lastDeployment, CountsPerItems deploymentsPerMonth,
                          long commitsCount, CountsPerItems commitsPerMonth,
                          Repository repository) {

  BomOverview {
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

  public static BomOverview of(BomOverview bom, CountsPerItems deploymentsPerMonth,
      CountsPerItems commitsPerMonth) {
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
        bom.deploymentsCount(),
        bom.deploymentsEnvironmentsCount(),
        bom.deploymentsVersionsCount(),
        bom.lastDeployment(),
        deploymentsPerMonth,
        bom.commitsCount(),
        commitsPerMonth,
        bom.repository()
    );
  }

  record Repository(String name, String url, Commit firstCommit, int totalCommits,
                           String defaultBranch) {

  }
}