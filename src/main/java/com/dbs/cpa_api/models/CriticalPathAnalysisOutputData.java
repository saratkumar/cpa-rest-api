package com.dbs.cpa_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="cpa_output")
public class CriticalPathAnalysisOutputData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String sourceSystem;

    private String businessDate;

    private String target;

    private String source;

    private int duration;

    private String legs;

    private String entity;

    private String sysParam;

}
