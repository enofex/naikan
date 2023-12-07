package com.enofex.naikan.overview;

import com.enofex.naikan.model.Project;
import java.time.LocalDateTime;

public record OverviewBom(String id, LocalDateTime timestamp, Project project) {

}
