package com.enofex.naikan.administration.project;

import java.time.LocalDateTime;

public record Project(String id, String name, String repository, LocalDateTime timestamp) {

}
