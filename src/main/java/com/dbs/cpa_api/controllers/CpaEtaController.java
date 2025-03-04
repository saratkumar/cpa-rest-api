package com.dbs.cpa_api.controllers;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.dto.CpaJobHistoryDto;
import com.dbs.cpa_api.dto.JobHistoryRequest;
import com.dbs.cpa_api.services.CpaEtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = {}, allowCredentials = "false") // Disable CORS for this controller
@RestController
public class CpaEtaController {
    @Autowired
    CpaEtaService cpaEtaService;

    @PostMapping("/get-eta")
    public List<CpaEtaDto> getEta(@RequestBody CpaEtaRequest cpaEtaRequest) {
        return cpaEtaService.fetchEta(cpaEtaRequest);
    }

    @PostMapping("/job-history")
    public CpaJobHistoryDto getListEta(@RequestBody JobHistoryRequest cpaHistoricalRequest) {
        return cpaEtaService.getJobHistory(cpaHistoricalRequest);
    }

}
