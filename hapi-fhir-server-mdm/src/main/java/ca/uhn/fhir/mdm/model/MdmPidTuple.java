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
package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

public class MdmPidTuple<T extends IResourcePersistentId> {
	private final T myGoldenPid;
	private final T mySourcePid;

	private MdmPidTuple(T theGoldenPid, T theSourcePid) {
		myGoldenPid = theGoldenPid;
		mySourcePid = theSourcePid;
	}

	public static <P extends IResourcePersistentId> MdmPidTuple<P> fromGoldenAndSource(P theGoldenPid, P theSourcePid) {
		return new MdmPidTuple<>(theGoldenPid, theSourcePid);
	}

	public T getGoldenPid() {
		return myGoldenPid;
	}

	public T getSourcePid() {
		return mySourcePid;
	}
}
