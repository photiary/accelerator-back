package com.funa.demo;

import com.funa.common.logging.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo controller to demonstrate logging functionality.
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    /**
     * Simple endpoint to demonstrate logging.
     *
     * @param message Optional message parameter
     * @return A response with the message and trace ID
     */
    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam(required = false, defaultValue = "Hello, World!") String message) {
        // Log at different levels
        logger.debug("Debug log from hello endpoint");
        logger.info("Processing hello request with message: {}", message);
        
        // Simulate setting a user ID (in a real app, this would come from authentication)
        String simulatedUserId = "user-123";
        LoggingUtils.setUserId(simulatedUserId);
        logger.info("User {} is accessing the hello endpoint", simulatedUserId);
        
        // Create response
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("traceId", LoggingUtils.getTraceId());
        response.put("userId", LoggingUtils.getUserId());
        
        return response;
    }
    
    /**
     * Endpoint to demonstrate error logging.
     *
     * @return Never returns normally
     */
    @GetMapping("/error")
    public Map<String, String> error() {
        logger.info("About to simulate an error");
        
        try {
            // Simulate an error
            throw new RuntimeException("Simulated error for logging demonstration");
        } catch (Exception e) {
            logger.error("An error occurred in the error endpoint", e);
            throw e;
        }
    }
}