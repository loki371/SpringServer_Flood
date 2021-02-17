package restAPI.security.services;

import org.springframework.security.core.Authentication;

public interface AppAuthorizer {
    boolean authorize(Authentication authentication, String action, Object callerObj);
}
