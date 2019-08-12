package ca.uhn.fhir.jpa.config.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.r5.TransactionProcessorVersionAdapterR5;
import ca.uhn.fhir.jpa.provider.GraphQLProvider;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryR5;
import ca.uhn.fhir.jpa.term.HapiTerminologySvcR5;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvcR5;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainR5;
import ca.uhn.fhir.validation.IValidatorModule;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r5.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r5.hapi.validation.CachingValidationSupport;
import org.hl7.fhir.r5.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.utils.IResourceValidator;
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
public class BaseR5Config extends BaseConfig {

	@Override
	public FhirContext fhirContext() {
		return fhirContextR5();
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
	public TransactionProcessor<Bundle, Bundle.BundleEntryComponent> transactionProcessor() {
		return new TransactionProcessor<>();
	}

	@Bean(name = GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider() {
		return new GraphQLProvider(fhirContextR5(), validationSupportChainR5(), graphqlStorageServices());
	}

	@Bean(name = "myInstanceValidatorR5")
	@Lazy
	public IValidatorModule instanceValidatorR5() {
		FhirInstanceValidator val = new FhirInstanceValidator();
		IResourceValidator.BestPracticeWarningLevel level = IResourceValidator.BestPracticeWarningLevel.Warning;
		val.setBestPracticeWarningLevel(level);
		val.setValidationSupport(validationSupportChainR5());
		return val;
	}

	@Bean
	public JpaValidationSupportChainR5 jpaValidationSupportChain() {
		return new JpaValidationSupportChainR5();
	}

	@Bean(name = "myJpaValidationSupportR5", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.r5.IJpaValidationSupportR5 jpaValidationSupportR5() {
		ca.uhn.fhir.jpa.dao.r5.JpaValidationSupportR5 retVal = new ca.uhn.fhir.jpa.dao.r5.JpaValidationSupportR5();
		return retVal;
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

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorR5 searchParamExtractor() {
		return new SearchParamExtractorR5();
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryR5();
	}

	@Bean(name = "mySystemDaoR5", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<Bundle, org.hl7.fhir.r5.model.Meta> systemDaoR5() {
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
	public IHapiTerminologyLoaderSvc terminologyLoaderService() {
		return new TerminologyLoaderSvcImpl();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IHapiTerminologySvcR5 terminologyService() {
		return new HapiTerminologySvcR5();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainR5")
	public IValidationSupport validationSupportChainR5() {
		return new CachingValidationSupport(jpaValidationSupportChain());
	}

}
