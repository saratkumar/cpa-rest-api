package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.models.CpaIntermediate;
import com.dbs.cpa_api.models.CpaRaw;
import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import com.dbs.cpa_api.repository.CpaIntermediateRespository;
import com.dbs.cpa_api.repository.CriticalPathAnalysisOutputDataRepository;
import com.dbs.cpa_api.services.ProcessWatcherServiceRequest;
import com.dbs.cpa_api.utils.CpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessWatcherServiceRequestImpl implements ProcessWatcherServiceRequest {

    @Autowired
    CpaIntermediateRespository cpaIntermediateRespository;

    @Autowired
    CriticalPathAnalysisOutputDataRepository cpaOutputRepository;

    @Override
    public Boolean processCpaIntermediate(List<CpaRaw> cpaRaws) {
        Map<String, List<String[]>> cpaIntermediaOutput = dummyMethod();

        Map.Entry<String, List<String[]>> firstEntry = cpaIntermediaOutput.entrySet().iterator().next();
        List<CpaIntermediate> cpaIntermediates = new ArrayList<>();
        for(int i=2; i<firstEntry.getValue().size(); i++) {
            String[] jobInfo = firstEntry.getValue().get(i);
            String[] jobDetails = CpaUtil.getJobDetails(jobInfo[0]);
            String system = jobDetails[1];
            String predecssorJob = jobInfo[1];
            int duration = Integer.valueOf(jobInfo[2]);
            CpaIntermediate cpaIntermediate = new CpaIntermediate();
            cpaIntermediate.setAppCode(system);
            cpaIntermediate.setBusinessDate(firstEntry.getKey());
            cpaIntermediate.setJobName(jobInfo[0]);
            cpaIntermediate.setPredecessorJob(predecssorJob);
            cpaIntermediate.setDuration(duration);
            cpaIntermediates.add(cpaIntermediate);
        }

        cpaIntermediateRespository.saveAll(cpaIntermediates);

        return processCpaOutput(cpaIntermediaOutput);

    }

    private Map<String, List<String[]>> dummyMethod() {

        Map<String, List<String[]>> r = new HashMap<>();


        List<String[]> list1 = new ArrayList<>();
        list1.add(new String[]{"10"}); //distinct job
        list1.add(new String[]{"15"}); // total job
        list1.add(new String[]{"Job_Name_OFP", "Job_Name2_LCRS", "10"});  // Jobname, Predecessor Job, duration
        list1.add(new String[]{"Job_Name_OFP", "Job_Name2_LCRS", "12"});

        r.put("20240716",  list1);

        return r;
    }


    @Override
    public Boolean processCpaOutput(Map<String, List<String[]>> cpaIntermediaOutput) {

        Map<String, List<String>> cpaOutput = dummyMethod1();

        Map.Entry<String, List<String>> firstEntry = cpaOutput.entrySet().iterator().next();
        List<CriticalPathAnalysisOutputData> criticalPathAnalysisOutputDatas = new ArrayList<>();
        for(int i=0;i<firstEntry.getValue().size();i++) {
            String legInfo = firstEntry.getValue().get(i);
            int index = legInfo.indexOf(")");
            String sourceTarget = legInfo.substring(0, index);
            String[] sourceTargetInfo = sourceTarget.split(" ");
            String source = sourceTargetInfo[0];
            String target = sourceTargetInfo[2];
            int duration = (int) Float.parseFloat(sourceTargetInfo[3].replace("(", ""));
            String[] sourceDetails = CpaUtil.getJobDetails(source);
            CriticalPathAnalysisOutputData criticalPathAnalysisOutputData = new CriticalPathAnalysisOutputData();
            criticalPathAnalysisOutputData.setSourceSystem(sourceDetails[1]);
            criticalPathAnalysisOutputData.setBusinessDate(firstEntry.getKey());
            criticalPathAnalysisOutputData.setDuration(duration);
            criticalPathAnalysisOutputData.setSource(source);
            criticalPathAnalysisOutputData.setTarget(target);
            criticalPathAnalysisOutputData.setLegs(legInfo);
            criticalPathAnalysisOutputDatas.add(criticalPathAnalysisOutputData);
        }

        List<CriticalPathAnalysisOutputData> savedData =  cpaOutputRepository.saveAll(criticalPathAnalysisOutputDatas);
        return savedData.size() == criticalPathAnalysisOutputDatas.size();
    }

    private Map<String, List<String>> dummyMethod1() {

        List<String> list1 = new ArrayList<>();

        list1.add("job1 to job10 (6.0) job1 -> job2 1.0");
        list1.add("job4 to job5 (6.0) job4 -> job5 2.0");

        return Map.of("20240716",list1);
    }
}
