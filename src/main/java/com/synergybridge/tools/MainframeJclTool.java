package com.synergybridge.tools;

import com.synergybridge.config.EnterpriseDelayConfig;
import com.synergybridge.responses.EnterpriseResponseGenerator;
import com.synergybridge.responses.ErrorCatalog;
import com.synergybridge.responses.EnterpriseError;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

/**
 * Mainframe JCL Submit Tool - Submit Jobs Like It's 1979!
 * 
 * "JCL: Because why use one line when 47 will do?"
 * 
 * This tool simulates submitting JCL jobs to the mainframe batch processing queue
 * with full JES2/JES3 compatibility. Features authentic job statuses including
 * the dreaded ABEND S0C7.
 * 
 * Fun Fact: The last person who fully understood JCL retired in 1997.
 * We've been guessing ever since.
 */
@ApplicationScoped
public class MainframeJclTool {

    private static final String TOOL_NAME = "mainframe-jcl-submit";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    @Inject
    EnterpriseResponseGenerator responseGenerator;

    /**
     * Job Class - Single letter A-Z for job classification.
     * Each letter means something different on every mainframe.
     */
    public enum JobClass {
        A, B, C, D, E, F, G, H, I, J, K, L, M, 
        N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    /**
     * Message Class - Where your job output goes (also A-Z).
     * Good luck remembering which one prints to the correct printer.
     */
    public enum MsgClass {
        A, B, C, D, E, F, G, H, I, J, K, L, M, 
        N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    /**
     * Submit a JCL job to the mainframe batch processing queue.
     * 
     * This operation submits your carefully crafted JCL to the mainframe,
     * where it will join the queue of 847 other jobs waiting for their
     * turn on the initiator. Results may vary. Tape mounts may be required.
     * 
     * @param jclSource JCL statements (//JOB card required, obviously)
     * @param jobClass Job class A-Z (consult your operations manual from 1985)
     * @param msgClass Message class A-Z (where your output goes to die)
     * @param priorityLevel Priority 0-15 (15 is highest, reserved for management)
     * @param accountingInfo Chargeback code (someone has to pay for those CPU cycles)
     * @param notifyUser TSO user ID for job completion notification
     * @param regionSizeMB Region size in megabytes (be conservative)
     * @param timeLimitMinutes CPU time limit (mainframe time is expensive)
     * @param tapeRetentionDays How long to keep those tapes
     * @return Job submission status with ID and queue position
     */
    @Tool(description = "Submit a JCL job to the mainframe batch processing queue with full JES2/JES3 compatibility. Returns job ID and status.")
    public String submitJcl(
            @ToolArg(description = "JCL statements (//JOB card required)") String jclSource,
            @ToolArg(description = "Job class: single letter A-Z") String jobClass,
            @ToolArg(description = "Message class: single letter A-Z") String msgClass,
            @ToolArg(description = "Priority level 0-15 (15 is highest)") Integer priorityLevel,
            @ToolArg(description = "Accounting info for chargeback") String accountingInfo,
            @ToolArg(description = "TSO user ID for notification") String notifyUser,
            @ToolArg(description = "Region size in megabytes") Integer regionSizeMB,
            @ToolArg(description = "CPU time limit in minutes") Integer timeLimitMinutes,
            @ToolArg(description = "Tape retention in days") Integer tapeRetentionDays) {

        // Validate required parameter
        if (jclSource == null || jclSource.trim().isEmpty()) {
            return "ERROR: jclSource is required. Please provide the JCL statements.\n" +
                   "Hint: Every JCL must start with //JOBNAME JOB...";
        }

        // Validate JOB card presence (sort of)
        if (!jclSource.contains("//") || !jclSource.toUpperCase().contains("JOB")) {
            return "ERROR: Invalid JCL. //JOB card is required.\n" +
                   "JES2 Syntax: //jobname JOB (accounting),'programmer name'\n" +
                   "Please consult the JCL Reference Manual (3rd floor, cabinet 7).";
        }

        // Validate priority level if provided
        if (priorityLevel != null && (priorityLevel < 0 || priorityLevel > 15)) {
            return "ERROR: priorityLevel must be between 0 and 15.\n" +
                   "Note: Priority 15 requires director-level approval form JCL-847-B.";
        }

        // Apply enterprise-grade delay (5 seconds of "submission")
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Job submission interrupted. JES2 connection lost.\n" +
                   "Your JCL may or may not have been submitted. Please check SDSF.";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Generate job ID
        String jobId = responseGenerator.generateJobId();
        
        // Get random status
        String status = responseGenerator.getRandomMainframeStatus();

        // Parse optional parameters with defaults
        JobClass jc = parseJobClass(jobClass);
        MsgClass mc = parseMsgClass(msgClass);
        int priority = (priorityLevel != null) ? priorityLevel : 8;

        // Build response
        StringBuilder response = new StringBuilder();
        response.append("=== Mainframe Job Submission Report ===\n\n");
        response.append("Job Submission Details:\n");
        response.append(String.format("  Job ID: %s\n", jobId));
        response.append(String.format("  Job Class: %s\n", jc));
        response.append(String.format("  Message Class: %s\n", mc));
        response.append(String.format("  Priority: %d\n", priority));
        
        if (accountingInfo != null && !accountingInfo.isEmpty()) {
            response.append(String.format("  Accounting: %s\n", accountingInfo));
        }
        if (notifyUser != null && !notifyUser.isEmpty()) {
            response.append(String.format("  Notify User: %s\n", notifyUser));
        }
        if (regionSizeMB != null) {
            response.append(String.format("  Region Size: %d MB\n", regionSizeMB));
        }
        if (timeLimitMinutes != null) {
            response.append(String.format("  Time Limit: %d minutes\n", timeLimitMinutes));
        }
        if (tapeRetentionDays != null) {
            response.append(String.format("  Tape Retention: %d days\n", tapeRetentionDays));
        }

        response.append(String.format("\nCurrent Status: %s\n", status));

        response.append("\nJob Tracking Information:\n");
        response.append("  - Use SDSF to monitor job progress\n");
        response.append("  - Output will be available in SYSOUT when complete\n");
        response.append("  - For tape mount requests, contact the operator\n");
        response.append("  - Estimated completion: Unknown (depends on batch window)\n");

        if (status.contains("ABEND")) {
            response.append("\n*** ATTENTION ***\n");
            response.append("Job has ABENDed. Please check the dump.\n");
            response.append("Common causes: Data exception, region too small, or cosmic rays.\n");
        }

        return response.toString();
    }

    private JobClass parseJobClass(String value) {
        if (value == null || value.isEmpty()) {
            return JobClass.A;
        }
        try {
            return JobClass.valueOf(value.toUpperCase().substring(0, 1));
        } catch (IllegalArgumentException e) {
            return JobClass.A;
        }
    }

    private MsgClass parseMsgClass(String value) {
        if (value == null || value.isEmpty()) {
            return MsgClass.A;
        }
        try {
            return MsgClass.valueOf(value.toUpperCase().substring(0, 1));
        } catch (IllegalArgumentException e) {
            return MsgClass.A;
        }
    }
}
