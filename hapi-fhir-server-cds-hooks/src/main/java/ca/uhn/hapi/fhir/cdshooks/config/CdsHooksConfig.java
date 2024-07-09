/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrSettings;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsServiceInterceptor;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrService;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.CdsCrDiscoveryServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICdsCrDiscoveryServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICrDiscoveryService;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICrDiscoveryServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchDaoSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchFhirClientSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsResolutionStrategySvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Configuration
@Import(CdsCrConfig.class)
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
			ICrDiscoveryServiceFactory theCrDiscoveryServiceFactory,
			FhirContext theFhirContext) {
		return new CdsServiceRegistryImpl(
				theCdsHooksContextBooter,
				theCdsPrefetchSvc,
				theObjectMapper,
				theCdsCrServiceFactory,
				theCrDiscoveryServiceFactory,
				theFhirContext);
	}

	@Bean
	public ICdsCrServiceRegistry cdsCrServiceRegistry() {
		return new CdsCrServiceRegistry();
	}

	@Bean
	public ICdsCrServiceFactory cdsCrServiceFactory(
			FhirContext theFhirContext,
			ICdsConfigService theCdsConfigService,
			ICdsCrServiceRegistry theCdsCrServiceRegistry) {
		return id -> {
			if (myRepositoryFactory == null) {
				return null;
			}
			RequestDetails rd =
					theCdsConfigService.createRequestDetails(theFhirContext, id, PLAN_DEFINITION_RESOURCE_NAME);
			Repository repository = myRepositoryFactory.create(rd);
			Optional<Class<? extends ICdsCrService>> clazz =
					theCdsCrServiceRegistry.find(theFhirContext.getVersion().getVersion());
			if (clazz.isEmpty()) {
				return null;
			}
			try {
				Constructor<? extends ICdsCrService> constructor =
						clazz.get().getConstructor(RequestDetails.class, Repository.class, ICdsConfigService.class);
				return constructor.newInstance(rd, repository, theCdsConfigService);
			} catch (NoSuchMethodException
					| InvocationTargetException
					| InstantiationException
					| IllegalAccessException e) {
				ourLog.error("Error encountered attempting to construct the CdsCrService: " + e.getMessage());
				return null;
			}
		};
	}

	@Bean
	public ICdsCrDiscoveryServiceRegistry cdsCrDiscoveryServiceRegistry() {
		return new CdsCrDiscoveryServiceRegistry();
	}

	@Bean
	public ICrDiscoveryServiceFactory crDiscoveryServiceFactory(
			FhirContext theFhirContext,
			ICdsConfigService theCdsConfigService,
			ICdsCrDiscoveryServiceRegistry theCdsCrDiscoveryServiceRegistry) {
		return id -> {
			if (myRepositoryFactory == null) {
				return null;
			}
			RequestDetails rd =
					theCdsConfigService.createRequestDetails(theFhirContext, id, PLAN_DEFINITION_RESOURCE_NAME);
			Repository repository = myRepositoryFactory.create(rd);
			Optional<Class<? extends ICrDiscoveryService>> clazz = theCdsCrDiscoveryServiceRegistry.find(
					theFhirContext.getVersion().getVersion());
			if (clazz.isEmpty()) {
				return null;
			}
			try {
				Constructor<? extends ICrDiscoveryService> constructor =
						clazz.get().getConstructor(IIdType.class, Repository.class);
				return constructor.newInstance(rd.getId(), repository);
			} catch (NoSuchMethodException
					| InvocationTargetException
					| InstantiationException
					| IllegalAccessException e) {
				ourLog.error("Error encountered attempting to construct the CrDiscoveryService: " + e.getMessage());
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
			FhirContext theFhirContext,
			@Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper,
			CdsCrSettings theCdsCrSettings) {
		return new CdsConfigServiceImpl(
				theFhirContext, theObjectMapper, theCdsCrSettings, myDaoRegistry, myRepositoryFactory, myRestfulServer);
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
