package com.enofex.naikan.administration.project;

import java.time.LocalDateTime;

record Project(String id, String name, String repository, LocalDateTime timestamp) {

}
