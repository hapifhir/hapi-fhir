package ca.uhn.fhir.cr.common;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.fhir.retrieve.SearchParamFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides an implementation of the cql-engine's RetrieveProvider
 * interface which is used for loading
 * data during CQL evaluation.
 */
public class HapiFhirRetrieveProvider extends SearchParamFhirRetrieveProvider implements IDaoRegistryUser {

	private static final Logger logger = LoggerFactory.getLogger(HapiFhirRetrieveProvider.class);

	private final DaoRegistry myDaoRegistry;
	private final RequestDetails myRequestDetails;

	public HapiFhirRetrieveProvider(DaoRegistry theDaoRegistry, SearchParameterResolver theSearchParameterResolver) {
		this(theDaoRegistry, theSearchParameterResolver, new SystemRequestDetails());
	}

	public HapiFhirRetrieveProvider(DaoRegistry registry, SearchParameterResolver searchParameterResolver,
											  RequestDetails requestDetails) {
		super(searchParameterResolver);
		this.myDaoRegistry = registry;
		this.myRequestDetails = requestDetails;
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		if (queries == null || queries.isEmpty()) {
			return Collections.emptyList();
		}

		List<Object> objects = new ArrayList<>();
		for (SearchParameterMap map : queries) {
			objects.addAll(executeQuery(dataType, map));
		}

		return objects;
	}

	protected List<IBaseResource> executeQuery(String dataType, SearchParameterMap map) {
		// TODO: Once HAPI breaks this out from the server dependencies
		// we can include it on its own.
		ca.uhn.fhir.jpa.searchparam.SearchParameterMap hapiMap = ca.uhn.fhir.jpa.searchparam.SearchParameterMap
			.newSynchronous();
		try {

			Method[] methods = hapiMap.getClass().getDeclaredMethods();
			List<Method> methodList = Arrays.asList(methods);
			List<Method> puts = methodList.stream().filter(x -> x.getName().equals("put")).collect(Collectors.toList());
			Method method = puts.get(0);
			method.setAccessible(true);

			for (Map.Entry<String, List<List<IQueryParameterType>>> entry : map.entrySet()) {
				method.invoke(hapiMap, entry.getKey(), entry.getValue());
			}

		} catch (Exception e) {
			logger.warn("Error converting search parameter map", e);
		}

		IBundleProvider bundleProvider = search(getClass(dataType), hapiMap, myRequestDetails);
		if (bundleProvider.isEmpty()) {
			return new ArrayList<>();
		}

		return bundleProvider.getAllResources();
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}
}
