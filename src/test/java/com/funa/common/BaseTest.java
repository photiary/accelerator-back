package com.funa.common;

import com.funa.common.config.TestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base test class that imports the TestConfig and sets the active profile to 'test'.
 * All test classes should extend this class to ensure consistent test configuration.
 * 
 * This class includes @AutoConfigureMockMvc to support web tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@ActiveProfiles("test")
public abstract class BaseTest {
    // No additional configuration needed
}
