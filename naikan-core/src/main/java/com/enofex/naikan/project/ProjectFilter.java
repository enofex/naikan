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
    this.packaging = List.copyOf(packaging);
    this.tags = List.copyOf(tags);
    this.groupIds = List.copyOf(groupIds);
    this.organizations = List.copyOf(organizations);
    this.organizationDepartments = List.copyOf(organizationDepartments);
    this.environments = List.copyOf(environments);
    this.environmentLocations = List.copyOf(environmentLocations);
    this.environmentTags = List.copyOf(environmentTags);
    this.teams = List.copyOf(teams);
    this.developers = List.copyOf(developers);
    this.developerOrganizations = List.copyOf(developerOrganizations);
    this.developerDepartments = List.copyOf(developerDepartments);
    this.developerRoles = List.copyOf(developerRoles);
    this.contacts = List.copyOf(contacts);
    this.contactRoles = List.copyOf(contactRoles);
    this.integrations = List.copyOf(integrations);
    this.integrationTags = List.copyOf(integrationTags);
    this.technologies = List.copyOf(technologies);
    this.technologyVersions = List.copyOf(technologyVersions);
    this.technologyTags = List.copyOf(technologyTags);
    this.deployments = List.copyOf(deployments);
    this.licenses = List.copyOf(licenses);
  }

  public record FilterItem(String name, long count) {

  }

}
