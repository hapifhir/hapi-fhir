package ca.uhn.fhir.batch2.jobs.parameters;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PartitionedUrlListJobParameters extends PartitionedJobParameters {
	@JsonProperty("partitionedUrl")
	@Nullable
	private List<PartitionedUrl> myPartitionedUrls;

	public List<PartitionedUrl> getPartitionedUrls() {
		if (myPartitionedUrls == null) {
			myPartitionedUrls = new ArrayList<>();
		}
		return myPartitionedUrls;
	}

	public PartitionedUrlListJobParameters addPartitionedUrl(@Nonnull PartitionedUrl thePartitionedUrl) {
		Validate.notNull(thePartitionedUrl);
		getPartitionedUrls().add(thePartitionedUrl);
		return this;
	}

	public PartitionedUrlListJobParameters addUrl(@Nonnull String theUrl) {
		PartitionedUrl partitionedUrl = new PartitionedUrl();
		partitionedUrl.setUrl(theUrl);
		partitionedUrl.setRequestPartitionId(RequestPartitionId.defaultPartition());
		return addPartitionedUrl(partitionedUrl);
	}
}
