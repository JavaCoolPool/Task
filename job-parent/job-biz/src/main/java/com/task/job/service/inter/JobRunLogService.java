package com.task.job.service.inter;

import java.util.Date;

import com.task.job.JobRunLog;
import com.task.job.emus.JobRunStatus;

public interface JobRunLogService {
	  	public JobRunLog insertJobRunLog(JobRunLog jobRunLog);
	    public JobRunLog getJobRunLogById(Integer id);
	    public int updateStatus(Integer id, JobRunStatus status,Date endTime,Date updatedAt, String remark);
	    public int cleanUp();
}
