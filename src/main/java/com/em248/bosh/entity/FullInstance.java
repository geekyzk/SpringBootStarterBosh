package com.em248.bosh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
@Data
public class FullInstance {

    @JsonProperty("agent_id")
    private String agentId;

    @JsonProperty("vm_cid")
    private String vmCid;

    @JsonProperty("resource_pool")
    private String resourcePool;

    @JsonProperty("disk_cid")
    private String diskCid;

    @JsonProperty("jon_name")
    private String jobName;

    private Integer index;

    @JsonProperty("resurrection_paused")
    private Boolean resurrectionPaused;

    @JsonProperty("job_state")
    private String jobState;

    @JsonProperty("ips")
    private List<String> ips;

    @JsonProperty("dns")
    private List<String> dns;

    private String id;

    private Boolean bootstrap;

    private Boolean ignore;

    private String az;

    @JsonProperty("vm_type")
    private String vmType;

    private String vitals;

    private List<String> processes;

    private String state;

}
