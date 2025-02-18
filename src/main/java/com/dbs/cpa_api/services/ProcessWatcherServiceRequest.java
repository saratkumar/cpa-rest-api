package com.dbs.cpa_api.services;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.models.CpaRaw;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProcessWatcherServiceRequest {

    public Boolean processCpaIntermediate(List<CpaRaw> cpaRaws, CpaGeneratorRequest cpaGeneratorRequest);

    Boolean processCpaOutput(Map<String, List<String[]>> cpaIntermediaOutput, CpaGeneratorRequest cpaGeneratorRequest);
}
