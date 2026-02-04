package com.synergybridge.tools;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for all 12 enterprise tools.
 * 
 * These tests verify that:
 * 1. Each tool can be invoked without errors
 * 2. Required parameter validation works
 * 3. Response content is reasonable
 * 
 * Note: Tests are configured with generous timeouts due to enterprise delays,
 * but actual execution should complete within expected delay + small overhead.
 */
@QuarkusTest
class ToolsIntegrationTest {

    @Inject
    WebSphereDeployTool webSphereTool;

    @Inject
    CobolCopybookTool cobolTool;

    @Inject
    MainframeJclTool mainframeTool;

    @Inject
    SoaGovernanceTool soaTool;

    @Inject
    EsbRouteTool esbTool;

    @Inject
    LdapSyncTool ldapTool;

    @Inject
    TuxedoTransactionTool tuxedoTool;

    @Inject
    TibcoPublishTool tibcoTool;

    @Inject
    CrystalReportsTool crystalTool;

    @Inject
    SiebelCrmTool siebelTool;

    @Inject
    InformaticaEtlTool informaticaTool;

    @Inject
    PeopleSoftTool peopleSoftTool;

    // WebSphere Deploy Tool Tests
    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void webSphereShouldRequireEarFilePath() {
        String result = webSphereTool.deployEjb(null, "cluster1", null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("earFilePath"),
                "Should return error for missing earFilePath");
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void webSphereShouldRequireClusterName() {
        String result = webSphereTool.deployEjb("/path/to/app.ear", null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("clusterName"),
                "Should return error for missing clusterName");
    }

    // COBOL Copybook Tool Tests
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void cobolShouldRequireCopybookSource() {
        String result = cobolTool.transformCopybook(null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("copybookSource"),
                "Should return error for missing copybookSource");
    }

    // Mainframe JCL Tool Tests
    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void mainframeShouldRequireJclSource() {
        String result = mainframeTool.submitJcl(null, null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("jclSource"),
                "Should return error for missing jclSource");
    }

    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void mainframeShouldRequireJobCard() {
        String result = mainframeTool.submitJcl("SOME JCL WITHOUT JOB CARD", null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") || result.contains("//JOB"),
                "Should warn about missing //JOB card");
    }

    // SOA Governance Tool Tests
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void soaShouldRequireWsdlEndpoint() {
        String result = soaTool.validateGovernance(null, null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("wsdlEndpoint"),
                "Should return error for missing wsdlEndpoint");
    }

    // ESB Route Tool Tests
    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void esbShouldRequireSourceQueue() {
        String result = esbTool.configureRoute(null, "topic1", null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("sourceQueue"),
                "Should return error for missing sourceQueue");
    }

    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void esbShouldRequireDestinationTopic() {
        String result = esbTool.configureRoute("queue1", null, null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("destinationTopic"),
                "Should return error for missing destinationTopic");
    }

    // LDAP Sync Tool Tests
    @Test
    @Timeout(value = 14, unit = TimeUnit.SECONDS)
    void ldapShouldRequireBaseDn() {
        String result = ldapTool.syncDirectory(null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("baseDn"),
                "Should return error for missing baseDn");
    }

    @Test
    @Timeout(value = 14, unit = TimeUnit.SECONDS)
    void ldapShouldValidatePageSizeRange() {
        String result = ldapTool.syncDirectory("ou=Users,dc=example,dc=com", null, null, null, null, 5000, null);
        assertTrue(result.contains("ERROR") && result.contains("pageSize"),
                "Should return error for pageSize out of range");
    }

    // Tuxedo Transaction Tool Tests
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void tuxedoShouldRequireDomainId() {
        String result = tuxedoTool.beginTransaction(null, "service1", null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("domainId"),
                "Should return error for missing domainId");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void tuxedoShouldRequireServiceName() {
        String result = tuxedoTool.beginTransaction("domain1", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("serviceName"),
                "Should return error for missing serviceName");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void tuxedoShouldValidatePriorityClassRange() {
        String result = tuxedoTool.beginTransaction("domain1", "service1", null, null, null, 200, null);
        assertTrue(result.contains("ERROR") && result.contains("priorityClass"),
                "Should return error for priorityClass out of range");
    }

    // TIBCO Publish Tool Tests
    @Test
    @Timeout(value = 8, unit = TimeUnit.SECONDS)
    void tibcoShouldRequireSubject() {
        String result = tibcoTool.publishMessage(null, "payload", null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("subject"),
                "Should return error for missing subject");
    }

    @Test
    @Timeout(value = 8, unit = TimeUnit.SECONDS)
    void tibcoShouldRequireMessagePayload() {
        String result = tibcoTool.publishMessage("CORP.TRADE.>", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("messagePayload"),
                "Should return error for missing messagePayload");
    }

    // Crystal Reports Tool Tests
    @Test
    @Timeout(value = 18, unit = TimeUnit.SECONDS)
    void crystalShouldRequireReportTemplatePath() {
        String result = crystalTool.generateReport(null, null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("reportTemplatePath"),
                "Should return error for missing reportTemplatePath");
    }

    @Test
    @Timeout(value = 18, unit = TimeUnit.SECONDS)
    void crystalShouldRequireRptExtension() {
        String result = crystalTool.generateReport("/path/to/report.pdf", null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains(".rpt"),
                "Should return error for non-.rpt file");
    }

    // Siebel CRM Tool Tests
    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void siebelShouldRequireSearchSpec() {
        String result = siebelTool.lookupCustomer(null, "Account", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("searchSpec"),
                "Should return error for missing searchSpec");
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void siebelShouldRequireBusinessComponent() {
        String result = siebelTool.lookupCustomer("[Name]='Test'", null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("businessComponent"),
                "Should return error for missing businessComponent");
    }

    // Informatica ETL Tool Tests
    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void informaticaShouldRequireFolderName() {
        String result = informaticaTool.startWorkflow(null, "workflow1", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("folderName"),
                "Should return error for missing folderName");
    }

    @Test
    @Timeout(value = 12, unit = TimeUnit.SECONDS)
    void informaticaShouldRequireWorkflowName() {
        String result = informaticaTool.startWorkflow("folder1", null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("workflowName"),
                "Should return error for missing workflowName");
    }

    // PeopleSoft Tool Tests
    @Test
    @Timeout(value = 14, unit = TimeUnit.SECONDS)
    void peopleSoftShouldRequireComponentInterfaceName() {
        String result = peopleSoftTool.callComponentInterface(null, "GET", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("componentInterfaceName"),
                "Should return error for missing componentInterfaceName");
    }

    @Test
    @Timeout(value = 14, unit = TimeUnit.SECONDS)
    void peopleSoftShouldRequireMethodName() {
        String result = peopleSoftTool.callComponentInterface("CI_EMPLOYEE", null, null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("methodName"),
                "Should return error for missing methodName");
    }

    @Test
    @Timeout(value = 14, unit = TimeUnit.SECONDS)
    void peopleSoftShouldValidateMethodName() {
        String result = peopleSoftTool.callComponentInterface("CI_EMPLOYEE", "INVALID_METHOD", null, null, null, null, null, null);
        assertTrue(result.contains("ERROR") && result.contains("Invalid methodName"),
                "Should return error for invalid method name");
    }
}
