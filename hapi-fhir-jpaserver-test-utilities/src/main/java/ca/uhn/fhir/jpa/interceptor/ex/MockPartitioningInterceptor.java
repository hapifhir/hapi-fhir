/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor.ex;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.helger.commons.lang.StackTraceHelper;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class is a partitioning interceptor for unit tests that can be programmatically
 * configured to return specific partition IDs for read and create operations.
 */
@Interceptor
public class MockPartitioningInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(MockPartitioningInterceptor.class);

	private final List<RequestPartitionId> myReadRequestPartitionIds = new ArrayList<>();
	private final List<RequestPartitionId> myCreateRequestPartitionIds = new ArrayList<>();
	private RequestPartitionId myAlwaysReturnPartition;

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId partitionIdentifyRead(
			ServletRequestDetails theRequestDetails, ReadPartitionIdRequestDetails theDetails) {

		// Just to be nice, figure out the first line in the stack that isn't a part of the
		// partitioning or interceptor infrastructure, just so it's obvious who is asking
		// for a partition ID
		String stack = getCallerStackLine();

		RequestPartitionId retVal = myAlwaysReturnPartition;
		if (retVal == null) {
			assertThat(myReadRequestPartitionIds)
					.describedAs("read partition ids")
					.isNotEmpty();
			retVal = myReadRequestPartitionIds.remove(0);
		}
		ourLog.info("Returning partition {} for read at: {}", retVal, stack);
		return retVal;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId PartitionIdentifyCreate(
			IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		assertNotNull(theResource);
		String stack = getCallerStackLine();
		RequestPartitionId retVal = myAlwaysReturnPartition;
		if (retVal == null) {
			assertThat(myCreateRequestPartitionIds)
					.describedAs("create partitions")
					.isNotEmpty();
			retVal = myCreateRequestPartitionIds.remove(0);
		}

		ourLog.info(
				"Returning partition [{}] for create of resource {} with date {}: {}",
				retVal,
				theResource,
				retVal.getPartitionDate(),
				stack);
		return retVal;
	}

	public void assertNoRemainingIds() {
		assertThat(myCreateRequestPartitionIds)
				.as(() -> "Still have " + myCreateRequestPartitionIds.size()
						+ " CREATE partitions remaining in interceptor")
				.isEmpty();
		assertThat(myReadRequestPartitionIds)
				.as("Found " + myReadRequestPartitionIds.size() + " READ partitions remaining in interceptor")
				.isEmpty();
	}

	/**
	 * CLears any partitions specified by {@link #addNextInterceptorCreateResult(RequestPartitionId)} or {@link #addNextIterceptorReadResult(RequestPartitionId)}
	 * or {@link #setAlwaysReturnPartition(RequestPartitionId)}.
	 */
	public void clearPartitions() {
		myCreateRequestPartitionIds.clear();
		myReadRequestPartitionIds.clear();
		myAlwaysReturnPartition = null;
	}

	/**
	 * The next call to this interceptor for a READ operation will return the given partition ID.
	 */
	public void addNextIterceptorReadResult(RequestPartitionId theRequestPartitionId) {
		myReadRequestPartitionIds.add(theRequestPartitionId);
		ourLog.info(
				"Adding partition {} for read (not have {})", theRequestPartitionId, myReadRequestPartitionIds.size());
	}

	/**
	 * The next call to this interceptor for a CREATE operation will return the given partition ID.
	 */
	public void addNextInterceptorCreateResult(RequestPartitionId theRequestPartitionId) {
		myCreateRequestPartitionIds.add(theRequestPartitionId);
	}

	/**
	 * All future calls to this interceptor will return the given partition ID,
	 * until {@link #clearPartitions()} is called.
	 */
	public void setAlwaysReturnPartition(RequestPartitionId theAlwaysReturnPartition) {
		myAlwaysReturnPartition = theAlwaysReturnPartition;
	}

	@Nonnull
	private static String getCallerStackLine() {
		String stack;

		stack = StackTraceHelper.getStackAsString(new Exception());
		stack = Arrays.stream(stack.split("\\n"))
				.filter(t -> t.contains("ca.uhn.fhir"))
				.filter(t -> !t.toLowerCase().contains("interceptor"))
				.filter(t -> !t.toLowerCase().contains("partitionhelper"))
				.filter(t -> !t.contains("Test"))
				.findFirst()
				.orElse("UNKNOWN");
		return stack;
	}
}
