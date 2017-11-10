package com.task.job.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.dubbo.common.json.JSON;
import com.task.job.service.inter.JobCallbackService;
import com.task.job.vo.JobCallBack;

public class JobCallbackListener implements MessageListener {

	  private final static Logger logger = LoggerFactory.getLogger(JobCallbackListener.class);
	    @Resource
	    private JobCallbackService jobCallbackService;
	    
	    @Override
	    public void onMessage(Message message) {
	        try {
	            String msg = new String(message.getBody(),message.getMessageProperties().getContentEncoding());
	            JobCallBack jobCallBack = JSON.parse(msg, JobCallBack.class);
	            jobCallbackService.update(jobCallBack);
	            logger.info(String.format("JobCallbackListener receive msg:%s",msg));
	        } catch (Throwable e) {
	            logger.error(String.format("JobCallbackListener receive Encoding Expcetion"),e);
	        }
	    }



}
