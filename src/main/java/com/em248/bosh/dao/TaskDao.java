package com.em248.bosh.dao;

import com.em248.bosh.config.BoshAutoConfiguration;
import com.em248.bosh.entity.Task;
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
public class TaskDao {

    private BoshAutoConfiguration boshConfig;

    @Resource(name = "boshHttpClient")
    private RestTemplate restTemplate;

    @Autowired
    public TaskDao(BoshAutoConfiguration boshConfig) {
        this.boshConfig = boshConfig;
    }


    public List<Task> getTasks(){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"tasks",
                String.class);
        log.info(respStr);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Task.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * @param state 任务状态
     * @return 返回指定状态的任务列表
     */
    public List<Task> getTasksByState(String state){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"tasks?state=" + state,
                String.class);
        log.info(respStr);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Task.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public  List<Task> getTasksByDeployment(String deployment){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"tasks?deployment=" + deployment,
                String.class);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, ObjectMapperUtil.getCollectionType(List.class, Task.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public Task getTasksById(Integer id){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"tasks/" + id,
                String.class);
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(respStr, Task.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     *
     * @param id 任务的id
     * @param type = debug|event|result
     * @return 返回log日志
     */
    public String getLogsByIdAndType(Integer id,String type){
        String respStr = restTemplate.getForObject(boshConfig.getBoshUrl() +"tasks/" + id +"/output?type=" + type,
                String.class);
        return respStr;
    }



}
