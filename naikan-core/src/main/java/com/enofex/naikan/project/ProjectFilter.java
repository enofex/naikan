package com.enofex.naikan.project;

import java.util.List;

public record ProjectFilter(
    List<FilterItem> packaging,
    List<FilterItem> tags,
    List<FilterItem> groupIds,
    List<FilterItem> organizations,
    List<FilterItem> organizationDepartments,
    List<FilterItem> environments,
    List<FilterItem> environmentLocations,
    List<FilterItem> environmentTags,
    List<FilterItem> teams,
    List<FilterItem> developers,
    List<FilterItem> developerOrganizations,
    List<FilterItem> developerDepartments,
    List<FilterItem> developerRoles,
    List<FilterItem> contacts,
    List<FilterItem> contactRoles,
    List<FilterItem> integrations,
    List<FilterItem> integrationTags,
    List<FilterItem> technologies,
    List<FilterItem> technologyVersions,
    List<FilterItem> technologyTags,
    List<FilterItem> deployments,
    List<FilterItem> licenses
) {

  public ProjectFilter(
      List<FilterItem> packaging,
      List<FilterItem> tags,
      List<FilterItem> groupIds,
      List<FilterItem> organizations,
      List<FilterItem> organizationDepartments,
      List<FilterItem> environments,
      List<FilterItem> environmentLocations,
      List<FilterItem> environmentTags,
      List<FilterItem> teams,
      List<FilterItem> developers,
      List<FilterItem> developerOrganizations,
      List<FilterItem> developerDepartments,
      List<FilterItem> developerRoles,
      List<FilterItem> contacts,
      List<FilterItem> contactRoles,
      List<FilterItem> integrations,
      List<FilterItem> integrationTags,
      List<FilterItem> technologies,
      List<FilterItem> technologyVersions,
      List<FilterItem> technologyTags,
      List<FilterItem> deployments,
      List<FilterItem> licenses
  ) {
    this.packaging = packaging != null ? List.copyOf(packaging) : List.of();
    this.tags = tags != null ? List.copyOf(tags) : List.of();
    this.groupIds = groupIds != null ? List.copyOf(groupIds) : List.of();
    this.organizations = organizations != null ? List.copyOf(organizations) : List.of();
    this.organizationDepartments =
        organizationDepartments != null ? List.copyOf(organizationDepartments) : List.of();
    this.environments = environments != null ? List.copyOf(environments) : List.of();
    this.environmentLocations =
        environmentLocations != null ? List.copyOf(environmentLocations) : List.of();
    this.environmentTags = environmentTags != null ? List.copyOf(environmentTags) : List.of();
    this.teams = teams != null ? List.copyOf(teams) : List.of();
    this.developers = developers != null ? List.copyOf(developers) : List.of();
    this.developerOrganizations =
        developerOrganizations != null ? List.copyOf(developerOrganizations) : List.of();
    this.developerDepartments =
        developerDepartments != null ? List.copyOf(developerDepartments) : List.of();
    this.developerRoles = developerRoles != null ? List.copyOf(developerRoles) : List.of();
    this.contacts = contacts != null ? List.copyOf(contacts) : List.of();
    this.contactRoles = contactRoles != null ? List.copyOf(contactRoles) : List.of();
    this.integrations = integrations != null ? List.copyOf(integrations) : List.of();
    this.integrationTags = integrationTags != null ? List.copyOf(integrationTags) : List.of();
    this.technologies = technologies != null ? List.copyOf(technologies) : List.of();
    this.technologyVersions =
        technologyVersions != null ? List.copyOf(technologyVersions) : List.of();
    this.technologyTags = technologyTags != null ? List.copyOf(technologyTags) : List.of();
    this.deployments = deployments != null ? List.copyOf(deployments) : List.of();
    this.licenses = licenses != null ? List.copyOf(licenses) : List.of();
  }

  public record FilterItem(String name, long count) {

  }

}
