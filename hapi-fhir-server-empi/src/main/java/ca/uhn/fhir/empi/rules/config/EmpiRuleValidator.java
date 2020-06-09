package ca.uhn.fhir.empi.rules.config;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class EmpiRuleValidator {
	private final FhirContext myFhirContext;
	private final Class<? extends IBaseResource> myPatientClass;
	private final Class<? extends IBaseResource> myPractitionerClass;
	private final FhirTerser myTerser;

	@Autowired
	public EmpiRuleValidator(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		myPatientClass = theFhirContext.getResourceDefinition("Patient").getImplementingClass();
		myPractitionerClass = theFhirContext.getResourceDefinition("Practitioner").getImplementingClass();
		myTerser = theFhirContext.newTerser();
	}

	public void validate(EmpiRulesJson theEmpiRulesJson) {
		validateSystemIsUri(theEmpiRulesJson);
		validateMatchFields(theEmpiRulesJson);
	}

	private void validateSystemIsUri(EmpiRulesJson theEmpiRulesJson) {
		if (theEmpiRulesJson.getEnterpriseEIDSystem() == null) {
			return;
		}

		try {
			new URI(theEmpiRulesJson.getEnterpriseEIDSystem());
		} catch (URISyntaxException e) {
			throw new ConfigurationException("Enterprise Identifier System (eidSystem) must be a valid URI");
		}
	}

	private void validateMatchFields(EmpiRulesJson theEmpiRulesJson) {
		for (EmpiFieldMatchJson fieldMatch : theEmpiRulesJson.getMatchFields()) {
			validateThreshold(fieldMatch);
			validatePath(fieldMatch);
		}
	}

	private void validateThreshold(EmpiFieldMatchJson theFieldMatch) {
		if (theFieldMatch.getMetric().isSimilarity()) {
			if (theFieldMatch.getMatchThreshold() == null) {
				throw new ConfigurationException("MatchField " + theFieldMatch.getName() + " metric " + theFieldMatch.getMetric() + " requires a matchThreshold");
			}
		} else if (theFieldMatch.getMatchThreshold() != null) {
			throw new ConfigurationException("MatchField " + theFieldMatch.getName() + " metric " + theFieldMatch.getMetric() + " should not have a matchThreshold");
		}
	}

	// FIXME KHS validate the other parts of the rules
	private void validatePath(EmpiFieldMatchJson theFieldMatch) {
		String resourceType = theFieldMatch.getResourceType();
		if ("*".equals(resourceType)) {
			validatePatientPath(theFieldMatch);
			// FIXME KHS test where one matches and the other doesnt
			validatePractitionerPath(theFieldMatch);
		} else if ("Patient".equals(resourceType)) {
			validatePatientPath(theFieldMatch);
		} else if ("Practitioner".equals(resourceType)) {
			validatePractitionerPath(theFieldMatch);
		} else {
			// FIXME KHS test
			throw new ConfigurationException("MatchField " + theFieldMatch.getName() + " has unknown resourceType " + resourceType);
		}
	}

	private void validatePatientPath(EmpiFieldMatchJson theFieldMatch) {
		try {
			myTerser.getDefinition(myPatientClass, theFieldMatch.getResourcePath());
		} catch (DataFormatException e) {
			throw new ConfigurationException("MatchField " +
				theFieldMatch.getName() +
				" resourceType " +
				theFieldMatch.getResourceType() +
				" has invalid path '" + theFieldMatch.getResourcePath() + "'.  " +
				e.getMessage());
		}
	}

	private void validatePractitionerPath(EmpiFieldMatchJson theFieldMatch) {
		try {
			myTerser.getDefinition(myPractitionerClass, theFieldMatch.getResourcePath());
		} catch (DataFormatException e) {
			throw new ConfigurationException("MatchField " +
				theFieldMatch.getName() +
				" resourceType " +
				theFieldMatch.getResourceType() +
				" has invalid path '" + theFieldMatch.getResourcePath() + "'.  " +
				e.getMessage());
		}
	}

}
