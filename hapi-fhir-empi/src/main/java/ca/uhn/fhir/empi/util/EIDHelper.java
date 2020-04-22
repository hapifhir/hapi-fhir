package ca.uhn.fhir.empi.util;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.model.CanonicalEID;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public final class EIDHelper {
	private final FhirContext myFhirContext;
	private final IEmpiSettings myEmpiConfig;

	@Autowired
	public EIDHelper(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		myFhirContext = theFhirContext;
		myEmpiConfig = theEmpiConfig;
	}

	public CanonicalEID createHapiEid() {
		return new CanonicalEID(
			Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM,
			UUID.randomUUID().toString(),
			null
		);
	}

	/**
	 * Given an {@link IBaseResource} representing a patient/practitioner/person, retrieve their externally-assigned EID,
	 * represented as a {@link CanonicalEID}
	 *
	 * @param theResource the resource to extract the EID from.
	 *
	 * @return An optional {@link CanonicalEID} representing the external EID. Absent if the EID is not present.
	 */
	public Optional<CanonicalEID> getExternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theResource);
	}

	/**
	 * Given an {@link IBaseResource} representing a patient/practitioner/person, retrieve their internally-assigned EID,
	 * represented as a {@link CanonicalEID}
	 *
	 * @param theResource the resource to extract the EID from.
	 *
	 * @return An optional {@link CanonicalEID} representing the internal EID. Absent if the EID is not present.
	 */
	public Optional<CanonicalEID> getHapiEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, theResource);
	}

	/**
	 * Determines whether two {@link CanonicalEID} have the same system and value.
	 *
	 * @param theFirstEid the first EID
	 * @param theSecondEid the second EID
	 *
	 * @return a boolean indicating whether they are the same.
	 */
	public boolean eidsMatch(CanonicalEID theFirstEid, CanonicalEID theSecondEid) {
		return Objects.equals(theFirstEid.getValue(), theSecondEid.getValue()) && Objects.equals(theFirstEid.getSystem(), theSecondEid.getSystem());
	}


}
