package ca.uhn.fhir.jpa.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.BaseConfigDstu3Plus;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcR4;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcR4;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.springframework.beans.factory.annotation.Autowire;
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
public class BaseR4Config extends BaseConfigDstu3Plus {

	@Override
	public FhirContext fhirContext() {
		return fhirContextR4();
	}

	@Bean
	@Override
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcR4();
	}

	@Bean
	@Primary
	public FhirContext fhirContextR4() {
		FhirContext retVal = FhirContext.forR4();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.what");

		return retVal;
	}

	@Bean
	public TransactionProcessor.ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterR4();
	}

	@Bean
	public TransactionProcessor transactionProcessor() {
		return new TransactionProcessor();
	}

	@Bean(name = GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider() {
		return new GraphQLProvider(fhirContextR4(), validationSupportChain(), graphqlStorageServices());
	}

	@Bean(name = "myResourceCountsCache")
	public ResourceCountCache resourceCountsCache() {
		ResourceCountCache retVal = new ResourceCountCache(() -> systemDaoR4().getResourceCounts());
		retVal.setCacheMillis(4 * DateUtils.MILLIS_PER_HOUR);
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IFulltextSearchSvc searchDaoR4() {
		FulltextSearchSvcImpl searchDao = new FulltextSearchSvcImpl();
		return searchDao;
	}

	@Bean(name = "mySystemDaoR4", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<Bundle, Meta> systemDaoR4() {
		ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4 retVal = new ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4();
		return retVal;
	}

	@Bean(name = "mySystemProviderR4")
	public ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4 systemProviderR4() {
		ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4 retVal = new ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4();
		retVal.setContext(fhirContextR4());
		retVal.setDao(systemDaoR4());
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ITermLoaderSvc termLoaderService() {
		return new TermLoaderSvcImpl();
	}

	@Override
	@Bean(autowire = Autowire.BY_TYPE)
	public ITermReadSvcR4 terminologyService() {
		return new TermReadSvcR4();
	}

}
