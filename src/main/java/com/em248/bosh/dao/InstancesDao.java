package com.em248.bosh.dao;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class InstancesDao  {

    TaskDao taskDao;
    BoshClient boshClient;

    @Autowired
    public InstancesDao(TaskDao taskDao, BoshClient boshClient) {

        this.taskDao = taskDao;
        this.boshClient = boshClient;
    }

    /**
     * 获取指定deployment下的所有实例
     * @param deploymentName
     * @return
     */
    public String getInstances(String deploymentName){
        try {
                return boshClient.getString( "deployments/"+ deploymentName +"/instances");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInstancesFull(String deploymentName){
        try {
            JSONObject jsonObject = boshClient.getJsonObject("deployments/" + deploymentName + "/instances?format=full");
            Integer id = jsonObject.getInteger("id");
            while(true){
                String result = taskDao.getLogsByIdAndType(id, "result");
                try {
                    Thread.sleep(1001);
                    if(result != null){
                        return result;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
