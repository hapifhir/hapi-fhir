package ca.uhn.fhir.jpa.api.pid;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import io.micrometer.core.lang.NonNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A resource pid list where the pids can have different resource types
 */
public class MixedResourcePidList extends BaseResourcePidList {
	@NonNull
	final List<String> myResourceTypes;

	public MixedResourcePidList(List<String> theResourceTypes, Collection<ResourcePersistentId> theIds, Date theLastDate) {
		super(theIds, theLastDate);
		myResourceTypes = theResourceTypes;
	}

	@Override
	public String getResourceType(int i) {
		return myResourceTypes.get(i);
	}
}
