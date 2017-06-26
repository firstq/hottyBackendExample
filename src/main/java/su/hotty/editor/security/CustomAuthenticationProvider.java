package su.hotty.editor.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private Authenticator authenticator;

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        final String username = a.getName();
        final String password = (String) a.getCredentials();
        log.debug("authenticate username: {} password length: {} session: {}", username, password.length(), sessionId);
        Authentication customAuthentication = new CustomUserAuthentication("ADMIN", a);
        customAuthentication.setAuthenticated(authenticator.authenticate(username, password));
        return customAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
