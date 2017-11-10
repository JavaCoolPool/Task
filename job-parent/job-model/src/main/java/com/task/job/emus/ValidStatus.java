package com.task.job.emus;

import org.apache.commons.lang3.StringUtils;

public enum ValidStatus {
	 VALID("Y","有效"),
	    INVALID("N","无效");
	    private String key;
	    private String desc;

	    ValidStatus(String key, String desc) {
	        this.key = key;
	        this.desc = desc;
	    }

	    public static ValidStatus getByValue(String value){
	        if(StringUtils.isBlank(value)){
	            return null;
	        }
	        for(ValidStatus v:values()){
	            if(v.getKey().equalsIgnoreCase(value.trim())){
	                return v;
	            }
	        }
	        return null;
	    }

	    public static boolean isValid(String value){
	        if(StringUtils.isNotBlank(value)
	                && VALID.getKey().equalsIgnoreCase(value.trim())){
	            return true;
	        }
	        return false;
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
