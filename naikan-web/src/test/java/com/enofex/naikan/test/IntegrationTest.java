package com.enofex.naikan.test;

import com.enofex.naikan.ContainersConfiguration;
import com.enofex.naikan.NaikanApplication;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    classes = {NaikanApplication.class, ContainersConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
@Rollback
@Transactional
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WithMockUser
public @interface IntegrationTest {

}
