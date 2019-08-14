package ca.uhn.fhir.jpa.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.graphql.JpaStorageServices;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryR4;
import ca.uhn.fhir.jpa.term.HapiTerminologySvcR4;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvcR4;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainR4;
import ca.uhn.fhir.validation.IValidatorModule;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import org.hl7.fhir.r4.hapi.validation.CachingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.utils.GraphQLEngine;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
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
 * Copyright (C) 2014 - 2019 University Health Network
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
public class BaseR4Config extends BaseConfig {

	@Override
	public FhirContext fhirContext() {
		return fhirContextR4();
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
	public TransactionProcessor<Bundle, Bundle.BundleEntryComponent> transactionProcessor() {
		return new TransactionProcessor<>();
	}

	@Bean(name = GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider() {
		return new GraphQLProvider(fhirContextR4(), validationSupportChainR4(), graphqlStorageServices());
	}

	@Bean(name = "myInstanceValidatorR4")
	@Lazy
	public IValidatorModule instanceValidatorR4() {
		FhirInstanceValidator val = new FhirInstanceValidator();
		IResourceValidator.BestPracticeWarningLevel level = IResourceValidator.BestPracticeWarningLevel.Warning;
		val.setBestPracticeWarningLevel(level);
		val.setValidationSupport(validationSupportChainR4());
		return val;
	}

	@Bean
	public JpaValidationSupportChainR4 jpaValidationSupportChain() {
		return new JpaValidationSupportChainR4();
	}

	@Bean(name = "myJpaValidationSupportR4", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.r4.IJpaValidationSupportR4 jpaValidationSupportR4() {
		ca.uhn.fhir.jpa.dao.r4.JpaValidationSupportR4 retVal = new ca.uhn.fhir.jpa.dao.r4.JpaValidationSupportR4();
		return retVal;
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

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorR4 searchParamExtractor() {
		return new SearchParamExtractorR4();
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryR4();
	}

	@Bean(name = "mySystemDaoR4", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<org.hl7.fhir.r4.model.Bundle, org.hl7.fhir.r4.model.Meta> systemDaoR4() {
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
	public IHapiTerminologyLoaderSvc terminologyLoaderService() {
		return new TerminologyLoaderSvcImpl();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IHapiTerminologySvcR4 terminologyService() {
		return new HapiTerminologySvcR4();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainR4")
	public IValidationSupport validationSupportChainR4() {
		return new CachingValidationSupport(jpaValidationSupportChain());
	}

}
