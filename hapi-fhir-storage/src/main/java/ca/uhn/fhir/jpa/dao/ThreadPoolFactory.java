package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.util.ThreadPoolUtil;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * Basic bean which will create a Task Executor
 */
public class ThreadPoolFactory {

	public AsyncTaskExecutor newThreadPool(Integer theBundleBatchPoolSize, Integer theBundleBatchMaxPoolSize, String theThreadPrefix) {
		return ThreadPoolUtil.newThreadPool(theBundleBatchPoolSize, theBundleBatchMaxPoolSize, theThreadPrefix);
	}
}
