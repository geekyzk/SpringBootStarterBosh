package com.em248.bosh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
@Data
public class Stemcell {


    private String name;

    private String version;

    private String cid;

    @JsonProperty("operating_system")
    private String operatingSystem;


    @JsonProperty("deployments")
    private List<Deployment> deployments;


    @Data
    static class Deployment {
        private String name;
    }

}
