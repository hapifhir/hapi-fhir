/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.validation.FhirContextValidationSupportSvc;
import ca.uhn.fhir.jpa.validation.ValidatorPolicyAdvisor;
import ca.uhn.fhir.jpa.validation.ValidatorResourceFetcher;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ValidationSupportConfig {

	@Autowired
	private FhirContext myFhirContext;

	@Bean
	public FhirContextValidationSupportSvc fhirValidationSupportSvc() {
		return new FhirContextValidationSupportSvc(myFhirContext);
	}

	@Bean(name = JpaConfig.DEFAULT_PROFILE_VALIDATION_SUPPORT)
	public DefaultProfileValidationSupport defaultProfileValidationSupport() {
		return new DefaultProfileValidationSupport(myFhirContext);
	}

	@Bean
	public InMemoryTerminologyServerValidationSupport inMemoryTerminologyServerValidationSupport(
			FhirContext theFhirContext, JpaStorageSettings theStorageSettings) {
		InMemoryTerminologyServerValidationSupport retVal =
				new InMemoryTerminologyServerValidationSupport(theFhirContext);
		retVal.setIssueSeverityForCodeDisplayMismatch(theStorageSettings.getIssueSeverityForCodeDisplayMismatch());
		return retVal;
	}

	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT)
	public IValidationSupport jpaValidationSupport(FhirContext theFhirContext) {
		return new JpaPersistedResourceValidationSupport(theFhirContext);
	}

	@Bean(name = "myInstanceValidator")
	public IInstanceValidatorModule instanceValidator(
			FhirContext theFhirContext, IValidationSupport theValidationSupportChain, DaoRegistry theDaoRegistry) {
		FhirInstanceValidator val = new FhirInstanceValidator(theValidationSupportChain);
		val.setValidatorResourceFetcher(
				jpaValidatorResourceFetcher(theFhirContext, theValidationSupportChain, theDaoRegistry));
		val.setValidatorPolicyAdvisor(jpaValidatorPolicyAdvisor());
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
		return val;
	}

	@Bean
	@Lazy
	public ValidatorResourceFetcher jpaValidatorResourceFetcher(
			FhirContext theFhirContext, IValidationSupport theValidationSupport, DaoRegistry theDaoRegistry) {
		return new ValidatorResourceFetcher(theFhirContext, theValidationSupport, theDaoRegistry);
	}

	@Bean
	@Lazy
	public ValidatorPolicyAdvisor jpaValidatorPolicyAdvisor() {
		return new ValidatorPolicyAdvisor();
	}
}
