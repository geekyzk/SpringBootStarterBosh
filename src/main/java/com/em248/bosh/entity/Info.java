package com.em248.bosh.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/12.
 */
public class Info {
    String name;
    String uuid;
    String version;
    String user;
    String cpi;
    UserAuthentication userAuthentication;
    Map<String,String > other;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCpi() {
        return cpi;
    }

    public void setCpi(String cpi) {
        this.cpi = cpi;
    }

    public UserAuthentication getUserAuthentication() {
        return userAuthentication;
    }

    public void setUserAuthentication(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    public Map<String, String> getOther() {
        return other;
    }

    public void setOther(Map<String, String> other) {
        this.other = other;
    }
}


class UserAuthentication{
    public  String type;
    public List<Urls> options;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public List<Urls> getOptions() {
        return options;
    }

    public void setOptions(List<Urls> options) {
        this.options = options;
    }
}

class Urls{
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
