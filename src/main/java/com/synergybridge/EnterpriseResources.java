package com.synergybridge;

import io.quarkiverse.mcp.server.Resource;
import io.quarkiverse.mcp.server.TextResourceContents;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnterpriseResources {

    private static final String ARCHITECTURE_DIAGRAM = """
            ╔══════════════════════════════════════════════════════════════════════╗
            ║            SynergyBridge Enterprise Architecture (v9.0)             ║
            ╠══════════════════════════════════════════════════════════════════════╣
            ║                                                                      ║
            ║   ┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐   ║
            ║   │  Claude   │     │  Goose   │     │  Other   │     │ Legacy   │   ║
            ║   │ Desktop   │     │   CLI    │     │MCP Client│     │ Terminal │   ║
            ║   └────┬─────┘     └────┬─────┘     └────┬─────┘     └────┬─────┘   ║
            ║        │                │                │                │          ║
            ║        └────────────────┴────────┬───────┴────────────────┘          ║
            ║                                  │                                   ║
            ║                          ┌───────▼───────┐                           ║
            ║                          │  MCP Protocol  │                           ║
            ║                          │ (HTTP / SSE)   │                           ║
            ║                          └───────┬───────┘                           ║
            ║                                  │                                   ║
            ║   ┌──────────────────────────────▼──────────────────────────────┐    ║
            ║   │              SynergyBridge MCP Server (Quarkus)              │    ║
            ║   │  ┌─────────────────────────────────────────────────────┐     │    ║
            ║   │  │                  12 Enterprise Tools                 │     │    ║
            ║   │  │  WebSphere │ COBOL │ JCL │ SOA │ ESB │ LDAP │ ... │     │    ║
            ║   │  └─────────────────────────────────────────────────────┘     │    ║
            ║   │  ┌─────────────────┐  ┌────────────────┐  ┌────────────┐    │    ║
            ║   │  │   Enterprise    │  │    Canned      │  │   Error    │    │    ║
            ║   │  │  Delay Engine   │  │   Responses    │  │ Injection  │    │    ║
            ║   │  └─────────────────┘  └────────────────┘  └────────────┘    │    ║
            ║   └─────────────────────────────────────────────────────────────┘    ║
            ║                                  │                                   ║
            ║        ┌─────────────────────────┼─────────────────────────┐         ║
            ║        │                         │                         │         ║
            ║   ┌────▼─────┐           ┌───────▼───────┐         ┌──────▼──────┐  ║
            ║   │WebSphere │           │   Mainframe   │         │  Corporate  │  ║
            ║   │  Cluster │           │  (Simulated)  │         │    LDAP     │  ║
            ║   │ (v8.5.5) │           │   z/OS 2.4    │         │  (Active    │  ║
            ║   └──────────┘           └───────────────┘         │  Directory) │  ║
            ║                                                     └─────────────┘  ║
            ║                                                                      ║
            ║   Note: All backend systems are simulated with enterprise-grade      ║
            ║   fidelity. No actual mainframes were harmed in this deployment.      ║
            ╚══════════════════════════════════════════════════════════════════════╝
            """;

    @Resource(uri = "synergybridge:///architecture-diagram",
              description = "Enterprise architecture diagram showing the SynergyBridge integration topology")
    TextResourceContents architectureDiagram() {
        return TextResourceContents.create("synergybridge:///architecture-diagram", ARCHITECTURE_DIAGRAM);
    }
}
