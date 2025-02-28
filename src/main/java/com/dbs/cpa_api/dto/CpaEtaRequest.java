package com.dbs.cpa_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CpaEtaRequest {

    private String businessDate;

    private String entity;

    private String system;
}
