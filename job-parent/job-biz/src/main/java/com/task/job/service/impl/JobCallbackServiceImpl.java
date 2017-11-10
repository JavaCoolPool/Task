package com.task.job.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.task.job.emus.CallMethodType;
import com.task.job.service.CallbackService;
import com.task.job.service.inter.JobCallbackService;
import com.task.job.vo.JobCallBack;

import javax.annotation.Resource;

@Service("jobCallbackService")
public class JobCallbackServiceImpl implements JobCallbackService {
    private final static Logger logger = LoggerFactory.getLogger(JobCallbackServiceImpl.class);
    @Resource
    private CallbackService callbackService;
    @Override
    public void update(JobCallBack jobCallBack) {
        if(jobCallBack==null){
            logger.warn("jobCallBack is null");
            return;
        }
        CallMethodType callMethodType = jobCallBack.getCallMethodType();
        if(callMethodType==null){
            logger.warn("jobCallBack callMethodType is null");
            return;
        }
        switch (callMethodType){
            case ON_INVOKE:
                callbackService.onInvoke(jobCallBack);
                break ;
            case ON_RETURN:
                callbackService.onReturn(jobCallBack);
                break ;
            case ON_THROW:
                callbackService.onThrow(jobCallBack);
                break ;
        }
    }
}
