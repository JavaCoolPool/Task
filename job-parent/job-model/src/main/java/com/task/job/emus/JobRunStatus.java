package com.task.job.emus;

public enum JobRunStatus {
	
		PREPAING("PREPAING","准备处理"),
	    PENDING("PENDING","处理中"),
	    SUCCESS("SUCCESS","成功"),
	    IGNORE("IGNORE","忽略"),
	    FAILED("FAILED","失败");
	    private String key;
	    private String desc;

	    JobRunStatus(String key, String desc) {
	        this.key = key;
	        this.desc = desc;
	    }

	    public static JobRunStatus getByValue(String value){
	        for(JobRunStatus j:values()){
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
