package com.em248.bosh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class DeploymentsDao{

    private BoshClient boshClient;

    @Autowired
    public DeploymentsDao(BoshClient boshClient) {
        this.boshClient = boshClient;
    }

    public String getAllDeployments(){
        try {
            return boshClient.getString("deployments");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeploymentByName(String name){
        try {
            return boshClient.getString("deployments/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
