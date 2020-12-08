package ca.uhn.fhir.cql.config;

/*-
 * #%L
 * HAPI FHIR - Clinical Quality Language
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.ValueSetResourceProvider;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcDstu3;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.dstu3.evaluation.ProviderFactory;
import org.opencds.cqf.dstu3.providers.HQMFProvider;
import org.opencds.cqf.dstu3.providers.JpaTerminologyProvider;
import org.opencds.cqf.dstu3.providers.LibraryOperationsProvider;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CqlDstu3Config extends BaseCqlConfig {

	@Bean
	TerminologyProvider terminologyProvider(ITermReadSvcDstu3 theITermReadSvc, FhirContext theFhirContext, ValueSetResourceProvider theValueSetResourceProvider, IValidationSupport theValidationSupport) {
		return new JpaTerminologyProvider(theITermReadSvc, theFhirContext, theValueSetResourceProvider, theValidationSupport);
	}

	@Bean
	EvaluationProviderFactory evaluationProviderFactory(FhirContext theFhirContext, DaoRegistry theDaoRegistry, TerminologyProvider theLocalSystemTerminologyProvider) {
		return new ProviderFactory(theFhirContext, theDaoRegistry, theLocalSystemTerminologyProvider);
	}

	@Bean
	NarrativeProvider narrativeProvider() {
		return new NarrativeProvider();
	}

	@Bean
	HQMFProvider theHQMFProvider() {
		return new HQMFProvider();
	}

	@Bean
	LibraryOperationsProvider LibraryOperationsProvider(LibraryResourceProvider theLibraryResourceProvider, NarrativeProvider theNarrativeProvider) {
		return new LibraryOperationsProvider(theLibraryResourceProvider, theNarrativeProvider);
	}
}
