package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.JpaConformanceProviderDstu2;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.dstu3.JpaConformanceProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

public class JpaServerDemo extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private WebApplicationContext myAppCtx;

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		setFhirContext(ContextHolder.getCtx());

		// Get the spring context from the web container (it's declared in web.xml)
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		
		/* 
		 * The hapi-fhir-server-resourceproviders-dev.xml file is a spring configuration
		 * file which is automatically generated as a part of hapi-fhir-jpaserver-base and
		 * contains bean definitions for a resource provider for each resource type
		 */
		String resourceProviderBeanName;
		FhirVersionEnum fhirVersion = ContextHolder.getCtx().getVersion().getVersion();
		switch (fhirVersion) {
		case DSTU2:
			resourceProviderBeanName = "myResourceProvidersDstu2";
			break;
		case DSTU3:
			resourceProviderBeanName = "myResourceProvidersDstu3";
			break;
		case R4:
			resourceProviderBeanName = "myResourceProvidersR4";
			break;
		default:
			throw new IllegalStateException(Msg.code(1532));
		}

		ResourceProviderFactory beans = myAppCtx.getBean(resourceProviderBeanName, ResourceProviderFactory.class);
		registerProviders(beans.createProviders());
		
		/* 
		 * The system provider implements non-resource-type methods, such as
		 * transaction, and global history.
		 */
		List<Object> systemProvider = new ArrayList<Object>();
		if (fhirVersion == FhirVersionEnum.DSTU2) {
			systemProvider.add(myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class));
		} else if (fhirVersion == FhirVersionEnum.DSTU3) {
			systemProvider.add(myAppCtx.getBean("mySystemProviderDstu3", JpaSystemProviderDstu3.class));
			systemProvider.add(myAppCtx.getBean(TerminologyUploaderProvider.class));
		} else if (fhirVersion == FhirVersionEnum.R4) {
			systemProvider.add(myAppCtx.getBean("mySystemProviderR4", JpaSystemProviderR4.class));
			systemProvider.add(myAppCtx.getBean(TerminologyUploaderProvider.class));
			systemProvider.add(myAppCtx.getBean(JpaConfig.GRAPHQL_PROVIDER_NAME));
		} else {
			throw new IllegalStateException(Msg.code(1533));
		}
		registerProviders(systemProvider);

		/*
		 * The conformance provider exports the supported resources, search parameters, etc for
		 * this server. The JPA version adds resource counts to the exported statement, so it
		 * is a nice addition.
		 */
		if (fhirVersion == FhirVersionEnum.DSTU2) {
			IFhirSystemDao<Bundle, MetaDt> systemDao = myAppCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(this, systemDao,
					myAppCtx.getBean(DaoConfig.class));
			confProvider.setImplementationDescription("Example Server");
			setServerConformanceProvider(confProvider);
		} else if (fhirVersion == FhirVersionEnum.DSTU3) {
			IFhirSystemDao<org.hl7.fhir.dstu3.model.Bundle, org.hl7.fhir.dstu3.model.Meta> systemDao = myAppCtx
					.getBean("mySystemDaoDstu3", IFhirSystemDao.class);
			JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(this, systemDao,
					myAppCtx.getBean(DaoConfig.class), myAppCtx.getBean(ISearchParamRegistry.class));
			confProvider.setImplementationDescription("Example Server");
			setServerConformanceProvider(confProvider);
		} else if (fhirVersion == FhirVersionEnum.R4) {
			IFhirSystemDao<org.hl7.fhir.r4.model.Bundle, org.hl7.fhir.r4.model.Meta> systemDao = myAppCtx
					.getBean("mySystemDaoR4", IFhirSystemDao.class);
			IValidationSupport validationSupport = myAppCtx.getBean(IValidationSupport.class);
			JpaCapabilityStatementProvider confProvider = new JpaCapabilityStatementProvider(this, systemDao,
					myAppCtx.getBean(DaoConfig.class), myAppCtx.getBean(ISearchParamRegistry.class), validationSupport);
			confProvider.setImplementationDescription("Example Server");
			setServerConformanceProvider(confProvider);
		} else {
			throw new IllegalStateException(Msg.code(1534));
		}

		/*
		 * Enable ETag Support (this is already the default)
		 */
		setETagSupport(ETagSupportEnum.ENABLED);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * Default to XML and pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * This is a simple paging strategy that keeps the last 10 searches in memory
		 */
		setPagingProvider(new FifoMemoryPagingProvider(10));

		// Register a CORS filter
		CorsInterceptor corsInterceptor = new CorsInterceptor();
		registerInterceptor(corsInterceptor);

		DaoConfig daoConfig = myAppCtx.getBean(DaoConfig.class);
		daoConfig.setAllowExternalReferences(ContextHolder.isAllowExternalRefs());
		daoConfig.setEnforceReferentialIntegrityOnDelete(!ContextHolder.isDisableReferentialIntegrity());
		daoConfig.setEnforceReferentialIntegrityOnWrite(!ContextHolder.isDisableReferentialIntegrity());
		daoConfig.setReuseCachedSearchResultsForMillis(ContextHolder.getReuseCachedSearchResultsForMillis());

		DaoRegistry daoRegistry = myAppCtx.getBean(DaoRegistry.class);
		IInterceptorBroadcaster interceptorBroadcaster = myAppCtx.getBean(IInterceptorBroadcaster.class);
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(ctx, daoRegistry, interceptorBroadcaster);
		getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);

		getInterceptorService().registerInterceptor(new ResponseHighlighterInterceptor());

		registerProvider(myAppCtx.getBean(BinaryAccessProvider.class));
	}

}
