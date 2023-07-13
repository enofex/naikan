package com.enofex.naikan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enofex.naikan.Filterable.FilterMatchMode;
import com.enofex.naikan.Filterable.FilterOperator;
import org.junit.jupiter.api.Test;

class FilterableTest {

  @Test
  void shouldReturnSearchString() {
    String search = "test";
    Filterable filterable = Filterable.of(search);

    assertEquals(search, filterable.search());
  }

  @Test
  void shouldReturnTrueIfSearchStringExists() {
    Filterable filterable = Filterable.of("test");

    assertTrue(filterable.hasSearch());
  }

  @Test
  void shouldReturnFalseIfSearchStringDoesNotExist() {
    Filterable filterable = Filterable.of("");

    assertFalse(filterable.hasSearch());
  }

  @Test
  void shouldReturnFalseIfSearchStringIsNull() {
    Filterable filterable = Filterable.of(null);

    assertFalse(filterable.hasSearch());
  }

  @Test
  void shouldReturnFalseIfFilterEmpty() {
    Filterable filterable = Filterable.emptySearch();

    assertFalse(filterable.hasSearch());
  }

  @Test
  void shouldReturnFilterMatchMode() {
    assertNull(Filterable.FilterMatchMode.of(null));
    assertNull(Filterable.FilterMatchMode.of("do not exists"));
    assertEquals(FilterMatchMode.CONTAINS, Filterable.FilterMatchMode.of("contains"));
  }

  @Test
  void shouldReturnFilterMatchModeName() {
    assertEquals("contains", FilterMatchMode.CONTAINS.getName());
    assertEquals("contains", FilterMatchMode.CONTAINS.toString());
  }

  @Test
  void shouldReturnFilterOperator() {
    assertNull(Filterable.FilterOperator.of(null));
    assertNull(Filterable.FilterOperator.of("do not exists"));
    assertEquals(FilterOperator.AND, Filterable.FilterOperator.of("and"));
    assertEquals(FilterOperator.OR, Filterable.FilterOperator.of("or"));
  }
}

