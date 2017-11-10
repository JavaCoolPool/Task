package org.job.biz;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.task.job.JobConfig;
import com.task.job.dao.JobConfigMapper;
	
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring.xml"})
public class ServiceTest extends AbstractJUnit4SpringContextTests {
	
/*	@Resource(name="jobConfigService")
	private JobConfigService jobConfigService;
		
	@Resource(name="jobConfigLoader")
    private JobConfigLoader jobConfigLoader;*/
	
	@Autowired
	private JobConfigMapper jobConfigMapper;
	
	@Test
	public  void testSpring(){
		JobConfig config = jobConfigMapper.selectByPrimaryKey(1);
		System.out.println("++++++++++++|");
	}
}
