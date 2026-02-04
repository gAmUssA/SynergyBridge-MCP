package com.synergybridge;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * Health/Info endpoint for the root URL.
 *
 * Because even enterprise systems deserve a landing page,
 * even if nobody reads it.
 */
@ApplicationScoped
public class HealthInfoEndpoint {

    void registerRoutes(@Observes Router router) {
        router.get("/").handler(this::handleInfo);
    }

    private void handleInfo(RoutingContext ctx) {
        String json = """
                {
                  "name": "%s",
                  "version": "%s",
                  "vendor": "%s",
                  "tagline": "%s",
                  "status": "OPERATIONAL (within acceptable parameters)",
                  "javaVersion": "%s",
                  "jvmInfo": "%s",
                  "javaVendor": "%s",
                  "certifications": ["ISO 9001", "CMMI Level 3", "Y2K Compliant", "GDPR Pending Review"],
                  "uptime": "%s",
                  "endpoints": {
                    "mcp_streamable_http": "/mcp",
                    "mcp_sse": "/mcp/sse",
                    "info": "/"
                  },
                  "support": "%s",
                  "documentation": "%s"
                }
                """.formatted(
                SynergyBridgeServer.SERVER_NAME,
                SynergyBridgeServer.SERVER_VERSION,
                SynergyBridgeServer.VENDOR,
                SynergyBridgeServer.TAGLINE,
                System.getProperty("java.version", "unknown"),
                System.getProperty("java.vm.name", "unknown") + " " + System.getProperty("java.vm.version", ""),
                System.getProperty("java.vendor", "unknown"),
                SynergyBridgeServer.UPTIME,
                SynergyBridgeServer.SUPPORT_CONTACT,
                SynergyBridgeServer.DOCUMENTATION
        );
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(json);
    }
}
