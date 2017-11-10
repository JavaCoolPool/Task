package com.task.job.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.task.job.JobConfig;
import com.task.job.dao.JobConfigMapper;
import com.task.job.emus.ValidStatus;
import com.task.job.service.inter.JobConfigService;
import com.task.job.utils.NumberUtils;

@Service("jobConfigService")
public class JobConfigServiceImpl implements JobConfigService {
		
		@Resource
	    private JobConfigMapper jobConfigMapper;
		
	    @Override
	    public List<JobConfig> getValidJobConfigs() {
	        JobConfig crond = new JobConfig(ValidStatus.VALID.getKey());
	        return jobConfigMapper.selectBySelective(crond);
	    }

	    @Override
	    public JobConfig getJobConfigByGroupName(String jobName,String jobGroup) {
	        JobConfig crond = new JobConfig(jobName,jobGroup);
	        crond.setIsValid(ValidStatus.VALID.getKey());
	        List<JobConfig> list = jobConfigMapper.selectBySelective(crond);
	        if(CollectionUtils.isEmpty(list)){
	            return null;
	        }
	        return list.get(0);
	    }

	    @Override
	    public int updateLastRunStatus(Integer id,Integer lastRunId, String lastRunStatus, Integer spendSecs) {
	        JobConfig jobConfig = jobConfigMapper.selectByPrimaryKey(id);
	        jobConfig.setLastRunId(lastRunId);
	        jobConfig.setLastRunStatus(lastRunStatus);
	        Integer maxSpendSecs = NumberUtils.isGreaterOrEqualThan(spendSecs,jobConfig.getMaxSpendSecs())?spendSecs:jobConfig.getMaxSpendSecs();
	        jobConfig.setMaxSpendSecs(maxSpendSecs);
	        return jobConfigMapper.updateByPrimaryKeySelective(jobConfig);
	    }
}
