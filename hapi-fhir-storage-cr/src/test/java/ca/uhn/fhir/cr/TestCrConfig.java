package ca.uhn.fhir.cr;

import ca.uhn.fhir.cr.common.ILibraryLoaderFactory;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.cql2elm.quick.FhirLibrarySourceProvider;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.cql.model.ModelIdentifier;
import org.opencds.cqf.cql.evaluator.engine.execution.TranslatingLibraryLoader;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.IJpaSystemProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.base.Strings;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.Map;


@Configuration
@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestCrConfig {

	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setAllowExternalReferences(true);
		storageSettings.setEnforceReferentialIntegrityOnWrite(false);
		storageSettings.setEnforceReferenceTargetTypes(false);
		storageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		//storageSettings.setResourceServerIdStrategy(Id);
		return storageSettings;
	}

	@Bean
	public PartitionHelper partitionHelper() {
		return new PartitionHelper();
	}

	@Bean
	public TestCqlProperties testCqlProperties(){
		return new TestCqlProperties();}

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
	@Scope("prototype")
	ILibraryLoaderFactory libraryLoaderFactory(Map<VersionedIdentifier, Library> theGlobalLibraryCache,
															 ModelManager theModelManager, EvaluationSettings theEvaluationSettings) {
		return lcp -> {

			if (theEvaluationSettings.getCqlOptions().useEmbeddedLibraries()) {
				lcp.add(new FhirLibrarySourceProvider());
			}

			return new TranslatingLibraryLoader(theModelManager, lcp, theEvaluationSettings.getCqlOptions().getCqlTranslatorOptions(), theGlobalLibraryCache);
		};
	}

	@Bean
	@Scope("prototype")
	public ModelManager modelManager(Map<ModelIdentifier, Model> theGlobalModelCache) {
		return new ModelManager(theGlobalModelCache);
	}

}
