package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

public abstract class BaseSearchParamRegistry implements ISearchParamRegistry {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseSearchParamRegistry.class);
	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;
	private volatile Map<String, List<JpaRuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private Collection<IFhirResourceDao<?>> myDaos;

	public BaseSearchParamRegistry() {
		super();
	}

	@Override
	public void forceRefresh() {
		// nothing by default
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
		return myBuiltInSearchParams;
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		Validate.notBlank(theResourceName, "theResourceName must not be blank or null");

		return myBuiltInSearchParams.get(theResourceName);
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		refreshCacheIfNecessary();
		List<JpaRuntimeSearchParam> retVal = myActiveUniqueSearchParams.get(theResourceName);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return retVal;
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
		refreshCacheIfNecessary();

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

	public void populateActiveSearchParams(Map<String, Map<String, RuntimeSearchParam>> theActiveSearchParams) {
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

		for (IFhirResourceDao<?> nextDao : myDaos) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(nextDao.getResourceType());
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<>();
			resourceNameToSearchParams.put(nextResourceName, nameToParam);

			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}

		myBuiltInSearchParams = Collections.unmodifiableMap(resourceNameToSearchParams);
	}

	protected abstract void refreshCacheIfNecessary();

}
