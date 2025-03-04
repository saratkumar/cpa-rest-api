package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CpaJobStatusRepository extends JpaRepository<CpaJobStatus, Long> {

    
    List<CpaJobStatus> findByBusinessDate(String businessDate);

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY business_date, system, target ORDER BY id desc) AS rn FROM cpa_job_status WHERE business_date = :businessDate AND system IN :systems AND entity = :entity AND target IS NOT NULL) t WHERE rn = 1", nativeQuery = true)
    List<CpaJobStatus> getJobStatusBySystems(String businessDate, String entity, String[] systems);

    CpaJobStatus findTopByOrderByIdDesc();

}
