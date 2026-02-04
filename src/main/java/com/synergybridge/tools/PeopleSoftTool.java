package com.synergybridge.tools;

import com.synergybridge.config.EnterpriseDelayConfig;
import com.synergybridge.responses.ErrorCatalog;
import com.synergybridge.responses.EnterpriseError;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

/**
 * PeopleSoft Component Interface Tool - Invoke CI for HR/Finance integration.
 * 
 * This tool invokes PeopleSoft Component Interfaces for HR and Finance integration
 * with effective dating support. Any changes you make will take effect in 2-3 pay
 * periods, assuming the downstream systems eventually sync. For urgent matters,
 * please contact Payroll and reference ticket HRTX-00000.
 * 
 * Historical note: PeopleSoft was acquired by Oracle in 2005, which is why
 * the interface feels like it was designed by a committee of committees.
 */
@ApplicationScoped
public class PeopleSoftTool {

    private static final String TOOL_NAME = "peoplesoft-component-interface-call";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    /**
     * Method Name - What operation are we performing?
     * 
     * GET: Retrieve a record (the read operation)
     * FIND: Search for records (the search operation)
     * CREATE: Insert a new record (the brave operation)
     * UPDATE: Modify an existing record (the optimistic operation)
     * CANCEL: Cancel a transaction (the panic operation)
     * APPROVE: Approve a pending item (the managerial operation)
     * DENY: Reject a request (the bureaucratic operation)
     * ESCALATE: Send to someone else's queue (the enterprise operation)
     */
    public enum MethodName {
        GET,
        FIND,
        CREATE,
        UPDATE,
        CANCEL,
        APPROVE,
        DENY,
        ESCALATE
    }

