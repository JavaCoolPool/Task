package com.task.job.service;

import com.alibaba.fastjson.JSON;
import com.task.job.service.inter.AbstractBatchJobService;
import com.task.job.vo.JobCallBack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

public abstract class MqBatchJobService<T> extends AbstractBatchJobService<T> {
    private final static Logger logger = LoggerFactory.getLogger(MqBatchJobService.class);

    private final static String BIZ_QUEUE_KERNEL_SCHEDULE_KEY="biz_queue_kernel_schedule_key";
    protected abstract AmqpTemplate getAmqpTemplate();
    protected void beforeExecute(JobCallBack jobCallBack) {
        logger.info(String.format("MqBatchJobService beforeExecute jobCallBack:%s",jobCallBack));
        getAmqpTemplate().convertAndSend(BIZ_QUEUE_KERNEL_SCHEDULE_KEY, JSON.toJSONString(jobCallBack));
    }
    protected void afterExecute(JobCallBack jobCallBack) {
        logger.info(String.format("MqBatchJobService afterExecute jobCallBack:%s",jobCallBack));
        getAmqpTemplate().convertAndSend(BIZ_QUEUE_KERNEL_SCHEDULE_KEY, JSON.toJSONString(jobCallBack));
    }
    protected void throwExecute(JobCallBack jobCallBack) {
        logger.info(String.format("MqBatchJobService throwExecute jobCallBack:%s",jobCallBack));
        getAmqpTemplate().convertAndSend(BIZ_QUEUE_KERNEL_SCHEDULE_KEY, JSON.toJSONString(jobCallBack));
    }

}
