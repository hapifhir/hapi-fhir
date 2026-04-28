/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class SearchParamValidatingInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SearchParamValidatingInterceptor.class);

	public static final String SEARCH_PARAM = "SearchParameter";
	public static final String SKIP_VALIDATION = SearchParamValidatingInterceptor.class.getName() + ".SKIP_VALIDATION";

	private FhirContext myFhirContext;

	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	private DaoRegistry myDaoRegistry;

	private IIdHelperService myIdHelperService;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSearchParamOnCreate(theResource, theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreUpdate(
			IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails) {
		validateSearchParamOnUpdate(theOldResource, theNewResource, theRequestDetails);
	}

	public void validateSearchParamOnCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		if (isNotSearchParameterResource(theResource)) {
			return;
		}

		// avoid a loop when loading our hard-coded core FhirContext SearchParameters
		boolean isStartup = theRequestDetails != null
				&& Boolean.TRUE == theRequestDetails.getUserData().get(SKIP_VALIDATION);
		if (isStartup) {
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);
		if (runtimeSearchParam == null) {
			return;
		}

		// Only check for retiring a non-disableable SP, no check for narrowing a non-disableable base
		// because we don't have a reference for whether a non-disableable was removed
		// This should be ok since we also handle non-disableable SPs at the SearchParamRegistryImpl cache level
		validateNonDisableableSpNotRetired(runtimeSearchParam);

		validateSearchParamOnCreateAndUpdate(runtimeSearchParam);

		SearchParameterMap searchParameterMap = extractSearchParameterMap(runtimeSearchParam);
		if (searchParameterMap != null) {
			validateStandardSpOnCreate(theRequestDetails, searchParameterMap);
		}
	}

	private void validateSearchParamOnCreateAndUpdate(RuntimeSearchParam theRuntimeSearchParam) {

		// Validate uplifted refchains
		List<IBaseExtension<?, ?>> refChainExtensions =
				theRuntimeSearchParam.getExtensions(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		for (IBaseExtension<?, ?> nextExtension : refChainExtensions) {
			List<? extends IBaseExtension> codeExtensions = nextExtension.getExtension().stream()
					.map(t -> (IBaseExtension<?, ?>) t)
					.filter(t -> HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE.equals(t.getUrl()))
					.collect(Collectors.toList());
			if (codeExtensions.size() != 1) {
				throw new UnprocessableEntityException(
						Msg.code(2283) + "Extension with URL " + HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN
								+ " must have exactly one child extension with URL "
								+ HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE);
			}
			if (codeExtensions.get(0).getValue() == null
					|| !"code"
							.equals(myFhirContext
									.getElementDefinition(
											codeExtensions.get(0).getValue().getClass())
									.getName())) {
				throw new UnprocessableEntityException(Msg.code(2284) + "Extension with URL "
						+ HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE
						+ " must have a value of type 'code'");
			}
		}
	}

	private void validateStandardSpOnCreate(RequestDetails theRequestDetails, SearchParameterMap searchParameterMap) {
		List<IResourcePersistentId> persistedIdList = getDao().searchForIds(searchParameterMap, theRequestDetails);
		if (isNotEmpty(persistedIdList)) {
			throw new UnprocessableEntityException(
					Msg.code(2196) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
		}
	}

	public void validateSearchParamOnUpdate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSearchParamOnUpdate(null, theResource, theRequestDetails);
	}

	/**
	 * Validates a SearchParameter resource on update/PUT, enforcing two constraints:
	 * <ol>
	 *   <li>Built-in non-disableable search parameters (see
	 *       {@link ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl#NON_DISABLEABLE_SEARCH_PARAMS})
	 *       may not have their status changed to anything other than {@code active}. Or it is part of a
	 *       multi-base SP, it cannot be removed from the base list.</li>
	 *   <li>A new SearchParameter (PUT-as-create) may not overlap an existing one with the
	 *       same base and code.</li>
	 * </ol>
	 *
	 * <p>Validation is skipped when {@link #SKIP_VALIDATION} is set in the request user-data
	 * (used internally by the CDR seeder to bypass the non-disableable check).
	 *
	 * @param theOldResource    the SearchParameter resource being updated
	 * @param theResource       the new version of SearchParameter resource
	 * @param theRequestDetails the current request context
	 * @throws ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException if validation fails
	 */
	public void validateSearchParamOnUpdate(
			@Nullable IBaseResource theOldResource, IBaseResource theResource, RequestDetails theRequestDetails) {
		if (isNotSearchParameterResource(theResource)) {
			return;
		}

		// avoid a loop when loading our hard-coded core FhirContext SearchParameters
		// skip Search Param validation if been set in the request
		boolean isStartup = theRequestDetails != null
				&& Boolean.TRUE == theRequestDetails.getUserData().get(SKIP_VALIDATION);
		if (isStartup) {
			logger.warn(
					"Skipping validation of submitted SearchParameter because {} flag is {}",
					SKIP_VALIDATION,
					Boolean.TRUE);
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);
		if (runtimeSearchParam == null) {
			return;
		}

		validateNonDisableableSpNotRetired(runtimeSearchParam);
		validateNonDisableableBasesNotRemoved(theOldResource, runtimeSearchParam);

		validateSearchParamOnCreateAndUpdate(runtimeSearchParam);

		SearchParameterMap searchParameterMap = extractSearchParameterMap(runtimeSearchParam);
		if (searchParameterMap != null) {
			validateStandardSpOnUpdate(theRequestDetails, runtimeSearchParam, searchParameterMap);
		}
	}

	private void validateNonDisableableSpNotRetired(RuntimeSearchParam theNewParam) {
		// Created by Claude Sonnet 4.6
		if (theNewParam.getStatus() == RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE) {
			return;
		}
		for (String nextBase : theNewParam.getBase()) {
			if (SearchParamRegistryImpl.isNonDisableableBuiltInSearchParam(
					theNewParam.getUri(), nextBase, theNewParam.getName())) {
				throw new UnprocessableEntityException(
						Msg.code(2875) + "Cannot change the status of built-in search parameter "
								+ nextBase + ":" + theNewParam.getName()
								+ " (with URL " + theNewParam.getUri() + ")"
								+ " because it is required for system operation. This parameter should be kept active.");
			}
		}
	}

	private void validateNonDisableableBasesNotRemoved(
			@Nullable IBaseResource theOldResource, RuntimeSearchParam theNewParam) {
		// Created by Claude Sonnet 4.6
		if (theOldResource == null) {
			return;
		}
		RuntimeSearchParam oldParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theOldResource);
		if (oldParam == null) {
			return;
		}
		for (String nextBase : oldParam.getBase()) {
			if (SearchParamRegistryImpl.isNonDisableableBuiltInSearchParam(
							theNewParam.getUri(), nextBase, theNewParam.getName())
					&& !theNewParam.getBase().contains(nextBase)) {
				throw new UnprocessableEntityException(Msg.code(2925) + "Cannot remove base '" + nextBase
						+ "' from built-in search parameter "
						+ theNewParam.getName()
						+ " (with URL " + theNewParam.getUri() + ")"
						+ " because it is required for system operation.");
			}
		}
	}

	private boolean isNewSearchParam(RuntimeSearchParam theSearchParam, Set<String> theExistingIds) {
		return theExistingIds.stream().noneMatch(resId -> resId.substring(resId.indexOf("/") + 1)
				.equals(theSearchParam.getId().getIdPart()));
	}

	private void validateStandardSpOnUpdate(
			RequestDetails theRequestDetails,
			RuntimeSearchParam runtimeSearchParam,
			SearchParameterMap searchParameterMap) {
		List<IResourcePersistentId> pidList = getDao().searchForIds(searchParameterMap, theRequestDetails);
		if (isNotEmpty(pidList)) {
			Set<String> resolvedResourceIds = myIdHelperService.translatePidsToFhirResourceIds(new HashSet<>(pidList));
			if (isNewSearchParam(runtimeSearchParam, resolvedResourceIds)) {
				throwDuplicateError();
			}
		}
	}

	private void throwDuplicateError() {
		throw new UnprocessableEntityException(
				Msg.code(2125) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
	}

	private boolean isNotSearchParameterResource(IBaseResource theResource) {
		return !SEARCH_PARAM.equalsIgnoreCase(myFhirContext.getResourceType(theResource));
	}

	@Nullable
	private SearchParameterMap extractSearchParameterMap(RuntimeSearchParam theRuntimeSearchParam) {
		SearchParameterMap retVal = new SearchParameterMap();

		String code = theRuntimeSearchParam.getName();
		List<String> theBases = List.copyOf(theRuntimeSearchParam.getBase());
		if (isBlank(code) || theBases.isEmpty()) {
			return null;
		}

		TokenAndListParam codeParam = new TokenAndListParam().addAnd(new TokenParam(code));
		TokenAndListParam basesParam = toTokenAndList(theBases);

		retVal.add("code", codeParam);
		retVal.add("base", basesParam);

		return retVal;
	}

	@Autowired
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Autowired
	public void setSearchParameterCanonicalizer(SearchParameterCanonicalizer theSearchParameterCanonicalizer) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizer;
	}

	@Autowired
	public void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@Autowired
	public void setIIDHelperService(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	private IFhirResourceDao getDao() {
		return myDaoRegistry.getResourceDao(SEARCH_PARAM);
	}

	private TokenAndListParam toTokenAndList(List<String> theBases) {
		TokenAndListParam retVal = new TokenAndListParam();

		if (theBases != null) {

			TokenOrListParam tokenOrListParam = new TokenOrListParam();
			retVal.addAnd(tokenOrListParam);

			for (String next : theBases) {
				if (isNotBlank(next)) {
					tokenOrListParam.addOr(new TokenParam(next));
				}
			}
		}

		if (retVal.getValuesAsQueryTokens().isEmpty()) {
			return null;
		}

		return retVal;
	}
}
