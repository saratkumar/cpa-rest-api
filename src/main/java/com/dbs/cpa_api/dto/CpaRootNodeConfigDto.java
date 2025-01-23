package com.dbs.cpa_api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpaRootNodeConfigDto {


    private long id;

    private String system;

    private String JobName;

    private String predecessorSystems;
}
