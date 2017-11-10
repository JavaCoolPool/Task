package com.task.job.job;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.task.job.JobConfig;
import com.task.job.emus.JobType;
import com.task.job.service.inter.JobConfigService;
import com.task.job.service.inter.JobMqInvokeService;
import com.task.job.utils.DateUtils;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class RemoteInvokeJob implements Job{
	
    private JobConfigService jobConfigService;
    private JobMqInvokeService jobMqInvokeService;
	
    private final static Logger logger = LoggerFactory.getLogger(RemoteInvokeJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try{
            initDependencyObject(jobExecutionContext);
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            logger.info(String.format("RemoteInvokeJob Begining [jobName:%s,jobGroup:%s]", jobKey.getName(), jobKey.getGroup()));
            Date schedulerTime = jobExecutionContext.getScheduledFireTime();
            Date actureTime = jobExecutionContext.getFireTime();
            logger.info(String.format("Job %s.%s should start on %s, acture start on %s, delay %d ms",
                    jobKey.getGroup(), jobKey.getName(),
                    DateUtils.formatDateTime(schedulerTime),
                    DateUtils.formatDateTime(actureTime),
                    (actureTime.getTime()-schedulerTime.getTime())));

            JobConfig jobConfig = jobConfigService.getJobConfigByGroupName(jobKey.getName(), jobKey.getGroup());
            /* job config has been deleted, don't need to run the job ! */
            if (jobConfig == null) {
                logger.warn(String.format("Not found JobConfig {jobGroup=[%s], jobName=[%s]}", jobKey.getGroup(), jobKey.getName()));
                return;
            }
            /* job is running has not been finished don't run the job! */
           /*     if (JobRunStatus.PENDING.getKey().equals(jobConfig.getLastRunStatus())) {
                logger.warn("The job status is PENDING... config data is: " + jobConfig);
                return;
            }*/
            /* job is in black windows time by exclude_cron TODO*/
            JobType  jobType= JobType.getByValue(jobConfig.getJobType());
            switch (jobType){
                case MQ:
                    jobMqInvokeService.doInvoke(jobConfig);
                    break;
                default:
                    logger.warn(String.format("JobType only support MQ|DUBBO jobType:%s  JobConfig {jobGroup=[%s], jobName=[%s]}",jobConfig.getJobType(), jobKey.getGroup(), jobKey.getName()));
                    break;
            }

        } catch (Throwable t) {
            logger.error(String.format("RemoteInvokeJob execute exception..."),t);
        }
	}
		
	  private void initDependencyObject(JobExecutionContext context) throws SchedulerException {
	        ApplicationContext ac = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
	        jobConfigService = ac.getBean(JobConfigService.class);
	        jobMqInvokeService = ac.getBean(JobMqInvokeService.class);
	    }
	

}
