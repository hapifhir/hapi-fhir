/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.svc;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Result object for {@link MdmSearchExpansionSvc}
 *
 * @since 8.0.0
 */
public class MdmSearchExpansionResults {

	private final Set<IIdType> myOriginalIdToExpandedId = new HashSet<>();
	private final Map<IIdType, IIdType> myExpandedIdToOriginalId = new HashMap<>();

	void addExpandedId(IIdType theOriginalId, IIdType theExpandedId) {
		assert isRemapCandidate(theOriginalId) : theOriginalId.getValue();
		myOriginalIdToExpandedId.add(theOriginalId);
		myExpandedIdToOriginalId.put(theExpandedId, theOriginalId);
	}

	public Optional<IIdType> getOriginalIdForExpandedId(IIdType theId) {
		assert isRemapCandidate(theId) : theId.getValue();

		// If we have this ID in the OriginalId map, it was explicitly
		// searched for, so even if it's also an expanded ID we don't
		// want to consider it as one
		if (myOriginalIdToExpandedId.contains(theId)) {
			return Optional.empty();
		}

		IIdType originalId = myExpandedIdToOriginalId.get(theId);
		return Optional.ofNullable(originalId);
	}

	public static boolean isRemapCandidate(IIdType theId) {
		return theId != null
				&& !theId.isLocal()
				&& !theId.isUuid()
				&& theId.hasResourceType()
				&& theId.hasIdPart()
				&& theId.getValue().equals(theId.toUnqualifiedVersionless().getValue());
	}
}
