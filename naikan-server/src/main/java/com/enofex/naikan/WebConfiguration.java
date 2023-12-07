package com.enofex.naikan;

import com.enofex.naikan.web.FilterableArgumentResolver;
import com.enofex.naikan.web.ProjectIdConverter;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new FilterableArgumentResolver());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new ProjectIdConverter());
  }
}