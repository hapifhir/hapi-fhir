package ca.uhn.fhir.util;

import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;

public final class ThreadPoolUtil {
	private ThreadPoolUtil() {
	}


	@Nonnull
	public static ThreadPoolTaskExecutor newThreadPool(int theCorePoolSize, int theMaxPoolSize, String theThreadNamePrefix) {
		ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
		asyncTaskExecutor.setCorePoolSize(theCorePoolSize);
		asyncTaskExecutor.setMaxPoolSize(theMaxPoolSize);
		asyncTaskExecutor.setQueueCapacity(0);
		asyncTaskExecutor.setAllowCoreThreadTimeOut(true);
		asyncTaskExecutor.setThreadNamePrefix(theThreadNamePrefix);
		asyncTaskExecutor.setRejectedExecutionHandler(new BlockPolicy());
		asyncTaskExecutor.initialize();
		return asyncTaskExecutor;
	}
}
