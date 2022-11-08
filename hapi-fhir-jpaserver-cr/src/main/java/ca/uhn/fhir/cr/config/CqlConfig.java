package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.cr.common.CodeCacheResourceChangeListener;
import ca.uhn.fhir.cr.common.CqlForkJoinWorkerThreadFactory;
import ca.uhn.fhir.cr.common.ElmCacheResourceChangeListener;
import ca.uhn.fhir.cr.common.JpaDataProviderFactory;
import ca.uhn.fhir.cr.common.JpaFhirDal;
import ca.uhn.fhir.cr.common.JpaFhirDalFactory;
import ca.uhn.fhir.cr.common.JpaFhirRetrieveProvider;
import ca.uhn.fhir.cr.common.JpaLibrarySourceProvider;
import ca.uhn.fhir.cr.common.JpaLibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.JpaTerminologyProvider;
import ca.uhn.fhir.cr.common.JpaTerminologyProviderFactory;
import ca.uhn.fhir.cr.common.LibraryLoaderFactory;
import ca.uhn.fhir.cr.common.LibraryManagerFactory;
import ca.uhn.fhir.cr.common.PreExpandedValidationSupport;
import ca.uhn.fhir.cr.common.interceptor.CqlExceptionHandlingInterceptor;
import ca.uhn.fhir.cr.common.provider.CrProviderFactory;
import ca.uhn.fhir.cr.common.provider.CrProviderLoader;
import ca.uhn.fhir.cr.dstu2.PreExpandedTermReadSvcDstu2;
import ca.uhn.fhir.cr.dstu3.PreExpandedTermReadSvcDstu3;
import ca.uhn.fhir.cr.r4.PreExpandedTermReadSvcR4;
import ca.uhn.fhir.cr.r5.PreExpandedTermReadSvcR5;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.cql2elm.quick.FhirLibrarySourceProvider;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.builder.Constants;
import org.opencds.cqf.cql.evaluator.builder.DataProviderComponents;
import org.opencds.cqf.cql.evaluator.builder.DataProviderFactory;
import org.opencds.cqf.cql.evaluator.builder.EndpointInfo;
import org.opencds.cqf.cql.evaluator.cql2elm.model.CacheAwareModelManager;
import org.opencds.cqf.cql.evaluator.cql2elm.util.LibraryVersionSelector;
import org.opencds.cqf.cql.evaluator.engine.execution.CacheAwareLibraryLoaderDecorator;
import org.opencds.cqf.cql.evaluator.engine.execution.TranslatingLibraryLoader;
import org.opencds.cqf.cql.evaluator.engine.model.CachingModelResolverDecorator;
import org.opencds.cqf.cql.evaluator.engine.retrieve.BundleRetrieveProvider;
import org.opencds.cqf.cql.evaluator.fhir.adapter.AdapterFactory;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.opencds.cqf.cql.evaluator.spring.fhir.adapter.AdapterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Import(AdapterConfiguration.class)
@Configuration
public class CqlConfig {

	private static final Logger log = LoggerFactory.getLogger(CqlConfig.class);


	@Bean
	CrProviderFactory cqlProviderFactory() {
		return new CrProviderFactory();
	}

	@Bean
	CrProviderLoader cqlProviderLoader() {
		return new CrProviderLoader();
	}


	@Bean
	public CqlProperties cqlProperties() {
		return new CqlProperties();
	}

	@Bean
	public CrProperties crProperties() {
		return new CrProperties();
	}

	@Bean
	public MeasureEvaluationOptions measureEvaluationOptions() {
		return crProperties().getMeasureEvaluation();
	}

	@Bean
	public CqlOptions cqlOptions() {
		return cqlProperties().getOptions();
	}

	@Bean
	public CqlExceptionHandlingInterceptor cqlExceptionHandlingInterceptor() {
		return new CqlExceptionHandlingInterceptor();
	}

	@Bean
	public CqlTranslatorOptions cqlTranslatorOptions(FhirContext fhirContext, CqlProperties cqlProperties) {
		CqlTranslatorOptions options = cqlProperties.getOptions().getCqlTranslatorOptions();

		if (fhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.R4)
			&& (options.getCompatibilityLevel().equals("1.5") || options.getCompatibilityLevel().equals("1.4"))) {
			log.warn("{} {} {}",
				"This server is configured to use CQL version > 1.4 and FHIR version <= DSTU3.",
				"Most available CQL content for DSTU3 and below is for CQL versions 1.3.",
				"If your CQL content causes translation errors, try setting the CQL compatibility level to 1.3");
		}

