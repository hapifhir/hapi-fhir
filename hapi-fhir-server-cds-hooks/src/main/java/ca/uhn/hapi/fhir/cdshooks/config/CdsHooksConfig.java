/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceDstu3;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceR4;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceR5;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsServiceInterceptor;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.CrDiscoveryServiceDstu3;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.CrDiscoveryServiceR4;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.CrDiscoveryServiceR5;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICrDiscoveryServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchDaoSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchFhirClientSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsResolutionStrategySvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.Ids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdsHooksConfig {

	public static final String CDS_HOOKS_OBJECT_MAPPER_FACTORY = "cdsHooksObjectMapperFactory";

	public static final String PLAN_DEFINITION_RESOURCE_NAME = "PlanDefinition";

	@Autowired(required = false)
	private DaoRegistry myDaoRegistry;

	@Autowired(required = false)
	private MatchUrlService myMatchUrlService;

	@Autowired(required = false)
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Autowired(required = false)
	private IRepositoryFactory myRepositoryFactory;

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
			ICdsCrServiceFactory theCdsCrServiceFactory,
			ICrDiscoveryServiceFactory theCrDiscoveryServiceFactory) {
		return new CdsServiceRegistryImpl(
				theCdsHooksContextBooter,
				theCdsPrefetchSvc,
				theObjectMapper,
				theCdsCrServiceFactory,
				theCrDiscoveryServiceFactory);
	}

	private RequestDetails createRequestDetails(FhirContext theFhirContext, String theId) {
		SystemRequestDetails rd = new SystemRequestDetails();
		rd.setServer(myRestfulServer);
		rd.setResponse(new SystemRestfulResponse(rd));
		rd.setId(Ids.newId(theFhirContext.getVersion().getVersion(), PLAN_DEFINITION_RESOURCE_NAME, theId));
		return rd;
	}

	@Bean
	public ICdsCrServiceFactory cdsCrServiceFactory(FhirContext theFhirContext) {
		return id -> {
			if (myRepositoryFactory == null) {
				return null;
			}
			RequestDetails rd = createRequestDetails(theFhirContext, id);
			Repository repository = myRepositoryFactory.create(rd);
			switch (theFhirContext.getVersion().getVersion()) {
				case DSTU3:
					return new CdsCrServiceDstu3(rd, repository);
				case R4:
					return new CdsCrServiceR4(rd, repository);
				case R5:
					return new CdsCrServiceR5(rd, repository);
				default:
					return null;
			}
		};
	}

	@Bean
	public ICrDiscoveryServiceFactory crDiscoveryServiceFactory(FhirContext theFhirContext) {
		return id -> {
			if (myRepositoryFactory == null) {
				return null;
			}
			RequestDetails rd = createRequestDetails(theFhirContext, id);
			Repository repository = myRepositoryFactory.create(rd);
			switch (theFhirContext.getVersion().getVersion()) {
				case DSTU3:
					return new CrDiscoveryServiceDstu3(rd.getId(), repository);
				case R4:
					return new CrDiscoveryServiceR4(rd.getId(), repository);
				case R5:
					return new CrDiscoveryServiceR5(rd.getId(), repository);
				default:
					return null;
			}
		};
	}

	@Bean
	public CdsServiceInterceptor cdsServiceInterceptor() {
		if (myResourceChangeListenerRegistry == null) {
			return null;
		}
		CdsServiceInterceptor listener = new CdsServiceInterceptor();
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(
				PLAN_DEFINITION_RESOURCE_NAME, SearchParameterMap.newSynchronous(), listener, 1000);
		return listener;
	}

	@Bean
	public ICdsConfigService cdsConfigService(
			FhirContext theFhirContext, @Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper) {
		return new CdsConfigServiceImpl(
				theFhirContext, theObjectMapper, myDaoRegistry, myRepositoryFactory, myRestfulServer);
	}

	@Bean
	CdsPrefetchSvc cdsPrefetchSvc(
			CdsResolutionStrategySvc theCdsResolutionStrategySvc,
			CdsPrefetchDaoSvc theResourcePrefetchDao,
			CdsPrefetchFhirClientSvc theResourcePrefetchFhirClient,
			ICdsHooksDaoAuthorizationSvc theCdsHooksDaoAuthorizationSvc) {
		return new CdsPrefetchSvc(
				theCdsResolutionStrategySvc,
				theResourcePrefetchDao,
				theResourcePrefetchFhirClient,
				theCdsHooksDaoAuthorizationSvc);
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
