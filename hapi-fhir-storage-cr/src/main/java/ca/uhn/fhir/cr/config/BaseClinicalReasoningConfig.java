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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
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
import ca.uhn.fhir.cr.common.ILibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.ITerminologyProviderFactory;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.evaluator.builder.DataProviderComponents;
import org.opencds.cqf.cql.evaluator.builder.EndpointInfo;
import org.opencds.cqf.cql.evaluator.cql2elm.util.LibraryVersionSelector;
import org.opencds.cqf.cql.evaluator.engine.model.CachingModelResolverDecorator;
import org.opencds.cqf.cql.evaluator.engine.retrieve.BundleRetrieveProvider;
import org.opencds.cqf.cql.evaluator.fhir.Constants;
import org.opencds.cqf.cql.evaluator.fhir.adapter.AdapterFactory;
import org.opencds.cqf.cql.evaluator.spring.fhir.adapter.AdapterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@Import({AdapterConfiguration.class, BaseRepositoryConfig.class})
public abstract class BaseClinicalReasoningConfig {

	private static final Logger ourLogger = LoggerFactory.getLogger(BaseClinicalReasoningConfig.class);

	@Bean
	@Scope("prototype")
	public ModelManager modelManager(Map<ModelIdentifier, Model> theGlobalModelCache) {
		return new ModelManager(theGlobalModelCache);
	}

	@Bean
	public CqlExceptionHandlingInterceptor cqlExceptionHandlingInterceptor() {
		return new CqlExceptionHandlingInterceptor();
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
	IDataProviderFactory dataProviderFactory(
			ModelResolver theModelResolver,
			DaoRegistry theDaoRegistry,
			SearchParameterResolver theSearchParameterResolver) {
		return (rd, t) -> {
			HapiFhirRetrieveProvider provider =
					new HapiFhirRetrieveProvider(theDaoRegistry, theSearchParameterResolver, rd);
			if (t != null) {
				provider.setTerminologyProvider(t);
				provider.setExpandValueSets(true);
				provider.setMaxCodesPerQuery(500);
				provider.setQueryBatchThreshold(500);
				provider.setModelResolver(theModelResolver);
			}
			return new CompositeDataProvider(theModelResolver, provider);
		};
	}

	@Bean
	org.opencds.cqf.cql.evaluator.builder.DataProviderFactory builderDataProviderFactory(
			FhirContext theFhirContext, ModelResolver theModelResolver) {
		return new org.opencds.cqf.cql.evaluator.builder.DataProviderFactory() {
			@Override
			public DataProviderComponents create(EndpointInfo theEndpointInfo) {
				// to do implement endpoint
				return null;
			}

			@Override
			public DataProviderComponents create(IBaseBundle theDataBundle) {
				return new DataProviderComponents(
						Constants.FHIR_MODEL_URI,
						theModelResolver,
						new BundleRetrieveProvider(theFhirContext, theDataBundle));
			}
		};
	}

	@Bean
	public HapiFhirRetrieveProvider fhirRetrieveProvider(
			DaoRegistry theDaoRegistry, SearchParameterResolver theSearchParameterResolver) {
		return new HapiFhirRetrieveProvider(theDaoRegistry, theSearchParameterResolver);
	}

	@Bean
	public ITerminologyProviderFactory terminologyProviderFactory(
			IValidationSupport theValidationSupport,
			Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		return rd -> new HapiTerminologyProvider(theValidationSupport, theGlobalCodeCache, rd);
	}

	@Bean
	ILibrarySourceProviderFactory librarySourceProviderFactory(DaoRegistry theDaoRegistry) {
		return rd -> new HapiLibrarySourceProvider(theDaoRegistry, rd);
	}

	// TODO: Use something like caffeine caching for this so that growth is limited.
	@Bean
	public Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library>
			globalLibraryCache() {
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
			IResourceChangeListenerRegistry theResourceChangeListenerRegistry,
			DaoRegistry theDaoRegistry,
			Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library>
					theGlobalLibraryCache) {
		ElmCacheResourceChangeListener listener =
				new ElmCacheResourceChangeListener(theDaoRegistry, theGlobalLibraryCache);
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener(
				"Library", SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	@Primary
	public CodeCacheResourceChangeListener codeCacheResourceChangeListener(
			IResourceChangeListenerRegistry theResourceChangeListenerRegistry,
			DaoRegistry theDaoRegistry,
			Map<org.cqframework.cql.elm.execution.VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		CodeCacheResourceChangeListener listener =
				new CodeCacheResourceChangeListener(theDaoRegistry, theGlobalCodeCache);
		theResourceChangeListenerRegistry.registerResourceResourceChangeListener(
				"ValueSet", SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	public ModelResolver modelResolver(FhirContext theFhirContext) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				return new CachingModelResolverDecorator(new R4FhirModelResolver());
			case DSTU3:
				return new CachingModelResolverDecorator(new Dstu3FhirModelResolver());
			default:
				throw new IllegalStateException(
						Msg.code(2224)
								+ "CQL support not yet implemented for this FHIR version. Please change versions or disable the CQL plugin.");
		}
	}

	@Bean
	public LibraryVersionSelector libraryVersionSelector(AdapterFactory theAdapterFactory) {
		return new LibraryVersionSelector(theAdapterFactory);
	}

	@Bean
	public Executor cqlExecutor() {
		CqlForkJoinWorkerThreadFactory factory = new CqlForkJoinWorkerThreadFactory();
		ForkJoinPool myCommonPool =
				new ForkJoinPool(Math.min(32767, Runtime.getRuntime().availableProcessors()), factory, null, false);

		return new DelegatingSecurityContextExecutor(myCommonPool, SecurityContextHolder.getContext());
	}

	@Bean
	public PreExpandedValidationSupportLoader preExpandedValidationSupportLoader(
			ValidationSupportChain theSupportChain, FhirContext theFhirContext) {
		return new PreExpandedValidationSupportLoader(theSupportChain, theFhirContext);
	}
}
