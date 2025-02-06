package com.dbs.cpa_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpaDefaultConJobConfigDto {

    private long id;

    private String sourceSystem;

    private String entity;

    private String source;

    private String targetSystem;

    private String target;

    private String lastJob;
}
