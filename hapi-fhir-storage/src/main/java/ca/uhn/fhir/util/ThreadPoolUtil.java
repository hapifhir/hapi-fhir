/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.util;

import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public final class ThreadPoolUtil {
	private ThreadPoolUtil() {}

	@Nonnull
	public static ThreadPoolTaskExecutor newThreadPool(
			int theCorePoolSize, int theMaxPoolSize, String theThreadNamePrefix) {
		return newThreadPool(theCorePoolSize, theMaxPoolSize, theThreadNamePrefix, 0);
	}

	@Nonnull
	public static ThreadPoolTaskExecutor newThreadPool(
			int theCorePoolSize, int theMaxPoolSize, String theThreadNamePrefix, int theQueueCapacity) {
		return newThreadPool(theCorePoolSize, theMaxPoolSize, theThreadNamePrefix, theQueueCapacity, null);
	}

	@Nonnull
	public static ThreadPoolTaskExecutor newThreadPool(
			int theCorePoolSize,
			int theMaxPoolSize,
			String theThreadNamePrefix,
			int theQueueCapacity,
			TaskDecorator taskDecorator) {
		Validate.isTrue(
				theCorePoolSize == theMaxPoolSize || theQueueCapacity == 0,
				"If the queue capacity is greater than 0, core pool size needs to match max pool size or the system won't grow the queue");
		Validate.isTrue(theThreadNamePrefix.endsWith("-"), "Thread pool prefix name must end with a hyphen");
		ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
		asyncTaskExecutor.setCorePoolSize(theCorePoolSize);
		asyncTaskExecutor.setMaxPoolSize(theMaxPoolSize);
		asyncTaskExecutor.setQueueCapacity(theQueueCapacity);
		asyncTaskExecutor.setAllowCoreThreadTimeOut(true);
		asyncTaskExecutor.setThreadNamePrefix(theThreadNamePrefix);
		asyncTaskExecutor.setRejectedExecutionHandler(new BlockPolicy());
		asyncTaskExecutor.setTaskDecorator(taskDecorator);
		asyncTaskExecutor.initialize();
		return asyncTaskExecutor;
	}
}
