package ca.uhn.fhir.mdm.rules.config;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmFilterSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MdmRuleValidator implements IMdmRuleValidator {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmRuleValidator.class);

	private final FhirContext myFhirContext;
	private final ISearchParamRegistry mySearchParamRetriever;
	private final FhirTerser myTerser;
	private final IFhirPath myFhirPath;

	@Autowired
	public MdmRuleValidator(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRetriever) {
		myFhirContext = theFhirContext;
		myTerser = myFhirContext.newTerser();
		if (myFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			myFhirPath = myFhirContext.newFhirPath();
		} else {
			ourLog.debug("Skipping FHIRPath validation as DSTU2 does not support FHIR");
			myFhirPath = null;
		}
		mySearchParamRetriever = theSearchParamRetriever;
	}

	public void validate(MdmRulesJson theMdmRules) {
		validateMdmTypes(theMdmRules);
		validateSearchParams(theMdmRules);
		validateMatchFields(theMdmRules);
		validateSystemsAreUris(theMdmRules);
		validateEidSystemsMatchMdmTypes(theMdmRules);
	}

	private void validateEidSystemsMatchMdmTypes(MdmRulesJson theMdmRules) {
		theMdmRules.getEnterpriseEIDSystems().keySet()
			.forEach(key -> {
				//Ensure each key is either * or a valid resource type.
				if (!key.equalsIgnoreCase("*") && !theMdmRules.getMdmTypes().contains(key)) {
					throw new ConfigurationException(Msg.code(1507) + String.format("There is an eidSystem set for [%s] but that is not one of the mdmTypes. Valid options are [%s].", key, buildValidEidKeysMessage(theMdmRules)));
				}
			});
	}

	private String buildValidEidKeysMessage(MdmRulesJson theMdmRulesJson) {
		List<String> validTypes = new ArrayList<>(theMdmRulesJson.getMdmTypes());
		validTypes.add("*");
		return String.join(", ", validTypes);
	}

	private void validateSystemsAreUris(MdmRulesJson theMdmRules) {
		theMdmRules.getEnterpriseEIDSystems().entrySet()
			.forEach(entry -> {
				String resourceType = entry.getKey();
				String uri = entry.getValue();
				if (!resourceType.equals("*")) {
					try {
						myFhirContext.getResourceType(resourceType);
					}catch (DataFormatException e) {
						throw new ConfigurationException(Msg.code(1508) + String.format("%s is not a valid resource type, but is set in the eidSystems field.", resourceType));
					}
				}
				validateIsUri(uri);
			});
	}

	public void validateMdmTypes(MdmRulesJson theMdmRulesJson) {
		ourLog.info("Validating MDM types {}", theMdmRulesJson.getMdmTypes());

		if (theMdmRulesJson.getMdmTypes() == null) {
			throw new ConfigurationException(Msg.code(1509) + "mdmTypes must be set to a list of resource types.");
		}
		for (String resourceType: theMdmRulesJson.getMdmTypes()) {
			validateTypeHasIdentifier(resourceType);
		}
	}

	public void validateTypeHasIdentifier(String theResourceType) {
		if (mySearchParamRetriever.getActiveSearchParam(theResourceType, "identifier") == null) {
			throw new ConfigurationException(Msg.code(1510) + "Resource Type " + theResourceType + " is not supported, as it does not have an 'identifier' field, which is necessary for MDM workflow.");
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
		String searchParam = SearchParameterUtil.stripModifier(theSearchParam);
		if (mySearchParamRetriever.getActiveSearchParam(theResourceType, searchParam) == null) {
			throw new ConfigurationException(Msg.code(1511) + "Error in " + theFieldName + ": " + theResourceType + " does not have a search parameter called '" + theSearchParam + "'");
		}
	}

	private void validateMatchFields(MdmRulesJson theMdmRulesJson) {
		ourLog.info("Validating match fields {}", theMdmRulesJson.getMatchFields());

		Set<String> names = new HashSet<>();
		for (MdmFieldMatchJson fieldMatch : theMdmRulesJson.getMatchFields()) {
			if (names.contains(fieldMatch.getName())) {
				throw new ConfigurationException(Msg.code(1512) + "Two MatchFields have the same name '" + fieldMatch.getName() + "'");
			}
			names.add(fieldMatch.getName());
			if (fieldMatch.getSimilarity() != null) {
				validateSimilarity(fieldMatch);
			} else if (fieldMatch.getMatcher() == null) {
				throw new ConfigurationException(Msg.code(1513) + "MatchField " + fieldMatch.getName() + " has neither a similarity nor a matcher.  At least one must be present.");
			}
			validatePath(theMdmRulesJson.getMdmTypes(), fieldMatch);
		}
	}

	private void validateSimilarity(MdmFieldMatchJson theFieldMatch) {
		MdmSimilarityJson similarity = theFieldMatch.getSimilarity();
		if (similarity.getMatchThreshold() == null) {
			throw new ConfigurationException(Msg.code(1514) + "MatchField " + theFieldMatch.getName() + " similarity " + similarity.getAlgorithm() + " requires a matchThreshold");
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
		ourLog.debug("Validating resource {} for {} ", theResourceType, theFieldMatch.getResourcePath());

		if (theFieldMatch.getFhirPath() != null && theFieldMatch.getResourcePath() != null) {
			throw new ConfigurationException(Msg.code(1515) + "MatchField [" +
				theFieldMatch.getName() +
				"] resourceType [" +
				theFieldMatch.getResourceType() +
				"] has defined both a resourcePath and a fhirPath. You must define one of the two.");
		}

		if (theFieldMatch.getResourcePath() == null && theFieldMatch.getFhirPath() == null) {
			throw new ConfigurationException(Msg.code(1516) + "MatchField [" +
				theFieldMatch.getName() +
					"] resourceType [" +
					theFieldMatch.getResourceType() +
					"] has defined neither a resourcePath or a fhirPath. You must define one of the two.");
		}

		if (theFieldMatch.getResourcePath() != null) {
			try { //Try to validate the struture definition path
				RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
				Class<? extends IBaseResource> implementingClass = resourceDefinition.getImplementingClass();
				String path = theResourceType + "." + theFieldMatch.getResourcePath();
				myTerser.getDefinition(implementingClass, path);
			} catch (DataFormatException | ConfigurationException | ClassCastException e) {
				//Fallback to attempting to FHIRPath evaluate it.
				throw new ConfigurationException(Msg.code(1517) + "MatchField " +
					theFieldMatch.getName() +
					" resourceType " +
					theFieldMatch.getResourceType() +
					" has invalid path '" + theFieldMatch.getResourcePath() + "'.  " + e.getMessage());
			}
		} else { //Try to validate the FHIRPath
			try {
				if (myFhirPath != null) {
					myFhirPath.parse(theResourceType + "." + theFieldMatch.getFhirPath());
				} else {
					ourLog.debug("Can't validate FHIRPath expression due to a lack of IFhirPath object.");
				}
			} catch (Exception e) {
				throw new ConfigurationException(Msg.code(1518) + "MatchField [" + theFieldMatch.getName() + "] resourceType [" + theFieldMatch.getResourceType() + "] has failed FHIRPath evaluation.  " + e.getMessage());
			}
		}
	}

	private void validateFieldPath(MdmFieldMatchJson theFieldMatch) {
		validateFieldPathForType(theFieldMatch.getResourceType(), theFieldMatch);
	}

	private void validateIsUri(String theUri) {
		ourLog.info("Validating system URI {}", theUri);
		try {
			new URI(theUri);
		} catch (URISyntaxException e) {
			throw new ConfigurationException(Msg.code(1519) + "Enterprise Identifier System (eidSystem) must be a valid URI");
		}
	}
}
