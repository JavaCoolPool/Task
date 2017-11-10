package com.task.job.emus;

public enum JobType {
	DUBBO("DUBBO","DUBBO调用"),
    MQ("MQ","MQ调用"),
    HTTP("HTTP","HTTP调用");
    private String key;
    private String desc;
    JobType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static JobType getByValue(String value){
        for(JobType j:values()){
            if(j.getKey().equals(value)){
                return j;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
