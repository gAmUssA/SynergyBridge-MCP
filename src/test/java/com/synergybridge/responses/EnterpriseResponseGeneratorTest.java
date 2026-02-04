package com.synergybridge.responses;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnterpriseResponseGenerator - the canned response provider.
 * 
 * These tests verify that:
 * 1. All canned responses are loaded correctly
 * 2. Response content matches specifications
 * 3. Generated IDs have correct formats
 */
@QuarkusTest
class EnterpriseResponseGeneratorTest {

    @Inject
    EnterpriseResponseGenerator responseGenerator;

    @Test
    void shouldReturnCobolOutputWithFillerFields() {
        String cobolOutput = responseGenerator.getCobolOutput();
        
        assertNotNull(cobolOutput, "COBOL output should not be null");
        assertFalse(cobolOutput.isEmpty(), "COBOL output should not be empty");
        assertTrue(cobolOutput.contains("FILLER"), 
                "COBOL output should contain FILLER fields");
    }

    @Test
    void shouldReturnCobolOutputWithMainframeDocReference() {
        String cobolOutput = responseGenerator.getCobolOutput();
        
        assertTrue(cobolOutput.contains("SEE_MAINFRAME_DOCUMENTATION") || 
                   cobolOutput.contains("MAINFRAME"),
                "COBOL output should reference mainframe documentation");
    }

    @Test
    void shouldReturnSiebelCustomerRecord() {
        String siebelCustomer = responseGenerator.getSiebelCustomer();
        
        assertNotNull(siebelCustomer, "Siebel customer should not be null");
        assertFalse(siebelCustomer.isEmpty(), "Siebel customer should not be empty");
        // Should be valid JSON-like structure
        assertTrue(siebelCustomer.contains("{") && siebelCustomer.contains("}"),
                "Siebel customer should be JSON formatted");
    }

    @Test
    void shouldReturnSiebelCustomerWithNullFields() {
        String siebelCustomer = responseGenerator.getSiebelCustomer();
        
        // Count null occurrences - should have many null fields
        int nullCount = countOccurrences(siebelCustomer, "null");
        assertTrue(nullCount >= 40,
                "Siebel customer should have approximately 47 null fields, but found " + nullCount);
    }

    @Test
    void shouldReturnExactly147GovernanceWarnings() {
        List<String> warnings = responseGenerator.getGovernanceWarnings();
        
        assertNotNull(warnings, "Governance warnings should not be null");
        assertEquals(147, warnings.size(),
                "Should return exactly 147 governance warnings");
    }

    @Test
    void governanceWarningsShouldStartWithWarning() {
        List<String> warnings = responseGenerator.getGovernanceWarnings();
        
        for (String warning : warnings) {
            assertTrue(warning.startsWith("Warning:") || warning.contains("Warning"),
                    "Each warning should start with 'Warning:', but found: " + warning);
        }
    }

    @Test
    void shouldReturnValidMainframeStatus() {
        String status = responseGenerator.getRandomMainframeStatus();
        
        assertNotNull(status, "Mainframe status should not be null");
        assertFalse(status.isEmpty(), "Mainframe status should not be empty");
        
        // Should be one of the expected statuses
        boolean validStatus = status.contains("HELD") || 
                             status.contains("QUEUED") || 
                             status.contains("WAITING") || 
                             status.contains("ABEND");
        assertTrue(validStatus, "Status should be one of HELD, QUEUED, WAITING, or ABEND, but was: " + status);
    }

    @Test
    void shouldReturnDifferentMainframeStatuses() {
        // Run multiple times to verify we get variety
        java.util.Set<String> statuses = new java.util.HashSet<>();
        
        for (int i = 0; i < 100; i++) {
            statuses.add(responseGenerator.getRandomMainframeStatus());
        }
        
        // Should have gotten at least 2 different statuses
        assertTrue(statuses.size() >= 2,
                "Should return variety of mainframe statuses, but only found " + statuses.size());
    }

    @Test
    void shouldGenerateValidJobId() {
        String jobId = responseGenerator.generateJobId();
        
        assertNotNull(jobId, "Job ID should not be null");
        assertTrue(jobId.startsWith("JOB"),
                "Job ID should start with 'JOB', but was: " + jobId);
    }

    @Test
    void shouldGenerateUniqueJobIds() {
        String jobId1 = responseGenerator.generateJobId();
        String jobId2 = responseGenerator.generateJobId();
        
        assertNotEquals(jobId1, jobId2,
                "Consecutive job IDs should be unique");
    }

    @Test
    void shouldGenerateValidTransactionId() {
        String txnId = responseGenerator.generateTransactionId();
        
        assertNotNull(txnId, "Transaction ID should not be null");
        assertTrue(txnId.startsWith("TXN"),
                "Transaction ID should start with 'TXN', but was: " + txnId);
    }

    @Test
    void shouldGenerateUniqueTransactionIds() {
        String txnId1 = responseGenerator.generateTransactionId();
        String txnId2 = responseGenerator.generateTransactionId();
        
        assertNotEquals(txnId1, txnId2,
                "Consecutive transaction IDs should be unique");
    }

    private int countOccurrences(String str, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }
}
