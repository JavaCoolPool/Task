package com.task.job.dao;

import com.task.job.JobMqInvoke;

public interface JobMqInvokeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobMqInvoke record);

    int insertSelective(JobMqInvoke record);

    JobMqInvoke selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobMqInvoke record);

    int updateByPrimaryKey(JobMqInvoke record);
}