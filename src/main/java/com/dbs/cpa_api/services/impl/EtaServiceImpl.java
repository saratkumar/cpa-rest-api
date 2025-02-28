package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.mapper.CpaEtaMapper;
import com.dbs.cpa_api.models.CpaEta;
import com.dbs.cpa_api.repository.CpaEtaRespository;
import com.dbs.cpa_api.services.CpaEtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EtaServiceImpl implements CpaEtaService {

    @Autowired
    CpaEtaRespository cpaEtaRespository;


    @Override
    public List<CpaEtaDto> fetchEta(CpaEtaRequest cpaEtaRequest) {
        List<CpaEta> cpaEtas = cpaEtaRespository.findFirstAndLastRecords(
                cpaEtaRequest.getSystem(),
                cpaEtaRequest.getBusinessDate(),
                cpaEtaRequest.getEntity()
        );

        return CpaEtaMapper.INSTANCE.toDTO(cpaEtas);
    }
}
