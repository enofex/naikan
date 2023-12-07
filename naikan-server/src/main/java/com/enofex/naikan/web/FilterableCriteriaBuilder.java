package com.enofex.naikan.web;

import com.enofex.naikan.web.Filterable.FilterMatchMode;
import com.enofex.naikan.web.Filterable.FilterMetadata;
import com.enofex.naikan.web.Filterable.FilterOperator;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public final class FilterableCriteriaBuilder {

  private static final Criteria EMPTY_CRITERIA = new Criteria();

  private final Filterable filterable;

  public FilterableCriteriaBuilder(Filterable filterable) {
    this.filterable = filterable;
  }

  public Criteria toFilters() {
    if (this.filterable != null && this.filterable.hasFilters()) {
      List<Criteria> ors = new ArrayList<>();
      List<Criteria> ands = new ArrayList<>();

      this.filterable.filters().forEach((key, filterMetadata) -> filterMetadata.forEach(meta -> {
        if (meta.operator() == null || FilterOperator.AND == meta.operator()) {
          ands.add(FILTER_CRITERIA.get(meta.matchMode()).apply(meta));
        } else if (FilterOperator.OR == meta.operator()) {
          ors.add(FILTER_CRITERIA.get(meta.matchMode()).apply(meta));
        }
      }));

      if (ors.isEmpty() && ands.isEmpty()) {
        return EMPTY_CRITERIA;
      }
      if (!ors.isEmpty() && !ands.isEmpty()) {
        return new Criteria().orOperator(ors).andOperator(ands);
      }
      if (!ors.isEmpty()) {
        return new Criteria().orOperator(ors);
      }
      return new Criteria().andOperator(ands);
    }

    return EMPTY_CRITERIA;
  }

  public Criteria toSearch(List<String> fields) {
    if (this.filterable != null && this.filterable.hasSearch()) {
      return new Criteria().orOperator(fields
          .stream()
          .map(field -> Criteria.where(field).regex(this.filterable.search(), "i"))
          .toList());
    }

    return EMPTY_CRITERIA;
  }

  private static LocalDateTime parseDate(Object date) {
    Instant instant = Instant.parse(String.valueOf(date));
    ZoneId zoneId = ZoneId.systemDefault();
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  private static final Map<FilterMatchMode, Function<FilterMetadata, Criteria>>
      FILTER_CRITERIA = new EnumMap<>(FilterMatchMode.class);

  static {
    FILTER_CRITERIA.put(FilterMatchMode.EQUALS,
        filter -> Criteria.where(filter.field()).is(String.valueOf(filter.value())));
    FILTER_CRITERIA.put(FilterMatchMode.NOT_EQUALS,
        filter -> Criteria.where(filter.field()).ne(String.valueOf(filter.value())));

    FILTER_CRITERIA.put(FilterMatchMode.IS,
        filter -> Criteria.where(filter.field())
            .is(NumberUtils.createNumber(String.valueOf(filter.value()))));
    FILTER_CRITERIA.put(FilterMatchMode.IS_NOT,
        filter -> Criteria.where(filter.field())
            .ne(NumberUtils.createNumber(String.valueOf(filter.value()))));

    FILTER_CRITERIA.put(FilterMatchMode.GREATER_THAN,
        filter -> Criteria.where(filter.field())
            .gt(NumberUtils.createNumber(String.valueOf(filter.value()))));
    FILTER_CRITERIA.put(FilterMatchMode.GREATER_THAN_OR_EQUAL_TO,
        filter -> Criteria.where(filter.field())
            .gte(NumberUtils.createNumber(String.valueOf(filter.value()))));

    FILTER_CRITERIA.put(FilterMatchMode.LESS_THAN,
        filter -> Criteria.where(filter.field())
            .lt(NumberUtils.createNumber(String.valueOf(filter.value()))));
    FILTER_CRITERIA.put(FilterMatchMode.LESS_THAN_OR_EQUAL_TO,
        filter -> Criteria.where(filter.field())
            .lte(NumberUtils.createNumber(String.valueOf(filter.value()))));

    FILTER_CRITERIA.put(FilterMatchMode.BEFORE, FILTER_CRITERIA.get(FilterMatchMode.LESS_THAN));
    FILTER_CRITERIA.put(FilterMatchMode.AFTER, FILTER_CRITERIA.get(FilterMatchMode.GREATER_THAN));

    FILTER_CRITERIA.put(FilterMatchMode.CONTAINS,
        filter -> Criteria.where(filter.field()).regex(String.valueOf(filter.value())));
    FILTER_CRITERIA.put(FilterMatchMode.NOT_CONTAINS,
        filter -> Criteria.where(filter.field()).not().regex(String.valueOf(filter.value())));

    FILTER_CRITERIA.put(FilterMatchMode.STARTS_WITH,
        filter -> Criteria.where(filter.field()).regex("^" + filter.value()));
    FILTER_CRITERIA.put(FilterMatchMode.ENDS_WITH,
        filter -> Criteria.where(filter.field()).regex(filter.value() + "$"));

    FILTER_CRITERIA.put(FilterMatchMode.DATE_IS,
        filter -> {
          LocalDateTime localDateTime = parseDate(filter.value());

          return Criteria.where(filter.field())
              .gte(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN))
              .lte(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX));
        });
    FILTER_CRITERIA.put(FilterMatchMode.DATE_IS_NOT,
        filter -> {
          LocalDateTime localDateTime = parseDate(filter.value());

          return Criteria.where(filter.field()).not()
              .gte(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN))
              .lte(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX));
        });
    FILTER_CRITERIA.put(FilterMatchMode.DATE_AFTER,
        filter -> Criteria.where(filter.field()).gt(parseDate(filter.value())));
    FILTER_CRITERIA.put(FilterMatchMode.DATE_BEFORE,
        filter -> Criteria.where(filter.field())
            .lt(parseDate(filter.value())));
  }
}
