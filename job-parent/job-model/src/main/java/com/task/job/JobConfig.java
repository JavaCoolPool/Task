package com.task.job;

import java.util.Date;

public class JobConfig {
    private Integer id;

    private String jobName;

    private String jobGroup;

    private String jobDesc;

    private String cron;

    private String excludeCron;

    private String jobType;

    private Integer jobInvokeId;

    private String isValid;

    private Integer lastRunId;

    private String lastRunStatus;

    private String remark;

    private Integer maxSpendSecs;

    private String contactsEmail;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc == null ? null : jobDesc.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

    public String getExcludeCron() {
        return excludeCron;
    }

    public void setExcludeCron(String excludeCron) {
        this.excludeCron = excludeCron == null ? null : excludeCron.trim();
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType == null ? null : jobType.trim();
    }

    public Integer getJobInvokeId() {
        return jobInvokeId;
    }

    public void setJobInvokeId(Integer jobInvokeId) {
        this.jobInvokeId = jobInvokeId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }

    public Integer getLastRunId() {
        return lastRunId;
    }

    public void setLastRunId(Integer lastRunId) {
        this.lastRunId = lastRunId;
    }

    public String getLastRunStatus() {
        return lastRunStatus;
    }

    public void setLastRunStatus(String lastRunStatus) {
        this.lastRunStatus = lastRunStatus == null ? null : lastRunStatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getMaxSpendSecs() {
        return maxSpendSecs;
    }

    public void setMaxSpendSecs(Integer maxSpendSecs) {
        this.maxSpendSecs = maxSpendSecs;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail == null ? null : contactsEmail.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();

    }
    
    public JobConfig() {
    }

    public JobConfig(String isValid) {
        this.isValid = isValid;
    }
    
    public JobConfig(String jobName, String jobGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }
    
    public JobConfig(String jobName, String jobGroup, String jobDesc) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDesc = jobDesc;
    }
    public JobConfig(String jobName, String jobGroup, String jobDesc, String cron) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDesc = jobDesc;
        this.cron = cron;
    }
    public JobConfig(Integer id,Integer lastRunId, String lastRunStatus, Integer maxSpendSecs) {
        this.id = id;
        this.lastRunId = lastRunId;
        this.lastRunStatus = lastRunStatus;
        this.maxSpendSecs = maxSpendSecs;
        this.updatedAt = new Date();
        this.updatedBy = "SYS";
    }
}