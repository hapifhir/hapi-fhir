package ca.uhn.fhir.cr;

import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.cr.common.CodeCacheResourceChangeListener;
import ca.uhn.fhir.cr.common.ElmCacheResourceChangeListener;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryInterceptor;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.IJpaSystemProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.base.Strings;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.CompiledLibrary;
import org.cqframework.cql.cql2elm.model.Model;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.elm.r1.VersionedIdentifier;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestCrConfig {
	@Bean
	public RestfulServer restfulServer(IFhirSystemDao<?, ?> fhirSystemDao, DaoRegistry daoRegistry, IJpaSystemProvider jpaSystemProvider, ResourceProviderFactory resourceProviderFactory, JpaStorageSettings jpaStorageSettings, ISearchParamRegistry searchParamRegistry, IValidationSupport theValidationSupport, DatabaseBackedPagingProvider databaseBackedPagingProvider, ValueSetOperationProvider theValueSetOperationProvider,
												  ReindexProvider myReindexProvider,
												  ApplicationContext myAppCtx) {
		RestfulServer ourRestServer = new RestfulServer(fhirSystemDao.getContext());

		TerminologyUploaderProvider myTerminologyUploaderProvider = myAppCtx.getBean(TerminologyUploaderProvider.class);

		ourRestServer.registerProviders(resourceProviderFactory.createProviders());
		ourRestServer.registerProvider(jpaSystemProvider);
		ourRestServer.registerProviders(myTerminologyUploaderProvider, myReindexProvider);
		ourRestServer.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
		ourRestServer.registerProvider(myAppCtx.getBean(DiffProvider.class));
		ourRestServer.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

		//to do
		String serverAddress = null;
		if (!Strings.isNullOrEmpty(serverAddress)) {
			ourRestServer.setServerAddressStrategy(new HardcodedServerAddressStrategy(serverAddress));
		} else {
			ourRestServer.setServerAddressStrategy(new IncomingRequestAddressStrategy());
		}

		return ourRestServer;
	}
	@Bean
	public TestCqlProperties testCqlProperties(){
		return new TestCqlProperties();}
	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setAllowExternalReferences(true);
		storageSettings.setEnforceReferentialIntegrityOnWrite(false);
		storageSettings.setEnforceReferenceTargetTypes(false);
		storageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		return storageSettings;
	}

	@Bean
	public PartitionHelper partitionHelper() {
		return new PartitionHelper();
	}

	@Bean
	public ModelManager modelManager(Map<ModelIdentifier, Model> theGlobalModelCache) {
		return new ModelManager(theGlobalModelCache);
	}

	@Bean
	public Map<VersionedIdentifier, CompiledLibrary> globalLibraryCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<ModelIdentifier, Model> globalModelCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<String, List<Code>> globalValueSetCache() {
		return new ConcurrentHashMap<>();
	}


	@Bean
	public ElmCacheResourceChangeListener elmCacheResourceChangeListener(
		IResourceChangeListenerRegistry theResourceChangeListenerRegistry,
		DaoRegistry theDaoRegistry,
		EvaluationSettings theEvaluationSettings) {
		ElmCacheResourceChangeListener listener =
			new ElmCacheResourceChangeListener(theDaoRegistry, theEvaluationSettings.getLibraryCache());
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener(
			"Library", SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	public CodeCacheResourceChangeListener codeCacheResourceChangeListener(
		IResourceChangeListenerRegistry theResourceChangeListenerRegistry,
		EvaluationSettings theEvaluationSettings,
		DaoRegistry theDaoRegistry) {

		CodeCacheResourceChangeListener listener = new CodeCacheResourceChangeListener(theDaoRegistry, theEvaluationSettings.getValueSetCache());
		//registry
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener(
			"ValueSet", SearchParameterMap.newSynchronous(), listener,1000);

		return listener;
	}

	@Bean
	public IResourceChangeListenerRegistry resourceChangeListenerRegistry(InMemoryResourceMatcher theInMemoryResourceMatcher, FhirContext theFhirContext, ResourceChangeListenerCacheFactory theResourceChangeListenerCacheFactory) {
		return new ResourceChangeListenerRegistryImpl(theFhirContext, theResourceChangeListenerCacheFactory, theInMemoryResourceMatcher);
	}

	@Bean
	IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
	}
	@Bean
	public ResourceChangeListenerRegistryInterceptor resourceChangeListenerRegistryInterceptor() {
		return new ResourceChangeListenerRegistryInterceptor();
	}



}
