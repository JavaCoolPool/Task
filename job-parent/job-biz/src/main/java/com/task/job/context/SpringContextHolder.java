package com.task.job.context;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;
	
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        SpringContextHolder.applicationContext = arg0;
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T>  Map<String, T>  getBeanTypes(Class<T> clazz){
       return applicationContext == null ? new HashedMap() : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, clazz, false, false);
    }
	
}
