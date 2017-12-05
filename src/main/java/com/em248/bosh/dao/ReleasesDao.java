package com.em248.bosh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/12.
 */
@Repository
public class ReleasesDao {

    private BoshClient boshClient;

    @Autowired
    public ReleasesDao(BoshClient boshClient) {
        this.boshClient = boshClient;
    }

    public String getReleases() {
        try {
            return boshClient.getString("releases");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
