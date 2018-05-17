package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseSearchParamRegistry<SP extends IBaseResource> implements ISearchParamRegistry, ApplicationContextAware {

	private static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSearchParamRegistry.class);
	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;
	private volatile Map<String, List<JpaRuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();
	@Autowired
	private FhirContext myCtx;
	private Collection<IFhirResourceDao<?>> myResourceDaos;
	private volatile Map<String, Map<String, RuntimeSearchParam>> myActiveSearchParams;
	@Autowired
	private DaoConfig myDaoConfig;
	private volatile long myLastRefresh;
	private ApplicationContext myApplicationContext;

	public BaseSearchParamRegistry() {
		super();
	}

	@Override
	public void requestRefresh() {
		synchronized (this) {
			myLastRefresh = 0;
		}
	}

	@Override
	public void forceRefresh() {
		requestRefresh();
		refreshCacheIfNecessary();
	}

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		RuntimeSearchParam retVal = null;
		Map<String, RuntimeSearchParam> params = getActiveSearchParams().get(theResourceName);
		if (params != null) {
			retVal = params.get(theParamName);
		}
		return retVal;
	}


	@Override
	public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
		return myActiveSearchParams;
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		return myActiveSearchParams.get(theResourceName);
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		List<JpaRuntimeSearchParam> retVal = myActiveUniqueSearchParams.get(theResourceName);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return retVal;
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {

		Map<Set<String>, List<JpaRuntimeSearchParam>> paramNamesToParams = myActiveParamNamesToUniqueSearchParams.get(theResourceName);
		if (paramNamesToParams == null) {
			return Collections.emptyList();
		}

		List<JpaRuntimeSearchParam> retVal = paramNamesToParams.get(theParamNames);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return Collections.unmodifiableList(retVal);
	}

	public Map<String, Map<String, RuntimeSearchParam>> getBuiltInSearchParams() {
		return myBuiltInSearchParams;
	}

	private Map<String, RuntimeSearchParam> getSearchParamMap(Map<String, Map<String, RuntimeSearchParam>> searchParams, String theResourceName) {
		Map<String, RuntimeSearchParam> retVal = searchParams.get(theResourceName);
		if (retVal == null) {
			retVal = new HashMap<>();
			searchParams.put(theResourceName, retVal);
		}
		return retVal;
	}

	public abstract IFhirResourceDao<SP> getSearchParameterDao();

	private void populateActiveSearchParams(Map<String, Map<String, RuntimeSearchParam>> theActiveSearchParams) {
		Map<String, List<JpaRuntimeSearchParam>> activeUniqueSearchParams = new HashMap<>();
		Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> activeParamNamesToUniqueSearchParams = new HashMap<>();

		Map<String, RuntimeSearchParam> idToRuntimeSearchParam = new HashMap<>();
		List<JpaRuntimeSearchParam> jpaSearchParams = new ArrayList<>();

		/*
		 * Loop through parameters and find JPA params
		 */
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextResourceNameToEntries : theActiveSearchParams.entrySet()) {
			List<JpaRuntimeSearchParam> uniqueSearchParams = activeUniqueSearchParams.get(nextResourceNameToEntries.getKey());
			if (uniqueSearchParams == null) {
				uniqueSearchParams = new ArrayList<>();
				activeUniqueSearchParams.put(nextResourceNameToEntries.getKey(), uniqueSearchParams);
			}
			Collection<RuntimeSearchParam> nextSearchParamsForResourceName = nextResourceNameToEntries.getValue().values();
			for (RuntimeSearchParam nextCandidate : nextSearchParamsForResourceName) {

				if (nextCandidate.getId() != null) {
					idToRuntimeSearchParam.put(nextCandidate.getId().toUnqualifiedVersionless().getValue(), nextCandidate);
				}

				if (nextCandidate instanceof JpaRuntimeSearchParam) {
					JpaRuntimeSearchParam nextCandidateCasted = (JpaRuntimeSearchParam) nextCandidate;
					jpaSearchParams.add(nextCandidateCasted);
					if (nextCandidateCasted.isUnique()) {
						uniqueSearchParams.add(nextCandidateCasted);
					}
				}
			}

		}

		Set<String> haveSeen = new HashSet<>();
		for (JpaRuntimeSearchParam next : jpaSearchParams) {
			if (!haveSeen.add(next.getId().toUnqualifiedVersionless().getValue())) {
				continue;
			}

			Set<String> paramNames = new HashSet<>();
			for (JpaRuntimeSearchParam.Component nextComponent : next.getComponents()) {
				String nextRef = nextComponent.getReference().getReferenceElement().toUnqualifiedVersionless().getValue();
				RuntimeSearchParam componentTarget = idToRuntimeSearchParam.get(nextRef);
				if (componentTarget != null) {
					next.getCompositeOf().add(componentTarget);
					paramNames.add(componentTarget.getName());
				} else {
					ourLog.warn("Search parameter {} refers to unknown component {}", next.getId().toUnqualifiedVersionless().getValue(), nextRef);
				}
			}

			if (next.getCompositeOf() != null) {
				Collections.sort(next.getCompositeOf(), new Comparator<RuntimeSearchParam>() {
					@Override
					public int compare(RuntimeSearchParam theO1, RuntimeSearchParam theO2) {
						return StringUtils.compare(theO1.getName(), theO2.getName());
					}
				});
				for (String nextBase : next.getBase()) {
					if (!activeParamNamesToUniqueSearchParams.containsKey(nextBase)) {
						activeParamNamesToUniqueSearchParams.put(nextBase, new HashMap<Set<String>, List<JpaRuntimeSearchParam>>());
					}
					if (!activeParamNamesToUniqueSearchParams.get(nextBase).containsKey(paramNames)) {
						activeParamNamesToUniqueSearchParams.get(nextBase).put(paramNames, new ArrayList<JpaRuntimeSearchParam>());
					}
					activeParamNamesToUniqueSearchParams.get(nextBase).get(paramNames).add(next);
				}
			}
		}

		myActiveUniqueSearchParams = activeUniqueSearchParams;
		myActiveParamNamesToUniqueSearchParams = activeParamNamesToUniqueSearchParams;
	}

	@PostConstruct
	public void postConstruct() {
		Map<String, Map<String, RuntimeSearchParam>> resourceNameToSearchParams = new HashMap<>();

		myResourceDaos = new ArrayList<>();
		Map<String, IFhirResourceDao> daos = myApplicationContext.getBeansOfType(IFhirResourceDao.class, false, false);
		for (IFhirResourceDao next : daos.values()) {
			myResourceDaos.add(next);
		}

		for (IFhirResourceDao<?> nextDao : myResourceDaos) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(nextDao.getResourceType());
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<>();
			resourceNameToSearchParams.put(nextResourceName, nameToParam);

			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}

		myBuiltInSearchParams = Collections.unmodifiableMap(resourceNameToSearchParams);

		refreshCacheIfNecessary();
	}

	@Override
	public void refreshCacheIfNecessary() {
		long refreshInterval = 60 * DateUtils.MILLIS_PER_MINUTE;
		if (System.currentTimeMillis() - refreshInterval > myLastRefresh) {
			synchronized (this) {
				if (System.currentTimeMillis() - refreshInterval > myLastRefresh) {
					StopWatch sw = new StopWatch();

					Map<String, Map<String, RuntimeSearchParam>> searchParams = new HashMap<>();
					for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : getBuiltInSearchParams().entrySet()) {
						for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
							String nextResourceName = nextBuiltInEntry.getKey();
							getSearchParamMap(searchParams, nextResourceName).put(nextParam.getName(), nextParam);
						}
					}

					SearchParameterMap params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);

					IBundleProvider allSearchParamsBp = getSearchParameterDao().search(params);
					int size = allSearchParamsBp.size();

					// Just in case..
					if (size >= MAX_MANAGED_PARAM_COUNT) {
						ourLog.warn("Unable to support >" + MAX_MANAGED_PARAM_COUNT + " search params!");
						size = MAX_MANAGED_PARAM_COUNT;
					}

					List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, size);
					for (IBaseResource nextResource : allSearchParams) {
						SP nextSp = (SP) nextResource;
						if (nextSp == null) {
							continue;
						}

						RuntimeSearchParam runtimeSp = toRuntimeSp(nextSp);
						if (runtimeSp == null) {
							continue;
						}

						for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myCtx, nextSp)) {
							if (isBlank(nextBaseName)) {
								continue;
							}

							Map<String, RuntimeSearchParam> searchParamMap = getSearchParamMap(searchParams, nextBaseName);
							String name = runtimeSp.getName();
							if (myDaoConfig.isDefaultSearchParamsCanBeOverridden() || !searchParamMap.containsKey(name)) {
								searchParamMap.put(name, runtimeSp);
							}

						}
					}

					Map<String, Map<String, RuntimeSearchParam>> activeSearchParams = new HashMap<>();
					for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextEntry : searchParams.entrySet()) {
						for (RuntimeSearchParam nextSp : nextEntry.getValue().values()) {
							String nextName = nextSp.getName();
							if (nextSp.getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE) {
								nextSp = null;
							}

							if (!activeSearchParams.containsKey(nextEntry.getKey())) {
								activeSearchParams.put(nextEntry.getKey(), new HashMap<>());
							}
							if (activeSearchParams.containsKey(nextEntry.getKey())) {
								ourLog.debug("Replacing existing/built in search param {}:{} with new one", nextEntry.getKey(), nextName);
							}

							if (nextSp != null) {
								activeSearchParams.get(nextEntry.getKey()).put(nextName, nextSp);
							} else {
								activeSearchParams.get(nextEntry.getKey()).remove(nextName);
							}
						}
					}

					myActiveSearchParams = activeSearchParams;

					populateActiveSearchParams(activeSearchParams);

					myLastRefresh = System.currentTimeMillis();
					ourLog.info("Refreshed search parameter cache in {}ms", sw.getMillis());
				}
			}
		}
	}

	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	public void refreshCacheOnSchedule() {
		refreshCacheIfNecessary();
	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myApplicationContext = theApplicationContext;
	}

	protected abstract RuntimeSearchParam toRuntimeSp(SP theNextSp);


}
