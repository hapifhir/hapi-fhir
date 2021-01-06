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
import ca.uhn.fhir.cql.common.provider.EvaluationProviderFactory;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.dstu3.evaluation.ProviderFactory;
import ca.uhn.fhir.cql.dstu3.provider.JpaTerminologyProvider;
import ca.uhn.fhir.cql.dstu3.provider.LibraryResolutionProviderImpl;
import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CqlDstu3Config extends BaseCqlConfig {

	@Lazy
	@Bean
	TerminologyProvider terminologyProvider() {
		return new JpaTerminologyProvider();
	}

	@Lazy
	@Bean
	EvaluationProviderFactory evaluationProviderFactory(FhirContext theFhirContext, DaoRegistry theDaoRegistry, TerminologyProvider theLocalSystemTerminologyProvider) {
		return new ProviderFactory(theFhirContext, theDaoRegistry, theLocalSystemTerminologyProvider);
	}

	@Lazy
	@Bean
	LibraryResolutionProvider libraryResolutionProvider() {
		return new LibraryResolutionProviderImpl();
	}

	@Lazy
	@Bean
	NarrativeProvider narrativeProvider() {
		return new NarrativeProvider();
	}

	@Lazy
	@Bean
	public MeasureOperationsProvider measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}
}
