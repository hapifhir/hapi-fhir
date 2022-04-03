package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntry;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This interceptor can be used to automatically narrow the scope of searches in order to
 * automatically restrict the searches to specific compartments.
 * <p>
 * For example, this interceptor
 * could be used to restrict a user to only viewing data belonging to Patient/123 (i.e. data
 * in the <code>Patient/123</code> compartment). In this case, a user performing a search
 * for<br/>
 * <code>http://baseurl/Observation?category=laboratory</code><br/>
 * would receive results as though they had requested<br/>
 * <code>http://baseurl/Observation?subject=Patient/123&category=laboratory</code>
 * </p>
 * <p>
 * Note that this interceptor should be used in combination with {@link AuthorizationInterceptor}
 * if you are restricting results because of a security restriction. This interceptor is not
 * intended to be a failsafe way of preventing users from seeing the wrong data (that is the
 * purpose of AuthorizationInterceptor). This interceptor is simply intended as a convenience to
 * help users simplify their queries while not receiving security errors for to trying to access
 * data they do not have access to see.
 * </p>
 *
 * @see AuthorizationInterceptor
 */
public class SearchNarrowingInterceptor {

	public static final String POST_FILTERING_LIST_ATTRIBUTE_NAME = SearchNarrowingInterceptor.class.getName() + "_POST_FILTERING_LIST";
	private IValidationSupport myValidationSupport;
	private int myPostFilterLargeValueSetThreshold = 500;

	/**
	 * Supplies a threshold over which any ValueSet-based rules will be applied by
	 *
	 *
	 * <p>
	 * Note that this setting will have no effect if {@link #setValidationSupport(IValidationSupport)}
	 * has not also been called in order to supply a validation support module for
	 * testing ValueSet membership.
	 * </p>
	 *
	 * @param thePostFilterLargeValueSetThreshold The threshold
	 * @see #setValidationSupport(IValidationSupport)
	 */
	public void setPostFilterLargeValueSetThreshold(int thePostFilterLargeValueSetThreshold) {
		Validate.isTrue(thePostFilterLargeValueSetThreshold > 0, "thePostFilterLargeValueSetThreshold must be a positive integer");
		myPostFilterLargeValueSetThreshold = thePostFilterLargeValueSetThreshold;
	}

