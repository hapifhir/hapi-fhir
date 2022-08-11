package ca.uhn.fhir.jpa.searchparam.submit.interceptor;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class SearchParamValidatingInterceptor {

	public static final String SEARCH_PARAM = "SearchParameter";

	private FhirContext myFhirContext;

	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	private DaoRegistry myDaoRegistry;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSearchParamOnCreate(theResource, theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreUpdate(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails) {
		validateSearchParamOnUpdate(theNewResource, theRequestDetails);
	}

	public void validateSearchParamOnCreate(IBaseResource theResource, RequestDetails theRequestDetails){
		if( isNotSearchParameterResource(theResource) ){
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);

		SearchParameterMap searchParameterMap = extractSearchParameterMap(runtimeSearchParam);

		List<ResourcePersistentId> persistedIdList = getDao().searchForIds(searchParameterMap, theRequestDetails);

		if( isNotEmpty(persistedIdList) ) {
			throw new UnprocessableEntityException(Msg.code(2131) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
		}
	}

	public void validateSearchParamOnUpdate(IBaseResource theResource, RequestDetails theRequestDetails){
		if( isNotSearchParameterResource(theResource) ){
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);

		SearchParameterMap searchParameterMap = extractSearchParameterMap(runtimeSearchParam);

		List<ResourcePersistentId> persistedIdList = getDao().searchForIds(searchParameterMap, theRequestDetails);

		if(isNotEmpty(persistedIdList)){
			String resourceId = runtimeSearchParam.getId().getIdPart();

			boolean isNewSearchParam = persistedIdList
				.stream()
				.map(theResourcePersistentId -> theResourcePersistentId.getId().toString())
				.noneMatch(anId -> anId.equals(resourceId));

			if(isNewSearchParam){
				throw new UnprocessableEntityException(Msg.code(2132) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
			}
		}
	}

	private boolean isNotSearchParameterResource(IBaseResource theResource){
		return ! SEARCH_PARAM.equalsIgnoreCase(myFhirContext.getResourceType(theResource));
	}

	private SearchParameterMap extractSearchParameterMap(RuntimeSearchParam theRuntimeSearchParam) {
		SearchParameterMap retVal = new SearchParameterMap();

		String theCode = theRuntimeSearchParam.getName();
		List<String> theBases = List.copyOf(theRuntimeSearchParam.getBase());

		TokenAndListParam codeParam = new TokenAndListParam().addAnd(new TokenParam(theCode));
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
