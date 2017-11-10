package com.task.job.emus;

public enum CallMethodType {

	ON_INVOKE("onInvoke","onInvoke调用"),
    ON_RETURN("onReturn","onReturn调用"),
    ON_THROW("onThrow","onThrow调用");
    private String key;
    private String desc;

    CallMethodType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static CallMethodType getByValue(String value){
        for(CallMethodType c:values()){
            if(c.getKey().equals(value)){
                return c;
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
