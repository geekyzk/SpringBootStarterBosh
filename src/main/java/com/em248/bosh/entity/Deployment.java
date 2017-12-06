package com.em248.bosh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
@Data
public class Deployment {

    private String name;

    @JsonProperty("cloud_config")
    private String cloudConfig;

    @JsonProperty("releases")
    private List<Release> releases;

    @JsonProperty("stemcells")
    private List<Stemcell> stemcells;

    @Data
    static class Release{
        private String name;
        private String version;
    }

    @Data
    static class Stemcell{
        private String name;
        private String version;
    }
}
