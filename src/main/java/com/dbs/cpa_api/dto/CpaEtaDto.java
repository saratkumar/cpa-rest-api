package com.dbs.cpa_api.dto;

import com.dbs.cpa_api.models.CpaEtaConfig;
import com.dbs.cpa_api.models.CpaRaw;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class CpaEtaDto {

    private long id;

    private String appCode;

    private Time startDelay;

    private Time endDelay;

    private String jobName;

    private String entity;

    private String businessDate;

    private CpaRaw cpaRaw;

    private CpaEtaConfig cpaEtaConfig;
}
