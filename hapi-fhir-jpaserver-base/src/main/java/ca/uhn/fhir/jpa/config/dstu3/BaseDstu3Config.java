package ca.uhn.fhir.jpa.config.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.BaseConfigDstu3Plus;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.dstu3.TransactionProcessorVersionAdapterDstu3;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcDstu3;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcDstu3;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcDstu3;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Meta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
public class BaseDstu3Config extends BaseConfigDstu3Plus {

	@Override
	public FhirContext fhirContext() {
		return fhirContextDstu3();
	}

	@Bean
	@Override
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcDstu3();
	}

	@Bean
	@Primary
	public FhirContext fhirContextDstu3() {
		FhirContext retVal = FhirContext.forDstu3();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean(name = GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider() {
		return new GraphQLProvider(fhirContextDstu3(), validationSupportChain(), graphqlStorageServices());
	}

	@Bean
	public TransactionProcessor.ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterDstu3();
	}

	@Bean
	public TransactionProcessor transactionProcessor() {
		return new TransactionProcessor();
	}

	@Bean(name = "myResourceCountsCache")
	public ResourceCountCache resourceCountsCache() {
		ResourceCountCache retVal = new ResourceCountCache(() -> systemDaoDstu3().getResourceCounts());
		retVal.setCacheMillis(4 * DateUtils.MILLIS_PER_HOUR);
		return retVal;
	}

	@Bean
	public IFulltextSearchSvc searchDaoDstu3() {
		return new FulltextSearchSvcImpl();
	}

	@Bean(name = "mySystemDaoDstu3")
	public IFhirSystemDao<Bundle, Meta> systemDaoDstu3() {
		return new ca.uhn.fhir.jpa.dao.dstu3.FhirSystemDaoDstu3();
	}

	@Bean(name = "mySystemProviderDstu3")
	public ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3 systemProviderDstu3() {
		ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3 retVal = new ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3();
		retVal.setContext(fhirContextDstu3());
		retVal.setDao(systemDaoDstu3());
		return retVal;
	}

	@Bean
	public ITermLoaderSvc termLoaderService() {
		return new TermLoaderSvcImpl();
	}

	@Override
	@Bean
	public ITermReadSvcDstu3 terminologyService() {
		return new TermReadSvcDstu3();
	}

}
