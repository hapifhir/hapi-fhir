/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A resource pid list where the pids can have different resource types
 */
public class MixedResourcePidList extends BaseResourcePidList {
	@Nonnull
	final List<String> myResourceTypes;

	public MixedResourcePidList(
			List<String> theResourceTypes,
			Collection<? extends IResourcePersistentId> theIds,
			Date theLastDate,
			RequestPartitionId theRequestPartitionId) {
		super(theIds, theLastDate, theRequestPartitionId);
		myResourceTypes = theResourceTypes;
	}

	@Override
	public String getResourceType(int i) {
		return myResourceTypes.get(i);
	}
}
