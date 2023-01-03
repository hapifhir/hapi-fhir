package ca.uhn.fhir.mdm.model;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

public class MdmPidTuple {
	private ResourcePersistentId myGoldenPid;
	private ResourcePersistentId mySourcePid;

	public ResourcePersistentId getGoldenPid(){
		return myGoldenPid;
	}

	public MdmPidTuple setGoldenPid(ResourcePersistentId theGoldenPid) {
		myGoldenPid = theGoldenPid;
		return this;
	}

	public MdmPidTuple setSourcePid(ResourcePersistentId theSourcePid) {
		mySourcePid = theSourcePid;
		return this;
	}

	public ResourcePersistentId getSourcePid(){
		return mySourcePid;
	}

	public Long getGoldenPidAsLong() {
		return myGoldenPid.getIdAsLong();
	}

	public Long getSourcePidAsLong() {
		return mySourcePid.getIdAsLong();
	}

	public String getGoldenPidAsString() {
		return (String) myGoldenPid.getId();
	}

	public String getSourcePidAsString() {
		return (String) mySourcePid.getId();
	}
}
