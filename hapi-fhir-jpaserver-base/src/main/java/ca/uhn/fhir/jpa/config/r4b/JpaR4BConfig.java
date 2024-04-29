/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.config.r4b;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.GeneratedDaoAndResourceProviderConfigR4B;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.dao.r4b.TransactionProcessorVersionAdapterR4B;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.graphql.GraphQLProviderWithIntrospection;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcR4B;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Meta;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Import({FhirContextR4BConfig.class, GeneratedDaoAndResourceProviderConfigR4B.class, JpaConfig.class})
public class JpaR4BConfig {

	@Bean
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcR4B();
	}

	@Bean
	public ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterR4B();
	}

	@Bean(name = JpaConfig.GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider(
			FhirContext theFhirContext,
			IGraphQLStorageServices theGraphqlStorageServices,
			IValidationSupport theValidationSupport,
			ISearchParamRegistry theSearchParamRegistry,
			IDaoRegistry theDaoRegistry) {
		return new GraphQLProviderWithIntrospection(
				theFhirContext,
				theValidationSupport,
				theGraphqlStorageServices,
				theSearchParamRegistry,
				theDaoRegistry);
	}

	@Bean(name = "mySystemDaoR4B")
	public IFhirSystemDao<Bundle, Meta> systemDaoR4() {
		ca.uhn.fhir.jpa.dao.r4b.FhirSystemDaoR4B retVal = new ca.uhn.fhir.jpa.dao.r4b.FhirSystemDaoR4B();
		return retVal;
	}

	@Bean(name = "mySystemProviderR4B")
	public JpaSystemProvider<Bundle, Meta> systemProviderR4(FhirContext theFhirContext) {
		JpaSystemProvider<Bundle, Meta> retVal = new JpaSystemProvider<>();
		retVal.setContext(theFhirContext);
		retVal.setDao(systemDaoR4());
		return retVal;
	}

	@Bean
	public ITermLoaderSvc termLoaderService(
			ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		return new TermLoaderSvcImpl(theDeferredStorageSvc, theCodeSystemStorageSvc);
	}
}
