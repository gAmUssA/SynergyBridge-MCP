package com.synergybridge.responses;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enterprise Error Catalog - A curated collection of authentic enterprise errors.
 * 
 * These errors have been lovingly collected from production systems worldwide,
 * representing the finest in cryptic messaging and blame deflection.
 * 
 * Implements a statistically accurate 10% failure rate, because enterprise systems
 * believe in keeping developers humble.
 */
@ApplicationScoped
public class ErrorCatalog {

    private static final List<EnterpriseError> ENTERPRISE_ERRORS = List.of(
        new EnterpriseError(
            "ERR_7842",
            "License server unreachable. Please contact your IBM representative."
        ),
        new EnterpriseError(
            "ERR_CORBA_0x80004005",
            "Object reference not valid. Have you tried restarting the application server cluster?"
        ),
        new EnterpriseError(
            "ERR_WAS_ADMIN",
            "Your WebSphere admin console session has expired. Please log in again and navigate through 47 menus."
        ),
        new EnterpriseError(
            "ERR_MAINFRAME_CONN",
            "CICS region unavailable. Batch window in progress (next available: Monday 6 AM)."
        ),
        new EnterpriseError(
            "ERR_SOA_POLICY",
            "Message rejected by SOA governance layer. Reason: 'Unapproved HTTP verb'."
        ),
        new EnterpriseError(
            "ERR_XML_PARSE",
            "XML parsing failed at line 1, column 1. Document appears to be valid but spiritually incorrect."
        ),
        new EnterpriseError(
            "ERR_LDAP_TIMEOUT",
            "LDAP query exceeded timeout. Consider narrowing your search from \"all employees\" to \"some employees\"."
        ),
        new EnterpriseError(
            "ERR_LICENSE_COUNT",
            "Maximum concurrent user license exceeded. Current users: 3. Licensed users: 2."
        ),
        new EnterpriseError(
            "ERR_TXN_ROLLBACK",
            "Transaction rolled back. Two-phase commit coordinator lost quorum (Mercury in retrograde)."
        ),
        new EnterpriseError(
            "ERR_VENDOR_SUPPORT",
            "This feature requires Enterprise Edition with Premium Support Gold Plus tier."
        )
    );

    /**
     * The probability of injecting an error (10% as specified).
     * Enterprise systems fail approximately 10% of the time for no apparent reason.
     */
    private static final double ERROR_PROBABILITY = 0.10;

    /**
     * Maybe injects an enterprise error based on the configured probability.
     * 
     * In enterprise systems, errors are not bugs—they are features that remind
     * developers of the fragility of existence and the importance of redundancy.
     * 
     * @return Optional containing an EnterpriseError if one was injected, empty otherwise
     */
    public Optional<EnterpriseError> maybeInjectError() {
        if (ThreadLocalRandom.current().nextDouble() < ERROR_PROBABILITY) {
            int index = ThreadLocalRandom.current().nextInt(ENTERPRISE_ERRORS.size());
            return Optional.of(ENTERPRISE_ERRORS.get(index));
        }
        return Optional.empty();
    }

    /**
     * Returns the list of all enterprise errors for testing and documentation purposes.
     * 
     * @return immutable list of all configured enterprise errors
     */
    public List<EnterpriseError> getAllErrors() {
        return ENTERPRISE_ERRORS;
    }

    /**
     * Returns the configured error probability.
     * 
     * @return the probability of error injection (0.10 = 10%)
     */
    public double getErrorProbability() {
        return ERROR_PROBABILITY;
    }
}
