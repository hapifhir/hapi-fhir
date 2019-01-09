package ca.uhn.fhir.jpa.subscription.module.config;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

// From BaseDstu3Config
public class SubscriptionDstu3Config extends BaseSubscriptionConfig {
	@Override
	public FhirContext fhirContext() {
		return fhirContextDstu3();
	}

	@Bean
	@Primary
	public FhirContext fhirContextDstu3() {
		FhirContext retVal = FhirContext.forDstu3();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu3();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorDstu3 searchParamExtractor() {
		return new SearchParamExtractorDstu3();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainDstu3")
	public IValidationSupport validationSupportChainDstu3() {
		return new DefaultProfileValidationSupport();
	}
}
