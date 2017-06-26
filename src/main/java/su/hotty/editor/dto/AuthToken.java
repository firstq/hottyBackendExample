package su.hotty.editor.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthToken {

    private String name;

    private Date expires_at;

    private Date issued_at;

    private String token_project;

    private List<String> roles;

    public AuthToken(String name) {
        this.name = name;
        this.expires_at = new Date(0);
        this.issued_at = new Date(0);
        this.token_project = "";
        this.roles = new ArrayList<>();
    }

    // for JSON mappers
    protected AuthToken() {
        this("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Date expires_at) {
        this.expires_at = expires_at;
    }

    public Date getIssued_at() {
        return issued_at;
    }

    public void setIssued_at(Date issued_at) {
        this.issued_at = issued_at;
    }

    public String getToken_project() {
        return token_project;
    }

    public void setToken_project(String token_project) {
        this.token_project = token_project;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken = (AuthToken) o;

        if (!name.equals(authToken.name)) return false;
        if (!expires_at.equals(authToken.expires_at)) return false;
        if (!issued_at.equals(authToken.issued_at)) return false;
        if (!token_project.equals(authToken.token_project)) return false;
        return roles.equals(authToken.roles);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + expires_at.hashCode();
        result = 31 * result + issued_at.hashCode();
        result = 31 * result + token_project.hashCode();
        result = 31 * result + roles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "name='" + name + '\'' +
                ", expires_at=" + expires_at +
                ", issued_at=" + issued_at +
                ", token_project='" + token_project + '\'' +
                ", roles=" + roles +
                '}';
    }
}
