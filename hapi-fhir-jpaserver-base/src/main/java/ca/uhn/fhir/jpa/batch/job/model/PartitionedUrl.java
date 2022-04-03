package ca.uhn.fhir.jpa.batch.job.model;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PartitionedUrl implements IModelJson {
	@JsonProperty("url")
	private String myUrl;

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	public PartitionedUrl() {
	}

	public PartitionedUrl(String theUrl, RequestPartitionId theRequestPartitionId) {
		myUrl = theUrl;
		myRequestPartitionId = theRequestPartitionId;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	public boolean isPartitioned() {
		return myRequestPartitionId != null && !myRequestPartitionId.isDefaultPartition();
	}
}
