package com.synergybridge.responses;

/**
 * Represents an authentic enterprise error with code and message.
 * 
 * These errors have been carefully curated from real enterprise systems,
 * where the error messages are as helpful as a "Something went wrong" dialog.
 * 
 * @param code The cryptic error code that will require a 200-page runbook to decode
 * @param message The error message that explains everything and nothing simultaneously
 */
public record EnterpriseError(String code, String message) {
    
    /**
     * Returns the full error string in enterprise format.
     * 
     * @return formatted error string that will make any developer sigh deeply
     */
    public String toErrorString() {
        return code + ": " + message;
    }
}
