package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntry;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Consumer;

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
	private static final Logger ourLog = LoggerFactory.getLogger(SearchNarrowingInterceptor.class);


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
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		// We don't support this operation type yet
		Validate.isTrue(theRequestDetails.getRestOperationType() != RestOperationTypeEnum.SEARCH_SYSTEM);

		if (theRequestDetails.getRestOperationType() != RestOperationTypeEnum.SEARCH_TYPE) {
			return true;
		}

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();
		RuntimeResourceDefinition resDef = ctx.getResourceDefinition(theRequestDetails.getResourceName());
		HashMap<String, List<String>> parameterToOrValues = new HashMap<>();
		AuthorizedList authorizedList = buildAuthorizedList(theRequestDetails);
		if (authorizedList == null) {
			return true;
		}

		/*
		 * Create a map of search parameter values that need to be added to the
		 * given request
		 */
		Collection<String> compartments = authorizedList.getAllowedCompartments();
		if (compartments != null) {
			processResourcesOrCompartments(theRequestDetails, resDef, parameterToOrValues, compartments, true);
		}
		Collection<String> resources = authorizedList.getAllowedInstances();
		if (resources != null) {
			processResourcesOrCompartments(theRequestDetails, resDef, parameterToOrValues, resources, false);
		}

		/*
		 * Add any param values to the actual request
		 */
		if (parameterToOrValues.size() > 0) {
			Map<String, String[]> newParameters = new HashMap<>(theRequestDetails.getParameters());
			for (Map.Entry<String, List<String>> nextEntry : parameterToOrValues.entrySet()) {
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
					boolean restrictedExistingList = false;
					for (int i = 0; i < existingValues.length; i++) {

						String nextExistingValue = existingValues[i];
						List<String> nextRequestedValues = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, nextExistingValue);
						List<String> nextPermittedValues = ListUtils.intersection(nextRequestedValues, nextAllowedValues);
						if (nextPermittedValues.size() > 0) {
							restrictedExistingList = true;
							existingValues[i] = ParameterUtil.escapeAndJoinOrList(nextPermittedValues);
						}

					}

					/*
					 * If none of the values that were requested by the client overlap at all
					 * with the values that the user is allowed to see, we'll just add the permitted
					 * list as a new list. Ultimately this scenario actually means that the client
					 * shouldn't get *any* results back, and adding a new AND parameter (that doesn't
					 * overlap at all with the others) is one way of ensuring that.
					 */
					if (!restrictedExistingList) {
						String[] newValues = Arrays.copyOf(existingValues, existingValues.length + 1);
						newValues[existingValues.length] = ParameterUtil.escapeAndJoinOrList(nextAllowedValues);
						newParameters.put(nextParamName, newValues);
					}
				}

			}
			theRequestDetails.setParameters(newParameters);
		}

		return true;
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void incomingRequestPreHandled(ServletRequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		if (theRequestDetails.getRestOperationType() != RestOperationTypeEnum.TRANSACTION) {
			return;
		}

		IBaseBundle bundle = (IBaseBundle) theRequestDetails.getResource();
		FhirContext ctx = theRequestDetails.getFhirContext();
		BundleEntryUrlProcessor processor = new BundleEntryUrlProcessor(ctx, theRequestDetails, theRequest, theResponse);
		BundleUtil.processEntries(ctx, bundle, processor);
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

			incomingRequestPostProcessed(subServletRequestDetails, myRequest, myResponse);

			theModifiableBundleEntry.setRequestUrl(myFhirContext, ServletRequestUtil.extractUrl(subServletRequestDetails));
		}
	}

	private void processResourcesOrCompartments(RequestDetails theRequestDetails, RuntimeResourceDefinition theResDef, HashMap<String, List<String>> theParameterToOrValues, Collection<String> theResourcesOrCompartments, boolean theAreCompartments) {
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

					List<RuntimeSearchParam> searchParams = theResDef.getSearchParamsForCompartmentName(compartmentName);
					if (searchParams.size() > 0) {

						// Resources like Observation have several fields that add the resource to
						// the compartment. In the case of Observation, it's subject, patient and performer.
						// For this kind of thing, we'll prefer the one called "patient".
						RuntimeSearchParam searchParam =
							searchParams
								.stream()
								.filter(t -> t.getName().equalsIgnoreCase(compartmentName))
								.findFirst()
								.orElse(searchParams.get(0));
						searchParamName = searchParam.getName();

					}
				}

				lastCompartmentName = compartmentName;
				lastSearchParamName = searchParamName;

			}

			if (searchParamName != null) {
				List<String> orValues = theParameterToOrValues.computeIfAbsent(searchParamName, t -> new ArrayList<>());
				orValues.add(nextCompartment);
			}
		}
	}

}
