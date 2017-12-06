package com.em248.bosh.dao;

import com.alibaba.fastjson.JSONObject;
import com.em248.bosh.config.BoshAutoConfiguration;
import com.em248.bosh.entity.Instance;
import com.em248.bosh.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
@Slf4j
public class InstancesDao  {

    TaskDao taskDao;

    @Resource(name = "boshHttpClient")
    private RestTemplate restTemplate;

    private BoshAutoConfiguration boshConfig;

    @Autowired
    public InstancesDao(BoshAutoConfiguration boshConfig,TaskDao taskDao) {
        this.boshConfig = boshConfig;
        this.taskDao = taskDao;
    }


    /**
     * 获取指定deployment下的所有实例
     * @param deploymentName deployment名字
     * @return 返回Instance实例集合
     */
    public List<Instance> getInstances(String deploymentName){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() + "deployments/"+ deploymentName +"/instances",
                String.class);
        log.info(respStr);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Instance.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getInstancesFull(String deploymentName){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() + "deployments/"+ deploymentName +"/instances?format=full",
                String.class);
        log.info(respStr);
        JSONObject instancesTaskObject = JSONObject.parseObject(respStr);
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
