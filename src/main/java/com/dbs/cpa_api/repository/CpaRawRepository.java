package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CpaRawRepository extends JpaRepository<CpaRaw, Long> {

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY job_name, business_date, app_code ORDER BY updated_at DESC) AS rn FROM cpa_raw WHERE business_date = :businessDate AND app_code IN :appCode AND entity = :entity AND job_name IN :jobNames) t WHERE rn = 1", nativeQuery = true)
    List<CpaRaw> findByBusinessDateAndAppCodeAndEntityAndJobName(@Param("businessDate") String businessDate, @Param("appCode") String[] appCode, @Param("entity") String entity, @Param("jobNames") List<String> jobNames);

}