package ca.uhn.fhir.jpa.config.dstu3;

import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.utils.IResourceValidator.BestPracticeWarningLevel;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.dstu3.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.dao.dstu3.SearchParamRegistryDstu3;
import ca.uhn.fhir.jpa.interceptor.RestHookSubscriptionDstu3Interceptor;
import ca.uhn.fhir.jpa.provider.dstu3.TerminologyUploaderProviderDstu3;
import ca.uhn.fhir.jpa.term.HapiTerminologySvcDstu3;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvcDstu3;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvc;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChainDstu3;
import ca.uhn.fhir.validation.IValidatorModule;

@Configuration
@EnableTransactionManagement
public class BaseDstu3Config extends BaseConfig {

	@Bean
	@Primary
	public FhirContext fhirContextDstu3() {
		FhirContext retVal = FhirContext.forDstu3();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean(name = "myInstanceValidatorDstu3")
	@Lazy
	public IValidatorModule instanceValidatorDstu3() {
		FhirInstanceValidator val = new FhirInstanceValidator();
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
		val.setValidationSupport(validationSupportChainDstu3());
		return val;
	}

	@Bean(name = "myJpaValidationSupportDstu3", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.dstu3.IJpaValidationSupportDstu3 jpaValidationSupportDstu3() {
		ca.uhn.fhir.jpa.dao.dstu3.JpaValidationSupportDstu3 retVal = new ca.uhn.fhir.jpa.dao.dstu3.JpaValidationSupportDstu3();
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IFulltextSearchSvc searchDaoDstu3() {
		FulltextSearchSvcImpl searchDao = new FulltextSearchSvcImpl();
		return searchDao;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorDstu3 searchParamExtractor() {
		return new SearchParamExtractorDstu3();
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu3();
	}

	@Bean(name = "mySystemDaoDstu3", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<org.hl7.fhir.dstu3.model.Bundle, org.hl7.fhir.dstu3.model.Meta> systemDaoDstu3() {
		ca.uhn.fhir.jpa.dao.dstu3.FhirSystemDaoDstu3 retVal = new ca.uhn.fhir.jpa.dao.dstu3.FhirSystemDaoDstu3();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu3")
	public ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3 systemProviderDstu3() {
		ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3 retVal = new ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3();
		retVal.setContext(fhirContextDstu3());
		retVal.setDao(systemDaoDstu3());
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IHapiTerminologyLoaderSvc terminologyLoaderService() {
		return new TerminologyLoaderSvc();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IHapiTerminologySvcDstu3 terminologyService() {
		return new HapiTerminologySvcDstu3();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public TerminologyUploaderProviderDstu3 terminologyUploaderProvider() {
		TerminologyUploaderProviderDstu3 retVal = new TerminologyUploaderProviderDstu3();
		retVal.setContext(fhirContextDstu3());
		return retVal;
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainDstu3")
	public IValidationSupport validationSupportChainDstu3() {
		return new JpaValidationSupportChainDstu3();
	}
	
	@Bean
	@Lazy
	public RestHookSubscriptionDstu3Interceptor restHookSubscriptionDstu3Interceptor() {
		return new RestHookSubscriptionDstu3Interceptor();
	}

}
