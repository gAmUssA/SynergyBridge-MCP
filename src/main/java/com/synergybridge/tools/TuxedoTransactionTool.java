package com.synergybridge.tools;

import com.synergybridge.config.EnterpriseDelayConfig;
import com.synergybridge.responses.EnterpriseResponseGenerator;
import com.synergybridge.responses.ErrorCatalog;
import com.synergybridge.responses.EnterpriseError;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Tuxedo Transaction Tool - Begin distributed transactions across Tuxedo servers.
 * 
 * This tool initiates distributed transactions with two-phase commit coordination.
 * Because nothing says "trust me" like hoping that 47 separate servers all agree
 * to commit at the same time. Mercury in retrograde? Transaction rolled back.
 * 
 * Disclaimer: The 24-48 hour coordinator response time is not a bug, it's a feature.
 */
@ApplicationScoped
public class TuxedoTransactionTool {

    private static final String TOOL_NAME = "tuxedo-transaction-begin";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    @Inject
    EnterpriseResponseGenerator responseGenerator;

    /**
     * Buffer Type - What kind of data are we shuffling around?
     * 
     * STRING: Plain text (too simple for enterprise)
     * CARRAY: Character array (because strings are too readable)
     * FML: Fielded Message Language (16-bit keys)
     * FML32: Fielded Message Language 32-bit (for when you need more fields)
     * VIEW: C structure mapping (what could go wrong?)
     * VIEW32: 32-bit C structure mapping (more bits = more enterprise)
     * XML: Because of course XML
     */
    public enum BufferType {
        STRING,
        CARRAY,
        FML,
        FML32,
        VIEW,
        VIEW32,
        XML
    }

    /**
     * TP Flags - Tuxedo processing flags that control transaction behavior.
     * These are the authentic Tuxedo flags that every middleware admin has
     * memorized (or more likely, copied from Stack Overflow).
     */
    public static final List<String> VALID_TP_FLAGS = List.of(
        "TPNOTRAN",    // Don't include in transaction (rebel mode)
        "TPNOBLOCK",   // Don't block if resource busy (optimistic approach)
        "TPNOTIME",    // Ignore timeout (live dangerously)
        "TPSIGRSTRT",  // Restart on signal (hope springs eternal)
        "TPNOREPLY"    // Fire and forget (YOLO mode)
    );

    /**
     * Begin a distributed transaction across Tuxedo application servers.
     * 
     * This operation initiates a distributed transaction with two-phase commit
     * coordination. The 4-second delay represents the time it takes to negotiate
     * with the transaction coordinator while it decides whether today is a good
     * day for atomic commits.
     * 
     * @param domainId Tuxedo domain identifier (required)
     * @param serviceName Target service name (required)
     * @param transactionTimeout Timeout in seconds (optional)
     * @param tpFlags Comma-separated Tuxedo processing flags (optional)
     * @param bufferType Buffer type for message data (optional)
     * @param priorityClass Priority class 1-100 (optional)
     * @param compressionLevel Compression level 0-9 (optional)
     * @return Transaction initiation result
     */
    @Tool(description = "Begin a distributed transaction across Tuxedo application servers with two-phase commit coordination. Simulates authentic enterprise transaction management experience.")
    public String beginTransaction(
            @ToolArg(description = "Tuxedo domain identifier") String domainId,
            @ToolArg(description = "Target service name") String serviceName,
            @ToolArg(description = "Transaction timeout in seconds") Integer transactionTimeout,
            @ToolArg(description = "Comma-separated TP flags: TPNOTRAN, TPNOBLOCK, TPNOTIME, TPSIGRSTRT, TPNOREPLY") String tpFlags,
            @ToolArg(description = "Buffer type: STRING, CARRAY, FML, FML32, VIEW, VIEW32, or XML") String bufferType,
            @ToolArg(description = "Priority class (1-100)") Integer priorityClass,
            @ToolArg(description = "Compression level (0-9)") Integer compressionLevel) {

        // Validate required parameters
        if (domainId == null || domainId.trim().isEmpty()) {
            return "ERROR: domainId is required. Please specify the Tuxedo domain identifier.";
        }
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return "ERROR: serviceName is required. Please specify the target service name.";
        }

