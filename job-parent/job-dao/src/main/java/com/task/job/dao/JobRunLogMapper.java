package com.task.job.dao;

import java.util.List;

import com.task.job.JobRunLog;

public interface JobRunLogMapper {
    int deleteByPrimaryKey(Integer id);
    
    int insert(JobRunLog record);

    int insertSelective(JobRunLog record);

    JobRunLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobRunLog record);

    int updateByPrimaryKey(JobRunLog record);
    
    
    List<JobRunLog> selectBySelective(JobRunLog record);

    int cleanup();
    
}