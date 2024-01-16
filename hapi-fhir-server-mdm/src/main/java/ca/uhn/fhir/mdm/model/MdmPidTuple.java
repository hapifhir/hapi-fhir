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

import java.util.Objects;
import java.util.StringJoiner;

public class MdmPidTuple<T extends IResourcePersistentId> {
	private final T myGoldenPid;

	@Nullable
	private final Integer myGoldenPartitionId;

	private final T mySourcePid;

	@Nullable
	private final Integer mySourcePartitionId;

	private MdmPidTuple(
			T theGoldenPid,
			@Nullable Integer theGoldenPartitionId,
			T theSourcePid,
			@Nullable Integer theSourcePartitionId) {
		myGoldenPid = theGoldenPid;
		mySourcePid = theSourcePid;
		myGoldenPartitionId = theGoldenPartitionId;
		mySourcePartitionId = theSourcePartitionId;
	}

	public static <P extends IResourcePersistentId> MdmPidTuple<P> fromGoldenAndSource(P theGoldenPid, P theSourcePid) {
		return new MdmPidTuple<>(theGoldenPid, null, theSourcePid, null);
	}

	public static <P extends IResourcePersistentId> MdmPidTuple<P> fromGoldenAndSourceAndPartitionIds(
			P theGoldenPid, Integer theGoldenPartitionId, P theSourcePid, Integer theSourcePartitionId) {
		return new MdmPidTuple<>(theGoldenPid, theGoldenPartitionId, theSourcePid, theSourcePartitionId);
	}

	public T getGoldenPid() {
		return myGoldenPid;
	}

	@Nullable
	public Integer getGoldenPartitionId() {
		return myGoldenPartitionId;
	}

	public T getSourcePid() {
		return mySourcePid;
	}

	@Nullable
	public Integer getSourcePartitionId() {
		return mySourcePartitionId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MdmPidTuple<?> that = (MdmPidTuple<?>) o;
		return Objects.equals(myGoldenPid, that.myGoldenPid)
				&& Objects.equals(myGoldenPartitionId, that.myGoldenPartitionId)
				&& Objects.equals(mySourcePid, that.mySourcePid)
				&& Objects.equals(mySourcePartitionId, that.mySourcePartitionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myGoldenPid, myGoldenPartitionId, mySourcePid, mySourcePartitionId);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MdmPidTuple.class.getSimpleName() + "[", "]")
				.add("myGoldenPid=" + myGoldenPid)
				.add("myGoldenPartitionId=" + myGoldenPartitionId)
				.add("mySourcePid=" + mySourcePid)
				.add("mySourcePartitionId=" + mySourcePartitionId)
				.toString();
	}
}
