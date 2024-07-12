/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be used to configure parameters for batch2 jobs.
 * Please note that these need to be backward compatible as we do not have a way to migrate them to a different structure at the moment.
 */
public class JobParameters implements IModelJson {
	@JsonProperty(value = "partitionId")
	private List<RequestPartitionId> myRequestPartitionIds;

	@JsonProperty("batchSize")
	private Integer myBatchSize;

	@JsonProperty("partitionedUrl")
	private List<PartitionedUrl> myPartitionedUrls;

	public void setRequestPartitionId(@Nullable RequestPartitionId theRequestPartitionId) {
		if (theRequestPartitionId != null) {
			myRequestPartitionIds = List.of(theRequestPartitionId);
		}
	}

	@Nullable
	public RequestPartitionId getRequestPartitionId() {
		return getFirstRequestPartitionIdOrNull();
	}

	@Nullable
	private RequestPartitionId getFirstRequestPartitionIdOrNull() {
		return myRequestPartitionIds == null || myRequestPartitionIds.isEmpty() ? null : myRequestPartitionIds.get(0);
	}

	@Nonnull
	public List<RequestPartitionId> getRequestPartitionIds() {
		if (myRequestPartitionIds == null) {
			myRequestPartitionIds = new ArrayList<>();
		}
		return myRequestPartitionIds;
	}

	public void addRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		getRequestPartitionIds().add(theRequestPartitionId);
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Nullable
	public Integer getBatchSize() {
		return myBatchSize;
	}

	public List<PartitionedUrl> getPartitionedUrls() {
		if (myPartitionedUrls == null) {
			myPartitionedUrls = new ArrayList<>();
		}
		return myPartitionedUrls;
	}

	public void addPartitionedUrl(@Nonnull PartitionedUrl theUrl) {
		getPartitionedUrls().add(theUrl);
	}

	public void addUrl(@Nonnull String theUrl) {
		getPartitionedUrls().add(new PartitionedUrl().setUrl(theUrl));
	}

	@VisibleForTesting
	public static JobParameters from(
			List<String> theUrls, List<RequestPartitionId> thePartitions, boolean theShouldAssignPartitionToUrl) {
		JobParameters parameters = new JobParameters();
		if (theShouldAssignPartitionToUrl) {
			assert theUrls.size() == thePartitions.size();
			for (int i = 0; i < theUrls.size(); i++) {
				PartitionedUrl partitionedUrl = new PartitionedUrl();
				partitionedUrl.setUrl(theUrls.get(i));
				partitionedUrl.setRequestPartitionId(thePartitions.get(i));
				parameters.addPartitionedUrl(partitionedUrl);
			}
		} else {
			theUrls.forEach(url -> parameters.addPartitionedUrl(new PartitionedUrl().setUrl(url)));
			thePartitions.forEach(parameters::addRequestPartitionId);
		}
		return parameters;
	}
}
