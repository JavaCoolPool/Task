package com.task.job.domain;

import com.task.job.JobConfig;
import com.task.job.constant.ScheduleContants;
import com.task.job.utils.StringUtil;

public class SchedulerJob {
		
	 private JobConfig jobConfig;

	    public transient Action action = Action.CREATE;

	    public static enum Action {
	        CREATE, UPDATE, DELETE
	    }
	    
	    public SchedulerJob(JobConfig jobConfig, Action action) {
	        this.jobConfig = jobConfig!=null?jobConfig:new JobConfig();
	        this.action = action;
	    }
	    
	    public String getJobGroup() {
	        return jobConfig.getJobGroup();
	    }

	    public String getJobName() {
	        return jobConfig.getJobName();
	    }

	    public String getCron() {
	        return jobConfig.getCron();
	    }

	    public String getJobDesc() {
	        return jobConfig.getJobDesc();
	    }

	    public String getTriggerName(){
	        return StringUtil.join("_", jobConfig!=null?jobConfig.getJobGroup():null,jobConfig!=null?jobConfig.getJobName():null, ScheduleContants.TRIGGER_NAME_SUFFIX);

	    }
	    public String getTriggerGroup(){
	        return StringUtil.join("_", jobConfig!=null?jobConfig.getJobGroup():null,jobConfig!=null?jobConfig.getJobName():null, ScheduleContants.TRIGGER_GROUP_SUFFIX);

	    }

	    public Action getAction() {
	        return action;
	    }

	    public void setAction(Action action) {
	        this.action = action;
	    }

	    @Override
	    public String toString() {
	        return "SchedulerJob{" +
	                "jobName=" + getJobName() +
	                ",jobGroup=" + getJobGroup() +
	                ",cron=" + getCron() +
	                ",action=" + action +
	                '}';
	    }
	
}
