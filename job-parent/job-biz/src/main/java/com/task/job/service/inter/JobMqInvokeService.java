package com.task.job.service.inter;

import com.task.job.JobConfig;
import com.task.job.JobMqInvoke;

public interface JobMqInvokeService {
	
    public JobMqInvoke getByJobConfig(JobConfig jobConfig);
    public void invokeRemote(JobMqInvoke jobMqInvoke);
    public void doInvoke(JobConfig jobConfig);
	
}
