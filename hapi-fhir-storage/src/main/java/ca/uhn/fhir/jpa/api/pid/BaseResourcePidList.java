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

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract public class BaseResourcePidList implements IResourcePidList {

	final List<BaseResourcePersistentId> myIds = new ArrayList<>();

	@Nullable
	final Date myLastDate;

	BaseResourcePidList(Collection<BaseResourcePersistentId> theIds, Date theLastDate) {
		myIds.addAll(theIds);
		myLastDate = theLastDate;
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
	public List<BaseResourcePersistentId> getIds() {
		return Collections.unmodifiableList(myIds);
	}

	public BaseResourcePersistentId getId(int theIndex) {
		return myIds.get(theIndex);
	}
}

