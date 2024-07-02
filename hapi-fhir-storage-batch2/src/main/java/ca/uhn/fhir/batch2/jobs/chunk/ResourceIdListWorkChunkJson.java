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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceIdListWorkChunkJson implements IModelJson {

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	@JsonProperty("ids")
	private List<TypedPidJson> myTypedPids;

	/**
	 * Constructor
	 */
	public ResourceIdListWorkChunkJson() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIdListWorkChunkJson(
			Collection<TypedPidJson> theTypedPids, RequestPartitionId theRequestPartitionId) {
		this();
		getTypedPids().addAll(theTypedPids);
		myRequestPartitionId = theRequestPartitionId;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	private List<TypedPidJson> getTypedPids() {
		if (myTypedPids == null) {
			myTypedPids = new ArrayList<>();
		}
		return myTypedPids;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("ids", myTypedPids)
				.append("requestPartitionId", myRequestPartitionId)
				.toString();
	}

	public <T extends IResourcePersistentId> List<T> getResourcePersistentIds(IIdHelperService<T> theIdHelperService) {
		if (myTypedPids.isEmpty()) {
			return Collections.emptyList();
		}

		return myTypedPids.stream()
				.map(t -> {
					T retval = theIdHelperService.newPidFromStringIdAndResourceName(t.getPid(), t.getResourceType());
					return retval;
				})
				.collect(Collectors.toList());
	}

	public int size() {
		return getTypedPids().size();
	}

	public void addTypedPid(String theResourceType, Long thePid) {
		getTypedPids().add(new TypedPidJson(theResourceType, thePid.toString()));
	}

	public String getResourceType(int index) {
		return getTypedPids().get(index).getResourceType();
	}
}
