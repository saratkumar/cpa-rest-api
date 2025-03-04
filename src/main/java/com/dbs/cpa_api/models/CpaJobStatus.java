package com.dbs.cpa_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CpaJobStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="system")
    private String appCode;

    private String businessDate;

    private String status;

    private String target;

    private String entity;

}
