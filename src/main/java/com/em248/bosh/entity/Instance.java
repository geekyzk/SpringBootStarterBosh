package com.em248.bosh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by geekyzk on 05/12/2017.
 */
@Data
public class Instance {

    @JsonProperty("agent_id")
    private String agentId;

    private String cid;

    private String job;

    private Integer index;

    private String id;

    @JsonProperty("expects_vm")
    private Boolean expectsVm;
}
