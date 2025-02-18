package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CpaRawRepository extends JpaRepository<CpaRaw, Long> {

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY business_date, system, jobname ORDER BY updated_at DESC) AS rn FROM cpa_raw WHERE business_date = :businessDate AND system IN :appCode AND entity = :entity AND jobname IS NOT NULL) t WHERE rn = 1", nativeQuery = true)
    List<CpaRaw> getCpaRaws(@Param("businessDate") String businessDate, @Param("appCode") String[] appCode, @Param("entity") String entity);

    Optional<CpaRaw> findByBusinessDateAndAppCodeInAndEntityAndJobNameIn(String businessDate, String[] systems, String entity, List<String> jobNames);


    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY business_date, system, jobname ORDER BY updated_at DESC) AS rn FROM cpa_raw WHERE business_date = :businessDate AND system IN :appCode AND entity = :entity AND jobname in :jobName) t WHERE rn = 1", nativeQuery = true)
    List<CpaRaw> getCpaRawsWithJobName(@Param("businessDate") String businessDate, @Param("appCode") String[] appCode, @Param("entity") String entity, @Param("jobName") List<String> jobName);


}
