package com.task.job.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.task.job.domain.JobConfigLoader;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class LoadConfigJob implements Job {

	private JobConfigLoader jobConfigLoader;

    private final static Logger logger = LoggerFactory.getLogger(LoadConfigJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            initDependencyObject(jobExecutionContext);
            jobConfigLoader.load();
        } catch (Throwable t) {
            logger.error(String.format("LoadConfigJob execute exception..."),t);
        }
    }

    private void initDependencyObject(JobExecutionContext context) throws SchedulerException {
        ApplicationContext ac = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
        jobConfigLoader = ac.getBean(JobConfigLoader.class);
    }
}
