package com.dbs.cpa_api.mapper;

import com.dbs.cpa_api.dto.CpaEtaDto;
import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.models.CpaEta;
import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CpaEtaMapper {

    CpaEtaMapper INSTANCE = Mappers.getMapper(CpaEtaMapper.class);

    CpaEta toEntity(CpaEtaDto cpaEtaDto);

    CpaEtaDto toDTO(CpaEta cpaEta);

    List<CpaEta> toEntity(List<CpaEtaDto> cpaEtaDto);

    List<CpaEtaDto> toDTO(List<CpaEta> cpaEtas);

}
