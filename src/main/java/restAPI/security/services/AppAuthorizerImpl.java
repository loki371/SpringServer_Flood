package restAPI.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service("appAuthorizer")
public class AppAuthorizerImpl {
    private final Logger logger = LoggerFactory.getLogger(AppAuthorizerImpl.class);

    public boolean authorize(Authentication authentication, String action, Object callerObj) {
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            if (user==null) {
                return isAllow;
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) user.getPrincipal();

            if (userDetails==null) {
                return isAllow;
            }

            for (GrantedAuthority role : userDetails.getAuthorities())
                if (role.getAuthority().equals(action)) {
                        isAllow = true;
                    }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            throw e;
        }
        return isAllow;
    }

}
