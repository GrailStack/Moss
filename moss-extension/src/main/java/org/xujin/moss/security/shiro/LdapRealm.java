package org.xujin.moss.security.shiro;

import org.xujin.moss.security.jwt.JwtToken;
import org.xujin.moss.security.jwt.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.ldap.AbstractLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.HashSet;
import java.util.Set;

/**
 * LDAP
 * @author xujin
 */
public class LdapRealm extends AbstractLdapRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapRealm.class);

    /**
     * LDAP信息
     * this.principalSuffix = "@corp.xxx.com";
       this.searchBase = "ou=xujin,DC=corp,DC=xujin,DC=org";
       this.url = "ldap://ip:端口";
     */
    public LdapRealm() {
        this.principalSuffix = "";
        this.searchBase = "";
        this.url = "";
        super.onInit();
    }


    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken authenticationToken,
                                                            LdapContextFactory ldapContextFactory) throws NamingException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        String password = JwtUtil.getPassWord(token);
        if (null==username  || !JwtUtil.verify(token, username,password)) {
            throw new AuthenticationException("token认证失败！");
        }
        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getLdapContext(username, password);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            LdapUtils.closeContext(ctx);
        }
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    @Override
    protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals,
                                                          LdapContextFactory ldapContextFactory) throws NamingException {
        System.out.println("————权限认证————");
        String username = JwtUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        //String role = userMapper.getRole(username);
        //每个角色拥有默认的权限
        //String rolePermission = userMapper.getRolePermission(username);
        //每个用户可以设置新的权限
        //String permission = userMapper.getPermission(username);
        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        //需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
       // roleSet.add(role);
       // permissionSet.add(rolePermission);
        //permissionSet.add(permission);
        //设置该用户拥有的角色和权限
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
}