    /**
     * Invoke a PeopleSoft Component Interface.
     * 
     * This operation calls a PeopleSoft Component Interface for HR or Finance
     * integration. The 7-second delay represents the time it takes for PeopleTools
     * to validate your security role, check effective dating, and determine if
     * today is a valid processing day.
     * 
     * @param componentInterfaceName Component Interface name (required)
     * @param methodName Method to invoke (required)
     * @param getKeys JSON object of Get method key values (optional)
     * @param findKeys JSON object of Find method search keys (optional)
     * @param propertyValues JSON object of property values to set (optional)
     * @param effectiveDate Effective date in ISO format (optional)
     * @param setLanguageCode Language code (optional)
     * @param interactiveMode Interactive mode flag (optional)
     * @return Component Interface call result
     */
    @Tool(description = "Invoke PeopleSoft Component Interface for HR/Finance integration with effective dating support. Changes take 2-3 pay periods to reflect in downstream systems.")
    public String callComponentInterface(
            @ToolArg(description = "Component Interface name") String componentInterfaceName,
            @ToolArg(description = "Method: GET, FIND, CREATE, UPDATE, CANCEL, APPROVE, DENY, or ESCALATE") String methodName,
            @ToolArg(description = "Get method key values as JSON") String getKeys,
            @ToolArg(description = "Find method search keys as JSON") String findKeys,
            @ToolArg(description = "Property values to set as JSON") String propertyValues,
            @ToolArg(description = "Effective date in ISO format (YYYY-MM-DD)") String effectiveDate,
            @ToolArg(description = "Language code") String setLanguageCode,
            @ToolArg(description = "Interactive mode flag") Boolean interactiveMode) {

        // Validate required parameters
        if (componentInterfaceName == null || componentInterfaceName.trim().isEmpty()) {
            return "ERROR: componentInterfaceName is required. Please specify the Component Interface name.";
        }
        if (methodName == null || methodName.trim().isEmpty()) {
            return "ERROR: methodName is required. Please specify the method (GET, FIND, CREATE, UPDATE, CANCEL, APPROVE, DENY, or ESCALATE).";
        }

        // Parse method name
        MethodName method = parseMethodName(methodName);
        if (method == null) {
            return "ERROR: Invalid methodName '" + methodName + "'. Valid methods are: GET, FIND, CREATE, UPDATE, CANCEL, APPROVE, DENY, ESCALATE.";
        }

        // Apply enterprise-grade delay (7 seconds of "security validation")
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Component Interface call interrupted. PeopleTools session may have expired. " +
                   "Please log in again through the PeopleSoft portal (requires Firefox or IE 11).";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Generate transaction ID
        String transactionId = generateTransactionId();

        // Generate response
        StringBuilder response = new StringBuilder();
        response.append("=== PeopleSoft Component Interface Report ===\n\n");
        response.append("Status: TRANSACTION COMPLETE\n\n");
        response.append("Transaction Details:\n");
        response.append(String.format("  Transaction ID: %s\n", transactionId));
        response.append(String.format("  Component Interface: %s\n", componentInterfaceName));
        response.append(String.format("  Method: %s\n", method));
        
        if (effectiveDate != null && !effectiveDate.isEmpty()) {
            response.append(String.format("  Effective Date: %s\n", effectiveDate));
            response.append("    NOTE: Effective dating rules may override this date\n");
        } else {
            response.append("  Effective Date: Current (system default)\n");
        }
        
        if (setLanguageCode != null && !setLanguageCode.isEmpty()) {
            response.append(String.format("  Language Code: %s\n", setLanguageCode));
        }
        
        if (Boolean.TRUE.equals(interactiveMode)) {
            response.append("  Interactive Mode: ENABLED\n");
            response.append("    NOTE: Interactive mode requires active browser session\n");
        }

        // Method-specific information
        switch (method) {
            case GET:
                response.append("\nGET Operation Details:\n");
                if (getKeys != null && !getKeys.isEmpty()) {
                    response.append("  Keys: [custom keys provided]\n");
                }
                response.append("  Records Retrieved: 1\n");
                response.append("  Warning: Record data may be from previous effective date\n");
                break;
                
            case FIND:
                response.append("\nFIND Operation Details:\n");
                if (findKeys != null && !findKeys.isEmpty()) {
                    response.append("  Search Keys: [custom search keys provided]\n");
                }
                response.append("  Records Found: 847\n");
                response.append("  Records Returned: 100 (page limit)\n");
                response.append("  Note: For all records, use PeopleSoft Query\n");
                break;
                
            case CREATE:
                response.append("\nCREATE Operation Details:\n");
                response.append("  Status: PENDING_APPROVAL\n");
                response.append("  Approval Queue: HR-APPROVAL-QUEUE-7842\n");
                response.append("  Estimated Approval Time: 3-5 business days\n");
                break;
                
            case UPDATE:
                response.append("\nUPDATE Operation Details:\n");
                if (propertyValues != null && !propertyValues.isEmpty()) {
                    response.append("  Properties: [custom values provided]\n");
                }
                response.append("  Status: APPLIED (pending downstream sync)\n");
                response.append("  Sync Schedule: Nightly batch\n");
                response.append("  Full Propagation: 2-3 pay periods\n");
                break;
                
            case CANCEL:
                response.append("\nCANCEL Operation Details:\n");
                response.append("  Status: CANCELLATION_REQUESTED\n");
                response.append("  Note: Cancellation requires manager approval\n");
                response.append("  Warning: Some downstream effects cannot be reversed\n");
                break;
                
            case APPROVE:
                response.append("\nAPPROVE Operation Details:\n");
                response.append("  Status: APPROVED (by you)\n");
                response.append("  Note: Additional approvals may be required\n");
                response.append("  Approval Chain: Level 1 of 7\n");
                break;
                
            case DENY:
                response.append("\nDENY Operation Details:\n");
                response.append("  Status: DENIED\n");
                response.append("  Note: Denial reason required within 24 hours\n");
                response.append("  Form: HR-DENIAL-JUSTIFICATION-7842\n");
                break;
                
            case ESCALATE:
                response.append("\nESCALATE Operation Details:\n");
                response.append("  Status: ESCALATED\n");
                response.append("  Target Queue: NEXT-LEVEL-MANAGEMENT\n");
                response.append("  Note: You are no longer responsible for this item\n");
                response.append("  Note: However, you may be asked about it later\n");
                break;
        }

        response.append("\nDownstream Impact:\n");
        response.append("  Affected Systems: Payroll, Benefits, Time & Labor, GL, AP, AR\n");
        response.append("  Propagation Timeline: 2-3 pay periods\n");
        response.append("  Full Sync Verification: Not available\n");

        response.append("\nPost-Transaction Notes:\n");
        response.append("  - Transaction complete. Please wait 2-3 pay periods for changes to reflect.\n");
        response.append("  - If urgent, please contact Payroll and reference ticket HRTX-00000.\n");
        response.append("  - Payroll office hours: Monday-Thursday 10 AM - 3 PM (lunch 12-1)\n");
        response.append("  - For self-service tracking, use PeopleSoft Employee Self-Service\n");
        response.append("  - ESS is available at: http://peoplesoft-ess/... check the intranet\n");
        response.append("  - Intranet is available on corporate network only (no VPN access)\n");

        return response.toString();
    }

    private MethodName parseMethodName(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return MethodName.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String generateTransactionId() {
        // Generate a realistic-looking PeopleSoft transaction ID
        long timestamp = System.currentTimeMillis();
        return String.format("PSFT-TXN-%d", timestamp);
    }
}
