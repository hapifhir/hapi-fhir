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
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ResourcePidListBuilder {
	private static final IResourcePidList EMPTY_CHUNK = new EmptyResourcePidList();

	public static IResourcePidList fromChunksAndDate(List<IResourcePidList> theChunks, Date theEnd) {
		if (theChunks.isEmpty()) {
			return empty();
		}

		Set<IResourcePersistentId> ids = new LinkedHashSet<>();
		RequestPartitionId requestPartitionId = null;

		Date endDate = null;
		Set<String> resourceTypes = new HashSet<>();
		boolean containsMixed = false;
		for (IResourcePidList chunk : theChunks) {

			Validate.isTrue(requestPartitionId == null || requestPartitionId == chunk.getRequestPartitionId());
			requestPartitionId = chunk.getRequestPartitionId();

			if (chunk.isEmpty()) {
				continue;
			}
			ids.addAll(chunk.getIds());
			endDate = getLatestDate(chunk, endDate, theEnd);
			if (chunk instanceof MixedResourcePidList) {
				containsMixed = true;
			} else {
				resourceTypes.add(chunk.getResourceType(0));
			}
		}

		if (containsMixed || resourceTypes.size() > 1) {
			List<String> types = new ArrayList<>();
			for (IResourcePidList chunk : theChunks) {
				for (int i = 0; i < chunk.size(); ++i) {
					types.add(chunk.getResourceType(i));
				}
			}
			return new MixedResourcePidList(types, ids, endDate, requestPartitionId);
		} else {
			IResourcePidList firstChunk = theChunks.get(0);
			String onlyResourceType = firstChunk.getResourceType(0);
			return new HomogeneousResourcePidList(onlyResourceType, ids, endDate, requestPartitionId);
		}
	}

	private static Date getLatestDate(IResourcePidList theChunk, Date theCurrentEndDate, Date thePassedInEndDate) {
		Date endDate = theCurrentEndDate;
		if (theCurrentEndDate == null) {
			endDate = theChunk.getLastDate();
		} else if (theChunk.getLastDate().after(endDate)
				&& theChunk.getLastDate().before(thePassedInEndDate)) {
			endDate = theChunk.getLastDate();
		}
		return endDate;
	}

	private static IResourcePidList empty() {
		return EMPTY_CHUNK;
	}
}
