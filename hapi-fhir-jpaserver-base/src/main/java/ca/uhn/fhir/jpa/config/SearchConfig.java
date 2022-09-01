package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.lastn.IElasticsearchSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SearchConfig {

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
}
