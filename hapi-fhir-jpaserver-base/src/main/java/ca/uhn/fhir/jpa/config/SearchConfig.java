/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.BatchResourceLoader;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.ISynchronousSearchSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchContinuationTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTaskParameters;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.IMetaTagSorter;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SearchConfig {
	public static final String SEARCH_TASK = "searchTask";
	public static final String CONTINUE_TASK = "continueTask";

	@Autowired
	protected JpaStorageSettings theStorageSettings;

	@Autowired
	protected HapiFhirLocalContainerEntityManagerFactoryBean theEntityManagerFactory;

	@Autowired
	protected SqlObjectFactory theSqlBuilderFactory;

	@Autowired
	protected HibernatePropertiesProvider theDialectProvider;

	@Autowired
	protected ISearchParamRegistry theSearchParamRegistry;

	@Autowired
	protected PartitionSettings thePartitionSettings;

	@Autowired
	protected IInterceptorBroadcaster theInterceptorBroadcaster;

	@Autowired
	protected IResourceTagDao theResourceTagDao;

	@Autowired
	protected DaoRegistry theDaoRegistry;

	@Autowired
	protected FhirContext theContext;

	@Autowired
	protected IIdHelperService theIdHelperService;

	@Autowired
	protected SearchStrategyFactory theSearchStrategyFactory;

	@Autowired
	protected SearchBuilderFactory theSearchBuilderFactory;

	@Autowired
	protected ISearchResultCacheSvc theSearchResultCacheSvc;

	@Autowired
	protected ISearchCacheSvc theSearchCacheSvc;

	@Autowired
	protected IPagingProvider thePagingProvider;

	@Autowired
	protected BeanFactory theBeanFactory;

	@Autowired
	protected ISynchronousSearchSvc theSynchronousSearchSvc;

	@Autowired
	protected PersistedJpaBundleProviderFactory thePersistedJpaBundleProviderFactory;

	@Autowired
	protected IRequestPartitionHelperSvc theRequestPartitionHelperService;

	@Autowired
	protected HapiTransactionService theHapiTransactionService;

	@Autowired
	protected IResourceHistoryTableDao theResourceHistoryTableDao;

	@Autowired
	protected BatchResourceLoader theBatchResourceLoader;

	@Bean
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl();
	}

	@Bean
	public ExceptionService exceptionService() {
		return new ExceptionService(theContext);
	}

	@Bean
	public BatchResourceLoader batchResourceLoader(
			IResourceMetadataExtractorSvc theResourceMetadataExtractorSvc,
			IJpaStorageResourceParser theJpaStorageResourceParser,
			ExternallyStoredResourceServiceRegistry theExternallyStoredResourceServiceRegistry,
			IMetaTagSorter theMetaTagSorter,
			IPartitionLookupSvc thePartitionLookupSvc) {
		return new BatchResourceLoader(
				theContext,
				theResourceMetadataExtractorSvc,
				theJpaStorageResourceParser,
				theExternallyStoredResourceServiceRegistry,
				theMetaTagSorter,
				thePartitionSettings,
				thePartitionLookupSvc);
	}

	@Bean(name = ISearchBuilder.SEARCH_BUILDER_BEAN_NAME)
	@Scope("prototype")
	public ISearchBuilder newSearchBuilder(String theResourceName, Class<? extends IBaseResource> theResourceType) {
		return new SearchBuilder(theResourceName, theResourceType);
	}

	@Bean(name = SEARCH_TASK)
	@Scope("prototype")
	public SearchTask createSearchTask(SearchTaskParameters theParams) {
		return new SearchTask(theParams);
	}

	@Bean(name = CONTINUE_TASK)
	@Scope("prototype")
	public SearchContinuationTask createSearchContinuationTask(SearchTaskParameters theParams) {
		return new SearchContinuationTask(theParams);
	}

	@PostConstruct
	public void validateConfiguration() {
		if (theStorageSettings.isIndexStorageOptimized()
				&& thePartitionSettings.isPartitioningEnabled()
				&& thePartitionSettings.isIncludePartitionInSearchHashes()) {
			throw new ConfigurationException(Msg.code(2525) + "Incorrect configuration. "
					+ "StorageSettings#isIndexStorageOptimized and PartitionSettings.isIncludePartitionInSearchHashes "
					+ "cannot be enabled at the same time.");
		}
	}
}
