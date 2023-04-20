package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.common.CodeCacheResourceChangeListener;
import ca.uhn.fhir.cr.common.CqlExceptionHandlingInterceptor;
import ca.uhn.fhir.cr.common.CqlForkJoinWorkerThreadFactory;
import ca.uhn.fhir.cr.common.ElmCacheResourceChangeListener;
import ca.uhn.fhir.cr.common.HapiFhirDal;
import ca.uhn.fhir.cr.common.HapiFhirRetrieveProvider;
import ca.uhn.fhir.cr.common.HapiLibrarySourceProvider;
import ca.uhn.fhir.cr.common.HapiTerminologyProvider;
import ca.uhn.fhir.cr.common.IDataProviderFactory;
import ca.uhn.fhir.cr.common.IFhirDalFactory;
import ca.uhn.fhir.cr.common.ILibraryLoaderFactory;
import ca.uhn.fhir.cr.common.ILibraryManagerFactory;
import ca.uhn.fhir.cr.common.ILibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.ITerminologyProviderFactory;
import ca.uhn.fhir.cr.config.CrProperties;
import ca.uhn.fhir.cr.config.CrProviderFactory;
import ca.uhn.fhir.cr.config.CrProviderLoader;
import ca.uhn.fhir.cr.config.PreExpandedValidationSupportLoader;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.cql2elm.quick.FhirLibrarySourceProvider;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.builder.DataProviderComponents;
import org.opencds.cqf.cql.evaluator.builder.EndpointInfo;
import org.opencds.cqf.cql.evaluator.cql2elm.model.CacheAwareModelManager;
import org.opencds.cqf.cql.evaluator.cql2elm.util.LibraryVersionSelector;
import org.opencds.cqf.cql.evaluator.engine.execution.CacheAwareLibraryLoaderDecorator;
import org.opencds.cqf.cql.evaluator.engine.execution.TranslatingLibraryLoader;
import org.opencds.cqf.cql.evaluator.engine.model.CachingModelResolverDecorator;
import org.opencds.cqf.cql.evaluator.engine.retrieve.BundleRetrieveProvider;
import org.opencds.cqf.cql.evaluator.fhir.Constants;
import org.opencds.cqf.cql.evaluator.fhir.adapter.AdapterFactory;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

@Configuration
@Import(TestCrConfig.class)
public class TestCrDstu3Config {
	@Bean
	public Function<RequestDetails, MeasureService> dstu3MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService dstu3measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider dstu3measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	@Bean
	CrProviderFactory cqlProviderFactory() {
		return new CrProviderFactory();
	}

	@Bean
	CrProviderLoader cqlProviderLoader(FhirContext theFhirContext, ResourceProviderFactory theResourceProviderFactory, CrProviderFactory theCqlProviderFactory) {
		return new CrProviderLoader(theFhirContext, theResourceProviderFactory, theCqlProviderFactory);
	}

	@Bean
	public CrProperties crProperties() {
		var cqlProperties = new CrProperties.CqlProperties();
		var translatorOptions = cqlProperties.getCqlTranslatorOptions();
		translatorOptions.setCompatibilityLevel("1.3");
		cqlProperties.setCqlTranslatorOptions(translatorOptions);
		var properties = new CrProperties();
		properties.setCqlProperties(cqlProperties);

		return properties;
	}

	@Bean
	public CrProperties.CqlProperties cqlProperties(CrProperties theCrProperties) {
		return theCrProperties.getCqlProperties();
	}

	@Bean
	public CrProperties.MeasureProperties measureProperties(CrProperties theCrProperties) {
		return theCrProperties.getMeasureProperties();
	}

	@Bean
	public MeasureEvaluationOptions measureEvaluationOptions(CrProperties theCrProperties) {
		return theCrProperties.getMeasureProperties().getMeasureEvaluationOptions();
	}

	@Bean
	public CqlOptions cqlOptions(CrProperties theCrProperties) {
		return theCrProperties.getCqlProperties().getCqlOptions();
	}

	@Bean
	public CqlExceptionHandlingInterceptor cqlExceptionHandlingInterceptor() {
		return new CqlExceptionHandlingInterceptor();
	}

	@Bean
	public CqlTranslatorOptions cqlTranslatorOptions(FhirContext theFhirContext, CrProperties.CqlProperties theCqlProperties) {
		CqlTranslatorOptions options = theCqlProperties.getCqlOptions().getCqlTranslatorOptions();

		return options;
	}

	@Bean
	public ModelManager modelManager(
		Map<ModelIdentifier, Model> theGlobalModelCache) {
		return new CacheAwareModelManager(theGlobalModelCache);
	}

	@Bean
	public ILibraryManagerFactory libraryManagerFactory(
		ModelManager theModelManager) {
		return (providers) -> {
			LibraryManager libraryManager = new LibraryManager(theModelManager);
			for (LibrarySourceProvider provider : providers) {
				libraryManager.getLibrarySourceLoader().registerProvider(provider);
			}
			return libraryManager;
		};
	}

	@Bean
	public SearchParameterResolver searchParameterResolver(FhirContext theFhirContext) {
		return new SearchParameterResolver(theFhirContext);
	}

