package com.em248.bosh.dao;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class VmsDao{
    TaskDao taskDao;
    BoshClient boshClient;

    @Autowired
    public VmsDao(TaskDao taskDao, BoshClient boshClient) {
        this.taskDao = taskDao;
        this.boshClient = boshClient;
    }

    public String getVms(String deployment){
        try {
            return boshClient.getString("deployments/"+deployment+"/vms");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVmsFull(String deployment){
        try {
            JSONObject jsonObject = boshClient.getJsonObject("deployments/" + deployment + "/vms?format=full");
            Integer id = jsonObject.getInteger("id");

            while(true){
                String result = taskDao.getLogsByIdAndType(id, "result");
                try {
                    Thread.sleep(1000);
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
