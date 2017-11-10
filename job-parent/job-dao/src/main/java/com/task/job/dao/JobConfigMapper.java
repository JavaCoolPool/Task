package com.task.job.dao;

import java.util.List;

import com.task.job.JobConfig;

public interface JobConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobConfig record);

    int insertSelective(JobConfig record);

    JobConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobConfig record);

    int updateByPrimaryKey(JobConfig record);
    
    List<JobConfig> selectBySelective(JobConfig record);
}