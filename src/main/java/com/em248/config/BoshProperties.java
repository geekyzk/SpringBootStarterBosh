package com.em248.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/12/5.
 */
@ConfigurationProperties(
        prefix = "spring.bosh"
)
public class BoshProperties {


    @NotNull
    private String host;

    @NotNull
    private String port;

    @NotNull
    private String username;

    @NotNull
    private String password;


    private Boolean basicAuth = true;

    private String clientId = "bosh_cli";

    private String clientSecret ="";


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(Boolean basicAuth) {
        this.basicAuth = basicAuth;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