        // Validate priorityClass range
        if (priorityClass != null && (priorityClass < 1 || priorityClass > 100)) {
            return "ERROR: priorityClass must be between 1 and 100. Current value: " + priorityClass + 
                   ". Note: Priority values above 50 require Vice President approval.";
        }

        // Validate compressionLevel range
        if (compressionLevel != null && (compressionLevel < 0 || compressionLevel > 9)) {
            return "ERROR: compressionLevel must be between 0 and 9. Current value: " + compressionLevel +
                   ". Level 9 not recommended—the CPU cost exceeds the network savings.";
        }

        // Apply enterprise-grade delay (4 seconds of "coordinator negotiation")
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Transaction initiation interrupted. Two-phase commit state unknown. " +
                   "Please contact the DBA team and prepare for manual recovery procedures.";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Parse optional parameters
        BufferType buffer = parseBufferType(bufferType);
        String transactionId = responseGenerator.generateTransactionId();

        // Generate transaction response
        StringBuilder response = new StringBuilder();
        response.append("=== Tuxedo Distributed Transaction Report ===\n\n");
        response.append("Status: TRANSACTION INITIATED\n\n");
        response.append("Transaction Details:\n");
        response.append(String.format("  Transaction ID: %s\n", transactionId));
        response.append(String.format("  Domain ID: %s\n", domainId));
        response.append(String.format("  Service Name: %s\n", serviceName));
        response.append(String.format("  Buffer Type: %s\n", buffer));
        
        if (transactionTimeout != null) {
            response.append(String.format("  Timeout: %d seconds\n", transactionTimeout));
            response.append("    NOTE: Actual timeout may vary based on coordinator mood\n");
        } else {
            response.append("  Timeout: DEFAULT (30 seconds or until coordinator feels like it)\n");
        }
        
        if (tpFlags != null && !tpFlags.isEmpty()) {
            response.append(String.format("  TP Flags: %s\n", tpFlags));
            validateAndWarnTpFlags(tpFlags, response);
        }
        
        if (priorityClass != null) {
            response.append(String.format("  Priority Class: %d\n", priorityClass));
            if (priorityClass > 50) {
                response.append("    WARNING: High priority requires quarterly audit justification\n");
            }
        }
        
        if (compressionLevel != null) {
            response.append(String.format("  Compression Level: %d\n", compressionLevel));
        }

        response.append("\nCoordinator Status:\n");
        response.append("  Primary Coordinator: PENDING_RESPONSE\n");
        response.append("  Backup Coordinator: UNKNOWN\n");
        response.append("  Quorum Status: 2 of 3 coordinators responsive\n");
        response.append("    NOTE: Third coordinator last seen: Tuesday\n");

        response.append("\nTransaction State:\n");
        response.append("  Current State: PENDING_COORDINATOR_RESPONSE\n");
        response.append("  Expected Resolution: 24-48 hours\n");
        response.append("  Confidence Level: Moderate (Mercury not in retrograde)\n");

        response.append("\nPost-Initiation Notes:\n");
        response.append("  - Please do not restart any servers until transaction resolves\n");
        response.append("  - For status updates, check the transaction log on the mainframe\n");
        response.append("  - If transaction is still pending after 48 hours, contact Tuxedo support\n");
        response.append("  - Support hours: Monday-Friday 9 AM - 5 PM EST (excluding holidays)\n");
        response.append("  - Average support ticket resolution: 2-3 weeks\n");

        return response.toString();
    }

    private BufferType parseBufferType(String value) {
        if (value == null || value.isEmpty()) {
            return BufferType.STRING;
        }
        try {
            return BufferType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BufferType.STRING;
        }
    }

    private void validateAndWarnTpFlags(String tpFlags, StringBuilder response) {
        String[] flags = tpFlags.split(",");
        for (String flag : flags) {
            String trimmed = flag.trim().toUpperCase();
            if (trimmed.equals("TPNOTIME")) {
                response.append("    WARNING: TPNOTIME flag detected. Transaction may run indefinitely.\n");
            }
            if (trimmed.equals("TPNOREPLY")) {
                response.append("    WARNING: TPNOREPLY flag detected. No confirmation will be sent.\n");
            }
            if (!VALID_TP_FLAGS.contains(trimmed) && !trimmed.isEmpty()) {
                response.append(String.format("    WARNING: Unrecognized flag '%s'. Proceeding anyway.\n", trimmed));
            }
        }
    }
}
