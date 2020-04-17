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
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Helper class to determine assurance level based on Link Source and Match Result.
 */
public final static class AssuranceLevelHelper {
	public static CanonicalIdentityAssuranceLevel getAssuranceLevel(EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theSource) {
		switch (theSource) {
			case MANUAL:
				switch (theMatchResult) {
					case MATCH:
					case NO_MATCH:
						return CanonicalIdentityAssuranceLevel.LEVEL4; //FIXME EMPI QUESTION the docs say no_match should be level 1 but i feel a manual no_match should be 4.
					case POSSIBLE_DUPLICATE:
					case POSSIBLE_MATCH:
						throw new InvalidRequestException("You cannot manually set a possible match!");
				}
				break;
			case AUTO:
				switch (theMatchResult) {
					case MATCH:
						return CanonicalIdentityAssuranceLevel.LEVEL3;
					case NO_MATCH:
						return CanonicalIdentityAssuranceLevel.LEVEL1;
					case POSSIBLE_MATCH:
						return CanonicalIdentityAssuranceLevel.LEVEL2;
					case POSSIBLE_DUPLICATE: //FIXME EMPI QUESTION PoSSIBLE_DUPLICATE doesnt result in an actual link being made, so is this valid??
						return CanonicalIdentityAssuranceLevel.LEVEL2;
				}
		}
		throw new InvalidRequestException("Couldn't figure out an assurance level for result: " + theMatchResult + " and source " + theSource);
	}
}
