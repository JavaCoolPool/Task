package com.task.job.vo;

import java.io.Serializable;

import com.task.job.emus.CallMethodType;
import com.task.job.emus.JobRunStatus;

public class JobCallBack implements Serializable {

	private Integer runId;
    private JobRunStatus jobRunStatus;
    private CallMethodType callMethodType;
    private Throwable ex;
    private String remark;

    public JobCallBack() {
    }

    public JobCallBack(Integer runId, JobRunStatus jobRunStatus,CallMethodType callMethodType) {
        this.runId = runId;
        this.jobRunStatus = jobRunStatus;
        this.callMethodType = callMethodType;
    }
    public JobCallBack(Integer runId, JobRunStatus jobRunStatus,CallMethodType callMethodType,String remark) {
        this.runId = runId;
        this.jobRunStatus = jobRunStatus;
        this.callMethodType = callMethodType;
        this.remark = remark;
    }

    public JobCallBack(Integer runId,JobRunStatus jobRunStatus, Throwable ex,CallMethodType callMethodType,String remark) {
        this.runId = runId;
        this.jobRunStatus = jobRunStatus;
        this.ex = ex;
        this.callMethodType = callMethodType;
        this.remark = remark;
    }

    public JobRunStatus getJobRunStatus() {
        return jobRunStatus;
    }

    public void setJobRunStatus(JobRunStatus jobRunStatus) {
        this.jobRunStatus = jobRunStatus;
    }

    public Integer getRunId() {
        return runId;
    }

    public void setRunId(Integer runId) {
        this.runId = runId;
    }

    public Throwable getEx() {
        return ex;
    }

    public void setEx(Throwable ex) {
        this.ex = ex;
    }

    public CallMethodType getCallMethodType() {
        return callMethodType;
    }

    public void setCallMethodType(CallMethodType callMethodType) {
        this.callMethodType = callMethodType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "JobCallBack{" +
                "runId=" + runId +
                ", jobRunStatus=" + jobRunStatus +
                ", callMethodType=" + callMethodType +
                '}';
    }
	
}
