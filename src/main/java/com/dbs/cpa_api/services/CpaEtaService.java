package com.dbs.cpa_api.services;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.models.CpaEta;

import java.util.List;

public interface CpaEtaService {

    List<CpaEtaDto> fetchEta(CpaEtaRequest cpaEtaRequest);
}
