package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.mapper.CPAOutputDataMapper;
import com.dbs.cpa_api.models.CpaRootNodeConfig;
import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import com.dbs.cpa_api.repository.CpaRootNodeConfigRepository;
import com.dbs.cpa_api.repository.CriticalPathAnalysisOutputDataRepository;
import com.dbs.cpa_api.services.CpaGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CpaGeneratorImplService implements CpaGeneratorService {
    @Autowired
    CriticalPathAnalysisOutputDataRepository cpaOutputDataRepo;

    @Autowired
    CpaRootNodeConfigRepository cpaRootNodeConfigRepository;

//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public List<CriticalPathAnalysisOutputDataDto> generateCPAPath(CpaGeneratorRequest cpaGeneratorRequest) {
        List<CriticalPathAnalysisOutputDataDto> criticalPathAnalysisOutputDataDtos = null;
        try {
//            Date date = formatter.parse(cpaGeneratorRequest.getBusinessDate());
            String[] systems = {cpaGeneratorRequest.getSystem()};
            if(cpaGeneratorRequest.getSystem().toLowerCase().contains("all")) {
               systems = fetchDependentSystems(cpaGeneratorRequest.getSystem());
            }
            List<CriticalPathAnalysisOutputData> criticalPathAnalysisOutputData =
                    cpaOutputDataRepo.findTop10ByBusinessDateAndTargetAndEntityAndSourceSystemInOrderByDurationDesc(
                            cpaGeneratorRequest.getBusinessDate(),
                            cpaGeneratorRequest.getJobName(),
                            cpaGeneratorRequest.getEntity(),
                            systems
                    );
            criticalPathAnalysisOutputDataDtos = CPAOutputDataMapper.INSTANCE.toDTO(criticalPathAnalysisOutputData);

            criticalPathAnalysisOutputDataDtos.stream().forEach(System.out::println);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return criticalPathAnalysisOutputDataDtos;
    }

    private String[] fetchDependentSystems(String system) {
        String[] systems = system.split("-");
        Optional<CpaRootNodeConfig> cpaRootNodeConfig= cpaRootNodeConfigRepository.getCpaRootNodeConfigBySystem(systems[0]);
        String sysList = cpaRootNodeConfig.get().getPredecessorSystems();
        return sysList != null ? (sysList+","+systems[0]).split(",") : new String[0];
    }

}
