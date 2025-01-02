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
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the pair of partition and (search) url, which can be used to configure batch2 jobs.
 * It will be used to determine which FHIR resources are selected for the job.
 * Please note that the url is a partial url, which means it does not include server base and tenantId,
 * and it starts with the with resource type.
 * e.g. Patient?, Observation?status=final
 */
public class PartitionedUrl implements IModelJson {
	@JsonProperty("url")
	@Pattern(
			regexp = "^[A-Z][A-Za-z0-9]+\\?.*",
			message = "If populated, URL must be a search URL in the form '{resourceType}?[params]'")
	private String myUrl;

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	public String getUrl() {
		return myUrl;
	}

	public PartitionedUrl setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public PartitionedUrl setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("myUrl", myUrl);
		b.append("myRequestPartitionId", myRequestPartitionId);
		return b.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PartitionedUrl)) {
			return false;
		}
		PartitionedUrl other = (PartitionedUrl) obj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(myUrl, other.myUrl);
		b.append(myRequestPartitionId, other.myRequestPartitionId);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myRequestPartitionId);
		b.append(myUrl);
		return b.hashCode();
	}
}
