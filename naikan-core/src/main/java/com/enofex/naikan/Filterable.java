package com.enofex.naikan;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class Filterable {

  private final String search;
  private final Map<String, List<FilterMetadata>> filters;

  private Filterable(String search) {
    this(search, Map.of());
  }

  private Filterable(String search, Map<String, List<FilterMetadata>> filters) {
    this.search = search;
    this.filters = Map.copyOf(filters);
  }

  public String search() {
    return search;
  }

  public boolean hasSearch() {
    return search != null && !search.isEmpty();
  }

  public Map<String, List<FilterMetadata>> filters() {
    return filters;
  }

  public boolean hasFilters() {
    return filters != null && !filters.isEmpty();
  }

  public static Filterable of(String search) {
    return new Filterable(search);
  }

  public static Filterable of(String search, Map<String, List<FilterMetadata>> filters) {
    return new Filterable(search, filters);
  }

  public static Filterable emptySearch() {
    return new Filterable("");
  }

  public record FilterMetadata(String field, Object value, FilterMatchMode matchMode,
                               FilterOperator operator) {

  }

  public enum FilterOperator {
    AND,
    OR;

    public static FilterOperator of(String name) {
      return Stream.of(FilterOperator.values())
          .filter(type -> type.toString().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }
  }

  public enum FilterMatchMode {
    STARTS_WITH("startsWith"),
    CONTAINS("contains"),
    NOT_CONTAINS("notContains"),
    ENDS_WITH("endsWith"),
    EQUALS("equals"),
    NOT_EQUALS("notEquals"),
    IN("in"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL_TO("lte"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    BETWEEN("between"),
    IS("is"),
    IS_NOT("isNot"),
    BEFORE("before"),
    AFTER("after"),
    DATE_IS("dateIs"),
    DATE_IS_NOT("dateIsNot"),
    DATE_BEFORE("dateBefore"),
    DATE_AFTER("dateAfter");

    private final String name;

    FilterMatchMode(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static FilterMatchMode of(String name) {
      return Stream.of(FilterMatchMode.values())
          .filter(type -> type.toString().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }
  }

}
