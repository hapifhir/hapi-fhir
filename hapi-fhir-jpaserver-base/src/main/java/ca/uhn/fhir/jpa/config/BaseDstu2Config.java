package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu2;
import ca.uhn.fhir.jpa.term.TermReadSvcDstu2;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.common.hapi.validation.validator.HapiToHl7OrgDstu2ValidatingSupportWrapper;
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
public class BaseDstu2Config extends BaseConfig {

	private static FhirContext ourFhirContextDstu2;
	private static FhirContext ourFhirContextDstu2Hl7Org;

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu2();
	}

	@Override
	public FhirContext fhirContext() {
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

	@Bean(name = "myInstanceValidator")
	@Lazy
	public IInstanceValidatorModule instanceValidator() {
		ValidationSupportChain validationSupportChain = validationSupportChain();
		CachingValidationSupport cachingValidationSupport = new CachingValidationSupport(new HapiToHl7OrgDstu2ValidatingSupportWrapper(validationSupportChain));
		FhirInstanceValidator retVal = new FhirInstanceValidator(cachingValidationSupport);
		retVal.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Warning);
		return retVal;
	}

	@Bean(name = JPA_VALIDATION_SUPPORT_CHAIN)
	public ValidationSupportChain validationSupportChain() {
		DefaultProfileValidationSupport defaultProfileValidationSupport = new DefaultProfileValidationSupport(fhirContext());
		InMemoryTerminologyServerValidationSupport inMemoryTerminologyServer = new InMemoryTerminologyServerValidationSupport(fhirContextDstu2());
		IValidationSupport jpaValidationSupport = jpaValidationSupportDstu2();
		CommonCodeSystemsTerminologyService commonCodeSystemsTermSvc = new CommonCodeSystemsTerminologyService(fhirContext());
		return new ValidationSupportChain(defaultProfileValidationSupport, jpaValidationSupport, inMemoryTerminologyServer, commonCodeSystemsTermSvc);
	}

	@Primary
	@Bean
	public IValidationSupport jpaValidationSupportDstu2() {
		JpaPersistedResourceValidationSupport retVal = new JpaPersistedResourceValidationSupport(fhirContextDstu2());
		return retVal;
	}

	@Bean(name = "myResourceCountsCache")
	public ResourceCountCache resourceCountsCache() {
		ResourceCountCache retVal = new ResourceCountCache(() -> systemDaoDstu2().getResourceCounts());
		retVal.setCacheMillis(4 * DateUtils.MILLIS_PER_HOUR);
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IFulltextSearchSvc searchDao() {
		FulltextSearchSvcImpl searchDao = new FulltextSearchSvcImpl();
		return searchDao;
	}

	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<Bundle, MetaDt> systemDaoDstu2() {
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
	public ITermReadSvc terminologyService() {
		return new TermReadSvcDstu2();
	}

}
