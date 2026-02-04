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
 * Crystal Reports Tool - Generate enterprise reports with extreme patience.
 * 
 * This tool generates Crystal Reports documents with enterprise data source binding
 * and scheduled distribution. The 12-second delay is just the beginning—actual
 * report completion is measured in business days, not seconds.
 * 
 * Historical note: Crystal Reports was first released in 1991. The reports
 * submitted that year are still generating.
 */
@ApplicationScoped
public class CrystalReportsTool {

    private static final String TOOL_NAME = "crystal-reports-generate";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    /**
     * Output Format - How would you like your report served?
     * 
     * PDF: Portable Document Format (most common)
     * RTF: Rich Text Format (for the Word crowd)
     * XLS: Excel format (so managers can "adjust" the numbers)
     * DOC: Word document (for those who fear PDFs)
     * RPT: Native Crystal format (requires Crystal viewer)
     * PRINTER_LPT1: Direct to parallel port (requires time machine)
     * FAX: Facsimile transmission (requires functioning fax machine, good luck)
     */
    public enum OutputFormat {
        PDF,
        RTF,
        XLS,
        DOC,
        RPT,
        PRINTER_LPT1,
        FAX
    }

    /**
     * Page Orientation - Which way should the paper go?
     * 
     * PORTRAIT: Vertical (tall and narrow)
     * LANDSCAPE: Horizontal (wide and short)
     * CONFUSED: Alternating orientation per page (for that extra challenge)
     */
    public enum PageOrientation {
        PORTRAIT,
        LANDSCAPE,
        CONFUSED
    }

