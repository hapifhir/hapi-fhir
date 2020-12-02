package ca.uhn.fhir.mdm.util;

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

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.CanonicalIdentityAssuranceLevel;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Helper class to determine assurance level based on Link Source and Match Result.
 * This is strictly for use in populating Golden Resource links.
 */
public final class AssuranceLevelUtil {

	private AssuranceLevelUtil() {
	}

	public static CanonicalIdentityAssuranceLevel getAssuranceLevel(MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theSource) {
		switch (theSource) {
			case MANUAL:
				return getAssuranceFromManualResult(theMatchResult);
			case AUTO:
				return getAssuranceFromAutoResult(theMatchResult);
		}
		throw new InvalidRequestException("Couldn't figure out an assurance level for result: " + theMatchResult + " and source " + theSource);
	}

	private static CanonicalIdentityAssuranceLevel getAssuranceFromAutoResult(MdmMatchResultEnum theMatchResult) {
		switch (theMatchResult) {
			case MATCH:
				return CanonicalIdentityAssuranceLevel.LEVEL2;
			case POSSIBLE_MATCH:
				return CanonicalIdentityAssuranceLevel.LEVEL1;
			case POSSIBLE_DUPLICATE:
			case NO_MATCH:
			default:
				throw new InvalidRequestException("An AUTO MDM Link may not have a match result of " + theMatchResult);
		}
	}

	private static CanonicalIdentityAssuranceLevel getAssuranceFromManualResult(MdmMatchResultEnum theMatchResult) {
		switch (theMatchResult) {
			case MATCH:
			case REDIRECT:
				return CanonicalIdentityAssuranceLevel.LEVEL3;
			case NO_MATCH:
			case POSSIBLE_DUPLICATE:
			case POSSIBLE_MATCH:
			default:
				throw new InvalidRequestException("A MANUAL MDM Link may not have a match result of " + theMatchResult);
		}
	}
}
