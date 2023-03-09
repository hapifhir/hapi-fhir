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
import ca.uhn.fhir.rest.server.IPagingProvider;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
	private final IPagingProvider myPagingProvider;

	public HapiFhirRetrieveProvider(DaoRegistry theDaoRegistry, SearchParameterResolver theSearchParameterResolver, IPagingProvider thePagingProvider) {
		this(theDaoRegistry, theSearchParameterResolver, new SystemRequestDetails(), thePagingProvider);
	}

	public HapiFhirRetrieveProvider(DaoRegistry registry, SearchParameterResolver searchParameterResolver,
											  RequestDetails requestDetails, IPagingProvider thePagingProvider) {
		super(searchParameterResolver);
		this.myDaoRegistry = registry;
		this.myRequestDetails = requestDetails;
		this.myPagingProvider = thePagingProvider;
	}

	static class QueryIterable implements Iterable<Object> {

		private final String dataType;
		private final List<SearchParameterMap> queries;

		private final BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc;

		public QueryIterable(String dataType, List<SearchParameterMap> queries, BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc) {
			this.dataType = dataType;
			this.queries = queries;
			this.queryFunc = queryFunc;
		}

		static class QueryIterator implements Iterator<Object> {

			private final String dataType;
			private final List<SearchParameterMap> queries;

			private final BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc;

			Iterator<IBaseResource> currentResult = null;

			public QueryIterator(String dataType, List<SearchParameterMap> queries, BiFunction<String, SearchParameterMap, Iterable<IBaseResource>> queryFunc) {
				this.dataType = dataType;
				this.queries = queries;
				this.queryFunc = queryFunc;
			}
			private int index = 0;

			@Override
			public boolean hasNext() {
				if (currentResult == null && index < queries.size()) {
					currentResult = loadNext();
				}
				else if (!currentResult.hasNext()) {
					currentResult = loadNext();
				}

				return currentResult != null && currentResult.hasNext();
			}

			@Override
			public Object next() {
				return currentResult.next();
			}

			Iterator<IBaseResource> loadNext() {
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
		// TODO: Once HAPI breaks this out from the server dependencies
		// we can include it on its own.
		ca.uhn.fhir.jpa.searchparam.SearchParameterMap hapiMap = new ca.uhn.fhir.jpa.searchparam.SearchParameterMap();
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

		if (bundleProvider.isEmpty()) {return new ArrayList<>();}

		return new BundleIterable(this.myRequestDetails, bundleProvider, this.myPagingProvider);
	}

	static class BundleIterable implements Iterable<IBaseResource> {

		private final IBundleProvider sourceBundleProvider;
		private final IPagingProvider pagingProvider;

		private final RequestDetails requestDetails;

		private int currentPageIndex = 0;


		public BundleIterable(RequestDetails requestDetails, IBundleProvider bundleProvider, IPagingProvider pagingProvider) {
			this.sourceBundleProvider = bundleProvider;
			this.pagingProvider = pagingProvider;
			this.requestDetails = requestDetails;
		}

		@Override
		public Iterator<IBaseResource> iterator() {
			return new BundleIterator(this.requestDetails, this.sourceBundleProvider, this.pagingProvider);
		}

		static class BundleIterator implements Iterator<IBaseResource> {

			private IBundleProvider currentBundleProvider;
			private List<IBaseResource> currentResourceList;
			private final IPagingProvider pagingProvider;
			private final RequestDetails requestDetails;

			private int currentResourceListIndex = 0;


			public BundleIterator(RequestDetails requestDetails, IBundleProvider bundleProvider, IPagingProvider pagingProvider) {
				this.currentBundleProvider = bundleProvider;
				this.pagingProvider = pagingProvider;
				this.requestDetails = requestDetails;
				initPage();
			}

			private void initPage() {
				var size = this.currentBundleProvider.getCurrentPageSize();
				this.currentResourceList = this.currentBundleProvider.getResources(0, size);
				currentResourceListIndex = 0;
			}

			private void loadNextPage() {
				currentBundleProvider = this.pagingProvider.retrieveResultList(this.requestDetails, this.currentBundleProvider.getUuid(), this.currentBundleProvider.getNextPageId());
				initPage();
			}

			@Override
			public boolean hasNext() {
				// We still have things in the current page to return, so we have a next.
				if (this.currentResourceListIndex < this.currentResourceList.size()) {
					return true;
				}

				// We're at the end of the current page, and there's no next page.
				if (this.currentBundleProvider.getNextPageId() == null) {
					return false;
				}

				// We have a next page, so let's load it.
				this.loadNextPage();;

				return this.hasNext();
			}


			@Override
			public IBaseResource next() {
				if (this.currentResourceListIndex >= this.currentResourceList.size()) {
					throw new RuntimeException("Shouldn't happen bruh");
				}

				var result = this.currentResourceList.get(this.currentResourceListIndex);
				this.currentResourceListIndex++;
				return result;
			}
		}
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}
}
