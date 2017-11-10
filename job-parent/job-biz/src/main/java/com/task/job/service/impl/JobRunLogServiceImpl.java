package com.task.job.service.impl;

import org.springframework.stereotype.Service;

import com.task.job.JobRunLog;
import com.task.job.constant.ScheduleContants;
import com.task.job.dao.JobRunLogMapper;
import com.task.job.emus.JobRunStatus;
import com.task.job.service.inter.JobRunLogService;

import javax.annotation.Resource;
import java.util.Date;

@Service("jobRunLogService")
public class JobRunLogServiceImpl implements JobRunLogService {
    @Resource
    private JobRunLogMapper jobRunLogMapper;
    
    @Override
    public JobRunLog insertJobRunLog(JobRunLog jobRunLog) {
        jobRunLogMapper.insert(jobRunLog);
        return jobRunLog;
    }

    @Override
    public JobRunLog getJobRunLogById(Integer id) {
        return jobRunLogMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public int updateStatus(Integer id, JobRunStatus status,Date endTime,Date updatedAt, String remark) {
        JobRunLog jobRunLog = new JobRunLog(id,status,endTime,remark,updatedAt, ScheduleContants.SYS_USER);
        return jobRunLogMapper.updateByPrimaryKeySelective(jobRunLog);
    }

    @Override
    public int cleanUp() {
        return jobRunLogMapper.cleanup();
    }
}
