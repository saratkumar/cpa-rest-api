package com.dbs.cpa_api.services;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CpaGeneratorService {

    public List<CriticalPathAnalysisOutputDataDto> generateCPAPath(CpaGeneratorRequest cpaGeneratorRequest);
}
