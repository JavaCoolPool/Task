package com.task.job.domain;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.task.job.job.RemoteInvokeJob;
import com.task.job.utils.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class QuartzManager {

	   private final static Logger logger = LoggerFactory.getLogger(QuartzManager.class);

	    @Resource
	    private Scheduler scheduler;
	
	    private JobDetail buildJob(SchedulerJob scheduleJob){
	        JobDetail jobDetail = newJob(RemoteInvokeJob.class)
	                .withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup())
	                .storeDurably()
	                .requestRecovery()
	                .withDescription(scheduleJob.getJobDesc())
	                .build();
	        return jobDetail;
	    }

	    /**
	     * build trigger
	     * @param scheduleJob
	     * @return
	     */
	    private CronTrigger buildTrigger(SchedulerJob scheduleJob){
	        TriggerKey triggerKey = new TriggerKey(scheduleJob.getTriggerName(),scheduleJob.getTriggerGroup());
	        CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCron());
	        CronTrigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(schedBuilder).forJob(scheduleJob.getJobName(),scheduleJob.getJobGroup()).build();
	        return trigger;
	    }

	    /**
	     * build JobKey
	     * @param scheduleJob
	     * @return
	     */
	    private JobKey buildJobKey(SchedulerJob scheduleJob){
	        return  new JobKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
	    }

	    /**
	     * build TriggerKey
	     * @param scheduleJob
	     * @return
	     */
	    private TriggerKey buildTriggerKey(SchedulerJob scheduleJob){
	        return new TriggerKey(scheduleJob.getTriggerName(),scheduleJob.getTriggerGroup());
	    }

	    /**
	     * addJob
	     * @param schedulerJob
	     * @return
	     * @throws SchedulerException
	     * @throws ParseException
	     */
	    public Date addJob(SchedulerJob schedulerJob)  {
	        try {
	            if (scheduler.checkExists(buildJobKey(schedulerJob))) {
	                logger.info(String.format("delete  Job  jobGroup[%s] jobName[%s] stating...", schedulerJob.getJobGroup(), schedulerJob.getJobName()));
	                deleteJob(schedulerJob);
	            }
	            JobDetail jobDetail = buildJob(schedulerJob);
	            CronTrigger trigger = buildTrigger(schedulerJob);
	            logger.info("starting schedule Quartz Job... with trigger: " + getTriggerDesc(trigger));
	            Date firstFireTime = scheduler.scheduleJob(jobDetail, trigger);
	            logger.info(String.format("finsh schedule Quartz Job...[JobName:%s,CronExpression:%s,firstFireTime:%s]", schedulerJob.getJobName(), trigger.getCronExpression(), DateUtils.formatTimeDateDefault(firstFireTime)));
	            return firstFireTime;
	        }catch (Throwable t){
	            logger.error(String.format("add schedule Quartz Job Exception[job:%s] ",schedulerJob),t);
	            return null;
	        }
	    }


	    /**
	     * rescheduleJob
	     * @param scheduleJob
	     * @return
	     * @throws SchedulerException
	     * @throws ParseException
	     */
	    public Date rescheduleJob(SchedulerJob scheduleJob){
	        try {
	            TriggerKey triggerKey = new TriggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
	            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
	            logger.info("Current trigger status: " + getTriggerDesc(oldTrigger));
	            CronTrigger newTrigger = buildTrigger(scheduleJob);
	            logger.info("Updated trigger status: " + getTriggerDesc(newTrigger));
	            logger.info(String.format("starting reschedule Quartz Job...[TriggerName:%s,CronExpression:%s]", triggerKey.getName(), newTrigger.getCronExpression()));
	            Date firstFireTime = scheduler.rescheduleJob(triggerKey, newTrigger);
	            logger.info(String.format("finsh reschedule Quartz Job...[TriggerName:%s,CronExpression:%s,firstFireTime:%s]", triggerKey.getName(), newTrigger.getCronExpression(), DateUtils.formatTimeDateDefault(firstFireTime)));
	            return firstFireTime;
	        }catch (Throwable t){
	            logger.error(String.format("reschedule Quartz Job Exception[job:%s] ",scheduleJob),t);
	            return null;
	        }
	    }


	    /**
	     * 针对Job暂停（Job 1---* Trigger）
	     * @param jobGroup
	     * @param jobName
	     * @throws SchedulerException
	     */
	    public void pauseJob(String jobGroup, String jobName) {
	        try {
	            logger.info(String.format("pauseJob with jobGroup[%s] jobName[%s] stating...", jobGroup, jobName));
	            JobKey jobKey = new JobKey(jobName, jobGroup);
	            if (!scheduler.checkExists(jobKey)) {
	                logger.warn(String.format("pauseJob with not exists Job  jobGroup[%s] jobName[%s] stating...", jobGroup, jobName));
	                return;
	            }
	            scheduler.pauseJob(jobKey);
	        }catch (Throwable t){
	            logger.error(String.format("pauseJob Quartz Job Exception[jobGroup:%s,jobName:%s] ",jobGroup,jobName),t);
	        }
	    }

	    /**
	     * 针对Trigger暂停（Job 1---* Trigger）
	     * @param triggerGroup
	     * @param triggerName
	     * @throws SchedulerException
	     */
	    public void pauseTrigger(String triggerGroup, String triggerName) {
	        try {
	            logger.info(String.format("pauseTrigger with triggerGroup[%s] triggerName[%s] stating...", triggerGroup, triggerName));
	            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
	            if (!scheduler.checkExists(triggerKey)) {
	                logger.warn(String.format("pauseTrigger with not exists Trigger  triggerGroup[%s] triggerName[%s] stating...", triggerGroup, triggerName));
	                return;
	            }
	            scheduler.pauseTrigger(triggerKey);
	        }catch (Throwable t){
	            logger.error(String.format("pauseTrigger Quartz Job Exception[triggerGroup:%s,triggerName:%s] ",triggerGroup,triggerName),t);
	        }
	    }

	    /**
	     * 恢复Trigger（Job 1---* Trigger）
	     * @param schedulerJob
	     * @throws SchedulerException
	     */
	    public void resumeTrigger(SchedulerJob schedulerJob) {
	        String triggerGroup = schedulerJob.getTriggerGroup();
	        String triggerName  = schedulerJob.getTriggerName();
	        try {
	            logger.info(String.format("resumeTrigger with triggerGroup[%s] triggerName[%s] stating...", triggerGroup, triggerName));
	            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
	            if (!scheduler.checkExists(triggerKey)) {
	                logger.warn(String.format("resumeTrigger with not exists Trigger  triggerGroup[%s] triggerName[%s] stating...", triggerGroup, triggerName));
	                return;
	            }
	            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
	            if (Trigger.TriggerState.PAUSED == triggerState) {
	                CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
	                if (cronTrigger != null) {
	                    //cronTrigger.setNextFireTime(cronTrigger.getFireTimeAfter(new Date()));   // 只在配合rescheduleJob时才有效！
	                    //cronTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);  // 丢弃错过的调度周期(default)
	                }
	                scheduler.resumeTrigger(triggerKey);
	            } else {
	                logger.info(String.format("Don't need to resumeTrigger the JOB with triggerGroup[%s] triggerName[%s], current triggerStatus is [%s], not PAUSED status[%s].", triggerGroup, triggerName, triggerState, Trigger.TriggerState.PAUSED));
	            }
	        }catch (Throwable t){
	            logger.error(String.format("resumeTrigger Quartz Job Exception[triggerGroup:%s,triggerName:%s] ",triggerGroup,triggerName),t);
	        }
	    }

	    /**
	     * 恢复Job（Job 1---* Trigger）
	     * @param schedulerJob
	     * @throws SchedulerException
	     */
	    public void resumeJob(SchedulerJob schedulerJob) {
	        try {
	            logger.info(String.format("resumeJob with jobGroup[%s] jobName[%s] stating...", schedulerJob.getJobGroup(), schedulerJob.getJobName()));
	            JobKey jobKey = buildJobKey(schedulerJob);
	            if (!scheduler.checkExists(jobKey)) {
	                logger.warn(String.format("resumeJob with not  Job  jobGroup[%s] jobName[%s] stating...", schedulerJob.getJobGroup(), schedulerJob.getJobName()));
	                return;
	            }
	            scheduler.resumeJob(jobKey);
	        }catch (Throwable t){
	            logger.error(String.format("resumeJob Quartz Job Exception[jobGroup:%s,jobName:%s] ",schedulerJob.getJobGroup(), schedulerJob.getJobName()),t);
	        }
	    }

	    /**
	     * 删除JOB
	     * @param schedulerJob
	     * @return
	     * @throws SchedulerException
	     */
	    public boolean deleteJob(SchedulerJob schedulerJob){
	        try {
	            JobKey jobKey = new JobKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());
	            TriggerKey triggerKey = new TriggerKey(schedulerJob.getTriggerName(), schedulerJob.getTriggerGroup());
	            logger.info(String.format("starting stop the running Quartz Job...[jobGroup:%s,jobName:%s]", schedulerJob.getJobGroup(), schedulerJob.getJobName()));
	            scheduler.pauseTrigger(triggerKey);
	            boolean result = scheduler.unscheduleJob(triggerKey);
	            logger.info(String.format("unschedule Quartz Job ...[jobGroup:%s,jobName:%s,result:%s]", schedulerJob.getJobGroup(), schedulerJob.getJobName(), result));
	            boolean delete = scheduler.deleteJob(jobKey);
	            logger.info(String.format("delete Quartz Job ...[jobGroup:%s,jobName:%s,delete:%s]", schedulerJob.getJobGroup(), schedulerJob.getJobName(), delete));
	            return delete;
	        }catch (Throwable t){
	            logger.error(String.format("add schedule Quartz Job Exception[job:%s] ",schedulerJob),t);
	            return false;
	        }
	    }

	    public void addCalendar(String excludeCron) throws ParseException, SchedulerException {
	        CronCalendar cronCal = new CronCalendar(excludeCron); // exclude 9-12
//	      CronCalendar cronCal = new CronCalendar("* * 9-12 * * ?"); // exclude 9-12
	        scheduler.addCalendar("excludeCron",cronCal,false,false);
	    }
	    
	    public List<JobDetail> getRunningJobDetails(){
	        List<JobDetail> jobs = Lists.newArrayList();
	        try {
	            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>anyGroup());
	            for (JobKey jobKey : jobKeys) {
	                JobDetail job = scheduler.getJobDetail(jobKey);
	                if (job != null) {
	                    jobs.add(job);
	                }
	            }
	            logger.debug("Total load " + jobs.size() + " jobs from quartz.");
	        }catch (Throwable t){
	            logger.error(String.format("getRunningJobDetails Exception "),t);
	        }
	        return jobs;
	    }

	    public List<Trigger> getAllTriggers()  {
	        List<Trigger> triggers = Lists.newArrayList();
	        List<JobDetail> jobs = getRunningJobDetails();
	        for(JobDetail job : jobs){
	            triggers.addAll(getTriggerByJob(job));
	        }
	        logger.info("Total load " + triggers.size() + " triggers from quartz.");
	        return triggers;
	    }

	    public List<? extends Trigger> getTriggerByJob(JobDetail job){
	        try {
	            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(new JobKey(job.getKey().getName(), job.getKey().getGroup()));
	            return triggers;
	        }catch (Throwable t){
	            logger.error(String.format("getTriggerByJob Exception"),t);
	            return null;
	        }
	    }

	   private static String getTriggerDesc(CronTrigger trigger) {
	        if (trigger == null) return null;
	        return String.format(
	                "CronTrigger[%s]-[%s]: {startTime=[%s], previousFireTime=[%s], nextFireTime=[%s], finalFireTime=[%s], endTime=[%s]}",
	                trigger.getKey().getName(), trigger.getCronExpression(),
	                trigger.getStartTime(), trigger.getPreviousFireTime(), trigger.getNextFireTime(),
	                trigger.getFinalFireTime(), trigger.getEndTime()
	        );
	    }
	    
}
