# SynergyBridge MCP

![Enterprise Grade](https://img.shields.io/badge/Enterprise-Grade-blue)
![Y2K Compliant](https://img.shields.io/badge/Y2K-Compliant-green)
![SOA Ready](https://img.shields.io/badge/SOA-Ready-orange)
![XML First](https://img.shields.io/badge/XML-First-red)
![Java 21](https://img.shields.io/badge/Java-21-brightgreen)
![Quarkus 3.31.2](https://img.shields.io/badge/Quarkus-3.31.2-blue)
![IBM Semeru](https://img.shields.io/badge/IBM_Semeru-OpenJ9-blueviolet)

> **Bridging the gap between your legacy investments and tomorrow's legacy investments.**

*Unify. Integrate. Synergize.*

---

## 📋 Executive Summary

**SynergyBridge MCP** is the industry's first Model Context Protocol server designed to bring the full power of enterprise legacy systems directly into your AI-assisted development workflow. Why settle for simple, modern tooling when you can experience the rich complexity that Fortune 500 companies have trusted since 1997?

Our platform seamlessly integrates WebSphere, COBOL, mainframe batch processing, and a comprehensive suite of enterprise middleware—all without any of the underlying functionality. Because sometimes, it's not about *doing* things. It's about *governing* the things you might someday do.

### Why SynergyBridge?

Modern developers have been spoiled by tools that "just work." They've forgotten the character-building experience of:

- Waiting 45 minutes for an application server to restart
- Debugging XML configuration files at 2 AM
- Explaining to stakeholders why "it works on my mainframe" isn't acceptable
- Navigating twelve layers of abstraction to print "Hello World"

SynergyBridge MCP reintroduces the enterprise experience to a new generation.

---

## 🚀 Installation

### Prerequisites

- **Java 21** or higher (IBM Semeru / Eclipse OpenJ9 is the default Docker runtime)
- **Gradle** 8.x (included via wrapper)
- Docker (optional, for containerized deployment — uses IBM Semeru base image)

### Building from Source

```bash
# Clone the repository
git clone https://github.com/your-org/SynergyBridge-MCP.git
cd SynergyBridge-MCP

# Build the project
./gradlew build

# Run tests
./gradlew test
```

### Running the Server

#### Development Mode

```bash
./gradlew quarkusDev
```

The server will be available at:
- **HTTP**: `http://localhost:8080/mcp`
- **SSE**: `http://localhost:8080/mcp/sse`

#### Production Mode

```bash
./gradlew build
java -jar build/quarkus-app/quarkus-run.jar
```

### Docker

#### Building the Docker Image

```bash
# Build for current architecture
docker build -t synergybridge-mcp:latest .

# Build for multiple architectures (x86 and ARM)
docker buildx build --platform linux/amd64,linux/arm64 \
  -t synergybridge-mcp:latest --push .
```

#### Running with Docker

```bash
docker run -p 8080:8080 synergybridge-mcp:latest
```

#### Docker Compose

```bash
docker-compose up -d
```

---

## 🔧 MCP Client Configuration

### Claude Desktop

Add the following to your Claude Desktop configuration file:

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "synergybridge": {
      "url": "http://localhost:8080/mcp",
      "name": "SynergyBridge MCP",
      "description": "Enterprise legacy system integration suite"
    }
  }
}
```

For SSE transport:

```json
{
  "mcpServers": {
    "synergybridge": {
      "url": "http://localhost:8080/mcp/sse",
      "transport": "sse",
      "name": "SynergyBridge MCP"
    }
  }
}
```

### Goose

Add the following to your Goose configuration (`~/.config/goose/config.yaml`):

```yaml
mcp_servers:
  synergybridge:
    url: "http://localhost:8080/mcp"
    name: "SynergyBridge MCP"
```

Or using environment variables:

```bash
export MCP_SERVER_SYNERGYBRIDGE_URL="http://localhost:8080/mcp"
```

### Generic MCP Client

For any MCP-compatible client, configure:

| Property | Value |
|----------|-------|
| **Server URL** | `http://localhost:8080/mcp` |
| **SSE URL** | `http://localhost:8080/mcp/sse` |
| **Transport** | HTTP or SSE |

---

## 🛠️ Tools Reference

SynergyBridge MCP provides 12 enterprise-grade tools, each carefully designed to simulate authentic legacy system behavior.

### 1. websphere-deploy-ejb

Deploy an Enterprise JavaBean to the WebSphere Application Server cluster with full J2EE 1.4 compliance.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `earFilePath` | string | Yes | Path to the EAR file |
| `clusterName` | string | Yes | Target cluster name |
| `jndiBindingStrategy` | enum | No | LEGACY_COMPAT, MODERN_LEGACY, ULTRA_LEGACY |
| `transactionIsolationLevel` | enum | No | READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE, CHAOS |
| `enableWorkloadManagement` | boolean | No | Enable WLM (requires separate license) |
| `connectionPoolMinSize` | integer | No | Minimum pool size (1-10000) |
| `connectionPoolMaxSize` | integer | No | Maximum pool size (1-10000) |
| `xmlDescriptorValidationMode` | enum | No | STRICT, LAX, YOLO |

**Response Time**: 8 seconds (simulating WebSphere deployment)

### 2. cobol-copybook-transform

Transform COBOL copybook definitions to JSON schema.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `copybookSource` | string | Yes | The COBOL COPYBOOK source code |
| `targetEncoding` | enum | No | EBCDIC_037, EBCDIC_500, EBCDIC_1140, ASCII_IF_YOU_MUST |
| `picClauseInterpretation` | enum | No | IBM_STRICT, IBM_RELAXED, MICROFOCUS, GUESSWORK |
| `handleOccurs` | enum | No | How to handle OCCURS clauses |
| `redefinesStrategy` | enum | No | FIRST_WINS, LAST_WINS, UNION_ALL, PRAY |
| `level88HandlingMode` | enum | No | AS_ENUM, AS_BOOLEAN, AS_MYSTERY |
| `preserveFillerFields` | boolean | No | Keep FILLER fields (for nostalgia) |

**Response Time**: 3 seconds

### 3. mainframe-jcl-submit

Submit JCL to the mainframe job scheduler.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `jclSource` | string | Yes | The JCL source code |
| `jobClass` | string | No | Job class (A-Z) |
| `priority` | integer | No | Job priority (0-15) |
| `notifyOnCompletion` | string | No | TSO user ID for notification |
| `typrunScan` | boolean | No | Perform syntax scan without execution |
| `accountingInfo` | string | No | Accounting information |
| `region` | string | No | Memory region size (e.g., "4096K") |

**Response Time**: 5 seconds

### 4. soa-governance-validate

Validate a service against corporate SOA governance policies.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `wsdlEndpoint` | string | Yes | WSDL endpoint URL |
| `governanceProfile` | enum | No | FINANCE, HEALTHCARE, RETAIL, YOLO_STARTUP |
| `validateCompatibility` | boolean | No | Check backward compatibility |
| `requireSla` | boolean | No | Enforce SLA documentation |
| `namespaceAuditMode` | enum | No | LENIENT, STRICT, PEDANTIC |
| `soapVersionEnforcement` | enum | No | SOAP11_ONLY, SOAP12_PREFERRED, REST_IS_NOT_SOA |

**Response Time**: 4 seconds

### 5. enterprise-service-bus-route

Configure message routing through the Enterprise Service Bus.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `sourceQueue` | string | Yes | Source queue name |
| `destinationTopic` | string | Yes | Destination topic |
| `transformationXslt` | string | No | XSLT transformation path |
| `contentBasedRouting` | object | No | Routing rules |
| `errorHandling` | enum | No | RETRY_FOREVER, DEAD_LETTER, CALL_STEVE |
| `messageEnrichment` | boolean | No | Enable message enrichment |
| `throttling` | integer | No | Messages per second limit |

**Response Time**: 6 seconds

### 6. ldap-corporate-directory-sync

Synchronize with the corporate LDAP directory.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `baseDn` | string | Yes | Base distinguished name |
| `filter` | string | No | LDAP search filter |
| `attributes` | array | No | Attributes to retrieve |
| `scope` | enum | No | BASE, ONE_LEVEL, SUBTREE |
| `pageSize` | integer | No | Page size (1-1000) |
| `referralHandling` | enum | No | FOLLOW, IGNORE, THROW_TANTRUM |
| `syncMode` | enum | No | FULL, INCREMENTAL, PRAYER_BASED |

**Response Time**: 7 seconds

### 7. tuxedo-transaction-begin

Begin a Tuxedo distributed transaction.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `domainId` | string | Yes | Tuxedo domain identifier |
| `serviceName` | string | Yes | Service to invoke |
| `transactionType` | enum | No | SYNCHRONOUS, ASYNCHRONOUS, CONVERSATIONAL |
| `tpFlags` | array | No | TPNOTRAN, TPNOBLOCK, TPNOTIME, TPSIGRSTRT, TPNOREPLY |
| `priorityClass` | integer | No | Priority (1-100) |
| `compressionLevel` | integer | No | Compression (0-9) |
| `timeoutOverride` | integer | No | Timeout in seconds |

**Response Time**: 4 seconds

### 8. tibco-rendezvous-publish

Publish messages via TIBCO Rendezvous.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `subject` | string | Yes | Publication subject |
| `messagePayload` | string | Yes | Message content |
| `certifiedDelivery` | boolean | No | Use certified messaging |
| `timeToLive` | integer | No | Message TTL in seconds |
| `sendSubject` | string | No | Override send subject |
| `replySubject` | string | No | Reply subject for responses |
| `fieldEncoding` | enum | No | NATIVE, PORTABLE, RV_MSG |

**Response Time**: 2 seconds (the fastest tool!)

### 9. crystal-reports-generate

Generate Crystal Reports with full enterprise formatting.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `reportTemplatePath` | string | Yes | Path to .rpt file |
| `outputFormat` | enum | No | PDF, EXCEL, WORD, HTML, PRINTER_LPT1, FAX |
| `dataSourceOdbc` | string | No | ODBC connection string |
| `parameterValues` | object | No | Report parameters |
| `subreportLinks` | array | No | Subreport configurations |
| `pageOrientation` | enum | No | PORTRAIT, LANDSCAPE, CONFUSED |
| `watermarkText` | string | No | Watermark text |
| `emailOnCompletion` | string | No | Email for notification |

**Response Time**: 12 seconds (the slowest tool - Enterprise reporting takes time!)

### 10. siebel-crm-customer-lookup

Look up customer records in Siebel CRM.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `searchSpec` | string | Yes | Siebel search specification |
| `businessComponent` | string | Yes | Business component name |
| `integrationObject` | string | No | Integration object |
| `viewMode` | enum | No | PERSONAL, MANAGER, ORGANIZATION, ALL_ACROSS_ORGANIZATIONS |
| `includeChildObjects` | boolean | No | Include child records |
| `maxRecords` | integer | No | Maximum records to return |
| `sortSpec` | string | No | Sort specification |
| `languageCode` | string | No | Language code |

**Response Time**: 9 seconds

### 11. informatica-etl-workflow-start

Start an Informatica PowerCenter ETL workflow.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `folderName` | string | Yes | Repository folder |
| `workflowName` | string | Yes | Workflow name |
| `parameterFile` | string | No | Parameter file path |
| `sessionOverrides` | object | No | Session property overrides |
| `recoveryStrategy` | enum | No | RESTART, RESUME, BLAME_DBA |
| `waitForCompletion` | boolean | No | Wait for workflow to complete |
| `osProfile` | string | No | Operating system profile |
| `pushdownOptimization` | enum | No | NONE, PARTIAL, FULL, AGGRESSIVE |

**Response Time**: 6 seconds

### 12. peoplesoft-component-interface-call

Call a PeopleSoft Component Interface.

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `componentInterfaceName` | string | Yes | Component interface name |
| `methodName` | enum | Yes | GET, FIND, CREATE, UPDATE, CANCEL, APPROVE, DENY, ESCALATE |
| `getKeys` | object | No | Keys for GET operation |
| `findKeys` | object | No | Keys for FIND operation |
| `propertyValues` | object | No | Property values to set |
| `effectiveDate` | string | No | Effective date (ISO format) |
| `setLanguageCode` | string | No | Language code |
| `interactiveMode` | boolean | No | Enable interactive mode |

**Response Time**: 7 seconds

---

## ⚠️ Troubleshooting

### The server won't start

Have you tried turning it off and on again? If that doesn't work:

1. **Check Java version**: Must be Java 21 or higher. IBM Semeru / OpenJ9 preferred.
2. **Port conflicts**: Ensure port 8080 is available
3. **File a Remedy ticket**: Average response time is 3-5 business days

### Tools are taking too long

This is by design. Enterprise systems require patience. If you need faster responses, please submit a change request to the Architecture Review Board (ARB). Meetings are held quarterly.

### Getting "License server unreachable" errors

This is also by design. Our tools randomly inject enterprise-grade errors at a 10% rate to provide an authentic experience. Consider it a feature.

### Nothing works and I'm questioning my career choices

Welcome to enterprise integration! You're now ready for Fortune 500 consulting. Please update your LinkedIn profile accordingly.

---

## 📜 License

This project is licensed under the **Enterprise Compatibility Assurance License (ECAL)**.

```
Enterprise Compatibility Assurance License (ECAL)

Copyright (c) 2024 SynergyBridge Enterprise Solutions Division

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

1. This software may only be used in environments where at least one 
   enterprise legacy system exists or is being planned.

2. Users must acknowledge that "microservices are just distributed monoliths 
   with better marketing."

3. All deployments must be documented in a SharePoint wiki that nobody 
   reads.

4. The above copyright notice and this permission notice shall be included 
   in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING BUT NOT LIMITED TO
PROCUREMENT OF SUBSTITUTE ENTERPRISE SOLUTIONS, LOSS OF DATA, OR PROFIT;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

---

## 🏢 Product Certifications

| Certification | Status |
|--------------|--------|
| ISO 9001 | ✓ Certified |
| CMMI Level 3 | ✓ Certified |
| Y2K Compliant | ✓ Certified |
| GDPR | Pending Review (since 2018) |
| SOC 2 | "In Progress" |
| Common Sense | Not Applicable |

---

## 📞 Support

For support inquiries:

1. Open a Remedy ticket (average response time: 3-5 business days)
2. Check the documentation on SharePoint (2007). Ask Brenda for the password.
3. Attend the bi-weekly Synergy Standup (2 hours, mandatory fun)
4. Escalate to the Enterprise Architecture Review Board (meetings quarterly)

---

## 🙏 Acknowledgments

Special thanks to:
- Every developer who has debugged XML configuration at 2 AM
- The COBOL programmers who refuse to retire
- Karen's spreadsheet (the true source of all enterprise data)
- The mainframe batch window (see you Monday at 6 AM)

---

*SynergyBridge MCP - Because the future was supposed to be simpler, but here we are.*
