package com.dbs.cpa_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@Entity
public class CpaEta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="system")
    private String appCode;

    private Time startDelay;

    private Time endDelay;

    @Column(name="jobname")
    private String jobName;

    private String entity;

    private String businessDate;

    @OneToOne
    @JoinColumn(name="cpa_raw_id", referencedColumnName = "id")
    private CpaRaw cpaRaw;

    @OneToOne
    @JoinColumn(name="eta_config_id", referencedColumnName = "id")
    private CpaEtaConfig cpaEtaConfig;
}
