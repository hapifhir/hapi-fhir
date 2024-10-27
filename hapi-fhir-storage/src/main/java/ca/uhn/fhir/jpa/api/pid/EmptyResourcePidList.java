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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * An empty resource pid list
 */
public class EmptyResourcePidList implements IResourcePidList {
	@Override
	public RequestPartitionId getRequestPartitionId() {
		return null;
	}

	@Override
	public Date getLastDate() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Nonnull
	@Override
	public List<TypedResourcePid> getTypedResourcePids() {
		return Collections.emptyList();
	}

	@Override
	public String getResourceType(int i) {
		throw new ArrayIndexOutOfBoundsException(
				Msg.code(2095) + "Attempting to get resource type from an empty resource pid list");
	}

	@Override
	public List<IResourcePersistentId> getIds() {
		return Collections.emptyList();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String toString() {
		return "[empty]";
	}
}
