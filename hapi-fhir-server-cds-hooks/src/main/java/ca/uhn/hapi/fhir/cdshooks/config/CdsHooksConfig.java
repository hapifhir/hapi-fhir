package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchDaoSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchFhirClientSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsResolutionStrategySvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdsHooksConfig {

	public static final String CDS_HOOKS_OBJECT_MAPPER_FACTORY = "cdsHooksObjectMapperFactory";

	@Autowired(required = false)
	private DaoRegistry myDaoRegistry;
	@Autowired(required = false)
	private MatchUrlService myMatchUrlService;

	@Bean(name = CDS_HOOKS_OBJECT_MAPPER_FACTORY)
	public ObjectMapper objectMapper(FhirContext theFhirContext) {
		return new CdsHooksObjectMapperFactory(theFhirContext).newMapper();
	}

	@Bean
	public ICdsServiceRegistry cdsServiceRegistry(CdsHooksContextBooter theCdsHooksContextBooter,
																 CdsPrefetchSvc theCdsPrefetchSvc,
																 @Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper) {
		return new CdsServiceRegistryImpl(theCdsHooksContextBooter, theCdsPrefetchSvc, theObjectMapper);
	}

	@Bean
	public ICdsConfigService cdsConfigService(FhirContext theFhirContext,
															@Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper) {
		return new CdsConfigServiceImpl(theFhirContext, theObjectMapper);
	}

	@Bean
	CdsPrefetchSvc cdsPrefetchSvc(CdsResolutionStrategySvc theCdsResolutionStrategySvc,
											CdsPrefetchDaoSvc theResourcePrefetchDao,
											CdsPrefetchFhirClientSvc theResourcePrefetchFhirClient,
											ICdsHooksDaoAuthorizationSvc theCdsHooksDaoAuthorizationSvc) {
		return new CdsPrefetchSvc(theCdsResolutionStrategySvc, theResourcePrefetchDao, theResourcePrefetchFhirClient, theCdsHooksDaoAuthorizationSvc);
	}

	@Bean
	CdsPrefetchDaoSvc resourcePrefetchDao(DaoRegistry theDaoRegistry, FhirContext theFhirContext) {
		return new CdsPrefetchDaoSvc(theDaoRegistry, myMatchUrlService, theFhirContext);
	}

	@Bean
	CdsPrefetchFhirClientSvc resourcePrefetchFhirClient(FhirContext theFhirContext) {
		return new CdsPrefetchFhirClientSvc(theFhirContext);
	}

	@Bean
	CdsResolutionStrategySvc cdsResolutionStrategySvc() {
		return new CdsResolutionStrategySvc(myDaoRegistry);
	}
}
