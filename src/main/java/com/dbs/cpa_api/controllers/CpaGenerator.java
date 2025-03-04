package com.dbs.cpa_api.controllers;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.services.CpaEtaService;
import com.dbs.cpa_api.services.CpaGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CpaGenerator {
    @Autowired
    CpaGeneratorService cpaGeneratorService;

    @Autowired
    CpaEtaService cpaEtaService;

    @PostMapping("/critical-path")
    public List<CriticalPathAnalysisOutputDataDto> generateCpaData(@Valid @RequestBody CpaGeneratorRequest cpaGeneratorRequest) {
        return cpaGeneratorService.generateCPAPath(cpaGeneratorRequest);
    }


}
