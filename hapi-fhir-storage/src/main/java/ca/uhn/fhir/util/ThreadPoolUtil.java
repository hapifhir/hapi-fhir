package ca.uhn.fhir.util;

import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import org.apache.commons.lang3.Validate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;

public final class ThreadPoolUtil {
	private ThreadPoolUtil() {
	}


	@Nonnull
	public static ThreadPoolTaskExecutor newThreadPool(int theCorePoolSize, int theMaxPoolSize, String theThreadNamePrefix) {
		Validate.matchesPattern(theThreadNamePrefix, "[a-z\\-]+", "Thread pool prefix name must consist only of lower-case letters and hyphens");
		Validate.isTrue(theThreadNamePrefix.endsWith("-"), "Thread pool prefix name must end with a hyphen");
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
