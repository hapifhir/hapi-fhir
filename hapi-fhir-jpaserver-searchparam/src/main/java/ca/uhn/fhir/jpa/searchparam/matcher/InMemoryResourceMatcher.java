/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.models.SearchMatchParameters;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryResourceMatcher implements IMatchingServices {

	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(InMemoryResourceMatcher.class);

	@Autowired
	ApplicationContext myApplicationContext;

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	StorageSettings myStorageSettings;

	@Autowired
	FhirContext myFhirContext;

	@Autowired
	SearchParamExtractorService mySearchParamExtractorService;

	@Autowired
	IndexedSearchParamExtractor myIndexedSearchParamExtractor;

	@Autowired
	private MatchUrlService myMatchUrlService;

	private final List<IParameterMatchHandler> myMatchHandlers = new LinkedList<>();

	private ValidationSupportInitializationState validationSupportState =
			ValidationSupportInitializationState.NOT_INITIALIZED;
	private IValidationSupport myValidationSupport = null;

	public InMemoryResourceMatcher() {
		addSearchParameterHandler(new DefaultParameterMatchHandler());
	}

	public void addSearchParameterHandler(IParameterMatchHandler theMatchHandler) {
		theMatchHandler.registerServices(this);
		myMatchHandlers.add(0, theMatchHandler);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * Lazy loads a {@link IValidationSupport} implementation just-in-time.
	 * If no suitable bean is available, or if a {@link ca.uhn.fhir.context.ConfigurationException} is thrown, matching
	 * can proceed, but the qualifiers that depend on the validation support will be disabled.
	 *
	 * @return A bean implementing {@link IValidationSupport} if one is available, otherwise null
	 */
	public IValidationSupport getValidationSupportOrNull() {
		if (validationSupportState == ValidationSupportInitializationState.NOT_INITIALIZED) {
			try {
				myValidationSupport = myApplicationContext.getBean(IValidationSupport.class);
				validationSupportState = ValidationSupportInitializationState.INITIALIZED;
			} catch (BeansException | ConfigurationException ignore) {
				// We couldn't get a validation support bean, and we don't want to waste cycles trying again
				ourLog.warn(
						Msg.code(2100)
								+ "No bean satisfying IValidationSupport could be initialized. Qualifiers dependent on IValidationSupport will not be supported.");
				validationSupportState = ValidationSupportInitializationState.FAILED;
			}
		}
		return myValidationSupport;
	}

	@Override
	public StorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	/**
	 * @deprecated Use {@link #match(String, IBaseResource, ResourceIndexedSearchParams, RequestDetails)}
	 */
	@Deprecated
	public InMemoryMatchResult match(
			String theCriteria,
			IBaseResource theResource,
			@Nullable ResourceIndexedSearchParams theIndexedSearchParams) {
		return match(theCriteria, theResource, theIndexedSearchParams, null);
	}

	/**
	 * This method is called in two different scenarios.  With a null theResource, it determines whether database matching might be required.
	 * Otherwise, it tries to perform the match in-memory, returning UNSUPPORTED if it's not possible.
	 * <p>
	 * Note that there will be cases where it returns UNSUPPORTED with a null resource, but when a non-null resource it returns supported and no match.
	 * This is because an earlier parameter may be matchable in-memory in which case processing stops and we never get to the parameter
	 * that would have required a database call.
	 *
	 * @param theIndexedSearchParams If the search params have already been calculated for the given resource,
	 *                               they can be passed in. Passing in {@literal null} is also fine, in which
	 *                               case they will be calculated for the resource. It can be preferable to
	 *                               pass in {@literal null} unless you already actually had to calculate the
	 *                               indexes for another reason, since we can be efficient here and only calculate
	 *                               the params that are actually relevant for the given search expression.
	 */
	@Deprecated
	public InMemoryMatchResult match(
			String theCriteria,
			IBaseResource theResource,
			@Nullable ResourceIndexedSearchParams theIndexedSearchParams,
			RequestDetails theRequestDetails) {
		SearchMatchParameters parameters = new SearchMatchParameters();
		parameters.setBaseResource(theResource);
		parameters.setCriteria(theCriteria);
		parameters.setRequestDetails(theRequestDetails);
		parameters.setIndexedSearchParams(theIndexedSearchParams);
		return match(parameters);
	}

	public InMemoryMatchResult match(SearchMatchParameters theSearchMatchParameters) {
		IBaseResource theResource = theSearchMatchParameters.getBaseResource();

		// get the resource definition
		if (!theSearchMatchParameters.hasRuntimeResourceDefinition()) {
			RuntimeResourceDefinition resourceDefinition;
			if (theResource == null && theSearchMatchParameters.hasCriteria()) {
				Validate.isTrue(
					!theSearchMatchParameters.getCriteria().startsWith("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
				Validate.isTrue(
					theSearchMatchParameters.getCriteria().contains("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
				resourceDefinition = UrlUtil.parseUrlResourceType(myFhirContext, theSearchMatchParameters.getCriteria());
			} else {
				resourceDefinition = myFhirContext.getResourceDefinition(theResource);
			}
			theSearchMatchParameters.setRuntimeResourceDefinition(resourceDefinition);
		}

		// get the SearchParamMap
		if (!theSearchMatchParameters.hasSearchParameterMap()) {
			SearchParameterMap searchParameterMap;
			try {
				searchParameterMap = myMatchUrlService.translateMatchUrl(theSearchMatchParameters.getCriteria(), theSearchMatchParameters.getRuntimeResourceDefinition());
			} catch (UnsupportedOperationException e) {
				return InMemoryMatchResult.unsupportedFromReason(InMemoryMatchResult.PARSE_FAIL);
			}
			searchParameterMap.clean();
			// set the search parameter map
			theSearchMatchParameters.setSearchParameterMap(searchParameterMap);
		}

		// we'll get the indexed search parameters for this flow only
		if (theSearchMatchParameters.isRequiresResourceIndexedSearchParams()) {
			getResourceIndexedSearchParameters(theSearchMatchParameters);
		}

		return doMatchInternal(theSearchMatchParameters);
//		return match(searchParameterMap, theResource, resourceDefinition, relevantSearchParams);
	}

	private void getResourceIndexedSearchParameters(SearchMatchParameters theParameters) {
		assert theParameters.hasSearchParameterMap() : "Search parameter map required to fetch resourceindexed search parameters";
		SearchParameterMap searchParameterMap = theParameters.getSearchParameterMap();

		if (!theParameters.hasResourceIndexedSearchParameters() && theParameters.hasBaseResource()) {
			// Don't index search params we don't actually need for the given criteria
			ISearchParamExtractor.ISearchParamFilter filter = theSearchParams -> theSearchParams.stream()
				.filter(t -> searchParameterMap.containsKey(t.getName()))
				.collect(Collectors.toList());
			ResourceIndexedSearchParams relevantSearchParams =
				myIndexedSearchParamExtractor.extractIndexedSearchParams(theParameters.getBaseResource(), theParameters.getRequestDetails(), filter);
			theParameters.setIndexedSearchParams(relevantSearchParams);
		} else {
			// fail
			// TODO - not required
		}
	}

	/**
	 * @param theCriteria
	 * @return result.supported() will be true if theCriteria can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(String theCriteria) {
		return match(theCriteria, null, null, null);
	}

	/**
	 * @param theSearchParameterMap
	 * @param theResourceDefinition
	 * @return result.supported() will be true if theSearchParameterMap can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(
			SearchParameterMap theSearchParameterMap, RuntimeResourceDefinition theResourceDefinition) {
//		return match(theSearchParameterMap, null, theResourceDefinition, null);
		SearchMatchParameters parameters = new SearchMatchParameters();
		parameters.setSearchParameterMap(theSearchParameterMap);
		parameters.setRuntimeResourceDefinition(theResourceDefinition);
		return doMatchInternal(parameters);
	}

	private InMemoryMatchResult doMatchInternal(SearchMatchParameters theParameters) {
		SearchParameterMap theSearchParameterMap = theParameters.getSearchParameterMap();
		assert theSearchParameterMap != null : "SearchParameterMap is required";

		if (theSearchParameterMap.getLastUpdated() != null) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(
				Constants.PARAM_LASTUPDATED, InMemoryMatchResult.STANDARD_PARAMETER);
		}
		if (theSearchParameterMap.containsKey(Location.SP_NEAR)) {
			return InMemoryMatchResult.unsupportedFromReason(InMemoryMatchResult.LOCATION_NEAR);
		}

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : theSearchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<IQueryParameterType>> theAndOrParams = entry.getValue();
			InMemoryMatchResult result = matchIdsWithAndOr(
				theParamName, theAndOrParams,
//				theResourceDefinition, theResource, theSearchParams
				theParameters
			);
			if (!result.matched()) {
				return result;
			}
		}
		return InMemoryMatchResult.successfulMatch();
	}

	// This method is modelled from SearchBuilder.searchForIdsWithAndOr()
	private InMemoryMatchResult matchIdsWithAndOr(
		String theParamName,
		List<List<IQueryParameterType>> theAndOrParams,
		SearchMatchParameters theParameters
	) {
		if (theAndOrParams.isEmpty()) {
			return InMemoryMatchResult.successfulMatch();
		}

		RuntimeResourceDefinition theResourceDefinition = theParameters.getRuntimeResourceDefinition();

		String resourceName = theResourceDefinition.getName();
		RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);

		List<String> reasons = new ArrayList<>();
		IParameterMatchHandler parameterMatchHandler = null;
		for (IParameterMatchHandler matchHandler : myMatchHandlers) {
			InMemoryMatchResult checkUnsupportedResult = matchHandler.checkForUnsupportedParameters(
				theParamName, paramDef, theAndOrParams
			);
			if (checkUnsupportedResult.supported()) {
				// all we need is one
				parameterMatchHandler = matchHandler;
				break;
			} else {
				// collect the reasons
				reasons.add(checkUnsupportedResult.getUnsupportedReason());
			}
		}

		if (parameterMatchHandler == null) {
			// cannot support this search parameter matching
			String reason = reasons.isEmpty() ? InMemoryMatchResult.NO_PARAMETER_MATCH_HANDLER_FOUND
				: String.join(", ", reasons);
			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName,
				reason);
		}
//		InMemoryMatchResult checkUnsupportedResult =
//				checkForUnsupportedParameters(theParamName, paramDef, theAndOrParams, theParameters.isAllowChainSearches());
//		if (!checkUnsupportedResult.supported()) {
//			return checkUnsupportedResult;
//		}

		// TODO - send to matchHandler
		// use the newly created public method and put this code there
		return parameterMatchHandler.matchResourceByParameters(
			theParamName,
			paramDef,
			theAndOrParams,
			theParameters
		);
	}

	private enum ValidationSupportInitializationState {
		NOT_INITIALIZED,
		INITIALIZED,
		FAILED
	}
}
