package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl implements ISearchParamRegistry, IResourceChangeListener {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private ISearchParamProvider mySearchParamProvider;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	private volatile ReadOnlySearchParamCache myBuiltInSearchParams;
	private volatile IPhoneticEncoder myPhoneticEncoder;
	private volatile JpaSearchParamCache myJpaSearchParamCache = new JpaSearchParamCache();
	private volatile RuntimeSearchParamCache myActiveSearchParams;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		requiresActiveSearchParams();
		return myActiveSearchParams.get(theResourceName, theParamName);
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		requiresActiveSearchParams();
		return getActiveSearchParams().getSearchParamMap(theResourceName);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			myResourceChangeListenerRegistry.refreshCacheWithRetry("SearchParameter");
		}
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		return myJpaSearchParamCache.getActiveUniqueSearchParams(theResourceName);
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
		return myJpaSearchParamCache.getActiveUniqueSearchParams(theResourceName, theParamNames);
	}

	private void initializeActiveSearchParams(Collection<IdDt> theSearchParamIds) {
		StopWatch sw = new StopWatch();

		RuntimeSearchParamCache searchParams = new RuntimeSearchParamCache();
		searchParams.putAll(myBuiltInSearchParams);
		long overriddenCount = overrideBuiltinSearchParamsWithActiveSearchParams(theSearchParamIds, searchParams);
		ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);
		myActiveSearchParams = searchParams;

		myJpaSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private void addJpaSearchParam(IdDt theResourceId) {
		StopWatch sw = new StopWatch();
		IBaseResource searchParameter = mySearchParamProvider.read(theResourceId);
		addSearchParam(myActiveSearchParams, searchParameter);
		myJpaSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private void removeJpaSearchParam(IdDt theResourceId) {
		StopWatch sw = new StopWatch();
		ourLog.info("Removing search parameter {}", theResourceId);
		IBaseResource searchParameter = mySearchParamProvider.read(theResourceId);
		removeSearchParam(myActiveSearchParams, searchParameter);
		myJpaSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private long overrideBuiltinSearchParamsWithActiveSearchParams(Collection<IdDt> theSearchParamIds, RuntimeSearchParamCache theSearchParams) {
		long retval = 0;

		for (IdDt searchParamId : theSearchParamIds) {
			IBaseResource searchParameter = mySearchParamProvider.read(searchParamId);
			retval += addSearchParam(theSearchParams, searchParameter);
		}
		return retval;
	}

	private long addSearchParam(RuntimeSearchParamCache theSearchParams, IBaseResource theSearchParameter) {
		if (theSearchParameter == null) {
			return 0;
		}

		RuntimeSearchParam runtimeSp = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theSearchParameter);
		if (runtimeSp == null) {
			return 0;
		}
		if (runtimeSp.getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE) {
			return 0;
		}

		long retval = 0;
		for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myFhirContext, theSearchParameter)) {
			if (isBlank(nextBaseName)) {
				continue;
			}

			Map<String, RuntimeSearchParam> searchParamMap = theSearchParams.getSearchParamMap(nextBaseName);
			String name = runtimeSp.getName();
			if (!searchParamMap.containsKey(name) || myModelConfig.isDefaultSearchParamsCanBeOverridden()) {
				ourLog.info("Adding search parameter {}.{}", nextBaseName, name);
				searchParamMap.put(name, runtimeSp);
				retval++;
			}
		}
		return retval;
	}

	private long removeSearchParam(RuntimeSearchParamCache theSearchParams, IBaseResource theSearchParameter) {
		if (theSearchParameter == null) {
			return 0;
		}

		RuntimeSearchParam runtimeSp = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theSearchParameter);
		if (runtimeSp == null) {
			return 0;
		}

		long retval = 0;
		for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myFhirContext, theSearchParameter)) {
			if (isBlank(nextBaseName)) {
				continue;
			}

			Map<String, RuntimeSearchParam> searchParamMap = theSearchParams.getSearchParamMap(nextBaseName);
			String name = runtimeSp.getName();
			ourLog.info("Removing search parameter {}.{}", nextBaseName, name);
			searchParamMap.remove(name, runtimeSp);
			retval++;
		}
		return retval;
	}

	@Override
	public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
		Map<String, RuntimeSearchParam> params = getActiveSearchParams(theResourceDef.getName());
		return params.get(theParamName);
	}

	@Override
	public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
		return getActiveSearchParams(theResourceDef.getName()).values();
	}

	// FIXME KHS move these into a generic abstract superclass
	@Override
	public void requestRefresh() {
		myResourceChangeListenerRegistry.requestRefresh("SearchParameter");
	}

	@Override
	public void forceRefresh() {
		myResourceChangeListenerRegistry.forceRefresh("SearchParameter");
	}

	@Override
	public ResourceChangeResult refreshCacheIfNecessary() {
		return myResourceChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter");
	}

	@PostConstruct
	public void registerListener() {
		myBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(myFhirContext);

		// FIXME KHS compare this searchparam with below
		SearchParameterMap activeSearchParameterMap = SearchParameterMap.newSynchronous();
		activeSearchParameterMap.add("status", new TokenParam(Enumerations.PublicationStatus.ACTIVE.toCode()));
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener("SearchParameter", activeSearchParameterMap, this);
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	@Override
	public ReadOnlySearchParamCache getActiveSearchParams() {
		requiresActiveSearchParams();
		if (myActiveSearchParams == null) {
			throw new IllegalStateException("SearchParamRegistry has not been initialized");
		}
		return ReadOnlySearchParamCache.fromRuntimeSearchParamCache(myActiveSearchParams);
	}

	/**
	 * All SearchParameters with the name "phonetic" encode the normalized index value using this phonetic encoder.
	 *
	 * @since 5.1.0
	 */
	@Override
	public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
		myPhoneticEncoder = thePhoneticEncoder;

		if (myActiveSearchParams == null) {
			return;
		}
		myActiveSearchParams.getSearchParamStream().forEach(searchParam -> myJpaSearchParamCache.setPhoneticEncoder(myPhoneticEncoder, searchParam));
	}

	@Override
	public void handleCreate(IdDt theResourceId) {
		addJpaSearchParam(theResourceId);
	}

	@Override
	public void handleUpdate(IdDt theResourceId) {
		addJpaSearchParam(theResourceId);
	}

	@Override
	public void handleDelete(IdDt theResourceId) {
		removeJpaSearchParam(theResourceId);
	}

	@Override
	public void handleInit(Collection<IdDt> theResourceIds) {
		initializeActiveSearchParams(theResourceIds);
	}


}
