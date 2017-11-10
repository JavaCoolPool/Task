package com.task.job.service.inter;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shhxzq.kernel.zk.component.ZkReentrantLock;
import com.shhxzq.kernel.zk.service.ZkShareLockService;
import com.task.job.emus.CallMethodType;
import com.task.job.emus.JobRunStatus;
import com.task.job.service.JobService;
import com.task.job.vo.JobCallBack;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractBatchJobService<T> implements JobService {
    private final static Logger logger = LoggerFactory.getLogger(AbstractBatchJobService.class);
    private final static long LOCK_TIMEOUT = 3000;

    protected String jobName = this.getClass().getSimpleName();

    protected abstract String getLockPath();

    protected abstract List<T> fetchList(int batchAmount);

    protected abstract int getBatchAmount();

    protected abstract void processList(List<T> list);

    protected void beforeExecute(JobCallBack jobCallBack) { }
    protected void afterExecute(JobCallBack jobCallBack) { }
    protected void throwExecute(JobCallBack jobCallBack) { }


    protected abstract ZkShareLockService getZkShareLockService();

    @Override
    public void execute(Integer runId) {
        ZkReentrantLock mutex = getZkShareLockService().newInterProcessMutex(String.format(getLockPath()+"%s",this.jobName));
        if(mutex.acquire(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)){
            try {
                logger.info(String.format("Job (name: %s,runId: %s) start  batchAmount:%s ",jobName,runId,getBatchAmount()));
                beforeExecute(new JobCallBack(runId, JobRunStatus.PENDING, CallMethodType.ON_INVOKE));
                List<T> dataList = fetchList(getBatchAmount());
                logger.info(String.format("Job (name: %s,runId: %s) start to process data(size: %d)", jobName,runId, null == dataList ? 0 : dataList.size()));
                if (CollectionUtils.isNotEmpty(dataList)) {
                    try {
                        processList(dataList);
                        logger.info(String.format("Job (name: %s,runId: %s) success process data(size: %d)", jobName,runId, null == dataList ? 0 : dataList.size()));
                        afterExecute(new JobCallBack(runId, JobRunStatus.SUCCESS,CallMethodType.ON_RETURN));
                    } catch (Throwable t) {
                        logger.error(String.format("Job (name: %s,runId: %s) Fail to process", jobName,runId), t);
                        throwExecute(new JobCallBack(runId, JobRunStatus.FAILED,t,CallMethodType.ON_THROW,"processList failure"));
                    }
                }
            }catch (Throwable t){
                logger.error(String.format("Fail to process Job (name: %s,runId: %s)", jobName,runId), t);
                throwExecute(new JobCallBack(runId, JobRunStatus.FAILED,t,CallMethodType.ON_THROW,"processJob failure"));
            }finally {
                mutex.release();
            }
        }else{
            logger.warn(String.format("Acquire Zookeeper lock for Job (name: %s,runId: %s) failed", jobName,runId));
            throwExecute(new JobCallBack(runId, JobRunStatus.IGNORE,CallMethodType.ON_THROW,"concurrent job failure"));
        }
    }
}
