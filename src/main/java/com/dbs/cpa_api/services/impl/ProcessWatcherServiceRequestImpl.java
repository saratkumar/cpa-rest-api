package com.dbs.cpa_api.services.impl;

import com.dbs.cpa_api.dto.CpaGeneratorRequest;
import com.dbs.cpa_api.models.CpaIntermediate;
import com.dbs.cpa_api.models.CpaRaw;
import com.dbs.cpa_api.models.CriticalPathAnalysisOutputData;
import com.dbs.cpa_api.repository.CpaIntermediateRespository;
import com.dbs.cpa_api.repository.CriticalPathAnalysisOutputDataRepository;
import com.dbs.cpa_api.services.ProcessWatcherServiceRequest;
import com.dbs.cpa_api.utils.CpaUtil;
import com.dbs.orwb.AcyclicLP;
import com.dbs.orwb.CPAInputProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProcessWatcherServiceRequestImpl implements ProcessWatcherServiceRequest {

    @Autowired
    CpaIntermediateRespository cpaIntermediateRespository;

    @Autowired
    CriticalPathAnalysisOutputDataRepository cpaOutputRepository;

    CPAInputProcessor cpaInputProcessor;
    ProcessWatcherServiceRequestImpl() {
        cpaInputProcessor = new CPAInputProcessor();
    }

    @Override
    public Boolean processCpaIntermediate(List<CpaRaw> cpaRaws, CpaGeneratorRequest cpaGeneratorRequest) {
        List<String[]> list = new ArrayList<>();

        for(CpaRaw cpaRaw: cpaRaws) {
            list.add(new String[] {cpaRaw.getAppCode(), cpaRaw.getBusinessDate(), cpaRaw.getJobName(), cpaRaw.getEntity(), String.valueOf(cpaRaw.getDuration()), cpaRaw.getSuccessorDependencies()
            });
        }

        Map<String, List<String[]>> cpaIntermediaOutput = null;
        if(list.size() > 0) {
            try {
                cpaIntermediaOutput = cpaInputProcessor.processingSingleFile(list);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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

//            cpaIntermediateRespository.saveAll(cpaIntermediates);
        }else {
            System.out.println("Unable to process the request as list is empty!");
        }

        return processCpaOutput(cpaIntermediaOutput, cpaGeneratorRequest);

    }

//    private Map<String, List<String[]>> dummyMethod() {
//
//        Map<String, List<String[]>> r = new HashMap<>();
//
//
//        List<String[]> list1 = new ArrayList<>();
//        list1.add(new String[]{"10"}); //distinct job
//        list1.add(new String[]{"15"}); // total job
//        list1.add(new String[]{"Job_Name_OFP", "Job_Name2_LCRS", "10"});  // Jobname, Predecessor Job, duration
//        list1.add(new String[]{"Job_Name_OFP", "Job_Name2_LCRS", "12"});
//
//        r.put("20240716",  list1);
//
//        return r;
//    }


    @Override
    public Boolean processCpaOutput(Map<String, List<String[]>> cpaIntermediaOutput, CpaGeneratorRequest cpaGeneratorRequest) {
        Map<String, List<String>> cpaOutput = null;
        try {
           cpaOutput = AcyclicLP.getFinalResultFromAcyclicAlgo(cpaIntermediaOutput, 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        List<CriticalPathAnalysisOutputData> savedData = new ArrayList<>();
        List<CriticalPathAnalysisOutputData> criticalPathAnalysisOutputDatas = new ArrayList<>();
        if(cpaOutput != null) {
            Map.Entry<String, List<String>> firstEntry = cpaOutput.entrySet().iterator().next();
            for(int i=0;i<firstEntry.getValue().size();i++) {
                String legInfo = firstEntry.getValue().get(i);
                int index = legInfo.indexOf(")");
                String sourceTarget = legInfo.substring(0, index);
                String[] sourceTargetInfo = sourceTarget.split(" ");
                String source = sourceTargetInfo[1];
                String target = sourceTargetInfo[3];
                int duration = (int) Float.parseFloat(sourceTargetInfo[4].replace("(", ""));
                String[] sourceDetails = CpaUtil.getJobDetails(source);
                CriticalPathAnalysisOutputData criticalPathAnalysisOutputData = new CriticalPathAnalysisOutputData();
                criticalPathAnalysisOutputData.setSourceSystem(sourceDetails[1]);
                criticalPathAnalysisOutputData.setBusinessDate(firstEntry.getKey());
                criticalPathAnalysisOutputData.setDuration(duration);
                criticalPathAnalysisOutputData.setSource(source);
                criticalPathAnalysisOutputData.setTarget(target);
                criticalPathAnalysisOutputData.setLegs(legInfo);
                criticalPathAnalysisOutputData.setEntity(cpaGeneratorRequest.getEntity());
                criticalPathAnalysisOutputData.setSysParam(cpaGeneratorRequest.getSystem());
                criticalPathAnalysisOutputDatas.add(criticalPathAnalysisOutputData);
            }
            savedData =  cpaOutputRepository.saveAll(criticalPathAnalysisOutputDatas);
            return (savedData.size() == criticalPathAnalysisOutputDatas.size());
        }

        return false;

    }

//    private Map<String, List<String>> dummyMethod1() {
//
//        List<String> list1 = new ArrayList<>();
//
//        list1.add("job1 to job10 (6.0) job1 -> job2 1.0");
//        list1.add("job4 to job5 (6.0) job4 -> job5 2.0");
//
//        return Map.of("20240716",list1);
//    }
}
