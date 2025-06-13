package com.intelijake.mall.pojo.query;

import lombok.Data;

@Data
public class AdminQuery {
    private Integer page;
    private Integer limit;
    private String username;
    private String email;
}
