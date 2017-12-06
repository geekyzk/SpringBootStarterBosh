package com.em248.bosh.entity;

import lombok.Data;

/**
 * Created by Administrator on 2017/1/12.
 */
@Data
public class Task {

    private Integer id;

    private String state;

    private String description;

    private Long timestamp;

    private String result;

    private String user;

    private String contextId;

}