		return options;
	}

	@Bean
	public ModelManager modelManager(
		Map<ModelIdentifier, Model> globalModelCache) {
		return new CacheAwareModelManager(globalModelCache);
	}

	@Bean
	public LibraryManagerFactory libraryManagerFactory(
		ModelManager modelManager) {
		return (providers) -> {
			LibraryManager libraryManager = new LibraryManager(modelManager);
			for (LibrarySourceProvider provider : providers) {
				libraryManager.getLibrarySourceLoader().registerProvider(provider);
			}
			return libraryManager;
		};
	}

	@Bean
	public SearchParameterResolver searchParameterResolver(FhirContext fhirContext) {
		return new SearchParameterResolver(fhirContext);
	}

	@Bean
	JpaFhirDalFactory jpaFhirDalFactory(DaoRegistry daoRegistry) {
		return rd -> new JpaFhirDal(daoRegistry, rd);
	}

	@Bean
	JpaDataProviderFactory jpaDataProviderFactory(ModelResolver modelResolver, DaoRegistry daoRegistry,
																 SearchParameterResolver searchParameterResolver) {
		return (rd, t) -> {
			JpaFhirRetrieveProvider provider = new JpaFhirRetrieveProvider(daoRegistry, searchParameterResolver, rd);
			if (t != null) {
				provider.setTerminologyProvider(t);
				provider.setExpandValueSets(true);
				provider.setMaxCodesPerQuery(2048);
				provider.setModelResolver(modelResolver);
			}
			return new CompositeDataProvider(modelResolver, provider);
		};
	}

	@Bean
	DataProviderFactory dataProviderFactory(FhirContext fhirContext, ModelResolver modelResolver) {
		return new DataProviderFactory() {
			@Override
			public DataProviderComponents create(EndpointInfo endpointInfo) {
				// to do implement endpoint
				return null;
			}

			@Override
			public DataProviderComponents create(IBaseBundle dataBundle) {
				return new DataProviderComponents(Constants.FHIR_MODEL_URI, modelResolver,
					new BundleRetrieveProvider(fhirContext, dataBundle));
			}
		};

	}

	@Bean
	public JpaFhirRetrieveProvider jpaFhirRetrieveProvider(DaoRegistry daoRegistry,
																			 SearchParameterResolver searchParameterResolver) {
		return new JpaFhirRetrieveProvider(daoRegistry, searchParameterResolver);
	}

	@SuppressWarnings("unchecked")
	@Bean
	IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType> valueSetDao(DaoRegistry daoRegistry) {
		return (IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType>) daoRegistry
			.getResourceDao("ValueSet");
	}

	@Bean
	public JpaTerminologyProviderFactory jpaTerminologyProviderFactory(ITermReadSvc theTerminologySvc,
																							 IValidationSupport theValidationSupport,
																							 Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> globalCodeCache) {
		return rd -> new JpaTerminologyProvider(theTerminologySvc, theValidationSupport, globalCodeCache,
			rd);
	}

	@Bean
	JpaLibrarySourceProviderFactory jpaLibrarySourceProviderFactory(DaoRegistry daoRegistry) {
		return rd -> new JpaLibrarySourceProvider(daoRegistry, rd);
	}

	@Bean
	LibraryLoaderFactory libraryLoaderFactory(
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> globalLibraryCache,
		ModelManager modelManager, CqlTranslatorOptions cqlTranslatorOptions, CqlProperties cqlProperties) {
		return lcp -> {

			if (cqlProperties.getOptions().useEmbeddedLibraries()) {
				lcp.add(new FhirLibrarySourceProvider());
			}

			return new CacheAwareLibraryLoaderDecorator(
				new TranslatingLibraryLoader(modelManager, lcp, cqlTranslatorOptions), globalLibraryCache) {
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
		IResourceChangeListenerRegistry resourceChangeListenerRegistry, DaoRegistry daoRegistry,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> globalLibraryCache) {
		ElmCacheResourceChangeListener listener = new ElmCacheResourceChangeListener(daoRegistry, globalLibraryCache);
		resourceChangeListenerRegistry.registerResourceResourceChangeListener("Library",
			SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	@Primary
	public CodeCacheResourceChangeListener codeCacheResourceChangeListener(
		IResourceChangeListenerRegistry resourceChangeListenerRegistry, DaoRegistry daoRegistry,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> globalCodeCache) {
		CodeCacheResourceChangeListener listener = new CodeCacheResourceChangeListener(daoRegistry, globalCodeCache);
		resourceChangeListenerRegistry.registerResourceResourceChangeListener("ValueSet",
			SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	public ModelResolver modelResolver(FhirContext fhirContext) {
		switch(fhirContext.getVersion().getVersion()) {
			case R4: return new CachingModelResolverDecorator(new R4FhirModelResolver());
			case DSTU3: return new CachingModelResolverDecorator(new Dstu3FhirModelResolver());
			default: throw new IllegalStateException("CQL support not yet implemented for this FHIR version. Please change versions or disable the CQL plugin.");
		}
	}

	@Bean
	public LibraryVersionSelector libraryVersionSelector(AdapterFactory adapterFactory) {
		return new LibraryVersionSelector(adapterFactory);
	}

	@Bean({ "terminologyService", "myTerminologySvc", "myTermReadSvc" })
	public ITermReadSvc termReadSvc(FhirContext fhirContext) {
		switch(fhirContext.getVersion().getVersion()) {
			case R5: return new PreExpandedTermReadSvcR5();
			case R4: return new PreExpandedTermReadSvcR4();
			case DSTU3: return new PreExpandedTermReadSvcDstu3();
			case DSTU2: return new PreExpandedTermReadSvcDstu2();
			default: throw new IllegalStateException("CQL support not yet implemented for this FHIR version. Please change versions or disable the CQL plugin.");
		}
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
	@Lazy(false)
	public IValidationSupport preExpandedValidationSupport(JpaValidationSupportChain jpaSupportChain,
																			 FhirContext fhirContext) {
		var preExpandedValidationSupport = new PreExpandedValidationSupport(fhirContext);
		jpaSupportChain.addValidationSupport(0, preExpandedValidationSupport);
		return preExpandedValidationSupport;
	}
}
