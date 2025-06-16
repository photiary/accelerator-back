package com.funa.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test configuration to ensure tests use the 'test' profile.
 * This ensures that tests use the H2 in-memory database configuration
 * from application-test.properties.
 * 
 * This configuration is automatically picked up by Spring Boot test classes
 * and applies the 'test' profile to all tests.
 */
@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {

    /**
     * This bean is a placeholder to ensure the configuration is loaded.
     * The main purpose of this class is to apply the 'test' profile to all tests.
     */
    @Bean
    public String testConfigurationLoaded() {
        return "Test configuration loaded with 'test' profile active";
    }
}
