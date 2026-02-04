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
 * TIBCO Rendezvous Publish Tool - Publish certified messages to TIBCO RV.
 * 
 * This tool publishes certified messages to TIBCO Rendezvous subjects with
 * guaranteed delivery confirmation. Well, "guaranteed" in the enterprise sense,
 * meaning we're pretty sure the message left our system. What happens after
 * that is between you and the middleware gods.
 * 
 * Fun fact: If a message is published to a subject with no listeners,
 * it makes no sound and is definitely not your problem.
 */
@ApplicationScoped
public class TibcoPublishTool {

    private static final String TOOL_NAME = "tibco-rendezvous-publish";

    @Inject
    EnterpriseDelayConfig delayConfig;

    @Inject
    ErrorCatalog errorCatalog;

    /**
     * Field Encoding - How should the message fields be encoded?
     * 
     * NATIVE: Platform-native encoding (works great until it doesn't)
     * PORTABLE: Cross-platform encoding (slower but safer)
     * RV_MSG: TIBCO RV native message format (maximum enterprise)
     */
    public enum FieldEncoding {
        NATIVE,
        PORTABLE,
        RV_MSG
    }

    /**
     * Publish a certified message to TIBCO Rendezvous.
     * 
     * This operation publishes a message to a TIBCO RV subject with optional
     * certified delivery. The 2-second delay is the shortest in our enterprise
     * suite, because even TIBCO has standards... low ones, but standards nonetheless.
     * 
     * @param subject RV subject (required, supports wildcards like CORP.TRADE.EQUITY.>)
     * @param messagePayload Message content (required)
     * @param certifiedDelivery Enable certified messaging (optional)
     * @param timeToLive TTL in seconds (optional)
     * @param sendSubject Explicit send subject (optional)
     * @param replySubject Reply subject for request/reply (optional)
     * @param fieldEncoding Field encoding type (optional)
     * @return Message publication result
     */
    @Tool(description = "Publish certified message to TIBCO Rendezvous subject with guaranteed delivery confirmation. The fastest tool in the enterprise suite, with only 2-second delay.")
    public String publishMessage(
            @ToolArg(description = "RV subject (e.g., CORP.TRADE.EQUITY.>)") String subject,
            @ToolArg(description = "Message payload content") String messagePayload,
            @ToolArg(description = "Enable certified messaging for guaranteed delivery") Boolean certifiedDelivery,
            @ToolArg(description = "Time to live in seconds") Integer timeToLive,
            @ToolArg(description = "Explicit send subject") String sendSubject,
            @ToolArg(description = "Reply subject for request/reply pattern") String replySubject,
            @ToolArg(description = "Field encoding: NATIVE, PORTABLE, or RV_MSG") String fieldEncoding) {

        // Validate required parameters
        if (subject == null || subject.trim().isEmpty()) {
            return "ERROR: subject is required. Please specify the RV subject (e.g., CORP.TRADE.EQUITY.>).";
        }
        if (messagePayload == null || messagePayload.trim().isEmpty()) {
            return "ERROR: messagePayload is required. What would you like to send to the void?";
        }

        // Apply enterprise-grade delay (2 seconds - the express lane!)
        try {
            delayConfig.applyDelay(TOOL_NAME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Message publication interrupted. Message may or may not have been sent. " +
                   "Please check the TIBCO logs, if you can find them.";
        }

        // Check for random enterprise error (10% chance)
        Optional<EnterpriseError> error = errorCatalog.maybeInjectError();
        if (error.isPresent()) {
            return error.get().toErrorString();
        }

        // Parse optional parameters
        FieldEncoding encoding = parseFieldEncoding(fieldEncoding);
        String messageId = generateMessageId();

        // Generate publication response
        StringBuilder response = new StringBuilder();
        response.append("=== TIBCO Rendezvous Message Publication Report ===\n\n");
        response.append("Status: MESSAGE PUBLISHED\n\n");
        response.append("Message Details:\n");
        response.append(String.format("  Message ID: %s\n", messageId));
        response.append(String.format("  Subject: %s\n", subject));
        response.append(String.format("  Payload Size: %d bytes\n", messagePayload.length()));
        response.append(String.format("  Field Encoding: %s\n", encoding));
        
        if (Boolean.TRUE.equals(certifiedDelivery)) {
            response.append("  Certified Delivery: ENABLED\n");
            response.append("    NOTE: Certified delivery requires listener acknowledgment\n");
            response.append("    NOTE: No listeners currently available to acknowledge\n");
        } else {
            response.append("  Certified Delivery: DISABLED (fire and forget mode)\n");
        }
        
        if (timeToLive != null) {
            response.append(String.format("  Time to Live: %d seconds\n", timeToLive));
        } else {
            response.append("  Time to Live: INFINITE (message will outlive us all)\n");
        }
        
        if (sendSubject != null && !sendSubject.isEmpty()) {
            response.append(String.format("  Send Subject: %s\n", sendSubject));
        }
        
        if (replySubject != null && !replySubject.isEmpty()) {
            response.append(String.format("  Reply Subject: %s\n", replySubject));
            response.append("    NOTE: No guarantee anyone is listening on the reply subject\n");
        }

        response.append("\nDelivery Statistics:\n");
        response.append("  Active Listeners: 0\n");
        response.append("  Messages Delivered: 0\n");
        response.append("  Messages Queued: 0\n");
        response.append("  This is expected—listeners are on vacation.\n");

        response.append("\nNetwork Status:\n");
        response.append("  Daemon Connection: ACTIVE\n");
        response.append("  Service: 7500 (default)\n");
        response.append("  Network: ;224.0.0.1\n");
        response.append("  Packet Loss: 0.0% (because nobody is listening anyway)\n");

        response.append("\nPost-Publication Notes:\n");
        response.append("  - Message was successfully published to the subject\n");
        response.append("  - If you expected listeners, please verify they are:\n");
        response.append("    * Subscribed to the correct subject\n");
        response.append("    * Actually running (check with IT)\n");
        response.append("    * Not blocked by the firewall (unlikely but possible)\n");
        response.append("    * Back from vacation (check team calendar)\n");
        response.append("  - For certified delivery tracking, check the ledger file\n");
        response.append("  - Ledger file location: Ask Bob. Bob knows.\n");

        return response.toString();
    }

    private FieldEncoding parseFieldEncoding(String value) {
        if (value == null || value.isEmpty()) {
            return FieldEncoding.NATIVE;
        }
        try {
            return FieldEncoding.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FieldEncoding.NATIVE;
        }
    }

    private String generateMessageId() {
        // Generate a realistic-looking TIBCO message ID
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 99999);
        return String.format("RV_%d_%05d", timestamp, random);
    }
}
