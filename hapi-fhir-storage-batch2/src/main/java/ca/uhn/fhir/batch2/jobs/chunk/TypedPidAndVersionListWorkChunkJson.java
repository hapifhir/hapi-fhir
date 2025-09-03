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
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

public class TypedPidAndVersionListWorkChunkJson implements IModelJson {

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	@JsonProperty("ids")
	private List<TypedPidAndVersionJson> myTypedPidAndVersions;

	/**
	 * Constructor
	 */
	public TypedPidAndVersionListWorkChunkJson() {
		super();
	}

	/**
	 * Constructor
	 */
	public TypedPidAndVersionListWorkChunkJson(
			RequestPartitionId theRequestPartitionId, List<TypedPidAndVersionJson> thePids) {
		this();
		setRequestPartitionId(theRequestPartitionId);
		setTypedPidAndVersions(thePids);
	}

	public List<TypedPidAndVersionJson> getTypedPidAndVersions() {
		return myTypedPidAndVersions;
	}

	public void setTypedPidAndVersions(List<TypedPidAndVersionJson> theTypedPidAndVersions) {
		myTypedPidAndVersions = theTypedPidAndVersions;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	@VisibleForTesting
	public void addTypedPidWithNullPartitionForUnitTest(String theResourceType, Long thePid, Long theVersionId) {
		if (myTypedPidAndVersions == null) {
			myTypedPidAndVersions = new ArrayList<>();
		}
		myTypedPidAndVersions.add(new TypedPidAndVersionJson(theResourceType, null, thePid.toString(), theVersionId));
	}
}
