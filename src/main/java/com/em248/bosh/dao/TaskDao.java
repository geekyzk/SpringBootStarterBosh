package com.em248.bosh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class TaskDao {

    private BoshClient boshClient;

    @Autowired
    public TaskDao(BoshClient boshClient) {
        this.boshClient = boshClient;
    }

    public String getTasks(){
        try {
            return boshClient.getString("tasks");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * curl -v -s -k 'https://admin:admin@192.168.50.4:25555/tasks?state=queued,processing,cancelling&verbose=2' | jq .
     * @param state
     * @return
     */
    public String getTasksByState(String state){
        try {
           return boshClient.getString("tasks?state=" + state);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTasksByDeployment(String deployment){
        try {
           return  boshClient.getString("tasks?deployment=" + deployment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTasksById(Integer id){
        try {
            return  boshClient.getString("tasks/"+id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param id
     * @param type = debug|event|result
     * @return
     */
    public String getLogsByIdAndType(Integer id,String type){
        try {
            return  boshClient.getString("tasks/" + id +"/output?type=" + type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
