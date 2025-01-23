package com.dbs.cpa_api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class CpaGeneratorRequest {

    @NotBlank(message = "Job Name is required.")
    private String jobName;

    @NotBlank(message = "Business Date is required and should use pattern yyyyMMdd")
    private String businessDate;

    @NotBlank(message = "System is required.")
    private String system;

    @NotBlank(message = "Entity is required.")
    private String entity;
}
