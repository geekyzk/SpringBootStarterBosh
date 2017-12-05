package com.em248.bosh.http;

import com.alibaba.fastjson.JSON;
import com.em248.bosh.config.BoshProperties;
import com.em248.bosh.entity.Info;
import com.em248.bosh.entity.Token;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
public class TokenClient {

    protected static final Logger logger = LoggerFactory.getLogger(TokenClient.class);

    /**
     * 根据用户名和密码生成口令
     *
     * @return
     */
    public static Token createToken(BoshProperties properties) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://" + properties.getHost() + ":" + properties.getPort() + "/info");
        HttpResponse execute = httpClient.execute(httpGet);
        Info info = JSON.parseObject(EntityUtils.toString(execute.getEntity()), Info.class);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("client_id", properties.getClientId()));
        formparams.add(new BasicNameValuePair("client_secret", properties.getClientSecret()));
        formparams.add(new BasicNameValuePair("password", properties.getPassword()));
        formparams.add(new BasicNameValuePair("username", properties.getUsername()));
        formparams.add(new BasicNameValuePair("grant_type", "password"));
        HttpPost httpPost = new HttpPost("https://" + properties.getHost() + ":8443/oauth/token");
        httpPost.setEntity(new UrlEncodedFormEntity(formparams));
        HttpResponse response = httpClient.execute(httpPost);
        Token token = JSON.parseObject(EntityUtils.toString(response.getEntity()), Token.class);
        logger.info("infoMap" + token.toString());
        return token;
    }

}