/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nullable;

public class MdmPidTuple<T extends IResourcePersistentId> {
	// LUKETODO: add the source partition ID and the golden partition ID
	private final T myGoldenPid;
	private final T mySourcePid;

	@Nullable
	private Integer myGoldenPartitionId;

	@Nullable
	private Integer mySourcePartitionId;

	private MdmPidTuple(
			T theGoldenPid,
			T theSourcePid,
			@Nullable Integer theGoldenPartitionId,
			@Nullable Integer theSourcePartitionId) {
		myGoldenPid = theGoldenPid;
		mySourcePid = theSourcePid;
		myGoldenPartitionId = theGoldenPartitionId;
		mySourcePartitionId = theSourcePartitionId;
	}

	public static <P extends IResourcePersistentId> MdmPidTuple<P> fromGoldenAndSource(P theGoldenPid, P theSourcePid) {
		return new MdmPidTuple<>(theGoldenPid, theSourcePid, null, null);
	}

	public static <P extends IResourcePersistentId> MdmPidTuple<P> fromGoldenAndSourceAndPartitionIds(
			P theGoldenPid, P theSourcePid, Integer theGoldenPartitionId, Integer theSourcePartitionId) {
		return new MdmPidTuple<>(theGoldenPid, theSourcePid, theGoldenPartitionId, theSourcePartitionId);
	}

	public T getGoldenPid() {
		return myGoldenPid;
	}

	public T getSourcePid() {
		return mySourcePid;
	}

	@Nullable
	public Integer getGoldenPartitionId() {
		return myGoldenPartitionId;
	}

	@Nullable
	public Integer getSourcePartitionId() {
		return mySourcePartitionId;
	}
}
