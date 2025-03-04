package com.dbs.cpa_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobHistoryRequest {

    private String entity;

    private String system;

    private String startDate;

    private String endDate;

    private String jobName;
}
