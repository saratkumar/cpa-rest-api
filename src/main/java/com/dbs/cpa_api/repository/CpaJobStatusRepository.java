package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpaJobStatusRepository extends JpaRepository<CpaJobStatus, Long> {

    List<CpaJobStatus> findByBusinessDate(String businessDate);

}
