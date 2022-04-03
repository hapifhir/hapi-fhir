package ca.uhn.fhir.jpa.config.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.GeneratedDaoAndResourceProviderConfigR5;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.config.SharedConfigDstu3Plus;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.dao.r5.TransactionProcessorVersionAdapterR5;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.graphql.GraphQLProviderWithIntrospection;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcR5;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcR5;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR5;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

@Configuration
@EnableTransactionManagement
@Import({
	FhirContextR5Config.class,
	GeneratedDaoAndResourceProviderConfigR5.class,
	SharedConfigDstu3Plus.class,
	JpaConfig.class
})
public class JpaR5Config {
	@Bean
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcR5();
	}


	@Bean
	public ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterR5();
	}

	@Bean(name = JpaConfig.GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider(FhirContext theFhirContext, IGraphQLStorageServices theGraphqlStorageServices, IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry, IDaoRegistry theDaoRegistry) {
        return new GraphQLProviderWithIntrospection(theFhirContext, theValidationSupport, theGraphqlStorageServices, theSearchParamRegistry, theDaoRegistry);
	}

	@Bean(name = "mySystemDaoR5")
	public IFhirSystemDao<Bundle, Meta> systemDaoR5() {
		ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoR5 retVal = new ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoR5();
		return retVal;
	}

	@Bean(name = "mySystemProviderR5")
	public ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5 systemProviderR5(FhirContext theFhirContext) {
		ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5 retVal = new ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5();
		retVal.setContext(theFhirContext);
		retVal.setDao(systemDaoR5());
		return retVal;
	}

	@Bean
	public ITermLoaderSvc terminologyLoaderService(ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		return new TermLoaderSvcImpl(theDeferredStorageSvc, theCodeSystemStorageSvc);
	}

	@Bean
	public ITermReadSvcR5 terminologyService() {
		return new TermReadSvcR5();
	}

}
