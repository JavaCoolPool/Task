package com.task.job.job;

import javax.annotation.Resource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.task.job.service.inter.JobRunLogService;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CleanUpJob implements Job {

	 private final static Logger logger = LoggerFactory.getLogger(CleanUpJob.class);
	    @Resource
	    private JobRunLogService jobRunLogService;
	    @Override
	    public void execute(JobExecutionContext context) throws JobExecutionException {
	        try {
	            initDependencyObject(context);
	            jobRunLogService.cleanUp();
	        } catch (Throwable t) {
	            logger.error(String.format("CleanUpJob execute exception..."), t);
	        }
	    }
	    
	    private void initDependencyObject(JobExecutionContext context) throws SchedulerException {
	        ApplicationContext ac = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
	        jobRunLogService = ac.getBean(JobRunLogService.class);
	    }
}
