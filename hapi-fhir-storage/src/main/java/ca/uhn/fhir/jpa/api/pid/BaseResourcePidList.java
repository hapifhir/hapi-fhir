/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseResourcePidList implements IResourcePidList {

	final List<IResourcePersistentId> myIds = new ArrayList<>();

	@Nullable
	final Date myLastDate;

	private final RequestPartitionId myRequestPartitionId;

	BaseResourcePidList(
			Collection<IResourcePersistentId> theIds, Date theLastDate, RequestPartitionId theRequestPartitionId) {
		myIds.addAll(theIds);
		myLastDate = theLastDate;
		myRequestPartitionId = theRequestPartitionId;
	}

	@Override
	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	@Override
	public Date getLastDate() {
		return myLastDate;
	}

	@Override
	public int size() {
		return myIds.size();
	}

	@Override
	@Nonnull
	public List<TypedResourcePid> getTypedResourcePids() {
		List<TypedResourcePid> retval = new ArrayList<>();
		for (int i = 0; i < myIds.size(); ++i) {
			retval.add(new TypedResourcePid(getResourceType(i), myIds.get(i)));
		}
		return Collections.unmodifiableList(retval);
	}

	@Override
	public boolean isEmpty() {
		return myIds.isEmpty();
	}

	@Override
	public List<IResourcePersistentId> getIds() {
		return Collections.unmodifiableList(myIds);
	}

	public IResourcePersistentId getId(int theIndex) {
		return myIds.get(theIndex);
	}

	@Override
	public String toString() {
		return myIds.toString();
	}
}
