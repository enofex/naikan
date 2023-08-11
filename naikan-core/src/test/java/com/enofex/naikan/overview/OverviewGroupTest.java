package com.enofex.naikan.overview;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.overview.OverviewGroup.Group;
import com.enofex.naikan.test.model.Boms;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class OverviewGroupTest {

  private final Project project = DeserializerFactory.newJsonDeserializer()
      .of(Boms.validBom0asInputStream()).project();

  @Test
  void shouldCreateOverviewGroup() {
    OverviewBom overviewBom = new OverviewBom("1" , LocalDateTime.now(), this.project);
    OverviewGroup overviewGroup = new OverviewGroup("123", new Group("name"),
        List.of(overviewBom), 1);

    assertAll(
        () -> assertEquals("123" , overviewGroup.uuid()),
        () -> assertEquals("name", overviewGroup.group().name()),
        () -> assertEquals(1, overviewGroup.count()),
        () -> assertIterableEquals(Collections.singletonList(overviewBom), overviewGroup.boms())
    );
  }

  @Test
  void shouldCreateRandomUuidWhenUuidIsNull() {
    OverviewGroup overviewGroup = new OverviewGroup(null, new Group("name"), List.of(), 0);

    assertNotNull(overviewGroup.uuid());
  }

  @Test
  void shouldCreateOverviewGroupWithUnmodifiableBomsList() {
    OverviewBom overviewBom = new OverviewBom("1" , LocalDateTime.now(), this.project);
    OverviewGroup overviewGroup = new OverviewGroup("123", new Group("name"),
        Collections.singletonList(overviewBom), 1);

    assertThrows(UnsupportedOperationException.class, () -> overviewGroup.boms().add(overviewBom));
  }
}
