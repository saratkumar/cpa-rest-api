package com.dbs.cpa_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CriticalPathAnalysisOutputDataDto {

    private String sourceSystem;

    private String businessDate;

    private String target;

    private String source;

    private int duration;

    private String legs;

    private String entity;



}
