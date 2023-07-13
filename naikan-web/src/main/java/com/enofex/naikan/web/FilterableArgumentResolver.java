package com.enofex.naikan.web;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.Filterable.FilterMatchMode;
import com.enofex.naikan.Filterable.FilterMetadata;
import com.enofex.naikan.Filterable.FilterOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class FilterableArgumentResolver implements HandlerMethodArgumentResolver {

  private static final int WITHOUT_FILTER_OPERATOR = 3;
  private static final int WITH_FILTER_OPERATOR = WITHOUT_FILTER_OPERATOR + 1;
  private static final int FIELD_INDEX = 0;
  private static final int VALUE_INDEX = 1;
  private static final int FILTER_MATCH_MODE_INDEX = 2;
  private static final int FILTER_OPERATOR_INDEX = 3;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameter().getType() == Filterable.class;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    String search = webRequest.getParameter("search");
    Map<String, List<FilterMetadata>> filters = filters(webRequest);

    return !filters.isEmpty() ? Filterable.of(search, filters) : Filterable.of(search);
  }

  private Map<String, List<FilterMetadata>> filters(NativeWebRequest webRequest) {
    String[] requestFilters = webRequest.getParameterMap().get("filter");

    if (requestFilters != null && requestFilters.length > 0) {
      Map<String, List<FilterMetadata>> filters = new HashMap<>();

      for (String requestFilter : requestFilters) {
        String[] split = requestFilter.split(",");

        if (isFilterComplete(split)) {
          String field = field(split);
          Object value = value(split);
          FilterMatchMode filteredMatchMode = filterMatchMode(split);
          FilterOperator filterOperator = filterOperator(split);

          filters.computeIfAbsent(field, k -> new ArrayList<>())
              .add(new FilterMetadata(field, value, filteredMatchMode, filterOperator));
        }
      }

      return filters;
    }

    return Map.of();
  }

  private static boolean isFilterComplete(String[] split) {
    return split != null
        && (split.length == WITHOUT_FILTER_OPERATOR
        || split.length == WITH_FILTER_OPERATOR);
  }

  private static String field(String[] split) {
    return split[FIELD_INDEX];
  }

  private static Object value(String[] split) {
    return  split[VALUE_INDEX];
  }

  private static FilterMatchMode filterMatchMode(String[] split) {
    return FilterMatchMode.of(split[FILTER_MATCH_MODE_INDEX]);
  }

  private static FilterOperator filterOperator(String[] split) {
    return split.length == WITHOUT_FILTER_OPERATOR ? null
        : FilterOperator.of(split[FILTER_OPERATOR_INDEX]);
  }
}