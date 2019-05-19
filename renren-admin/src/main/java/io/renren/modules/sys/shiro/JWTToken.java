package io.renren.modules.sys.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {

    private String password;
    private String username;
    // 密钥
    private String token;

    private Integer corid;

    public Integer getCorid() {
        return corid;
    }

    public void setCorid(Integer corid) {
        this.corid = corid;
    }

    public JWTToken(String password, String username,Integer corid) {
        this.password = password;
        this.username = username;
        this.corid = corid;
    }

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token==null?password:token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
