package com.task.job.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.task.job.JobRunLog;
import com.task.job.emus.JobRunStatus;
import com.task.job.service.CallbackService;
import com.task.job.service.inter.JobConfigService;
import com.task.job.service.inter.JobRunLogService;
import com.task.job.utils.DateUtils;
import com.task.job.utils.StringUtil;
import com.task.job.vo.JobCallBack;
	
@Service("callbackService")
public class CallbackServiceImpl implements CallbackService {

	@Resource
    private JobRunLogService jobRunLogService;
	
    @Resource
    private JobConfigService jobConfigService;
	
    @Override
    public void onInvoke(JobCallBack jobCallBack) {
        JobRunStatus jobRunStatus = jobCallBack!=null && jobCallBack.getJobRunStatus()!=null?jobCallBack.getJobRunStatus():JobRunStatus.PENDING;
        jobRunLogService.updateStatus(jobCallBack.getRunId(),jobRunStatus,null,new Date(),StringUtil.trunc(jobCallBack.getRemark(),1000));
        JobRunLog jobRunLog = jobRunLogService.getJobRunLogById(jobCallBack.getRunId());
        jobConfigService.updateLastRunStatus(jobRunLog.getJobConfigId(),jobRunLog.getId(),jobRunLog.getStatus(),null);
    }

    @Override
    public void onReturn(JobCallBack jobCallBack) {
        JobRunStatus jobRunStatus = jobCallBack!=null && jobCallBack.getJobRunStatus()!=null?jobCallBack.getJobRunStatus():JobRunStatus.SUCCESS;
        jobRunLogService.updateStatus(jobCallBack.getRunId(),jobRunStatus,new Date(),new Date(),StringUtil.trunc(jobCallBack.getRemark(), 1000));
        JobRunLog jobRunLog = jobRunLogService.getJobRunLogById(jobCallBack.getRunId());
        long spendSecs = DateUtils.diffSecond(jobRunLog.getStartTime(),jobRunLog.getEndTime());
        jobConfigService.updateLastRunStatus(jobRunLog.getJobConfigId(),jobRunLog.getId(),jobRunLog.getStatus(),(int)spendSecs );
    }

    @Override
    public void onThrow(JobCallBack jobCallBack) {
        String remark = StringUtil.join("|", jobCallBack.getRemark(), jobCallBack.getEx()!=null?jobCallBack.getEx().getMessage():"");
        JobRunStatus jobRunStatus = jobCallBack!=null && jobCallBack.getJobRunStatus()!=null?jobCallBack.getJobRunStatus():JobRunStatus.FAILED;
        jobRunLogService.updateStatus(jobCallBack.getRunId(),jobRunStatus,new Date(),new Date(),StringUtil.trunc(remark,1000));
        JobRunLog jobRunLog = jobRunLogService.getJobRunLogById(jobCallBack.getRunId());
        long spendSecs = DateUtils.diffSecond(jobRunLog.getStartTime(), jobRunLog.getEndTime());
        jobConfigService.updateLastRunStatus(jobRunLog.getJobConfigId(),jobRunLog.getId(),jobRunLog.getStatus(),(int)spendSecs );
    }

}
