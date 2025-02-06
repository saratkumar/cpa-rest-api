package com.dbs.cpa_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CpaDefaultConnectionJobsConfigs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String sourceSystem;

    private String entity;

    private String source;

    private String targetSystem;

    private String target;

    private String lastJob;
}
