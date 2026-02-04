package com.synergybridge.config;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnterpriseDelayConfig - the enterprise delay configuration.
 * 
 * These tests verify that:
 * 1. All 12 tools have configured delays
 * 2. Delay values match the specification
 * 3. Default delay is returned for unknown tools
 */
@QuarkusTest
class EnterpriseDelayConfigTest {

    @Inject
    EnterpriseDelayConfig delayConfig;

    @ParameterizedTest
    @CsvSource({
        "websphere-deploy-ejb, 8",
        "cobol-copybook-transform, 3",
        "mainframe-jcl-submit, 5",
        "soa-governance-validate, 4",
        "enterprise-service-bus-route, 6",
        "ldap-corporate-directory-sync, 7",
        "crystal-reports-generate, 12",
        "tuxedo-transaction-begin, 4",
        "tibco-rendezvous-publish, 2",
        "siebel-crm-customer-lookup, 9",
        "informatica-etl-workflow-start, 6",
        "peoplesoft-component-interface-call, 7"
    })
    void shouldReturnCorrectDelayForTool(String toolName, int expectedSeconds) {
        Duration delay = delayConfig.getDelay(toolName);
        
        assertNotNull(delay, "Delay should not be null for " + toolName);
        assertEquals(expectedSeconds, delay.toSeconds(),
                "Delay for " + toolName + " should be " + expectedSeconds + " seconds");
    }

    @Test
    void shouldReturnDefaultDelayForUnknownTool() {
        Duration delay = delayConfig.getDelay("unknown-enterprise-tool");
        
        assertNotNull(delay, "Delay should not be null for unknown tool");
        assertEquals(5, delay.toSeconds(), "Default delay should be 5 seconds");
    }

    @Test
    void shouldReturnDefaultDelayForNullToolName() {
        Duration delay = delayConfig.getDelay(null);
        
        assertNotNull(delay, "Delay should not be null for null tool name");
        assertEquals(5, delay.toSeconds(), "Default delay should be 5 seconds");
    }

    @Test
    void shouldReturnDefaultDelayForEmptyToolName() {
        Duration delay = delayConfig.getDelay("");
        
        assertNotNull(delay, "Delay should not be null for empty tool name");
        assertEquals(5, delay.toSeconds(), "Default delay should be 5 seconds");
    }

    @Test
    void crystalReportsShouldHaveLongestDelay() {
        Duration crystalDelay = delayConfig.getDelay("crystal-reports-generate");
        
        // Crystal Reports should have the longest delay (12 seconds)
        assertEquals(12, crystalDelay.toSeconds(), 
                "Crystal Reports should have the longest delay of 12 seconds");
    }

    @Test
    void tibcoShouldHaveShortestDelay() {
        Duration tibcoDelay = delayConfig.getDelay("tibco-rendezvous-publish");
        
        // TIBCO should have the shortest delay (2 seconds)
        assertEquals(2, tibcoDelay.toSeconds(),
                "TIBCO should have the shortest delay of 2 seconds");
    }

    @Test
    void allDelaysShouldBeBetweenTwoAndTwelveSeconds() {
        String[] allTools = {
            "websphere-deploy-ejb",
            "cobol-copybook-transform",
            "mainframe-jcl-submit",
            "soa-governance-validate",
            "enterprise-service-bus-route",
            "ldap-corporate-directory-sync",
            "crystal-reports-generate",
            "tuxedo-transaction-begin",
            "tibco-rendezvous-publish",
            "siebel-crm-customer-lookup",
            "informatica-etl-workflow-start",
            "peoplesoft-component-interface-call"
        };

        for (String tool : allTools) {
            Duration delay = delayConfig.getDelay(tool);
            long seconds = delay.toSeconds();
            
            assertTrue(seconds >= 2 && seconds <= 12,
                    "Delay for " + tool + " should be between 2-12 seconds, but was " + seconds);
        }
    }
}
