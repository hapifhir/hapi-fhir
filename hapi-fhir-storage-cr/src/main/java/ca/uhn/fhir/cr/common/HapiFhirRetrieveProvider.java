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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.fhir.retrieve.SearchParamFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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

	public HapiFhirRetrieveProvider(
			DaoRegistry registry, SearchParameterResolver searchParameterResolver, RequestDetails requestDetails) {
		super(searchParameterResolver);
		this.myDaoRegistry = registry;
		this.myRequestDetails = requestDetails;
	}

	/**
	 * The queryIterable class provides an Iterable to cycle through a series of search queries and results of those queries, implementation of this avoided loading all resources into a list.
	 */
	static class QueryIterable implements Iterable<Object> {

		private final String dataType;
		private final List<SearchParameterMap> queries;

		private final BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc;

		public QueryIterable(
				String dataType,
				List<SearchParameterMap> queries,
				BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc) {
			this.dataType = dataType;
			this.queries = queries;
			this.queryFunc = queryFunc;
		}

		static class QueryIterator implements Iterator<Object> {

			private final String dataType;
			private final List<SearchParameterMap> queries;

			private final BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc;

			Iterator<IBaseResource> currentResult = null;

			public QueryIterator(
					String dataType,
					List<SearchParameterMap> queries,
					BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc) {
				this.dataType = dataType;
				this.queries = queries;
				this.queryFunc = queryFunc;
			}

			private int index = 0;

			@Override
			public boolean hasNext() {
				// initial load of first query results
				if (currentResult == null) {
					currentResult = loadNext();
				}
				// loop through all batches of results
				// when query results exhaust load next query
				while (currentResult != null && !currentResult.hasNext()) {
					currentResult = loadNext();
				}
				// hasNext on current query result
				return currentResult != null && currentResult.hasNext();
			}

			@Override
			public Object next() {
				return currentResult.next();
			}

			Iterator<IBaseResource> loadNext() {
				// check to make sure there are more queries remaining
				if (index >= queries.size()) {
					return null;
				}
				// extract next query result
				var result = this.queryFunc.apply(dataType, queries.get(index)).iterator();
				index++;
				return result;
			}
		}

		public Iterator<Object> iterator() {
			return new QueryIterator(dataType, queries, queryFunc);
		}
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		if (queries == null || queries.isEmpty()) {
			return Collections.emptyList();
		}

		return new QueryIterable(dataType, queries, this::executeQuery);
	}

	protected Iterable<IBaseResource> executeQuery(String dataType, SearchParameterMap map) {
		ca.uhn.fhir.jpa.searchparam.SearchParameterMap hapiMap = new ca.uhn.fhir.jpa.searchparam.SearchParameterMap();
		try {

			for (Map.Entry<String, List<List<IQueryParameterType>>> entry : map.entrySet()) {
				hapiMap.put(entry.getKey(), entry.getValue());
			}

		} catch (Exception e) {
			logger.warn("Error converting search parameter map", e);
		}

		return search(getClass(dataType), hapiMap, myRequestDetails);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}
}
