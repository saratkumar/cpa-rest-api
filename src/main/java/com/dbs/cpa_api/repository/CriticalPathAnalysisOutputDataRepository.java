package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CriticalPathAnalysisOutputDataRepository extends JpaRepository<CriticalPathAnalysisOutputData, Long> {

    List<CriticalPathAnalysisOutputData> findTop10ByBusinessDateAndTargetAndEntityAndSourceSystemInOrderByDurationDesc(String businessDate, String jobName, String entity, String[] systems);

}
