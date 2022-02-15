package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.dao.TransactionProcessorVersionAdapterDstu2;
import ca.uhn.fhir.jpa.term.TermReadSvcDstu2;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.common.hapi.validation.validator.HapiToHl7OrgDstu2ValidatingSupportWrapper;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
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
	FhirContextDstu2Config.class,
	ResourceProviderConfigDstu2.class,
	JpaConfig.class
})
public class JpaDstu2Config {

	@Bean(name = "myInstanceValidator")
	@Lazy
	public IInstanceValidatorModule instanceValidator(ValidationSupportChain theValidationSupportChain) {
		CachingValidationSupport cachingValidationSupport = new CachingValidationSupport(new HapiToHl7OrgDstu2ValidatingSupportWrapper(theValidationSupportChain));
		FhirInstanceValidator retVal = new FhirInstanceValidator(cachingValidationSupport);
		retVal.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
		return retVal;
	}

	@Bean
	public ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterDstu2();
	}


	@Bean(name = "myDefaultProfileValidationSupport")
	public DefaultProfileValidationSupport defaultProfileValidationSupport(FhirContext theFhirContext) {
		return new DefaultProfileValidationSupport(theFhirContext);
	}

	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	public ValidationSupportChain validationSupportChain(DefaultProfileValidationSupport theDefaultProfileValidationSupport, FhirContext theFhirContext) {
		InMemoryTerminologyServerValidationSupport inMemoryTerminologyServer = new InMemoryTerminologyServerValidationSupport(theFhirContext);
		IValidationSupport jpaValidationSupport = jpaValidationSupportDstu2(theFhirContext);
		CommonCodeSystemsTerminologyService commonCodeSystemsTermSvc = new CommonCodeSystemsTerminologyService(theFhirContext);
		return new ValidationSupportChain(theDefaultProfileValidationSupport, jpaValidationSupport, inMemoryTerminologyServer, commonCodeSystemsTermSvc);
	}

	@Primary
	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT)
	public IValidationSupport jpaValidationSupportDstu2(FhirContext theFhirContext) {
		return new JpaPersistedResourceValidationSupport(theFhirContext);
	}

	@Bean(name = "mySystemDaoDstu2")
	public IFhirSystemDao<Bundle, MetaDt> systemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 systemProviderDstu2(FhirContext theFhirContext) {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2();
		retVal.setDao(systemDaoDstu2());
		retVal.setContext(theFhirContext);
		return retVal;
	}

	@Bean
	public ITermReadSvc terminologyService() {
		return new TermReadSvcDstu2();
	}

}
