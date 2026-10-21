package com.medtech.app;

import com.medtech.app.config.AsyncSyncConfiguration;
import com.medtech.app.config.DatabaseTestcontainer;
import com.medtech.app.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        MedtechApp.class,
        AsyncSyncConfiguration.class,
        TestSecurityConfiguration.class,
        com.medtech.app.config.JacksonHibernateConfiguration.class,
        DatabaseTestcontainer.class,
    }
)
public @interface IntegrationTest {}
