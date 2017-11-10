package com.task.job;

public class JobInvokeEntry {
		private Integer runId;
	    private String  jobName;
	    private String  jobGroup;

	    public JobInvokeEntry() {
	    }

	    public JobInvokeEntry(Integer runId, String jobName, String jobGroup) {
	        this.runId = runId;
	        this.jobName = jobName;
	        this.jobGroup = jobGroup;
	    }

	    public Integer getRunId() {
	        return runId;
	    }

	    public void setRunId(Integer runId) {
	        this.runId = runId;
	    }

	    public String getJobName() {
	        return jobName;
	    }

	    public void setJobName(String jobName) {
	        this.jobName = jobName;
	    }

	    public String getJobGroup() {
	        return jobGroup;
	    }

	    public void setJobGroup(String jobGroup) {
	        this.jobGroup = jobGroup;
	    }

	    @Override
	    public String toString() {
	        return "JobInvokeEntry{" +
	                "runId=" + runId +
	                ", jobName='" + jobName + '\'' +
	                ", jobGroup='" + jobGroup + '\'' +
	                '}';
	    }
}
