package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaEta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CpaEtaRespository extends JpaRepository<CpaEta, Long> {

    @Query("SELECT e FROM CpaEta e " +
            "WHERE e.id IN ( " +
            "   (SELECT MIN(e2.id) FROM CpaEta e2 WHERE e2.appCode = :appCode AND e2.businessDate = :businessDate AND e2.entity = :entity), " +
            "   (SELECT MAX(e2.id) FROM CpaEta e2 WHERE e2.appCode = :appCode AND e2.businessDate = :businessDate AND e2.entity = :entity) " +
            ")")
    List<CpaEta> findFirstAndLastRecords(@Param("appCode") String appCode,
                                         @Param("businessDate") String businessDate,
                                         @Param("entity") String entity);

    List<CpaEta> findByJobNameAndAppCodeAndEntityAndBusinessDateBetween(String jobName, String appCode, String entity, String startDate, String endDate);

}
