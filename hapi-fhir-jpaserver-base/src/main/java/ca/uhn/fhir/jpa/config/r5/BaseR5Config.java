package ca.uhn.fhir.jpa.config.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.BaseConfigDstu3Plus;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.r5.TransactionProcessorVersionAdapterR5;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcR5;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcR5;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR5;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Meta;
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
public class BaseR5Config extends BaseConfigDstu3Plus {

	@Override
	public FhirContext fhirContext() {
		return fhirContextR5();
	}

	@Bean
	@Override
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcR5();
	}

	@Bean
	@Primary
	public FhirContext fhirContextR5() {
		FhirContext retVal = FhirContext.forR5();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.what");

		return retVal;
	}

	@Bean
	public TransactionProcessor.ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterR5();
	}

	@Bean
	public TransactionProcessor transactionProcessor() {
		return new TransactionProcessor();
	}

	@Bean(name = GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider() {
		return new GraphQLProvider(fhirContextR5(), validationSupportChain(), graphqlStorageServices());
	}

	@Bean(name = "myResourceCountsCache")
	public ResourceCountCache resourceCountsCache() {
		ResourceCountCache retVal = new ResourceCountCache(() -> systemDaoR5().getResourceCounts());
		retVal.setCacheMillis(4 * DateUtils.MILLIS_PER_HOUR);
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IFulltextSearchSvc searchDaoR5() {
		FulltextSearchSvcImpl searchDao = new FulltextSearchSvcImpl();
		return searchDao;
	}

	@Bean(name = "mySystemDaoR5", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<Bundle, Meta> systemDaoR5() {
		ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoR5 retVal = new ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoR5();
		return retVal;
	}

	@Bean(name = "mySystemProviderR5")
	public ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5 systemProviderR5() {
		ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5 retVal = new ca.uhn.fhir.jpa.provider.r5.JpaSystemProviderR5();
		retVal.setContext(fhirContextR5());
		retVal.setDao(systemDaoR5());
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ITermLoaderSvc terminologyLoaderService() {
		return new TermLoaderSvcImpl();
	}

	@Override
	@Bean(autowire = Autowire.BY_TYPE)
	public ITermReadSvcR5 terminologyService() {
		return new TermReadSvcR5();
	}

}
