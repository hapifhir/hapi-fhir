/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EIDHelper {

	private final FhirContext myFhirContext;
	private final IMdmSettings myMdmSettings;

	@Autowired
	public EIDHelper(FhirContext theFhirContext, IMdmSettings theMdmSettings) {
		myFhirContext = theFhirContext;
		myMdmSettings = theMdmSettings;
	}

	public CanonicalEID createHapiEid() {
		return new CanonicalEID(
				MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM,
				UUID.randomUUID().toString(),
				null);
	}

	/**
	 * Given an {@link IAnyResource} representing a type supported by MDM, retrieve their externally-assigned EID,
	 * represented as a {@link CanonicalEID}
	 *
	 * @param theResource the resource to extract the EID from.
	 *
	 * @return An optional {@link CanonicalEID} representing the external EID. Absent if the EID is not present.
	 */
	public List<CanonicalEID> getExternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, getEidSystemsFor(theResource), theResource);
	}

	/**
	 * Returns the one EID that best identifies the given resource, chosen by the order in which EID systems
	 * are configured for its resource type rather than by the order identifiers appear on the resource.
	 * Callers that need a stable single value for a resource - a subscription message key, for instance -
	 * must use this rather than picking the first element of {@link #getExternalEid(IBaseResource)}, whose
	 * order follows the payload.
	 *
	 * @param theResource the resource to extract the EID from
	 * @return the primary external EID, or empty if the resource carries none
	 */
	public Optional<CanonicalEID> getPrimaryExternalEid(IBaseResource theResource) {
		List<CanonicalEID> externalEids = getExternalEid(theResource);
		if (externalEids.isEmpty()) {
			return Optional.empty();
		}

		for (String eidSystem : getEidSystemsFor(theResource)) {
			Optional<CanonicalEID> eidForSystem = externalEids.stream()
					.filter(eid -> Objects.equals(eid.getSystem(), eidSystem))
					.findFirst();
			if (eidForSystem.isPresent()) {
				return eidForSystem;
			}
		}

		// Defensive only: the EIDs were extracted from these same systems, so one of them always matched.
		return Optional.of(externalEids.get(0));
	}

	private List<String> getEidSystemsFor(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		return myMdmSettings.getMdmRules().getEnterpriseEIDSystemsForResourceType(resourceType);
	}

	/**
	 * Given an {@link IAnyResource} representing a type supported by MDM, retrieve their internally-assigned EID,
	 * represented as a {@link CanonicalEID}
	 *
	 * @param theResource the resource to extract the EID from.
	 *
	 * @return An optional {@link CanonicalEID} representing the internal EID. Absent if the EID is not present.
	 */
	public List<CanonicalEID> getHapiEid(IAnyResource theResource) {
		return CanonicalEID.extractFromResource(
				myFhirContext, MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, theResource);
	}

	/**
	 * Determines whether two lists of {@link CanonicalEID} have any intersection. Two resources are considered a match if
	 * a single {@link CanonicalEID} matches between the two collections.
	 * <p>
	 * EIDs are compared on system and value, so that the same value issued by two different EID systems - an
	 * MRN and an NPI that happen to read alike, say - is not mistaken for one identifier.
	 * {@code Identifier.use} takes no part in the comparison.
	 * </p>
	 *
	 * @param theFirstResourceEids the first EID
	 * @param theSecondResourceEids the second EID
	 *
	 * @return a boolean indicating whether there is a match between these two identifier sets.
	 */
	public boolean eidMatchExists(List<CanonicalEID> theFirstResourceEids, List<CanonicalEID> theSecondResourceEids) {
		Set<String> firstKeys = theFirstResourceEids.stream()
				.map(CanonicalEID::getSystemAndValueKey)
				.collect(Collectors.toSet());
		return theSecondResourceEids.stream()
				.map(CanonicalEID::getSystemAndValueKey)
				.anyMatch(firstKeys::contains);
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a source resource that has a golden resource with an
	 * official EID, but the incoming resource also has an EID that does not match.
	 */
	public boolean hasEidOverlap(IAnyResource theExistingGoldenResource, IAnyResource theComparingGoldenResource) {
		List<CanonicalEID> firstEids = this.getExternalEid(theExistingGoldenResource);
		List<CanonicalEID> secondEids = this.getExternalEid(theComparingGoldenResource);
		if (firstEids.isEmpty() || secondEids.isEmpty()) {
			return false;
		}
		return this.eidMatchExists(firstEids, secondEids);
	}
}
