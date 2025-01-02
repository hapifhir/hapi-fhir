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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FhirIdListWorkChunkJson implements IModelJson {

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	@JsonProperty("fhirIds")
	private List<FhirIdJson> myFhirIds;

	/**
	 * Constructor
	 */
	public FhirIdListWorkChunkJson() {
		super();
	}

	/**
	 * Constructor
	 */
	public FhirIdListWorkChunkJson(Collection<FhirIdJson> theFhirIds, RequestPartitionId theRequestPartitionId) {
		this();
		getFhirIds().addAll(theFhirIds);
		myRequestPartitionId = theRequestPartitionId;
	}

	public FhirIdListWorkChunkJson(int theBatchSize, RequestPartitionId theRequestPartitionId) {
		myFhirIds = new ArrayList<>(theBatchSize);
		myRequestPartitionId = theRequestPartitionId;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public List<FhirIdJson> getFhirIds() {
		if (myFhirIds == null) {
			myFhirIds = new ArrayList<>();
		}
		return myFhirIds;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("ids", myFhirIds)
				.append("requestPartitionId", myRequestPartitionId)
				.toString();
	}

	public int size() {
		return getFhirIds().size();
	}

	public boolean isEmpty() {
		return getFhirIds().isEmpty();
	}

	public void add(FhirIdJson theFhirId) {
		getFhirIds().add(theFhirId);
	}

	public void clear() {
		getFhirIds().clear();
	}
}
