package com.dbs.cpa_api.utils;

import java.util.Map;

public class CpaUtil {

    public static String[] getJobDetails(String jobInfo) {
        int index =jobInfo.lastIndexOf('_');
        String jobName = jobInfo.substring(0,index);
        String system = jobInfo.substring((index+1), jobInfo.length());
        return new String[]{jobName, system};
    }
}
