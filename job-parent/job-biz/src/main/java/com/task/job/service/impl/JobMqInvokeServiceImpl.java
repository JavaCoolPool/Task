package com.task.job.service.impl;

import org.springframework.stereotype.Service;

import com.task.job.JobConfig;
import com.task.job.JobMqInvoke;
import com.task.job.service.inter.JobMqInvokeService;

@Service("jobMqInvokeService")
public class JobMqInvokeServiceImpl implements JobMqInvokeService {
	
	@Override
	public JobMqInvoke getByJobConfig(JobConfig jobConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invokeRemote(JobMqInvoke jobMqInvoke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doInvoke(JobConfig jobConfig) {
		// TODO Auto-generated method stub

	}

}
