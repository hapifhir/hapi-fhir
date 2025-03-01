/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Can be used to configure parameters for batch2 jobs.
 * Please note that these need to be backward compatible as we do not have a way to migrate them to a different structure at the moment.
 */
public class PartitionedUrlJobParameters implements IModelJson {
	@JsonProperty(value = "partitionId")
	@Nullable
	private RequestPartitionId myRequestPartitionId;

	@JsonProperty("batchSize")
	private Integer myBatchSize;

	@JsonProperty("partitionedUrl")
	private List<PartitionedUrl> myPartitionedUrls;

	public void setRequestPartitionId(@Nullable RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	@Nullable
	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
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
		// TODO MM: added for backward compatibility, it can be removed once requestPartitionId is deprecated
		myPartitionedUrls.stream()
				.filter(thePartitionedUrl -> thePartitionedUrl.getRequestPartitionId() == null)
				.forEach(thePartitionedUrl -> thePartitionedUrl.setRequestPartitionId(myRequestPartitionId));
		return myPartitionedUrls;
	}

	public void addPartitionedUrl(@Nonnull PartitionedUrl theUrl) {
		getPartitionedUrls().add(theUrl);
	}

	public void addUrl(@Nonnull String theUrl) {
		getPartitionedUrls().add(new PartitionedUrl().setUrl(theUrl));
	}

	public List<String> getUrls() {
		return getPartitionedUrls().stream()
				.map(PartitionedUrl::getUrl)
				.filter(url -> !StringUtils.isBlank(url))
				.collect(Collectors.toList());
	}
}
