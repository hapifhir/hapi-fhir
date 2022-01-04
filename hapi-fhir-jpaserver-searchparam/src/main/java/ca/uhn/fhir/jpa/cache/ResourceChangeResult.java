package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An immutable object containing the count of resource creates, updates and deletes detected by a cache refresh operation.
 * Used internally for testing.
 */
public class ResourceChangeResult {
	public final long created;
	public final long updated;
	public final long deleted;

	public ResourceChangeResult() {
		created = 0;
		updated = 0;
		deleted = 0;
	}

	private ResourceChangeResult(long theCreated, long theUpdated, long theDeleted) {
		created = theCreated;
		updated = theUpdated;
		deleted = theDeleted;
	}

	public static ResourceChangeResult fromCreated(int theCreated) {
		return new ResourceChangeResult(theCreated, 0, 0);
	}

	public static ResourceChangeResult fromResourceChangeEvent(IResourceChangeEvent theResourceChangeEvent) {
		return new ResourceChangeResult(theResourceChangeEvent.getCreatedResourceIds().size(), theResourceChangeEvent.getUpdatedResourceIds().size(), theResourceChangeEvent.getDeletedResourceIds().size());
	}

	public ResourceChangeResult plus(ResourceChangeResult theResult) {
		return new ResourceChangeResult(created + theResult.created, updated + theResult.updated, deleted + theResult.deleted);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("created", created)
			.append("updated", updated)
			.append("deleted", deleted)
			.toString();
	}
}
