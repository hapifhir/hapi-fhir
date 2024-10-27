/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
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
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SearchConfig {
	public static final String SEARCH_TASK = "searchTask";
	public static final String CONTINUE_TASK = "continueTask";

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;

	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;

	@Autowired
	private HibernatePropertiesProvider myDialectProvider;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	protected IResourceTagDao myResourceTagDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IResourceSearchViewDao myResourceSearchViewDao;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private PlatformTransactionManager myManagedTxManager;

	@Autowired
	private SearchStrategyFactory mySearchStrategyFactory;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private ISearchResultCacheSvc mySearchResultCacheSvc;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Autowired
	private IPagingProvider myPagingProvider;

	@Autowired
	private BeanFactory myBeanFactory;

	@Autowired
	private ISynchronousSearchSvc mySynchronousSearchSvc;

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	@Bean
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl(
				myContext,
				myStorageSettings,
				myInterceptorBroadcaster,
				myHapiTransactionService,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myDaoRegistry,
				mySearchBuilderFactory,
				mySynchronousSearchSvc,
				myPersistedJpaBundleProviderFactory,
				mySearchParamRegistry,
				mySearchStrategyFactory,
				exceptionService(),
				myBeanFactory);
	}

	@Bean
	public ExceptionService exceptionService() {
		return new ExceptionService(myContext);
	}

	@Bean(name = ISearchBuilder.SEARCH_BUILDER_BEAN_NAME)
	@Scope("prototype")
	public ISearchBuilder newSearchBuilder(
			IDao theDao, String theResourceName, Class<? extends IBaseResource> theResourceType) {
		return new SearchBuilder(
				theDao,
				theResourceName,
				myStorageSettings,
				myEntityManagerFactory,
				mySqlBuilderFactory,
				myDialectProvider,
				mySearchParamRegistry,
				myPartitionSettings,
				myInterceptorBroadcaster,
				myResourceTagDao,
				myDaoRegistry,
				myResourceSearchViewDao,
				myContext,
				myIdHelperService,
				theResourceType);
	}

	@Bean(name = SEARCH_TASK)
	@Scope("prototype")
	public SearchTask createSearchTask(SearchTaskParameters theParams) {
		return new SearchTask(
				theParams,
				myHapiTransactionService,
				myContext,
				myInterceptorBroadcaster,
				mySearchBuilderFactory,
				mySearchResultCacheSvc,
				myStorageSettings,
				mySearchCacheSvc,
				myPagingProvider);
	}

	@Bean(name = CONTINUE_TASK)
	@Scope("prototype")
	public SearchContinuationTask createSearchContinuationTask(SearchTaskParameters theParams) {
		return new SearchContinuationTask(
				theParams,
				myHapiTransactionService,
				myContext,
				myInterceptorBroadcaster,
				mySearchBuilderFactory,
				mySearchResultCacheSvc,
				myStorageSettings,
				mySearchCacheSvc,
				myPagingProvider,
				exceptionService() // singleton
				);
	}

	@PostConstruct
	public void validateConfiguration() {
		if (myStorageSettings.isIndexStorageOptimized()
				&& myPartitionSettings.isPartitioningEnabled()
				&& myPartitionSettings.isIncludePartitionInSearchHashes()) {
			throw new ConfigurationException(Msg.code(2525) + "Incorrect configuration. "
					+ "StorageSettings#isIndexStorageOptimized and PartitionSettings.isIncludePartitionInSearchHashes "
					+ "cannot be enabled at the same time.");
		}
	}
}
