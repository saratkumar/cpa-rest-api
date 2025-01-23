package com.dbs.cpa_api.mapper;

import com.dbs.cpa_api.dto.CriticalPathAnalysisOutputDataDto;
import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CPAOutputDataMapper {

    CPAOutputDataMapper INSTANCE = Mappers.getMapper(CPAOutputDataMapper.class);

    CriticalPathAnalysisOutputData toEntity(CriticalPathAnalysisOutputDataDto criticalPathAnalysisOutputDataDto);

    CriticalPathAnalysisOutputDataDto toDTO(CriticalPathAnalysisOutputData criticalPathAnalysisOutputData);

    List<CriticalPathAnalysisOutputData> toEntity(List<CriticalPathAnalysisOutputDataDto> criticalPathAnalysisOutputDataDtos);

    List<CriticalPathAnalysisOutputDataDto> toDTO(List<CriticalPathAnalysisOutputData> cpaRaw);

}
