package com.enofex.naikan.test;

import com.enofex.naikan.MongoConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {TestNaikanApplication.class,
    MongoConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Rollback
@Transactional
@ActiveProfiles("integration-test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IntegrationTest {

}
