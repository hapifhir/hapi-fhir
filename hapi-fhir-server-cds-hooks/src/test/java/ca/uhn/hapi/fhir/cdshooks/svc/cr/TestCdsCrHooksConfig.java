package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.config.BaseRepositoryConfig;
import ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.config.TestCdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.controller.TestServerAppCtx;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import org.cqframework.cql.cql2elm.model.Model;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class, SearchParamConfig.class})
public class TestCdsCrHooksConfig {
	@Bean
	ResourceChangeListenerCacheFactory resourceChangeListenerCacheFactory() {
		return new ResourceChangeListenerCacheFactory();
	}
	@Bean
	@Scope("prototype")
	ResourceChangeListenerCache resourceChangeListenerCache(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		return new ResourceChangeListenerCache(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}

	@Bean
	public Map<ModelIdentifier, Model> globalModelCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library>
	globalLibraryCache() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	EvaluationSettings evaluationSettings(
		Map<ModelIdentifier, Model> theGlobalModelCache,
		Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library>
			theGlobalLibraryCache) {
		var evaluationSettings = new EvaluationSettings();
		evaluationSettings.setCqlOptions(new CqlOptions());
		evaluationSettings.setModelCache(theGlobalModelCache);
		evaluationSettings.setLibraryCache(theGlobalLibraryCache);

		return evaluationSettings;
	}

	@Bean
	IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(EvaluationSettings theEvaluationSettings) {
		return r -> new PlanDefinitionProcessor(r, theEvaluationSettings);
	}

	@Bean
	public PlanDefinitionOperationsProvider r4PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

}
