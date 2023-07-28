package com.enofex.naikan.overview;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class OverviewTopGroupsTest {

  @Test
  void shouldCreateOverviewTopGroups() {
    List<String> names = Arrays.asList("Group A", "Group B", "Group C");
    List<Long> counts = Arrays.asList(10L, 20L, 30L);

    OverviewTopGroups overviewTopGroups = new OverviewTopGroups(names, counts);

    assertAll(
        () -> assertEquals(names, overviewTopGroups.names()),
        () -> assertEquals(counts, overviewTopGroups.counts()),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> overviewTopGroups.names().add("Group D")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> overviewTopGroups.counts().add(40L))
    );
  }
}
