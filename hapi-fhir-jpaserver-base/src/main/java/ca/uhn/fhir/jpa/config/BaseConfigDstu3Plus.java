package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.dao.r4.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.term.TermCodeSystemStorageSvcImpl;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermReindexingSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.CachingValidationSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public abstract class BaseConfigDstu3Plus extends BaseConfig {

	@Bean
	public ITermCodeSystemStorageSvc termCodeSystemStorageSvc() {
		return new TermCodeSystemStorageSvcImpl();
	}

	@Bean
	public ITermDeferredStorageSvc termDeferredStorageSvc() {
		return new TermDeferredStorageSvcImpl();
	}

	@Bean
	public ITermReindexingSvc termReindexingSvc() {
		return new TermReindexingSvcImpl();
	}

	@Bean
	public abstract ITermVersionAdapterSvc terminologyVersionAdapterSvc();

	@Bean(name="myDefaultProfileValidationSupport")
	public IContextValidationSupport defaultProfileValidationSupport() {
		return new DefaultProfileValidationSupport(fhirContext());
	}

	@Bean
	public IContextValidationSupport jpaValidationSupportChain() {
		return new JpaValidationSupportChain(fhirContext());
	}

	@Bean(name = "myJpaValidationSupport")
	public IContextValidationSupport jpaValidationSupport() {
		return new JpaPersistedResourceValidationSupport(fhirContext());
	}

	@Primary
	@Bean(name = "myJpaValidationSupportChain")
	public IContextValidationSupport validationSupportChain() {
		return new CachingValidationSupport(jpaValidationSupportChain());
	}

	@Bean
	public abstract ITermReadSvc terminologyService();
}
