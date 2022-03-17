package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.jpa.validation.ValidatorPolicyAdvisor;
import ca.uhn.fhir.jpa.validation.ValidatorResourceFetcher;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.common.hapi.validation.validator.HapiToHl7OrgDstu2ValidatingSupportWrapper;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ValidationSupportConfig {
	@Bean(name = "myDefaultProfileValidationSupport")
	public DefaultProfileValidationSupport defaultProfileValidationSupport(FhirContext theFhirContext) {
		return new DefaultProfileValidationSupport(theFhirContext);
	}

	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	public JpaValidationSupportChain jpaValidationSupportChain(FhirContext theFhirContext) {
		return new JpaValidationSupportChain(theFhirContext);
	}

	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT)
	public IValidationSupport jpaValidationSupport(FhirContext theFhirContext) {
		return new JpaPersistedResourceValidationSupport(theFhirContext);
	}

	@Bean(name = "myInstanceValidator")
	public IInstanceValidatorModule instanceValidator(FhirContext theFhirContext, CachingValidationSupport theCachingValidationSupport, ValidationSupportChain theValidationSupportChain) {
		if (theFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			FhirInstanceValidator val = new FhirInstanceValidator(theCachingValidationSupport);
			val.setValidatorResourceFetcher(jpaValidatorResourceFetcher());
			val.setValidatorPolicyAdvisor(jpaValidatorPolicyAdvisor());
			val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
			val.setValidationSupport(theCachingValidationSupport);
			return val;
		} else {
			CachingValidationSupport cachingValidationSupport = new CachingValidationSupport(new HapiToHl7OrgDstu2ValidatingSupportWrapper(theValidationSupportChain));
			FhirInstanceValidator retVal = new FhirInstanceValidator(cachingValidationSupport);
			retVal.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
			return retVal;
		}
	}

	@Bean
	@Lazy
	public ValidatorResourceFetcher jpaValidatorResourceFetcher() {
		return new ValidatorResourceFetcher();
	}

	@Bean
	@Lazy
	public ValidatorPolicyAdvisor jpaValidatorPolicyAdvisor() {
		return new ValidatorPolicyAdvisor();
	}

}
