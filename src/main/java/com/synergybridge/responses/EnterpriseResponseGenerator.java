package com.synergybridge.responses;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * EnterpriseResponseGenerator - Your one-stop shop for authentic enterprise responses.
 * 
 * "Because nothing says 'enterprise-grade' like hardcoded responses loaded from resource files."
 * 
 * This generator provides canned responses that simulate the authentic experience of
 * working with legacy enterprise systems, complete with FILLER fields, null values,
 * and references to Karen's spreadsheet.
 */
@ApplicationScoped
public class EnterpriseResponseGenerator {

    private static final String CANNED_RESPONSES_PATH = "canned-responses/";

    /**
     * Returns the canonical COBOL copybook transformation output.
     * 
     * Features authentic FILLER fields that no one can explain and a helpful
     * reference to mainframe documentation that may or may not exist.
     * 
     * @return JSON string representing the COBOL output
     */
    public String getCobolOutput() {
        return loadResource("cobol-output.json");
    }

    /**
     * Returns the Siebel CRM customer record.
     * 
     * Contains 47 null fields, as is tradition, plus a helpful note about where
     * the actual data might be found (hint: it's on Karen's desktop).
     * 
     * @return JSON string representing the Siebel customer
     */
    public String getSiebelCustomer() {
        return loadResource("siebel-customer.json");
    }

    /**
     * Returns all 147 SOA governance warnings.
     * 
     * These warnings represent years of accumulated policy wisdom, including
     * suggestions to migrate to microservices that have been "pending" since 2015.
     * 
     * @return List of exactly 147 warning messages
     */
    public List<String> getGovernanceWarnings() {
        String content = loadResource("governance-warnings.txt");
        if (content == null || content.isEmpty()) {
            return getDefaultGovernanceWarnings();
        }
        
        List<String> warnings = new ArrayList<>();
        for (String line : content.split("\\r?\\n")) {
            if (!line.trim().isEmpty()) {
                warnings.add(line);
            }
        }
        return warnings;
    }

    /**
     * Returns a random mainframe job status.
     * 
     * Because in the mainframe world, you never know if your job will run today,
     * tomorrow, or end in an ABEND S0C7.
     * 
     * @return A randomly selected mainframe status
     */
    public String getRandomMainframeStatus() {
        String content = loadResource("mainframe-statuses.txt");
        if (content == null || content.isEmpty()) {
            return getDefaultMainframeStatuses().get(0);
        }
        
        List<String> statuses = new ArrayList<>();
        for (String line : content.split("\\r?\\n")) {
            if (!line.trim().isEmpty()) {
                statuses.add(line);
            }
        }
        
        if (statuses.isEmpty()) {
            return "ABEND S0C7 - CONTACT SYSTEMS PROGRAMMING";
        }
        
        int index = ThreadLocalRandom.current().nextInt(statuses.size());
        return statuses.get(index);
    }

    /**
     * Generates a random mainframe job ID.
     * 
     * Format: JOB{5-digit number} - authentic z/OS job identifier format.
     * 
     * @return A randomly generated job ID
     */
    public String generateJobId() {
        int jobNumber = ThreadLocalRandom.current().nextInt(10000, 99999);
        return "JOB" + jobNumber;
    }

    /**
     * Generates a random Tuxedo transaction ID.
     * 
     * Format: TXN-{timestamp}-{random} - looks official enough.
     * 
     * @return A randomly generated transaction ID
     */
    public String generateTransactionId() {
        long timestamp = System.currentTimeMillis();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String.format("TXN-%d-%d", timestamp, random);
    }

    /**
     * Loads a resource file from the canned-responses directory.
     * 
     * @param filename The name of the file to load
     * @return The content of the file, or null if not found
     */
    private String loadResource(String filename) {
        String path = CANNED_RESPONSES_PATH + filename;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[ENTERPRISE WARNING] Resource not found: " + path + 
                    ". Please contact your system administrator (expected response time: 3-5 business days).");
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            System.err.println("[ENTERPRISE ERROR] Failed to load resource: " + path + 
                ". Error: " + e.getMessage() + ". Have you tried turning it off and on again?");
            return null;
        }
    }

    /**
     * Default governance warnings in case the file can't be loaded.
     * (This should never happen in a properly configured enterprise environment.)
     */
    private List<String> getDefaultGovernanceWarnings() {
        List<String> warnings = new ArrayList<>();
        warnings.add("Warning: Consider migrating to microservices (issued since 2015)");
        warnings.add("Warning: SOAP 1.1 detected. SOAP 1.2 recommended since 2007");
        warnings.add("Warning: Namespace prefix 'ns1' is not descriptive");
        warnings.add("Warning: Service name does not follow naming convention CORP-[DIVISION]-[APP]-[VER]-Service");
        // Add enough to have some warnings, but not 147 - the file should be used
        for (int i = 5; i <= 147; i++) {
            warnings.add("Warning: Generic governance issue #" + i);
        }
        return warnings;
    }

    /**
     * Default mainframe statuses in case the file can't be loaded.
     */
    private List<String> getDefaultMainframeStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add("HELD - AWAITING TAPE MOUNT");
        statuses.add("QUEUED - POSITION 847");
        statuses.add("WAITING FOR INITIATOR");
        statuses.add("ABEND S0C7 - CONTACT SYSTEMS PROGRAMMING");
        return statuses;
    }
}
