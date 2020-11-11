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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.json.EmpiSimilarityJson;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmpiRuleValidator implements IEmpiRuleValidator {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRuleValidator.class);

	private final FhirContext myFhirContext;
	private final ISearchParamRetriever mySearchParamRetriever;
	private final Class<? extends IBaseResource> myPatientClass;
	private final Class<? extends IBaseResource> myPractitionerClass;
	private final FhirTerser myTerser;

	@Autowired
	public EmpiRuleValidator(FhirContext theFhirContext, ISearchParamRetriever theSearchParamRetriever) {
		myFhirContext = theFhirContext;
		myPatientClass = theFhirContext.getResourceDefinition("Patient").getImplementingClass();
		myPractitionerClass = theFhirContext.getResourceDefinition("Practitioner").getImplementingClass();
		myTerser = myFhirContext.newTerser();
		mySearchParamRetriever = theSearchParamRetriever;
	}

	public void validate(EmpiRulesJson theEmpiRulesJson) {
		validateMdmTypes(theEmpiRulesJson);
		validateSearchParams(theEmpiRulesJson);
		validateMatchFields(theEmpiRulesJson);
		validateSystemIsUri(theEmpiRulesJson);
	}

	public void validateMdmTypes(EmpiRulesJson theEmpiRulesJson) {
		for (String resourceType: theEmpiRulesJson.getMdmTypes()) {
			validateTypeHasIdentifier(resourceType);
		}
	}

	public void validateTypeHasIdentifier(String theResourceType) {
		if (mySearchParamRetriever.getActiveSearchParam(theResourceType, "identifier") == null) {
			throw new ConfigurationException("Resource Type " + theResourceType + " is not supported, as it does not have an 'identifier' field, which is necessary for MDM workflow.");
		}
	}

	private void validateSearchParams(EmpiRulesJson theEmpiRulesJson) {
		for (EmpiResourceSearchParamJson searchParams : theEmpiRulesJson.getCandidateSearchParams()) {
			searchParams.iterator().forEachRemaining(
				searchParam -> validateSearchParam("candidateSearchParams", searchParams.getResourceType(), searchParam));
		}
		for (EmpiFilterSearchParamJson filter : theEmpiRulesJson.getCandidateFilterSearchParams()) {
			validateSearchParam("candidateFilterSearchParams", filter.getResourceType(), filter.getSearchParam());
		}
	}

	private void validateSearchParam(String theFieldName, String theTheResourceType, String theTheSearchParam) {
		if (EmpiConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(theTheResourceType)) {
			validateResourceSearchParam(theFieldName, "Patient", theTheSearchParam);
			validateResourceSearchParam(theFieldName, "Practitioner", theTheSearchParam);
		} else {
			validateResourceSearchParam(theFieldName, theTheResourceType, theTheSearchParam);
		}
	}

	private void validateResourceSearchParam(String theFieldName, String theResourceType, String theSearchParam) {
		if (mySearchParamRetriever.getActiveSearchParam(theResourceType, theSearchParam) == null) {
			throw new ConfigurationException("Error in " + theFieldName + ": " + theResourceType + " does not have a search parameter called '" + theSearchParam + "'");
		}
	}

	private void validateMatchFields(EmpiRulesJson theEmpiRulesJson) {
		Set<String> names = new HashSet<>();
		for (EmpiFieldMatchJson fieldMatch : theEmpiRulesJson.getMatchFields()) {
			if (names.contains(fieldMatch.getName())) {
				throw new ConfigurationException("Two MatchFields have the same name '" + fieldMatch.getName() + "'");
			}
			names.add(fieldMatch.getName());
			if (fieldMatch.getSimilarity() != null) {
				validateSimilarity(fieldMatch);
			} else if (fieldMatch.getMatcher() == null) {
				throw new ConfigurationException("MatchField " + fieldMatch.getName() + " has neither a similarity nor a matcher.  At least one must be present.");
			}
			validatePath(theEmpiRulesJson.getMdmTypes(), fieldMatch);
		}
	}

	private void validateSimilarity(EmpiFieldMatchJson theFieldMatch) {
		EmpiSimilarityJson similarity = theFieldMatch.getSimilarity();
		if (similarity.getMatchThreshold() == null) {
			throw new ConfigurationException("MatchField " + theFieldMatch.getName() + " similarity " + similarity.getAlgorithm() + " requires a matchThreshold");
		}
	}

	private void validatePath(List<String> theMdmTypes, EmpiFieldMatchJson theFieldMatch) {
		String resourceType = theFieldMatch.getResourceType();


		if (EmpiConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(resourceType)) {
			validateFieldPathForAllTypes(theMdmTypes, theFieldMatch);
		} else {
			validateFieldPath(theFieldMatch);
		}
	}

	private void validateFieldPathForAllTypes(List<String> theMdmResourceTypes, EmpiFieldMatchJson theFieldMatch) {

		for (String resourceType: theMdmResourceTypes) {
			validateFieldPathForType(resourceType, theFieldMatch);
		}
	}

	private void validateFieldPathForType(String theResourceType, EmpiFieldMatchJson theFieldMatch) {
		try {
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
			Class<? extends IBaseResource> implementingClass = resourceDefinition.getImplementingClass();
			myTerser.getDefinition(implementingClass, theResourceType + "." + theFieldMatch.getResourcePath());
		} catch (DataFormatException | ConfigurationException e) {
			throw new ConfigurationException("MatchField " +
				theFieldMatch.getName() +
				" resourceType " +
				theFieldMatch.getResourceType() +
				" has invalid path '" + theFieldMatch.getResourcePath() + "'.  " +
				e.getMessage());

		}
	}

	private void validateFieldPath(EmpiFieldMatchJson theFieldMatch) {
		validateFieldPathForType(theFieldMatch.getResourceType(), theFieldMatch);
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
}
