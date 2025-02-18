package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.mapper.CPAOutputDataMapper;
import com.dbs.cpa_api.models.*;
import com.dbs.cpa_api.repository.*;
import com.dbs.cpa_api.services.CpaGeneratorService;
import com.dbs.cpa_api.services.ProcessWatcherServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

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

    Logger logger = LoggerFactory.getLogger(CpaGeneratorImplService.class);

    Boolean isForDependentSystems = false;

    @Override
    public List<CriticalPathAnalysisOutputDataDto> generateCPAPath(CpaGeneratorRequest cpaGeneratorRequest) {
        List<CriticalPathAnalysisOutputDataDto> criticalPathAnalysisOutputDataDtos = null;
        try {
            String[] systems = {cpaGeneratorRequest.getSystem()};
            List<String> jobNames = new ArrayList<>();

            /* Check if process need to process dependent systems **/
            isForDependentSystems = cpaGeneratorRequest.getSystem().toLowerCase().contains("all");
            if(isForDependentSystems) {
                systems = fetchDependentSystems(cpaGeneratorRequest.getSystem());
                /* Fetch jobnames from cpa root node config*/
                jobNames = getJobNames(systems);
                jobNames.add(cpaGeneratorRequest.getJobName());
                Boolean isDependencyProcessed = processDependentSystem(systems, jobNames, cpaGeneratorRequest);
                if(!isDependencyProcessed) return criticalPathAnalysisOutputDataDtos;
            } else {
                jobNames.add(cpaGeneratorRequest.getJobName());
            }

            if(cpaGeneratorRequest.getIsDefault() == null || !cpaGeneratorRequest.getIsDefault()) {
                List<CpaRaw> cpaRaws = fetchCpaRaws(cpaGeneratorRequest, jobNames, systems);
                if(!cpaRaws.isEmpty()) {
                    Boolean isProcessed = processWatcherServiceRequest.processCpaIntermediate(cpaRaws, cpaGeneratorRequest);
                    if(isProcessed) {
                        criticalPathAnalysisOutputDataDtos = fetchCpaOutputRecords(cpaGeneratorRequest, systems);
                        addJobStatusEntry(cpaGeneratorRequest);
                    }
                }
            } else { // If request comes from UI and if default is true then just return data from cpa_output table
                criticalPathAnalysisOutputDataDtos =  fetchCpaOutputRecords(cpaGeneratorRequest, systems);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return criticalPathAnalysisOutputDataDtos;
    }

    /**
     * process the dependent system and its jobstatus to trigger the master system process
     * @param systems
     * @param jobNames
     * @param cpaGeneratorRequest
     * @return
     */

    private Boolean processDependentSystem(String[] systems, List<String> jobNames, CpaGeneratorRequest cpaGeneratorRequest) {
        Boolean status = checkDependencySystemJobStatus(cpaGeneratorRequest, systems);
        if(!status) {
            logger.warn("Dependency Systems are not processed yet for the business date" + cpaGeneratorRequest.getBusinessDate());
            return false;
        }



        List<CpaDefaultConnectionJobsConfigs> cpaDefaultConnectionJobsConfigs = cpaDefaultConJobsConfigRepository.findBySourceSystemIn(systems);
        for (CpaDefaultConnectionJobsConfigs connectionJobsConfig : cpaDefaultConnectionJobsConfigs) {
            configMap.put(connectionJobsConfig.getSourceSystem(), connectionJobsConfig.getTarget());
        }

        return true;
    }

    /**
     * Fetch dependent systems against the requested system
     * @param system
     * @return
     */
    private String[] fetchDependentSystems(String system) {
        String[] systems = system.split("-");
        Optional<CpaRootNodeConfig> cpaRootNodeConfig = cpaRootNodeConfigRepository.getCpaRootNodeConfigBySystem(systems[0]);
        String sysList = cpaRootNodeConfig.get().getPredecessorSystems();
        return sysList != null ? (systems[0] + ";" +sysList).split(";") : new String[0];
    }

    /**
     * Check status of each slave system before processing the master System
     * @param cpaGeneratorRequest
     * @param systems
     * @return
     */

    private Boolean checkDependencySystemJobStatus(CpaGeneratorRequest cpaGeneratorRequest, String[] systems) {
        List<CpaJobStatus> cpaJobStatusList = cpaJobStatusRepository.getJobStatusBySystems(cpaGeneratorRequest.getBusinessDate(),cpaGeneratorRequest.getEntity(), systems);
        return cpaJobStatusList.size() == systems.length;
    }

    /**
     * fetch job names from root node config table against the system
     * @param systems
     * @return
     */

    private List<String> getJobNames(String[] systems) {
        String[] newArray = Arrays.copyOfRange(systems, 1, systems.length);
        List<CpaRootNodeConfig> cpaRootNodeConfigs = cpaRootNodeConfigRepository.findBySystemIn(newArray);
//        return cpaRootNodeConfigs.stream().map(e -> e.getJobName()).collect(Collectors.toList());
        return cpaRootNodeConfigs.stream().map(e -> e.getLastJob()).collect(Collectors.toList());
    }

    /***
     * fetch records from cpa_raw table to feed into cpa_intermediate program
     * @param request
     * @param jobName
     * @param systems
     * @return
     * @throws Exception
     */

    private List<CpaRaw> fetchCpaRaws(CpaGeneratorRequest request, List<String> jobName, String[] systems) throws Exception {


        if(checkIfJobPresent(request, jobName, systems)) {
            List<CpaRaw> cpaRaws  = cpaRawRepository.getCpaRaws(request.getBusinessDate(),systems, request.getEntity());

            if(isForDependentSystems) {
                // Chaining the different systems together to create a path
                for(int i=systems.length-1;i>0;i--) {
                    int p =i;
                    cpaRaws.stream().filter(c -> c.getAppCode().equals(systems[p]) && (c.getSuccessorDependencies() == null || c.getSuccessorDependencies().isEmpty())).forEach(x -> x.setSuccessorDependencies(configMap.get(systems[p])));
                }
            }
            return cpaRaws;
        } else {
            throw new Exception("Job name(s) is not present for the given business date");
        }
    }

    /**
     * Check whether job is available or not
     *
     * If job is available then get the records from the cpa raw else throw error that mention job is not availble for the day
     */

    private Boolean checkIfJobPresent(CpaGeneratorRequest request, List<String> jobName, String[] systems) {
        List<CpaRaw> cpaRaw = cpaRawRepository.getCpaRawsWithJobName(
                request.getBusinessDate(),
                systems,
                request.getEntity(),
                jobName
        );

        return cpaRaw.size() == jobName.size();

    }

    private List<CriticalPathAnalysisOutputDataDto> fetchCpaOutputRecords(CpaGeneratorRequest cpaGeneratorRequest, String[] systems) {
        List<CriticalPathAnalysisOutputData> criticalPathAnalysisOutputData =
                cpaOutputDataRepo.findTop10ByBusinessDateAndTargetAndEntityAndSysParamOrderByDurationDesc(
                        cpaGeneratorRequest.getBusinessDate(),
                        cpaGeneratorRequest.getJobName(),
                        cpaGeneratorRequest.getEntity(),
                        cpaGeneratorRequest.getSystem()
                );
        return CPAOutputDataMapper.INSTANCE.toDTO(criticalPathAnalysisOutputData);
    }

    /**
     * Update generate request status in Job Status table if request is not Default
     * @param cpaGeneratorRequest
     */
    private void addJobStatusEntry(CpaGeneratorRequest cpaGeneratorRequest) {
        CpaJobStatus cpaJobStatus = new CpaJobStatus();
        cpaJobStatus.setAppCode(cpaGeneratorRequest.getSystem());
        cpaJobStatus.setBusinessDate(cpaGeneratorRequest.getBusinessDate());
        cpaJobStatus.setTarget(cpaGeneratorRequest.getJobName());
        cpaJobStatus.setStatus("Y");
        cpaJobStatus.setEntity(cpaGeneratorRequest.getEntity());
        cpaJobStatusRepository.save(cpaJobStatus);
    }

}
