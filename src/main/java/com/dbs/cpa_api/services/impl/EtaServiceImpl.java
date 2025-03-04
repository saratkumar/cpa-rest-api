package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CpaEtaRequest;
import com.dbs.cpa_api.dto.CpaJobHistoryDto;
import com.dbs.cpa_api.dto.JobHistoryRequest;
import com.dbs.cpa_api.mapper.CpaEtaMapper;
import com.dbs.cpa_api.models.CpaEta;
import com.dbs.cpa_api.repository.CpaEtaRespository;
import com.dbs.cpa_api.services.CpaEtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    @Override
    public List<CpaJobHistoryDto>  getJobHistory(JobHistoryRequest jobHistoryRequest) {

      List<CpaEta> cpaEtas = cpaEtaRespository.findByJobNameAndAppCodeAndEntityAndBusinessDateBetween(
              jobHistoryRequest.getJobName(),
              jobHistoryRequest.getSystem(),
              jobHistoryRequest.getEntity(),
              jobHistoryRequest.getStartDate(),
              jobHistoryRequest.getEndDate()
      );
      List<CpaJobHistoryDto> cpaJobHistoryDtos = new ArrayList<>(cpaEtas.size());
      cpaEtas.forEach(e -> {
          CpaJobHistoryDto cpaJobHistoryDto = new CpaJobHistoryDto();
          cpaJobHistoryDto.setBusinessDate(e.getBusinessDate());
          cpaJobHistoryDto.setStartDateTime(e.getCpaRaw().getJobStartDateTime());
          cpaJobHistoryDto.setEndDateTime(e.getCpaRaw().getJobEndDateTime());
          cpaJobHistoryDto.setStartDelay(e.getStartDelay());
          cpaJobHistoryDto.setEndDelay(e.getEndDelay());
          cpaJobHistoryDto.setId(e.getId());
          cpaJobHistoryDtos.add(cpaJobHistoryDto);
      });

      return cpaJobHistoryDtos;

    }
}
