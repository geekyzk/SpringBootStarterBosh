package com.em248.bosh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class StemcellsDao{

    private BoshClient boshClient;

    @Autowired
    public StemcellsDao(BoshClient boshClient) {
        this.boshClient = boshClient;
    }

    public String getStemcells(){
        try {
            return boshClient.getString("stemcells");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
