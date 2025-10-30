/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.hapi.fhir.cdshooks.api.CDSHooksVersion;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestJsonDeserializer;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchDaoSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchFhirClientSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsResolutionStrategySvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdsHooksConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(CdsHooksConfig.class);

	public static final String CDS_HOOKS_OBJECT_MAPPER_FACTORY = "cdsHooksObjectMapperFactory";

	public static final String PLAN_DEFINITION_RESOURCE_NAME = "PlanDefinition";

	@Autowired(required = false)
	private DaoRegistry myDaoRegistry;

	@Autowired(required = false)
	private MatchUrlService myMatchUrlService;

	@Autowired(required = false)
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Autowired(required = false)
	private RestfulServer myRestfulServer;

	@Bean(name = CDS_HOOKS_OBJECT_MAPPER_FACTORY)
	public ObjectMapper objectMapper(FhirContext theFhirContext) {
		return new CdsHooksObjectMapperFactory(theFhirContext).newMapper();
	}

	@Bean
	public ICdsServiceRegistry cdsServiceRegistry(
			CdsHooksContextBooter theCdsHooksContextBooter,
			CdsPrefetchSvc theCdsPrefetchSvc,
			@Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper,
			FhirContext theFhirContext,
			@Nullable CDSHooksVersion theCDSHooksVersion) {
		final CdsServiceRequestJsonDeserializer cdsServiceRequestJsonDeserializer =
				new CdsServiceRequestJsonDeserializer(theFhirContext, theObjectMapper);
		return new CdsServiceRegistryImpl(
				theCdsHooksContextBooter,
				theCdsPrefetchSvc,
				theObjectMapper,
				cdsServiceRequestJsonDeserializer,
				CDSHooksVersion.getOrDefault(theCDSHooksVersion));
	}

	@Bean
	public ICdsConfigService cdsConfigService(
			FhirContext theFhirContext, @Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper) {
		return new CdsConfigServiceImpl(theFhirContext, theObjectMapper, myDaoRegistry, myRestfulServer);
	}

	@Bean
	CdsPrefetchSvc cdsPrefetchSvc(
			CdsResolutionStrategySvc theCdsResolutionStrategySvc,
			CdsPrefetchDaoSvc theResourcePrefetchDao,
			CdsPrefetchFhirClientSvc theResourcePrefetchFhirClient,
			ICdsHooksDaoAuthorizationSvc theCdsHooksDaoAuthorizationSvc,
			@Nullable IInterceptorBroadcaster theInterceptorBroadcaster) {
		return new CdsPrefetchSvc(
				theCdsResolutionStrategySvc,
				theResourcePrefetchDao,
				theResourcePrefetchFhirClient,
				theCdsHooksDaoAuthorizationSvc,
				theInterceptorBroadcaster);
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
