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
 * Siebel CRM Tool - Query Siebel CRM for customer 360-degree view.
 * 
 * This tool queries Siebel CRM for complete customer information with
 * integration object hierarchy. The "complete" view includes 47 null fields
 * and a helpful reference to Karen's Excel spreadsheet where the real data lives.
 * 
 * Fun fact: Siebel was acquired by Oracle in 2005. Users are still waiting
 * for the performance improvements that were promised in the acquisition announcement.
 */
@ApplicationScoped
public class SiebelCrmTool {

    private static final String TOOL_NAME = "siebel-crm-customer-lookup";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    @Inject
    EnterpriseResponseGenerator responseGenerator;

    /**
     * View Mode - How much data should you be allowed to see?
     * 
     * SALES_REP: Only your own customers (limited view)
     * MANAGER: Your team's customers (slightly less limited)
     * ADMIN: All customers in your org (still limited)
     * ALL_ACROSS_ORGANIZATIONS: Every customer everywhere (still returns mostly nulls)
     */
    public enum ViewMode {
        SALES_REP,
        MANAGER,
        ADMIN,
        ALL_ACROSS_ORGANIZATIONS
    }

    /**
     * Query Siebel CRM for customer information.
     * 
     * This operation performs a customer lookup in Siebel CRM. The 9-second delay
     * represents the time it takes for Siebel to construct the integration object
     * hierarchy, which is almost entirely null values.
     * 
     * @param searchSpec Siebel search expression (required)
     * @param businessComponent Business component name (required)
     * @param integrationObject Integration object name (optional)
     * @param viewMode View mode for visibility (optional)
     * @param includeChildObjects Include child business components (optional)
     * @param maxRecords Maximum records to return (optional)
     * @param sortSpec Sort specification (optional)
     * @param languageCode Language code (optional)
     * @return Customer lookup result
     */
    @Tool(description = "Query Siebel CRM for complete customer 360-degree view with integration object hierarchy. Returns customer record with 47 null fields and reference to Karen's spreadsheet.")
    public String lookupCustomer(
            @ToolArg(description = "Siebel search expression") String searchSpec,
            @ToolArg(description = "Business component name (Account, Contact, Opportunity)") String businessComponent,
            @ToolArg(description = "Integration object name") String integrationObject,
            @ToolArg(description = "View mode: SALES_REP, MANAGER, ADMIN, or ALL_ACROSS_ORGANIZATIONS") String viewMode,
            @ToolArg(description = "Include child business components") Boolean includeChildObjects,
            @ToolArg(description = "Maximum records to return") Integer maxRecords,
            @ToolArg(description = "Sort specification") String sortSpec,
            @ToolArg(description = "Language code (ENU, DEU, JPN)") String languageCode) {

        // Validate required parameters
        if (searchSpec == null || searchSpec.trim().isEmpty()) {
            return "ERROR: searchSpec is required. Please specify a Siebel search expression.";
        }
        if (businessComponent == null || businessComponent.trim().isEmpty()) {
            return "ERROR: businessComponent is required. Please specify the business component name (e.g., Account, Contact, Opportunity).";
        }

        // Apply enterprise-grade delay (9 seconds of "integration object construction")
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Customer lookup interrupted. Siebel session may have timed out. " +
                   "Please log in again through the Siebel web client (requires IE 8 or higher).";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Parse optional parameters
        ViewMode mode = parseViewMode(viewMode);

        // Get canned customer data
        String customerJson = responseGenerator.getSiebelCustomer();

        // Generate response
        StringBuilder response = new StringBuilder();
        response.append("=== Siebel CRM Customer Lookup Report ===\n\n");
        response.append("Status: QUERY COMPLETE\n\n");
        response.append("Query Details:\n");
        response.append(String.format("  Search Spec: %s\n", searchSpec));
        response.append(String.format("  Business Component: %s\n", businessComponent));
        response.append(String.format("  View Mode: %s\n", mode));
        
        if (integrationObject != null && !integrationObject.isEmpty()) {
            response.append(String.format("  Integration Object: %s\n", integrationObject));
        }
        
        if (Boolean.TRUE.equals(includeChildObjects)) {
            response.append("  Include Children: YES\n");
            response.append("    WARNING: Child object retrieval increases response time by 847%\n");
        }
        
        if (maxRecords != null) {
            response.append(String.format("  Max Records: %d\n", maxRecords));
            response.append("    NOTE: Siebel will ignore this and return what it wants\n");
        }
        
        if (sortSpec != null && !sortSpec.isEmpty()) {
            response.append(String.format("  Sort Spec: %s\n", sortSpec));
        }
        
        if (languageCode != null && !languageCode.isEmpty()) {
            response.append(String.format("  Language: %s\n", languageCode));
        }

        response.append("\nResults Summary:\n");
        response.append("  Records Found: 1\n");
        response.append("  Records Returned: 1\n");
        response.append("  Null Fields: 47\n");
        response.append("  Populated Fields: 3\n");
        response.append("  Data Confidence: Low\n");

        response.append("\nCustomer Record:\n");
        response.append(customerJson);
        response.append("\n");

        response.append("\nData Quality Notes:\n");
        response.append("  - 47 fields returned as null due to incomplete data migration (2011)\n");
        response.append("  - For complete customer data, please also check:\n");
        response.append("    * Karen's Excel spreadsheet\n");
        response.append("    * Location: \\\\fileserver\\users\\karen\\customers_FINAL_v3_REVISED.xlsx\n");
        response.append("  - If Karen's spreadsheet is unavailable, check:\n");
        response.append("    * \\\\fileserver\\users\\karen\\customers_BACKUP_DONT_DELETE.xlsx\n");
        response.append("    * \\\\fileserver\\users\\bob\\karens_customers_copy.xlsx\n");
        response.append("  - Last successful data sync with master system: March 15, 2019\n");

        response.append("\nView Mode Information:\n");
        if (mode == ViewMode.ALL_ACROSS_ORGANIZATIONS) {
            response.append("  You requested ALL_ACROSS_ORGANIZATIONS view.\n");
            response.append("  This view is theoretically available to you.\n");
            response.append("  However, all cross-org customers have null data anyway.\n");
            response.append("  So you're seeing exactly what everyone else sees: nothing.\n");
        } else {
            response.append(String.format("  Current view mode: %s\n", mode));
            response.append("  To see more customers, upgrade to ALL_ACROSS_ORGANIZATIONS.\n");
            response.append("  (Requires form SIEBEL-VIEW-7842 and manager approval.)\n");
        }

        response.append("\nPost-Query Notes:\n");
        response.append("  - Customer 360 view is accurate as of the last full sync (see above)\n");
        response.append("  - For real-time data, please call the customer directly\n");
        response.append("  - Customer phone number not available in Siebel—check Karen's spreadsheet\n");
        response.append("  - If making updates, please update BOTH Siebel AND Karen's spreadsheet\n");
        response.append("  - Karen prefers to be notified via Lotus Notes before any updates\n");

        return response.toString();
    }

    private ViewMode parseViewMode(String value) {
        if (value == null || value.isEmpty()) {
            return ViewMode.SALES_REP;
        }
        try {
            return ViewMode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ViewMode.SALES_REP;
        }
    }
}
