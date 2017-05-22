package ca.uhn.fhir.jpa.config;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.interceptor.RestHookSubscriptionDstu2Interceptor;
import ca.uhn.fhir.jpa.interceptor.RestHookSubscriptionDstu3Interceptor;
import ca.uhn.fhir.jpa.term.HapiTerminologySvcDstu2;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.validation.IValidatorModule;
import org.hl7.fhir.instance.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.instance.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.instance.validation.IResourceValidator.BestPracticeWarningLevel;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class BaseDstu2Config extends BaseConfig {

	private static FhirContext ourFhirContextDstu2;
	private static FhirContext ourFhirContextDstu2Hl7Org;

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu2();
	}

	@Bean(name = "myFhirContextDstu2")
	@Lazy
	public FhirContext fhirContextDstu2() {
		if (ourFhirContextDstu2 == null) {
			ourFhirContextDstu2 = FhirContext.forDstu2();
		}
		return ourFhirContextDstu2;
	}

	@Bean(name = "myFhirContextDstu2Hl7Org")
	@Lazy
	public FhirContext fhirContextDstu2Hl7Org() {
		if (ourFhirContextDstu2Hl7Org == null) {
			ourFhirContextDstu2Hl7Org = FhirContext.forDstu2Hl7Org();
		}
		return ourFhirContextDstu2Hl7Org;
	}

	@Bean(name = "myJpaValidationSupportDstu2", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupportDstu2 jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2();
		return retVal;
	}

	@Bean(name = "myInstanceValidatorDstu2")
	@Lazy
	public IValidatorModule instanceValidatorDstu2() {
		FhirInstanceValidator retVal = new FhirInstanceValidator();
		retVal.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
		retVal.setValidationSupport(new ValidationSupportChain(new DefaultProfileValidationSupport(), jpaValidationSupportDstu2()));
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IFulltextSearchSvc searchDao() {
		FulltextSearchSvcImpl searchDao = new FulltextSearchSvcImpl();
		return searchDao;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorDstu2 searchParamExtractor() {
		return new SearchParamExtractorDstu2();
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu2();
	}

	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<ca.uhn.fhir.model.dstu2.resource.Bundle, MetaDt> systemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 systemProviderDstu2() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2();
		retVal.setDao(systemDaoDstu2());
		retVal.setContext(fhirContextDstu2());
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IHapiTerminologySvc terminologyService() {
		return new HapiTerminologySvcDstu2();
	}

	@Bean
	@Lazy
	public RestHookSubscriptionDstu2Interceptor restHookSubscriptionDstu2Interceptor() {
		return new RestHookSubscriptionDstu2Interceptor();
	}

}
