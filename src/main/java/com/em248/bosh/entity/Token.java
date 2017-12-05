package com.em248.bosh.entity;

/**
 * Created by geekyzk on 05/12/2017.
 */


public class Token {

    String access_token;

    String token_type;

    String refresh_token;

    Integer expires_in;

    String scope;

    String jti;

    Long  createTime = System.currentTimeMillis();

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public boolean checkExpires(){
        if(getExpires_in() != null && (System.currentTimeMillis() - this.createTime)< getExpires_in()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in=" + expires_in +
                ", scope='" + scope + '\'' +
                ", jti='" + jti + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
