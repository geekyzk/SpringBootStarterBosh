package com.em248.bosh.dao;

import com.alibaba.fastjson.JSONObject;
import com.em248.bosh.config.BoshAutoConfiguration;
import com.em248.bosh.entity.Vm;
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
public class VmsDao {
    private TaskDao taskDao;
    private BoshAutoConfiguration boshConfig;

    @Resource(name = "boshHttpClient")
    private RestTemplate restTemplate;

    @Autowired
    public VmsDao(TaskDao taskDao, BoshAutoConfiguration boshConfig) {
        this.boshConfig = boshConfig;
        this.taskDao = taskDao;
    }


    public List<Vm> getVms(String deployment) {
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"deployments/" + deployment + "/vms",
                String.class);
        log.info(respStr);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Vm.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getVmsFull(String deployment) {
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() + "deployments/" + deployment + "/vms?format=full",
                String.class);
        log.info(respStr);
        JSONObject instancesTaskObject = JSONObject.parseObject(respStr);
        System.out.println(instancesTaskObject.toJSONString());
        Integer id = instancesTaskObject.getInteger("id");
        while(true){
            String result = taskDao.getLogsByIdAndType(id, "result");
            try {
                Thread.sleep(1001);
                if(result != null){
                    return result;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
