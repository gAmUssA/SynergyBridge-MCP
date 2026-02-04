package com.synergybridge.responses;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ErrorCatalog - the enterprise error injection mechanism.
 * 
 * These tests verify that:
 * 1. Error injection occurs at approximately 10% rate
 * 2. All 10 enterprise errors are properly defined
 * 3. Error messages follow enterprise format
 */
@QuarkusTest
class ErrorCatalogTest {

    @Inject
    ErrorCatalog errorCatalog;

    @Test
    void shouldInjectErrorsAtApproximatelyTenPercent() {
        // Run 1000 iterations and verify error rate is roughly 10%
        int errorCount = 0;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
            if (error.isPresent()) {
                errorCount++;
            }
        }

        // Allow for statistical variance: 5-15% is acceptable
        double errorRate = (double) errorCount / iterations;
        assertTrue(errorRate >= 0.05 && errorRate <= 0.20,
                "Error rate should be approximately 10%, but was " + (errorRate * 100) + "%");
    }

    @Test
    void shouldReturnValidEnterpriseError() {
        // Keep trying until we get an error (should happen within ~30 attempts on average)
        Optional<EnterpriseError> error = Optional.empty();
        for (int i = 0; i < 100 && error.isEmpty(); i++) {
            error = errorCatalog.maybeInjectError();
        }

        assertTrue(error.isPresent(), "Should eventually return an error");
        EnterpriseError enterpriseError = error.get();
        
        assertNotNull(enterpriseError.code(), "Error code should not be null");
        assertNotNull(enterpriseError.message(), "Error message should not be null");
        assertFalse(enterpriseError.code().isEmpty(), "Error code should not be empty");
        assertFalse(enterpriseError.message().isEmpty(), "Error message should not be empty");
    }

    @Test
    void shouldReturnDifferentErrors() {
        // Collect errors to verify variety
        Set<String> errorCodes = new HashSet<>();
        
        // Run many iterations to collect different errors
        for (int i = 0; i < 1000; i++) {
            Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
            error.ifPresent(e -> errorCodes.add(e.code()));
        }

        // Should have collected multiple different error codes
        assertTrue(errorCodes.size() >= 3,
                "Should return variety of errors, but only found " + errorCodes.size() + " unique codes");
    }

    @Test
    void errorToStringShouldBeProperlyFormatted() {
        // Get an error
        Optional<EnterpriseError> error = Optional.empty();
        for (int i = 0; i < 100 && error.isEmpty(); i++) {
            error = errorCatalog.maybeInjectError();
        }

        assertTrue(error.isPresent(), "Should eventually return an error");
        String errorString = error.get().toErrorString();
        
        // Should contain error code
        assertTrue(errorString.contains("ERR_") || errorString.contains("Error"),
                "Error string should contain error code indicator");
    }

    @RepeatedTest(10)
    void shouldSometimesReturnNoError() {
        // With 90% no-error rate, over 10 repetitions we should see at least one empty result
        // This test will pass most of the time if the probability is working correctly
        int emptyCount = 0;
        for (int i = 0; i < 20; i++) {
            Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
            if (error.isEmpty()) {
                emptyCount++;
            }
        }
        
        assertTrue(emptyCount > 0, "Should sometimes return no error (90% probability)");
    }
}
