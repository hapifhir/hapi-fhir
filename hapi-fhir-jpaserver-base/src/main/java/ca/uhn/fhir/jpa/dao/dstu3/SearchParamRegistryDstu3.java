package ca.uhn.fhir.jpa.dao.dstu3;

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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.*;
import java.util.Map.Entry;

import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.util.JpaConstants;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.RuntimeSearchParam.RuntimeSearchParamStatusEnum;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;

public class SearchParamRegistryDstu3 extends BaseSearchParamRegistry {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamRegistryDstu3.class);
	public static final int MAX_MANAGED_PARAM_COUNT = 10000;

	private volatile Map<String, Map<String, RuntimeSearchParam>> myActiveSearchParams;

	@Autowired
	private DaoConfig myDaoConfig;

	private volatile long myLastRefresh;

	@Autowired
	private IFhirResourceDao<SearchParameter> mySpDao;

	@Override
	public void forceRefresh() {
		synchronized (this) {
			myLastRefresh = 0;
		}
	}

	@Override
	public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
		refreshCacheIfNecessary();
		return myActiveSearchParams;
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		refreshCacheIfNecessary();
		return myActiveSearchParams.get(theResourceName);
	}

	private Map<String, RuntimeSearchParam> getSearchParamMap(Map<String, Map<String, RuntimeSearchParam>> searchParams, String theResourceName) {
		Map<String, RuntimeSearchParam> retVal = searchParams.get(theResourceName);
		if (retVal == null) {
			retVal = new HashMap<>();
			searchParams.put(theResourceName, retVal);
		}
		return retVal;
	}

	protected void refreshCacheIfNecessary() {
		long refreshInterval = 60 * DateUtils.MILLIS_PER_MINUTE;
		if (System.currentTimeMillis() - refreshInterval > myLastRefresh) {
			synchronized (this) {
				if (System.currentTimeMillis() - refreshInterval > myLastRefresh) {
					StopWatch sw = new StopWatch();

					Map<String, Map<String, RuntimeSearchParam>> searchParams = new HashMap<>();
					for (Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : getBuiltInSearchParams().entrySet()) {
						for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
							String nextResourceName = nextBuiltInEntry.getKey();
							getSearchParamMap(searchParams, nextResourceName).put(nextParam.getName(), nextParam);
						}
					}

					SearchParameterMap params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);

					IBundleProvider allSearchParamsBp = mySpDao.search(params);
					int size = allSearchParamsBp.size();

					// Just in case..
					if (size > MAX_MANAGED_PARAM_COUNT) {
						ourLog.warn("Unable to support >" + MAX_MANAGED_PARAM_COUNT + " search params!");
						size = MAX_MANAGED_PARAM_COUNT;
					}

					List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, size);
					for (IBaseResource nextResource : allSearchParams) {
						SearchParameter nextSp = (SearchParameter) nextResource;
						JpaRuntimeSearchParam runtimeSp = toRuntimeSp(nextSp);
						if (runtimeSp == null) {
							continue;
						}

						for (org.hl7.fhir.dstu3.model.CodeType nextBaseName : nextSp.getBase()) {
							String resourceType = nextBaseName.getValue();
							if (isBlank(resourceType)) {
								continue;
							}

							Map<String, RuntimeSearchParam> searchParamMap = getSearchParamMap(searchParams, resourceType);
							String name = runtimeSp.getName();
							if (myDaoConfig.isDefaultSearchParamsCanBeOverridden() || !searchParamMap.containsKey(name)) {
								searchParamMap.put(name, runtimeSp);
							}

						}
					}

					Map<String, Map<String, RuntimeSearchParam>> activeSearchParams = new HashMap<>();
					for (Entry<String, Map<String, RuntimeSearchParam>> nextEntry : searchParams.entrySet()) {
						for (RuntimeSearchParam nextSp : nextEntry.getValue().values()) {
							String nextName = nextSp.getName();
							if (nextSp.getStatus() != RuntimeSearchParamStatusEnum.ACTIVE) {
								nextSp = null;
							}

							if (!activeSearchParams.containsKey(nextEntry.getKey())) {
								activeSearchParams.put(nextEntry.getKey(), new HashMap<String, RuntimeSearchParam>());
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

					super.populateActiveSearchParams(activeSearchParams);

					myLastRefresh = System.currentTimeMillis();
					ourLog.info("Refreshed search parameter cache in {}ms", sw.getMillis());
				}
			}
		}
	}

	private JpaRuntimeSearchParam toRuntimeSp(SearchParameter theNextSp) {
		String name = theNextSp.getCode();
		String description = theNextSp.getDescription();
		String path = theNextSp.getExpression();
		RestSearchParameterTypeEnum paramType = null;
		RuntimeSearchParamStatusEnum status = null;
		switch (theNextSp.getType()) {
		case COMPOSITE:
			paramType = RestSearchParameterTypeEnum.COMPOSITE;
			break;
		case DATE:
			paramType = RestSearchParameterTypeEnum.DATE;
			break;
		case NUMBER:
			paramType = RestSearchParameterTypeEnum.NUMBER;
			break;
		case QUANTITY:
			paramType = RestSearchParameterTypeEnum.QUANTITY;
			break;
		case REFERENCE:
			paramType = RestSearchParameterTypeEnum.REFERENCE;
			break;
		case STRING:
			paramType = RestSearchParameterTypeEnum.STRING;
			break;
		case TOKEN:
			paramType = RestSearchParameterTypeEnum.TOKEN;
			break;
		case URI:
			paramType = RestSearchParameterTypeEnum.URI;
			break;
		case NULL:
			break;
		}
		if (theNextSp.getStatus() != null) {
			switch (theNextSp.getStatus()) {
			case ACTIVE:
				status = RuntimeSearchParamStatusEnum.ACTIVE;
				break;
			case DRAFT:
				status = RuntimeSearchParamStatusEnum.DRAFT;
				break;
			case RETIRED:
				status = RuntimeSearchParamStatusEnum.RETIRED;
				break;
			case UNKNOWN:
				status = RuntimeSearchParamStatusEnum.UNKNOWN;
				break;
			case NULL:
				break;
			}
		}
		Set<String> providesMembershipInCompartments = Collections.emptySet();
		Set<String> targets = toStrings(theNextSp.getTarget());

		if (isBlank(name) || isBlank(path) || paramType == null) {
			if (paramType != RestSearchParameterTypeEnum.COMPOSITE) {
				return null;
			}
		}

		IIdType id = theNextSp.getIdElement();
		String uri = "";
		boolean unique = false;

		List<Extension> uniqueExts = theNextSp.getExtensionsByUrl(JpaConstants.EXT_SP_UNIQUE);
		if (uniqueExts.size() > 0) {
			IPrimitiveType<?> uniqueExtsValuePrimitive = uniqueExts.get(0).getValueAsPrimitive();
			if (uniqueExtsValuePrimitive != null) {
				if ("true".equalsIgnoreCase(uniqueExtsValuePrimitive.getValueAsString())) {
					unique = true;
				}
			}
		}

		List<JpaRuntimeSearchParam.Component> components = new ArrayList<>();
		for (SearchParameter.SearchParameterComponentComponent next : theNextSp.getComponent()) {
			components.add(new JpaRuntimeSearchParam.Component(next.getExpression(), next.getDefinition()));
		}

		JpaRuntimeSearchParam retVal = new JpaRuntimeSearchParam(id, uri, name, description, path, paramType, providesMembershipInCompartments, targets, status, unique, components, theNextSp.getBase());
		return retVal;
	}

	private Set<String> toStrings(List<CodeType> theTarget) {
		HashSet<String> retVal = new HashSet<String>();
		for (CodeType next : theTarget) {
			if (isNotBlank(next.getValue())) {
				retVal.add(next.getValue());
			}
		}
		return retVal;
	}

}
