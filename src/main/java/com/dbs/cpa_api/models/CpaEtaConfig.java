package com.dbs.cpa_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Entity
public class CpaEtaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="system")
    private String appCode;

    private Time startTime;

    private Time endTime;

    @Column(name="jobname")
    private String jobName;

    private String entity;


}
