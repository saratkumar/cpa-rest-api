package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaDefaultConnectionJobsConfigs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpaDefaultConJobsConfigRepository extends JpaRepository<CpaDefaultConnectionJobsConfigs, Long> {

    List<CpaDefaultConnectionJobsConfigs> findBySourceSystemIn(String[] systems);
}