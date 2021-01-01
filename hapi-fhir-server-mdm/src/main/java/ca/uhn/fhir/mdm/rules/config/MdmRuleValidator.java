package ca.uhn.fhir.mdm.rules.config;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmFilterSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
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
public class MdmRuleValidator implements IMdmRuleValidator {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmRuleValidator.class);

	private final FhirContext myFhirContext;
	private final ISearchParamRetriever mySearchParamRetriever;
	private final Class<? extends IBaseResource> myPatientClass;
	private final Class<? extends IBaseResource> myPractitionerClass;
	private final FhirTerser myTerser;

	@Autowired
	public MdmRuleValidator(FhirContext theFhirContext, ISearchParamRetriever theSearchParamRetriever) {
		myFhirContext = theFhirContext;
		myPatientClass = theFhirContext.getResourceDefinition("Patient").getImplementingClass();
		myPractitionerClass = theFhirContext.getResourceDefinition("Practitioner").getImplementingClass();
		myTerser = myFhirContext.newTerser();
		mySearchParamRetriever = theSearchParamRetriever;
	}

	public void validate(MdmRulesJson theMdmRules) {
		validateMdmTypes(theMdmRules);
		validateSearchParams(theMdmRules);
		validateMatchFields(theMdmRules);
		validateSystemIsUri(theMdmRules);
	}

	public void validateMdmTypes(MdmRulesJson theMdmRulesJson) {
		ourLog.info("Validating MDM types {}", theMdmRulesJson.getMdmTypes());

		if (theMdmRulesJson.getMdmTypes() == null) {
			throw new ConfigurationException("mdmTypes must be set to a list of resource types.");
		}
		for (String resourceType: theMdmRulesJson.getMdmTypes()) {
			validateTypeHasIdentifier(resourceType);
		}
	}

	public void validateTypeHasIdentifier(String theResourceType) {
		if (mySearchParamRetriever.getActiveSearchParam(theResourceType, "identifier") == null) {
			throw new ConfigurationException("Resource Type " + theResourceType + " is not supported, as it does not have an 'identifier' field, which is necessary for MDM workflow.");
		}
	}

	private void validateSearchParams(MdmRulesJson theMdmRulesJson) {
		ourLog.info("Validating search parameters {}", theMdmRulesJson.getCandidateSearchParams());

		for (MdmResourceSearchParamJson searchParams : theMdmRulesJson.getCandidateSearchParams()) {
			searchParams.iterator().forEachRemaining(
				searchParam -> validateSearchParam("candidateSearchParams", searchParams.getResourceType(), searchParam));
		}
		for (MdmFilterSearchParamJson filter : theMdmRulesJson.getCandidateFilterSearchParams()) {
			validateSearchParam("candidateFilterSearchParams", filter.getResourceType(), filter.getSearchParam());
		}
	}

	private void validateSearchParam(String theFieldName, String theTheResourceType, String theTheSearchParam) {
		if (MdmConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(theTheResourceType)) {
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

	private void validateMatchFields(MdmRulesJson theMdmRulesJson) {
		ourLog.info("Validating match fields {}", theMdmRulesJson.getMatchFields());

		Set<String> names = new HashSet<>();
		for (MdmFieldMatchJson fieldMatch : theMdmRulesJson.getMatchFields()) {
			if (names.contains(fieldMatch.getName())) {
				throw new ConfigurationException("Two MatchFields have the same name '" + fieldMatch.getName() + "'");
			}
			names.add(fieldMatch.getName());
			if (fieldMatch.getSimilarity() != null) {
				validateSimilarity(fieldMatch);
			} else if (fieldMatch.getMatcher() == null) {
				throw new ConfigurationException("MatchField " + fieldMatch.getName() + " has neither a similarity nor a matcher.  At least one must be present.");
			}
			validatePath(theMdmRulesJson.getMdmTypes(), fieldMatch);
		}
	}

	private void validateSimilarity(MdmFieldMatchJson theFieldMatch) {
		MdmSimilarityJson similarity = theFieldMatch.getSimilarity();
		if (similarity.getMatchThreshold() == null) {
			throw new ConfigurationException("MatchField " + theFieldMatch.getName() + " similarity " + similarity.getAlgorithm() + " requires a matchThreshold");
		}
	}

	private void validatePath(List<String> theMdmTypes, MdmFieldMatchJson theFieldMatch) {
		String resourceType = theFieldMatch.getResourceType();

		if (MdmConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(resourceType)) {
			validateFieldPathForAllTypes(theMdmTypes, theFieldMatch);
		} else {
			validateFieldPath(theFieldMatch);
		}
	}

	private void validateFieldPathForAllTypes(List<String> theMdmResourceTypes, MdmFieldMatchJson theFieldMatch) {

		for (String resourceType: theMdmResourceTypes) {
			validateFieldPathForType(resourceType, theFieldMatch);
		}
	}

	private void validateFieldPathForType(String theResourceType, MdmFieldMatchJson theFieldMatch) {
		ourLog.debug(" validating resource {} for {} ", theResourceType, theFieldMatch.getResourcePath());

		try {
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
			Class<? extends IBaseResource> implementingClass = resourceDefinition.getImplementingClass();
			String path = theResourceType + "." + theFieldMatch.getResourcePath();
			myTerser.getDefinition(implementingClass, path);
		} catch (DataFormatException | ConfigurationException | ClassCastException e) {
			throw new ConfigurationException("MatchField " +
				theFieldMatch.getName() +
				" resourceType " +
				theFieldMatch.getResourceType() +
				" has invalid path '" + theFieldMatch.getResourcePath() + "'.  " +
				e.getMessage());
		}
	}

	private void validateFieldPath(MdmFieldMatchJson theFieldMatch) {
		validateFieldPathForType(theFieldMatch.getResourceType(), theFieldMatch);
	}

	private void validateSystemIsUri(MdmRulesJson theMdmRulesJson) {
		if (theMdmRulesJson.getEnterpriseEIDSystem() == null) {
			return;
		}

		ourLog.info("Validating system URI {}", theMdmRulesJson.getEnterpriseEIDSystem());

		try {
			new URI(theMdmRulesJson.getEnterpriseEIDSystem());
		} catch (URISyntaxException e) {
			throw new ConfigurationException("Enterprise Identifier System (eidSystem) must be a valid URI");
		}
	}
}
