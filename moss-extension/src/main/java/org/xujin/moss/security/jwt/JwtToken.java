package org.xujin.moss.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * jwtToken
 * @author xujin
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
