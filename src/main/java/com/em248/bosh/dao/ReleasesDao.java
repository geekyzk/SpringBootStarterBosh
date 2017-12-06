package com.em248.bosh.dao;

import com.em248.bosh.config.BoshAutoConfiguration;
import com.em248.bosh.entity.Release;
import com.em248.bosh.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
@Slf4j
public class ReleasesDao {

    @Resource(name = "boshHttpClient")
    private RestTemplate restTemplate;

    private BoshAutoConfiguration boshConfig;

    @Autowired
    public ReleasesDao(BoshAutoConfiguration boshConfig) {
        this.boshConfig = boshConfig;
    }

    public List<Release> getReleases() {
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() + "releases",
                String.class);
        log.info(respStr);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Release.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