	@Bean
	IFhirDalFactory fhirDalFactory(DaoRegistry theDaoRegistry) {
		return rd -> new HapiFhirDal(theDaoRegistry, rd);
	}

	@Bean
	IDataProviderFactory dataProviderFactory(ModelResolver theModelResolver, DaoRegistry theDaoRegistry,
														  SearchParameterResolver theSearchParameterResolver) {
		return (rd, t) -> {
			HapiFhirRetrieveProvider provider = new HapiFhirRetrieveProvider(theDaoRegistry, theSearchParameterResolver, rd);
			if (t != null) {
				provider.setTerminologyProvider(t);
				provider.setExpandValueSets(true);
				provider.setMaxCodesPerQuery(500);
				provider.setModelResolver(theModelResolver);
			}
			return new CompositeDataProvider(theModelResolver, provider);
		};
	}

	@Bean
	org.opencds.cqf.cql.evaluator.builder.DataProviderFactory builderDataProviderFactory(FhirContext theFhirContext, ModelResolver theModelResolver) {
		return new org.opencds.cqf.cql.evaluator.builder.DataProviderFactory() {
			@Override
			public DataProviderComponents create(EndpointInfo theEndpointInfo) {
				// to do implement endpoint
				return null;
			}

			@Override
			public DataProviderComponents create(IBaseBundle theDataBundle) {
				return new DataProviderComponents(Constants.FHIR_MODEL_URI, theModelResolver,
					new BundleRetrieveProvider(theFhirContext, theDataBundle));
			}
		};

	}

	@Bean
	public HapiFhirRetrieveProvider fhirRetrieveProvider(DaoRegistry theDaoRegistry,
																		  SearchParameterResolver theSearchParameterResolver) {
		return new HapiFhirRetrieveProvider(theDaoRegistry, theSearchParameterResolver);
	}

	@Bean
	public ITerminologyProviderFactory terminologyProviderFactory(
		IValidationSupport theValidationSupport,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		return rd -> new HapiTerminologyProvider(theValidationSupport, theGlobalCodeCache,
			rd);
	}

	@Bean
	ILibrarySourceProviderFactory librarySourceProviderFactory(DaoRegistry theDaoRegistry) {
		return rd -> new HapiLibrarySourceProvider(theDaoRegistry, rd);
	}

	@Bean
	ILibraryLoaderFactory libraryLoaderFactory(
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> theGlobalLibraryCache,
		ModelManager theModelManager, CqlTranslatorOptions theCqlTranslatorOptions, CrProperties.CqlProperties theCqlProperties) {
		return lcp -> {

			if (theCqlProperties.getCqlOptions().useEmbeddedLibraries()) {
				lcp.add(new FhirLibrarySourceProvider());
			}

			return new CacheAwareLibraryLoaderDecorator(
				new TranslatingLibraryLoader(theModelManager, lcp, theCqlTranslatorOptions, null), theGlobalLibraryCache) {
				// TODO: This is due to a bug with the ELM annotations which prevent options
				// from matching the way they should
				@Override
				protected Boolean translatorOptionsMatch(org.cqframework.cql.elm.execution.Library library) {
					return true;
				}
			};
		};
	}

	// TODO: Use something like caffeine caching for this so that growth is limited.
	@Bean
	public Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> globalLibraryCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> globalCodeCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<ModelIdentifier, Model> globalModelCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	@Primary
	public ElmCacheResourceChangeListener elmCacheResourceChangeListener(
		IResourceChangeListenerRegistry theResourceChangeListenerRegistry, DaoRegistry theDaoRegistry,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> theGlobalLibraryCache) {
		ElmCacheResourceChangeListener listener = new ElmCacheResourceChangeListener(theDaoRegistry, theGlobalLibraryCache);
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener("Library",
			SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	@Primary
	public CodeCacheResourceChangeListener codeCacheResourceChangeListener(
		IResourceChangeListenerRegistry theResourceChangeListenerRegistry, DaoRegistry theDaoRegistry,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		CodeCacheResourceChangeListener listener = new CodeCacheResourceChangeListener(theDaoRegistry, theGlobalCodeCache);
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener("ValueSet",
			SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	public ModelResolver modelResolver(FhirContext theFhirContext) {
		return new CachingModelResolverDecorator(new Dstu3FhirModelResolver());
	}

	@Bean
	public LibraryVersionSelector libraryVersionSelector(AdapterFactory theAdapterFactory) {
		return new LibraryVersionSelector(theAdapterFactory);
	}

	@Bean(name = "cqlExecutor")
	public Executor cqlExecutor() {
		CqlForkJoinWorkerThreadFactory factory = new CqlForkJoinWorkerThreadFactory();
		ForkJoinPool myCommonPool = new ForkJoinPool(Math.min(32767, Runtime.getRuntime().availableProcessors()),
			factory,
			null, false);

		return new DelegatingSecurityContextExecutor(myCommonPool,
			SecurityContextHolder.getContext());
	}

	@Bean
	public PreExpandedValidationSupportLoader preExpandedValidationSupportLoader(ValidationSupportChain theSupportChain,
																										  FhirContext theFhirContext) {
		return new PreExpandedValidationSupportLoader(theSupportChain, theFhirContext);
	}
}
