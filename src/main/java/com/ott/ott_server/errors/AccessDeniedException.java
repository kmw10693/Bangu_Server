package com.ott.ott_server.errors;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Permission not accessible to this resource.");
    }
}