    /**
     * Generate a Crystal Reports document.
     * 
     * This operation queues a report for generation with the specified template
     * and parameters. The 12-second delay represents the time it takes for
     * Crystal Reports to realize you've asked it to do something. Actual
     * report completion is estimated at 4-6 business days.
     * 
     * @param reportTemplatePath Path to the .rpt template file (required)
     * @param outputFormat Output format type (optional)
     * @param dataSourceOdbc ODBC connection string (optional)
     * @param parameterValues JSON string of report parameter values (optional)
     * @param subreportLinks JSON array of subreport linking configuration (optional)
     * @param pageOrientation Page orientation (optional)
     * @param watermarkText Watermark text overlay (optional)
     * @param emailOnCompletion Lotus Notes email address for notification (optional)
     * @return Report generation result
     */
    @Tool(description = "Generate Crystal Reports document with enterprise data source binding and scheduled distribution. The longest-running tool in the suite with 12-second initial delay and 4-6 business day completion time.")
    public String generateReport(
            @ToolArg(description = "Path to the .rpt template file") String reportTemplatePath,
            @ToolArg(description = "Output format: PDF, RTF, XLS, DOC, RPT, PRINTER_LPT1, or FAX") String outputFormat,
            @ToolArg(description = "ODBC connection string for data source") String dataSourceOdbc,
            @ToolArg(description = "Report parameter values as JSON") String parameterValues,
            @ToolArg(description = "Subreport linking configuration as JSON array") String subreportLinks,
            @ToolArg(description = "Page orientation: PORTRAIT, LANDSCAPE, or CONFUSED") String pageOrientation,
            @ToolArg(description = "Watermark text overlay") String watermarkText,
            @ToolArg(description = "Lotus Notes email address for completion notification") String emailOnCompletion) {

        // Validate required parameters
        if (reportTemplatePath == null || reportTemplatePath.trim().isEmpty()) {
            return "ERROR: reportTemplatePath is required. Please specify the path to the .rpt template file.";
        }

        // Validate .rpt extension (because Crystal Reports is picky)
        if (!reportTemplatePath.toLowerCase().endsWith(".rpt")) {
            return "ERROR: reportTemplatePath must end with .rpt extension. Crystal Reports refuses to " +
                   "acknowledge any other file format. Please rename your file or submit a change request " +
                   "to the Architecture Review Board for format exception approval (estimated: 6-8 weeks).";
        }

        // Apply enterprise-grade delay (12 seconds - the crown jewel of enterprise delays)
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Report generation interrupted. Crystal Reports engine may be unstable. " +
                   "Please restart the report server and wait 15 minutes for re-initialization.";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Parse optional parameters
        OutputFormat format = parseOutputFormat(outputFormat);
        PageOrientation orientation = parsePageOrientation(pageOrientation);
        String jobId = generateReportJobId();

        // Generate report response
        StringBuilder response = new StringBuilder();
        response.append("=== Crystal Reports Generation Report ===\n\n");
        response.append("Status: REPORT QUEUED\n\n");
        response.append("Job Details:\n");
        response.append(String.format("  Job ID: %s\n", jobId));
        response.append(String.format("  Template: %s\n", reportTemplatePath));
        response.append(String.format("  Output Format: %s\n", format));
        response.append(String.format("  Page Orientation: %s\n", orientation));
        
        if (dataSourceOdbc != null && !dataSourceOdbc.isEmpty()) {
            response.append(String.format("  Data Source: %s\n", dataSourceOdbc));
            response.append("    NOTE: ODBC connection will be attempted at report generation time\n");
            response.append("    NOTE: If connection fails, report will silently generate with no data\n");
        } else {
            response.append("  Data Source: DEFAULT (last known working connection from 2014)\n");
        }
        
        if (parameterValues != null && !parameterValues.isEmpty()) {
            response.append("  Parameters: [custom parameters provided]\n");
        }
        
        if (subreportLinks != null && !subreportLinks.isEmpty()) {
            response.append("  Subreports: [subreport configuration provided]\n");
            response.append("    WARNING: Subreports increase generation time exponentially\n");
        }
        
        if (watermarkText != null && !watermarkText.isEmpty()) {
            response.append(String.format("  Watermark: \"%s\"\n", watermarkText));
        }

        // Output format specific warnings
        if (format == OutputFormat.PRINTER_LPT1) {
            response.append("\nPRINTER_LPT1 Warning:\n");
            response.append("  - Direct printer output requires parallel port connection\n");
            response.append("  - Please ensure printer is powered on and connected via LPT1\n");
            response.append("  - If using USB-to-Parallel adapter, results may vary\n");
            response.append("  - Print queue: HELD (awaiting paper jam resolution)\n");
        }
        
        if (format == OutputFormat.FAX) {
            response.append("\nFAX Output Warning:\n");
            response.append("  - Fax transmission requires active phone line\n");
            response.append("  - Long distance charges may apply\n");
            response.append("  - Fax cover sheet will be auto-generated (cannot be disabled)\n");
            response.append("  - Recipient fax number extracted from report parameter 'FAX_NUM'\n");
            response.append("  - If FAX_NUM not provided, report will be faxed to IT department\n");
        }

        if (orientation == PageOrientation.CONFUSED) {
            response.append("\nCONFUSED Orientation Warning:\n");
            response.append("  - Pages will alternate between portrait and landscape\n");
            response.append("  - This is by design (per 2007 executive request)\n");
            response.append("  - Printing double-sided not recommended\n");
        }

        response.append("\nEstimated Completion:\n");
        response.append("  Time to Queue: COMPLETE (this step)\n");
        response.append("  Time to Process: 4-6 business days\n");
        response.append("  Time to Review: Pending manager availability\n");

        if (emailOnCompletion != null && !emailOnCompletion.isEmpty()) {
            response.append(String.format("\nNotification:\n"));
            response.append(String.format("  Email: %s\n", emailOnCompletion));
            response.append("  NOTE: Please check your Lotus Notes inbox\n");
            response.append("  NOTE: Also check Junk Mail folder (Lotus Notes filtering is... creative)\n");
            response.append("  NOTE: If email not received within 7 business days, check with Brenda\n");
        } else {
            response.append("\nNotification:\n");
            response.append("  Email notifications not configured\n");
            response.append("  Please check report server manually\n");
            response.append("  Report server URL: Ask Brenda (she has it bookmarked)\n");
        }

        response.append("\nPost-Queue Notes:\n");
        response.append("  - Report is now in the generation queue\n");
        response.append("  - Current queue position: 847\n");
        response.append("  - Report server is processing requests from last Tuesday\n");
        response.append("  - For urgent reports, please submit form CR-URGENT-7842\n");
        response.append("  - Urgent form requires Director signature and VP notification\n");
        response.append("  - \"Urgent\" processing reduces wait time to 2-3 business days\n");

        return response.toString();
    }

    private OutputFormat parseOutputFormat(String value) {
        if (value == null || value.isEmpty()) {
            return OutputFormat.PDF;
        }
        try {
            return OutputFormat.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OutputFormat.PDF;
        }
    }

    private PageOrientation parsePageOrientation(String value) {
        if (value == null || value.isEmpty()) {
            return PageOrientation.PORTRAIT;
        }
        try {
            return PageOrientation.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PageOrientation.PORTRAIT;
        }
    }

    private String generateReportJobId() {
        // Generate a realistic-looking Crystal Reports job ID
        long timestamp = System.currentTimeMillis();
        return String.format("CR-JOB-%d", timestamp);
    }
}
