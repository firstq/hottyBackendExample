package su.hotty.editor.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class Authenticator {

    @Value("${security.admin.username}")
    private String username;

    @Value("${security.admin.password}")
    private String password;

    public synchronized boolean authenticate(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

}
