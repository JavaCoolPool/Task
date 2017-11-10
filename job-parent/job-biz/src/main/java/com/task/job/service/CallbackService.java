package com.task.job.service;

import com.task.job.vo.JobCallBack;


public interface CallbackService {
    /**
     * JOB触发
     * @param jobCallBack
     */
    public void onInvoke(JobCallBack jobCallBack);

    /**
     * JOB返回
     * @param jobCallBack
     */
    public void onReturn(JobCallBack jobCallBack);
    /**
     * JOB返回
     * @param jobCallBack
     */
    public void onThrow(JobCallBack jobCallBack);
}
