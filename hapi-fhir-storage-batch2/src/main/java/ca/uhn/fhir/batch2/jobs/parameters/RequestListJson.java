package ca.uhn.fhir.batch2.jobs.parameters;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestListJson implements IModelJson {
	@JsonProperty("partitionedUrls")
	private List<PartitionedUrl> myPartitionedUrls;

	@Override
	public String toString() {
		return "RequestListJson{" +
			"myPartitionedUrls=" + myPartitionedUrls +
			'}';
	}

	public List<PartitionedUrl> getPartitionedUrls() {
		return myPartitionedUrls;
	}

	public void setPartitionedUrls(List<PartitionedUrl> thePartitionedUrls) {
		myPartitionedUrls = thePartitionedUrls;
	}
}
