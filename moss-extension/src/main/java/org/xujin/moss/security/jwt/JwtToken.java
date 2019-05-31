package org.xujin.moss.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * jwtToken
 * @author xujin
 */
public class JwtToken implements AuthenticationToken {
    private String token;
    private DecodedJWT jwt;
    public JwtToken(String token) {
        this.token = token;
        this.jwt = JWT.decode(token);
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getClaim(String claim) {
        return this.jwt.getClaim(claim).asString();
    }
}
