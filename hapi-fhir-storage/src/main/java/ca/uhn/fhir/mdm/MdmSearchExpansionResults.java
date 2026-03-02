/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Result object for MDM search expansion
 *
 * @since 8.0.0
 */
public class MdmSearchExpansionResults {

	private static final String EXPANSION_RESULTS = MdmSearchExpansionResults.class.getName() + "_EXPANSION_RESULTS";

	private final Set<IIdType> myOriginalIdToExpandedId = new HashSet<>();
	private final Map<IIdType, IIdType> myExpandedIdToOriginalId = new HashMap<>();

	public void addExpandedId(IIdType theOriginalId, IIdType theExpandedId) {
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

	public Set<IIdType> getExpandedIds() {
		return Set.copyOf(myExpandedIdToOriginalId.keySet());
	}

	public static boolean isRemapCandidate(IIdType theId) {
		return theId != null
				&& !theId.isLocal()
				&& !theId.isUuid()
				&& theId.hasResourceType()
				&& theId.hasIdPart()
				&& theId.getValue().equals(theId.toUnqualifiedVersionless().getValue());
	}

	/**
	 * Retrieves cached expansion results from the request details user data.
	 *
	 * @param theRequestDetails The request details containing cached results
	 * @return The cached expansion results, or null if none exist
	 * @since 8.0.0
	 */
	@Nullable
	public static MdmSearchExpansionResults getCachedExpansionResults(@Nonnull RequestDetails theRequestDetails) {
		return (MdmSearchExpansionResults) theRequestDetails.getUserData().get(EXPANSION_RESULTS);
	}

	/**
	 * Stores expansion results in the request details user data.
	 *
	 * @param theRequestDetails The request details to store results in
	 * @param theResults The expansion results to store
	 * @since 8.0.0
	 */
	public static void cacheExpansionResults(
			@Nonnull RequestDetails theRequestDetails, @Nonnull MdmSearchExpansionResults theResults) {
		theRequestDetails.getUserData().put(EXPANSION_RESULTS, theResults);
	}

	/**
	 * Clears cached expansion results from the request details user data.
	 *
	 * @param theRequestDetails The request details to clear results from
	 * @since 8.0.0
	 */
	public static void clearCachedExpansionResults(@Nonnull RequestDetails theRequestDetails) {
		theRequestDetails.getUserData().remove(EXPANSION_RESULTS);
	}
}
