package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
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
import ca.uhn.fhir.jpa.search.lastn.IElasticsearchSvc;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
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
	private DaoConfig myDaoConfig;
	@Autowired
	private HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;
	@Autowired
	private HibernatePropertiesProvider myDialectProvider;
	@Autowired
	private ModelConfig myModelConfig;
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

	@Bean
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl(
			myContext,
			myDaoConfig,
			myInterceptorBroadcaster,
			myManagedTxManager,
			mySearchCacheSvc,
			mySearchResultCacheSvc,
			myDaoRegistry,
			mySearchBuilderFactory,
			mySynchronousSearchSvc,
			myPersistedJpaBundleProviderFactory,
			myRequestPartitionHelperService,
			mySearchParamRegistry,
			mySearchStrategyFactory,
			exceptionService(),
			myBeanFactory
		);
	}

	@Bean
	public ExceptionService exceptionService() {
		return new ExceptionService(myContext);
	}

	@Bean(name = ISearchBuilder.SEARCH_BUILDER_BEAN_NAME)
	@Scope("prototype")
	public ISearchBuilder newSearchBuilder(IDao theDao, String theResourceName, Class<? extends IBaseResource> theResourceType, DaoConfig theDaoConfig) {
		return new SearchBuilder(theDao,
			theResourceName,
			myDaoConfig,
			myEntityManagerFactory,
			mySqlBuilderFactory,
			myDialectProvider,
			myModelConfig,
			mySearchParamRegistry,
			myPartitionSettings,
			myInterceptorBroadcaster,
			myResourceTagDao,
			myDaoRegistry,
			myResourceSearchViewDao,
			myContext,
			myIdHelperService,
			theResourceType
		);
	}

	@Bean(name = SEARCH_TASK)
	@Scope("prototype")
	public SearchTask createSearchTask(SearchTaskParameters theParams) {
		return new SearchTask(theParams,
			myManagedTxManager,
			myContext,
			mySearchStrategyFactory,
			myInterceptorBroadcaster,
			mySearchBuilderFactory,
			mySearchResultCacheSvc,
			myDaoConfig,
			mySearchCacheSvc,
			myPagingProvider
		);
	}


	@Bean(name = CONTINUE_TASK)
	@Scope("prototype")
	public SearchContinuationTask createSearchContinuationTask(SearchTaskParameters theParams) {
		return new SearchContinuationTask(theParams,
			myManagedTxManager,
			myContext,
			mySearchStrategyFactory,
			myInterceptorBroadcaster,
			mySearchBuilderFactory,
			mySearchResultCacheSvc,
			myDaoConfig,
			mySearchCacheSvc,
			myPagingProvider,
			exceptionService() // singleton
		);
	}
}
