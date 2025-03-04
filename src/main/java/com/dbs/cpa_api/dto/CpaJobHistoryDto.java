package com.dbs.cpa_api.dto;

import com.dbs.cpa_api.models.CpaEtaConfig;
import com.dbs.cpa_api.models.CpaRaw;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CpaJobHistoryDto {
    List<CpaEtaHistory> cpaEtaHistories;

    private Time startTime;

    private Time endTime;

    @Getter
    @Setter
    public static class CpaEtaHistory {
        private long id;

        private String businessDate;

        private LocalDateTime startDateTime;

        private LocalDateTime EndDateTime;

        private Time startDelay;

        private Time endDelay;
    }



}
