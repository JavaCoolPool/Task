package com.task.job.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.task.job.JobConfig;
import com.task.job.constant.ScheduleContants;
import com.task.job.emus.DiffType;
import com.task.job.service.inter.JobConfigService;
import com.task.job.utils.BeanUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weihonglin on 16/7/18.
 */
@Component
public class JobConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(JobConfigLoader.class);

    @Resource
    private QuartzManager quartzManager;
    @Resource
    private JobConfigService jobConfigService;



    public List<SchedulerJob> load(){
        List<SchedulerJob> schedulerJobs = merage();
        if(CollectionUtils.isEmpty(schedulerJobs)){
            logger.info("JobConfigLoader begin loading but no schdulerJob changed to be loaded...");
            return schedulerJobs;
        }
        for(SchedulerJob schedulerJob:schedulerJobs){
            SchedulerJob.Action action= schedulerJob.getAction();
            switch (action){
                case CREATE:
                    quartzManager.addJob(schedulerJob);
                    break;
                case UPDATE:
                    quartzManager.rescheduleJob(schedulerJob);
                    break;
                case DELETE:
                    quartzManager.deleteJob(schedulerJob);
                    break;
                default:
                    logger.warn(String.format("illegal job:%s,action:%s ",schedulerJob,action));
                    break;
            }

        }

        return schedulerJobs;
    }


    /**
     * load from db merge running jobDetail
     * @return
     * @throws SchedulerException
     */
    public List<SchedulerJob> merage(){
        List<SchedulerJob> schedulerJobs = Lists.newArrayList();

        logger.info("starting to load JobConfig list from DB...");
        List<JobConfig> pendingConfigs = jobConfigService.getValidJobConfigs();
        logger.info("loaded config list from DB count: " + pendingConfigs.size());

        logger.info("starting to load running list from Quartz...");
        List<JobDetail> runningJobs = quartzManager.getRunningJobDetails();
        logger.info("loaded running list from Quartz count: " + runningJobs.size());

        Map<JobKey,JobKeyWrapper> pendingJobKeyWrappers = buildPending(pendingConfigs);
        Map<JobKey,JobKeyWrapper> runningJobKeyWrappers = buildRunning(runningJobs);

        Map<DiffType,Map<JobKey,JobKeyWrapper>> diffMap = BeanUtils.diffKey(pendingJobKeyWrappers, runningJobKeyWrappers);
        Map<JobKey,JobKeyWrapper> createMap=diffMap.get(DiffType.LEFT);
        Map<JobKey,JobKeyWrapper> updateMap=diffMap.get(DiffType.INTERSECTION);
        Map<JobKey,JobKeyWrapper> deleteMap=diffMap.get(DiffType.RIGHT);
        logger.info(String.format("merge schedule[createMap:%s,updateMap:%s,deleteMap:%s]",createMap,updateMap,deleteMap));
        if(CollectionUtils.isNotEmpty(pendingConfigs)){
            for(JobConfig jobConfig:pendingConfigs){
                JobKey key = new JobKey(jobConfig.getJobName(),jobConfig.getJobGroup());
                if(createMap.containsKey(key)){
                    schedulerJobs.add(buildJob(jobConfig, SchedulerJob.Action.CREATE));
                }
                if(updateMap.containsKey(key)){
                    schedulerJobs.add(buildJob(jobConfig, SchedulerJob.Action.UPDATE));
                }
            }
        }
        if(CollectionUtils.isNotEmpty(runningJobs)){
            for(JobDetail jobDetail:runningJobs){
                JobKey key = jobDetail.getKey();
                if(deleteMap.containsKey(key)){
                    schedulerJobs.add(buildJob(jobDetail, SchedulerJob.Action.DELETE));
                }
            }
        }
        return schedulerJobs;
    }


    private SchedulerJob buildJob(JobConfig jobConfig, SchedulerJob.Action action) {
        SchedulerJob job = new SchedulerJob(jobConfig,action);
        return job;
    }
    private SchedulerJob buildJob(JobDetail jobDetail, SchedulerJob.Action delete) {
        JobConfig config = new JobConfig(jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),jobDetail.getDescription());
        return buildJob(config, delete);
    }


    private Map<JobKey,JobKeyWrapper> buildPending( List<JobConfig> jobConfigs){
        HashMap<JobKey,JobKeyWrapper> jobKeyWrappers = Maps.newLinkedHashMap();
        if(CollectionUtils.isEmpty(jobConfigs)){
            return jobKeyWrappers;
        }
        for(JobConfig jobConfig:jobConfigs){
            JobKey jobKey = new JobKey(jobConfig.getJobName(),jobConfig.getJobGroup());
            JobKeyWrapper jobKeyWrapper = new JobKeyWrapper(jobKey,jobConfig.getCron());
            jobKeyWrappers.put(jobKey,jobKeyWrapper);
        }
        return jobKeyWrappers;
    }

    private Map<JobKey,JobKeyWrapper>  buildRunning( List<JobDetail> jobDetails){
        HashMap<JobKey,JobKeyWrapper> jobKeyWrappers = Maps.newLinkedHashMap();
        if(CollectionUtils.isEmpty(jobDetails)){
            return jobKeyWrappers;
        }
        for(JobDetail jobDetail:jobDetails){
            List<? extends Trigger> triggers = quartzManager.getTriggerByJob(jobDetail);
            if(CollectionUtils.isEmpty(triggers)){
                logger.warn(String.format("load running jobDetail[JobName:%s,JobGroup:%s] has no triggers ",jobDetail.getKey().getName(),jobDetail.getKey().getGroup()));
                continue;
            }
            if(ScheduleContants.QUARTZ_DEFAULT_GROUP.equalsIgnoreCase(jobDetail.getKey().getGroup())){
                logger.warn(String.format("load running default system job ignore[JobName:%s,JobGroup:%s] ",jobDetail.getKey().getName(),jobDetail.getKey().getGroup()));
                continue;
            }
            for(Trigger trigger:triggers){
                if(!(trigger instanceof CronTrigger)){
                    logger.warn(String.format("load running jobDetail[JobName:%s,JobGroup:%s]  has no CronTrigger but trigger[TriggerName:%s,TriggerGroup:%s] ",jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),trigger.getKey().getName(),trigger.getKey().getGroup()));
                    continue;
                }
                JobKeyWrapper jobKeyWrapper = new JobKeyWrapper(jobDetail.getKey(),(((CronTrigger) trigger).getCronExpression()));
                jobKeyWrappers.put(jobDetail.getKey(),jobKeyWrapper);
            }

        }
        return jobKeyWrappers;
    }




    class JobKeyWrapper{
        private JobKey jobKey;
        private String cronExpress;

        public JobKeyWrapper(JobKey jobKey, String cronExpress) {
            this.jobKey = jobKey;
            this.cronExpress = cronExpress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JobKeyWrapper that = (JobKeyWrapper) o;
            return   new EqualsBuilder()
                    .append(this.getJobKey(), that.getJobKey())
                    .append(this.getCronExpress(),that.getCronExpress())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return   new HashCodeBuilder()
                    .append(this.getJobKey())
                    .append(this.getCronExpress())
                    .hashCode();
        }

        public JobKey getJobKey() {
            return jobKey;
        }

        public void setJobKey(JobKey jobKey) {
            this.jobKey = jobKey;
        }

        public String getCronExpress() {
            return cronExpress;
        }

        public void setCronExpress(String cronExpress) {
            this.cronExpress = cronExpress;
        }
    }
}
