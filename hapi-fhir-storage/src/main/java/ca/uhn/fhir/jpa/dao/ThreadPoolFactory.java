package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.util.ThreadPoolUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Basic bean which will create a Task Executor
 */
public class ThreadPoolFactory {


	public ThreadPoolTaskExecutor newThreadPool(Integer theBundleBatchPoolSize, Integer theBundleBatchMaxPoolSize, String theThreadPrefix) {
		return ThreadPoolUtil.newThreadPool(theBundleBatchPoolSize, theBundleBatchMaxPoolSize, theThreadPrefix);
	}
}
