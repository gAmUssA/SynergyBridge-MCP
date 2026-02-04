package com.synergybridge.config;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Map;
import org.jboss.logging.Logger;

/**
 * Enterprise-grade delay configuration for simulating authentic legacy system performance.
 * 
 * Each tool has been carefully calibrated to replicate the true enterprise experience,
 * where patience is not just a virtue—it's a job requirement.
 * 
 * Delay times are based on extensive field research (i.e., staring at progress bars
 * in production systems since 1997).
 */
@ApplicationScoped
public class EnterpriseDelayConfig {

    private static final Logger LOG = Logger.getLogger(EnterpriseDelayConfig.class);

    private static final Map<String, Duration> REALISTIC_DELAYS = Map.ofEntries(
        Map.entry("websphere-deploy-ejb", Duration.ofSeconds(8)),
        Map.entry("cobol-copybook-transform", Duration.ofSeconds(3)),
        Map.entry("mainframe-jcl-submit", Duration.ofSeconds(5)),
        Map.entry("soa-governance-validate", Duration.ofSeconds(4)),
        Map.entry("enterprise-service-bus-route", Duration.ofSeconds(6)),
        Map.entry("ldap-corporate-directory-sync", Duration.ofSeconds(7)),
        Map.entry("crystal-reports-generate", Duration.ofSeconds(12)),
        Map.entry("tuxedo-transaction-begin", Duration.ofSeconds(4)),
        Map.entry("tibco-rendezvous-publish", Duration.ofSeconds(2)),
        Map.entry("siebel-crm-customer-lookup", Duration.ofSeconds(9)),
        Map.entry("informatica-etl-workflow-start", Duration.ofSeconds(6)),
        Map.entry("peoplesoft-component-interface-call", Duration.ofSeconds(7))
    );

    // Default delay for any unconfigured tools (because enterprise systems always have surprises)
    private static final Duration DEFAULT_DELAY = Duration.ofSeconds(5);

    /**
     * Gets the configured delay for a specific tool.
     * 
     * @param toolName the name of the tool (can be null or empty)
     * @return the Duration to wait before responding (simulating enterprise latency)
     */
    public Duration getDelay(String toolName) {
        if (toolName == null || toolName.isEmpty()) {
            return DEFAULT_DELAY;
        }
        return REALISTIC_DELAYS.getOrDefault(toolName, DEFAULT_DELAY);
    }

    /**
     * Applies the enterprise-grade delay for the specified tool.
     * This method blocks the current thread to simulate authentic legacy system performance.
     * 
     * Note: In a real enterprise environment, you would also need to file three change requests
     * and attend a 2-hour CAB meeting before this method returns.
     * 
     * @param toolName the name of the tool
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void applyDelay(String toolName) throws InterruptedException {
        Duration delay = getDelay(toolName);
        LOG.infof(">>> Tool invoked: [%s] — applying %ds enterprise-grade delay...", toolName, delay.toSeconds());
        Thread.sleep(delay.toMillis());
        LOG.infof("<<< Tool complete: [%s] — delay fulfilled, returning response", toolName);
    }
}
