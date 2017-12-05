package com.em248.bosh.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.em248.bosh.config.BoshAutoConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class BoshClient {

    private Logger log = LoggerFactory.getLogger(getClass());
    private BoshAutoConfiguration boshConfig;

    @Resource(name = "boshHttpClient")
    private CloseableHttpClient httpClient;

    @Autowired
    public BoshClient(BoshAutoConfiguration boshConfig) {
        this.boshConfig = boshConfig;
    }

    public JSONArray getJSONArray(String url) throws IOException{
        HttpGet httpGet = new HttpGet(boshConfig.getBoshUrl() + url);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String s = EntityUtils.toString(execute.getEntity());
        log.info(s);
        JSONArray tasks = JSON.parseObject(s, JSONArray.class);
        return tasks;
    }

    public JSONObject getJsonObject(String url) throws IOException{
        HttpGet httpGet = new HttpGet(boshConfig.getBoshUrl() + url);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        JSONObject tasks = JSON.parseObject(EntityUtils.toString(execute.getEntity()), JSONObject.class);
        return tasks;
    }

    public String getString(String url) throws IOException{
        HttpGet httpGet = new HttpGet(boshConfig.getBoshUrl() + url);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        return execute.getEntity() == null ? null : EntityUtils.toString(execute.getEntity());
    }


}
