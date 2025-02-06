package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.mapper.CPAOutputDataMapper;
import com.dbs.cpa_api.models.*;
import com.dbs.cpa_api.repository.*;
import com.dbs.cpa_api.services.CpaGeneratorService;
import com.dbs.cpa_api.services.ProcessWatcherServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CpaGeneratorImplService implements CpaGeneratorService {
    @Autowired
    CriticalPathAnalysisOutputDataRepository cpaOutputDataRepo;

    @Autowired
    CpaRootNodeConfigRepository cpaRootNodeConfigRepository;

    @Autowired
    CpaRawRepository cpaRawRepository;

    @Autowired
    CpaDefaultConJobsConfigRepository cpaDefaultConJobsConfigRepository;

    @Autowired
    ProcessWatcherServiceRequest processWatcherServiceRequest;

    @Autowired
    CpaJobStatusRepository cpaJobStatusRepository;

    Map<String, String> configMap = new HashMap<>();

    @Override
    public List<CriticalPathAnalysisOutputDataDto> generateCPAPath(CpaGeneratorRequest cpaGeneratorRequest) {
        List<CriticalPathAnalysisOutputDataDto> criticalPathAnalysisOutputDataDtos = null;
        try {
            String[] systems = {cpaGeneratorRequest.getSystem()};
            List<String> jobNames = new ArrayList<>();
            Boolean isStandalone = cpaGeneratorRequest.getSystem().toLowerCase().contains("all");
            if (isStandalone) {
                systems = fetchDependentSystems(cpaGeneratorRequest.getSystem());
                List<CpaDefaultConnectionJobsConfigs> cpaDefaultConnectionJobsConfigs = cpaDefaultConJobsConfigRepository.findBySourceSystemIn(systems);
                for (CpaDefaultConnectionJobsConfigs connectionJobsConfig : cpaDefaultConnectionJobsConfigs) {
                    jobNames.add(connectionJobsConfig.getLastJob());
                    configMap.put(connectionJobsConfig.getSourceSystem(), connectionJobsConfig.getLastJob());
                }

            }
            jobNames.add(cpaGeneratorRequest.getJobName());
            List<CpaRaw> cpaRaws = fetchCpaRaws(cpaGeneratorRequest.getBusinessDate(), jobNames, cpaGeneratorRequest.getEntity(), systems);

            Boolean isProcessed = processWatcherServiceRequest.processCpaIntermediate(cpaRaws);

            if (isProcessed && cpaGeneratorRequest.getIsDefault() == null) {
                List<CriticalPathAnalysisOutputData> criticalPathAnalysisOutputData =
                        cpaOutputDataRepo.findTop10ByBusinessDateAndTargetAndEntityAndSourceSystemInOrderByDurationDesc(
                                cpaGeneratorRequest.getBusinessDate(),
                                cpaGeneratorRequest.getJobName(),
                                cpaGeneratorRequest.getEntity(),
                                systems
                        );
                criticalPathAnalysisOutputDataDtos = CPAOutputDataMapper.INSTANCE.toDTO(criticalPathAnalysisOutputData);
            }
            // Updating the Job Status table after completion of standalone generation
            if(isStandalone) {

                CpaJobStatus cpaJobStatus = new CpaJobStatus();
                cpaJobStatus.setAppCode(cpaGeneratorRequest.getSystem());
                cpaJobStatus.setBusinessDate(cpaGeneratorRequest.getBusinessDate());
                cpaJobStatus.setTargetJob(cpaGeneratorRequest.getJobName());
                cpaJobStatus.setStatus("completed");
                cpaJobStatusRepository.save(cpaJobStatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return criticalPathAnalysisOutputDataDtos;
    }


    private List<CpaRaw> fetchCpaRaws(String businessDate, List<String> jobName, String entity, String[] systems) {
        List<CpaRaw> cpaRaws = cpaRawRepository.findByBusinessDateAndAppCodeAndEntityAndJobName(
                businessDate,
                systems,
                entity,
                jobName
        );

        // Chaining the different systems together to create a path
        configMap.entrySet().stream().forEach(e -> {
            cpaRaws.stream().filter(c -> c.getAppCode().equals(e.getKey()) && c.getSuccessorDependencies() == null).forEach(x -> x.setSuccessorDependencies(e.getValue()));
        });

        return cpaRaws;
    }


    private String[] fetchDependentSystems(String system) {
        String[] systems = system.split("-");
        Optional<CpaRootNodeConfig> cpaRootNodeConfig = cpaRootNodeConfigRepository.getCpaRootNodeConfigBySystem(systems[0]);
        String sysList = cpaRootNodeConfig.get().getPredecessorSystems();
        return sysList != null ? (sysList + "," + systems[0]).split(",") : new String[0];
    }

}
