package com.task.job.listener;

import java.util.Map;

import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.dubbo.common.json.JSON;
import com.task.job.JobInvokeEntry;
import com.task.job.context.SpringContextHolder;
import com.task.job.service.JobService;

public class JobTriggerListener implements MessageListener {
	
	  private final static Logger logger = LoggerFactory.getLogger(JobTriggerListener.class);
	    @Override
	    public void onMessage(Message message) {
	        try {
	            String msg = new String(message.getBody(),message.getMessageProperties().getContentEncoding());
	            logger.info(String.format("JobTriggerListener receive msg:%s",msg));
	            JobInvokeEntry  jobInvokeEntry = JSON.parse(msg, JobInvokeEntry.class);
	            //获取context中JobService的子bean
	            Map<String,JobService> map = SpringContextHolder.getBeanTypes(JobService.class);
	            logger.info(String.format("JobTriggerListener all jobService jobService:%s",map));
	            for(Map.Entry<String,JobService> entry: map.entrySet()){
	                JobService jobService = entry.getValue();
	                JobKey jobKey = new JobKey(jobInvokeEntry.getJobName(),jobInvokeEntry.getJobGroup());
	                if(jobKey.equals(jobService.getJobKey())){
	                    logger.info(String.format("jobService:%s begin excute jobKey[jobName:%s,jobGroup:%s]", entry.getKey(), jobKey.getName(), jobKey.getGroup()));
	                    entry.getValue().execute(jobInvokeEntry.getRunId());
	                    logger.info(String.format("jobService:%s end excute jobKey[jobName:%s,jobGroup:%s]",entry.getKey(),jobKey.getName(),jobKey.getGroup()));
	                }
	            }
	        } catch (Throwable e) {
	            logger.error(String.format("JobTriggerListener receive Expcetion"),e);
	        }
	    }
}