	/**
	 * Supplies a validation support module that will be used to apply the
	 *
	 * @see #setPostFilterLargeValueSetThreshold(int)
	 * @since 6.0.0
	 */
	public SearchNarrowingInterceptor setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		return this;
	}

	/**
	 * Subclasses should override this method to supply the set of compartments that
	 * the user making the request should actually have access to.
	 * <p>
	 * Typically this is done by examining <code>theRequestDetails</code> to find
	 * out who the current user is and then building a list of Strings.
	 * </p>
	 *
	 * @param theRequestDetails The individual request currently being applied
	 * @return The list of allowed compartments and instances that should be used
	 * for search narrowing. If this method returns <code>null</code>, no narrowing will
	 * be performed
	 */
	protected AuthorizedList buildAuthorizedList(@SuppressWarnings("unused") RequestDetails theRequestDetails) {
		return null;
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
	public boolean hookIncomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		// We don't support this operation type yet
		Validate.isTrue(theRequestDetails.getRestOperationType() != RestOperationTypeEnum.SEARCH_SYSTEM);

		AuthorizedList authorizedList = buildAuthorizedList(theRequestDetails);
		if (authorizedList == null) {
			return true;
		}

		// Add rules to request so that the SearchNarrowingConsentService can pick them up
		List<AllowedCodeInValueSet> postFilteringList = getPostFilteringList(theRequestDetails);
		if (authorizedList.getAllowedCodeInValueSets() != null) {
			postFilteringList.addAll(authorizedList.getAllowedCodeInValueSets());
		}

		if (theRequestDetails.getRestOperationType() != RestOperationTypeEnum.SEARCH_TYPE) {
			return true;
		}

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();
		RuntimeResourceDefinition resDef = ctx.getResourceDefinition(theRequestDetails.getResourceName());
		/*
		 * Create a map of search parameter values that need to be added to the
		 * given request
		 */
		Collection<String> compartments = authorizedList.getAllowedCompartments();
		if (compartments != null) {
			Map<String, List<String>> parameterToOrValues = processResourcesOrCompartments(theRequestDetails, resDef, compartments, true);
			applyParametersToRequestDetails(theRequestDetails, parameterToOrValues, true);
		}
		Collection<String> resources = authorizedList.getAllowedInstances();
		if (resources != null) {
			Map<String, List<String>> parameterToOrValues = processResourcesOrCompartments(theRequestDetails, resDef, resources, false);
			applyParametersToRequestDetails(theRequestDetails, parameterToOrValues, true);
		}
		List<AllowedCodeInValueSet> allowedCodeInValueSet = authorizedList.getAllowedCodeInValueSets();
		if (allowedCodeInValueSet != null) {
			Map<String, List<String>> parameterToOrValues = processAllowedCodes(resDef, allowedCodeInValueSet);
			applyParametersToRequestDetails(theRequestDetails, parameterToOrValues, false);
		}

		return true;
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void hookIncomingRequestPreHandled(ServletRequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		if (theRequestDetails.getRestOperationType() != RestOperationTypeEnum.TRANSACTION) {
			return;
		}

		IBaseBundle bundle = (IBaseBundle) theRequestDetails.getResource();
		FhirContext ctx = theRequestDetails.getFhirContext();
		BundleEntryUrlProcessor processor = new BundleEntryUrlProcessor(ctx, theRequestDetails, theRequest, theResponse);
		BundleUtil.processEntries(ctx, bundle, processor);
	}

	private void applyParametersToRequestDetails(RequestDetails theRequestDetails, @Nullable Map<String, List<String>> theParameterToOrValues, boolean thePatientIdMode) {
		if (theParameterToOrValues != null) {
			Map<String, String[]> newParameters = new HashMap<>(theRequestDetails.getParameters());
			for (Map.Entry<String, List<String>> nextEntry : theParameterToOrValues.entrySet()) {
				String nextParamName = nextEntry.getKey();
				List<String> nextAllowedValues = nextEntry.getValue();

				if (!newParameters.containsKey(nextParamName)) {

					/*
					 * If we don't already have a parameter of the given type, add one
					 */
					String nextValuesJoined = ParameterUtil.escapeAndJoinOrList(nextAllowedValues);
					String[] paramValues = {nextValuesJoined};
					newParameters.put(nextParamName, paramValues);

				} else {

					/*
					 * If the client explicitly requested the given parameter already, we'll
					 * just update the request to have the intersection of the values that the client
					 * requested, and the values that the user is allowed to see
					 */
					String[] existingValues = newParameters.get(nextParamName);

					if (thePatientIdMode) {
						List<String> nextAllowedValueIds = nextAllowedValues
							.stream()
							.map(t -> t.lastIndexOf("/") > -1 ? t.substring(t.lastIndexOf("/") + 1) : t)
							.collect(Collectors.toList());
						boolean restrictedExistingList = false;
						for (int i = 0; i < existingValues.length; i++) {

							String nextExistingValue = existingValues[i];
							List<String> nextRequestedValues = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, nextExistingValue);
							List<String> nextPermittedValues = ListUtils.union(
								ListUtils.intersection(nextRequestedValues, nextAllowedValues),
								ListUtils.intersection(nextRequestedValues, nextAllowedValueIds)
							);
							if (nextPermittedValues.size() > 0) {
								restrictedExistingList = true;
								existingValues[i] = ParameterUtil.escapeAndJoinOrList(nextPermittedValues);
							}

						}

						/*
						 * If none of the values that were requested by the client overlap at all
						 * with the values that the user is allowed to see, the client shouldn't
						 * get *any* results back. We return an error code indicating that the
						 * caller is forbidden from accessing the resources they requested.
						 */
						if (!restrictedExistingList) {
							throw new ForbiddenOperationException(Msg.code(2026) + "Value not permitted for parameter " + UrlUtil.escapeUrlParam(nextParamName));
						}

					} else {

						int existingValuesCount = existingValues.length;
						String[] newValues = Arrays.copyOf(existingValues, existingValuesCount + nextAllowedValues.size());
						for (int i = 0; i < nextAllowedValues.size(); i++) {
							newValues[existingValuesCount + i] = nextAllowedValues.get(i);
						}
						newParameters.put(nextParamName, newValues);

					}

				}

			}
			theRequestDetails.setParameters(newParameters);
		}
	}

	@Nullable
	private Map<String, List<String>> processResourcesOrCompartments(RequestDetails theRequestDetails, RuntimeResourceDefinition theResDef, Collection<String> theResourcesOrCompartments, boolean theAreCompartments) {
		Map<String, List<String>> retVal = null;

		String lastCompartmentName = null;
		String lastSearchParamName = null;
		for (String nextCompartment : theResourcesOrCompartments) {
			Validate.isTrue(StringUtils.countMatches(nextCompartment, '/') == 1, "Invalid compartment name (must be in form \"ResourceType/xxx\": %s", nextCompartment);
			String compartmentName = nextCompartment.substring(0, nextCompartment.indexOf('/'));

			String searchParamName = null;
			if (compartmentName.equalsIgnoreCase(lastCompartmentName)) {

				// Avoid doing a lookup for the same thing repeatedly
				searchParamName = lastSearchParamName;

			} else {

				if (compartmentName.equalsIgnoreCase(theRequestDetails.getResourceName())) {

					searchParamName = "_id";

				} else if (theAreCompartments) {

					searchParamName = selectBestSearchParameterForCompartment(theRequestDetails, theResDef, compartmentName);
				}

				lastCompartmentName = compartmentName;
				lastSearchParamName = searchParamName;

			}

			if (searchParamName != null) {
				if (retVal == null) {
					retVal = new HashMap<>();
				}
				List<String> orValues = retVal.computeIfAbsent(searchParamName, t -> new ArrayList<>());
				orValues.add(nextCompartment);
			}
		}

		return retVal;
	}

	@Nullable
	private Map<String, List<String>> processAllowedCodes(RuntimeResourceDefinition theResDef, List<AllowedCodeInValueSet> theAllowedCodeInValueSet) {
		Map<String, List<String>> retVal = null;

		for (AllowedCodeInValueSet next : theAllowedCodeInValueSet) {
			String resourceName = next.getResourceName();
			String valueSetUrl = next.getValueSetUrl();

			ValidateUtil.isNotBlankOrThrowIllegalArgument(resourceName, "Resource name supplied by SearchNarrowingInterceptor must not be null");
			ValidateUtil.isNotBlankOrThrowIllegalArgument(valueSetUrl, "ValueSet URL supplied by SearchNarrowingInterceptor must not be null");

			if (!resourceName.equals(theResDef.getName())) {
				continue;
			}

			if (shouldHandleThroughConsentService(valueSetUrl)) {
				continue;
			}

			String paramName;
			if (next.isNegate()) {
				paramName = next.getSearchParameterName() + Constants.PARAMQUALIFIER_TOKEN_NOT_IN;
			} else {
				paramName = next.getSearchParameterName() + Constants.PARAMQUALIFIER_TOKEN_IN;
			}

			if (retVal == null) {
				retVal = new HashMap<>();
			}
			retVal.computeIfAbsent(paramName, k -> new ArrayList<>()).add(valueSetUrl);
		}

		return retVal;
	}

	/**
	 * For a given ValueSet URL, expand the valueset and check if the number of
	 * codes present is larger than the post filter threshold.
	 */
	private boolean shouldHandleThroughConsentService(String theValueSetUrl) {
		if (myValidationSupport != null && myPostFilterLargeValueSetThreshold != -1) {
			ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);
			ValueSetExpansionOptions options = new ValueSetExpansionOptions();
			options.setCount(myPostFilterLargeValueSetThreshold);
			options.setIncludeHierarchy(false);
			IValidationSupport.ValueSetExpansionOutcome outcome = myValidationSupport.expandValueSet(ctx, options, theValueSetUrl);
			if (outcome != null && outcome.getValueSet() != null) {
				FhirTerser terser = myValidationSupport.getFhirContext().newTerser();
				List<IBase> contains = terser.getValues(outcome.getValueSet(), "ValueSet.expansion.contains");
				int codeCount = contains.size();
				return codeCount >= myPostFilterLargeValueSetThreshold;
			}
		}
		return false;
	}


	private String selectBestSearchParameterForCompartment(RequestDetails theRequestDetails, RuntimeResourceDefinition theResDef, String compartmentName) {
		String searchParamName = null;

		Set<String> queryParameters = theRequestDetails.getParameters().keySet();

		List<RuntimeSearchParam> searchParams = theResDef.getSearchParamsForCompartmentName(compartmentName);
		if (searchParams.size() > 0) {

			// Resources like Observation have several fields that add the resource to
			// the compartment. In the case of Observation, it's subject, patient and performer.
			// For this kind of thing, we'll prefer the one that matches the compartment name.
			Optional<RuntimeSearchParam> primarySearchParam =
				searchParams
					.stream()
					.filter(t -> t.getName().equalsIgnoreCase(compartmentName))
					.findFirst();

			if (primarySearchParam.isPresent()) {
				String primarySearchParamName = primarySearchParam.get().getName();
				// If the primary search parameter is actually in use in the query, use it.
				if (queryParameters.contains(primarySearchParamName)) {
					searchParamName = primarySearchParamName;
				} else {
					// If the primary search parameter itself isn't in use, check to see whether any of its synonyms are.
					Optional<RuntimeSearchParam> synonymInUse = findSynonyms(searchParams, primarySearchParam.get())
						.stream()
						.filter(t -> queryParameters.contains(t.getName()))
						.findFirst();
					if (synonymInUse.isPresent()) {
						// if a synonym is in use, use it
						searchParamName = synonymInUse.get().getName();
					} else {
						// if not, i.e., the original query is not filtering on this field at all, use the primary search param
						searchParamName = primarySearchParamName;
					}
				}
			} else {
				// Otherwise, fall back to whatever search parameter is available
				searchParamName = searchParams.get(0).getName();
			}

		}
		return searchParamName;
	}

	private List<RuntimeSearchParam> findSynonyms(List<RuntimeSearchParam> searchParams, RuntimeSearchParam primarySearchParam) {
		// We define two search parameters in a compartment as synonyms if they refer to the same field in the model, ignoring any qualifiers

		String primaryBasePath = getBasePath(primarySearchParam);

		return searchParams
			.stream()
			.filter(t -> primaryBasePath.equals(getBasePath(t)))
			.collect(Collectors.toList());
	}

	private String getBasePath(RuntimeSearchParam searchParam) {
		int qualifierIndex = searchParam.getPath().indexOf(".where");
		if (qualifierIndex == -1) {
			return searchParam.getPath();
		} else {
			return searchParam.getPath().substring(0, qualifierIndex);
		}
	}

	private class BundleEntryUrlProcessor implements Consumer<ModifiableBundleEntry> {
		private final FhirContext myFhirContext;
		private final ServletRequestDetails myRequestDetails;
		private final HttpServletRequest myRequest;
		private final HttpServletResponse myResponse;

		public BundleEntryUrlProcessor(FhirContext theFhirContext, ServletRequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) {
			myFhirContext = theFhirContext;
			myRequestDetails = theRequestDetails;
			myRequest = theRequest;
			myResponse = theResponse;
		}

		@Override
		public void accept(ModifiableBundleEntry theModifiableBundleEntry) {
			ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();

			String url = theModifiableBundleEntry.getRequestUrl();

			ServletSubRequestDetails subServletRequestDetails = ServletRequestUtil.getServletSubRequestDetails(myRequestDetails, url, paramValues);
			BaseMethodBinding<?> method = subServletRequestDetails.getServer().determineResourceMethod(subServletRequestDetails, url);
			RestOperationTypeEnum restOperationType = method.getRestOperationType();
			subServletRequestDetails.setRestOperationType(restOperationType);

			hookIncomingRequestPostProcessed(subServletRequestDetails, myRequest, myResponse);

			theModifiableBundleEntry.setRequestUrl(myFhirContext, ServletRequestUtil.extractUrl(subServletRequestDetails));
		}
	}


	static List<AllowedCodeInValueSet> getPostFilteringList(RequestDetails theRequestDetails) {
		List<AllowedCodeInValueSet> retVal = getPostFilteringListOrNull(theRequestDetails);
		if (retVal == null) {
			retVal = new ArrayList<>();
			theRequestDetails.setAttribute(POST_FILTERING_LIST_ATTRIBUTE_NAME, retVal);
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	static List<AllowedCodeInValueSet> getPostFilteringListOrNull(RequestDetails theRequestDetails) {
		return (List<AllowedCodeInValueSet>) theRequestDetails.getAttribute(POST_FILTERING_LIST_ATTRIBUTE_NAME);
	}


}
