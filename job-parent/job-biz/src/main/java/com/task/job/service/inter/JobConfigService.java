package com.task.job.service.inter;

import java.util.List;

import com.task.job.JobConfig;

public interface JobConfigService {
	
	  /**
     * @return
     */
    public List<JobConfig> getValidJobConfigs();

    /**
     * @param jobName
     * @param jobGroup
     * @return
     */
    public JobConfig getJobConfigByGroupName(String jobName,String jobGroup);

    /**
     * @param id
     * @param lastRunId
     * @param lastRunStatus
     * @param spendSecs
     * @return
     */
    public int updateLastRunStatus(Integer id,Integer lastRunId,String lastRunStatus,Integer spendSecs);
	
}
