package com.dbs.cpa_api.services;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.dto.CpaJobHistoryDto;
import com.dbs.cpa_api.dto.JobHistoryRequest;

import java.util.List;

public interface CpaEtaService {

    List<CpaEtaDto> fetchEta(CpaEtaRequest cpaEtaRequest);


    List<CpaJobHistoryDto> getJobHistory(JobHistoryRequest jobHistoryRequest);

}
