package com.dbs.cpa_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class CriticalPathAnalysisOutputData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String sourceSystem;

    private String businessDate;

    private String target;

    private String source;

    private int duration;

    private int legs;

    private String entity;

}
