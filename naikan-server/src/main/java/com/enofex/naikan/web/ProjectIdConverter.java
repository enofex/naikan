package com.enofex.naikan.web;

import org.springframework.core.convert.converter.Converter;

public final class ProjectIdConverter implements Converter<String, ProjectId> {

  @Override
  public ProjectId convert(String source) {
    return ProjectId.of(source);
  }
}