package com.dbs.cpa_api.repository;

import com.dbs.cpa_api.models.CpaRootNodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CpaRootNodeConfigRepository extends JpaRepository<CpaRootNodeConfig, Long> {

    public Optional<CpaRootNodeConfig> getCpaRootNodeConfigBySystem(String system);

}
