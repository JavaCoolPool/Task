package com.task.job.service;

import org.quartz.JobKey;

public interface JobService {
    /**
     * JobRunLog.Id
     * @param runId
     */
    public void execute(Integer runId);

    /**
     * @return
     */
    public JobKey getJobKey();
}
