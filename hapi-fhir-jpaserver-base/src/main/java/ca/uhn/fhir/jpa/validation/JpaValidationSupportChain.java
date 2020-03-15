package ca.uhn.fhir.jpa.validation;

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
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class JpaValidationSupportChain extends ValidationSupportChain {

	private final FhirContext myFhirContext;

	@Autowired
	@Qualifier("myJpaValidationSupport")
	public IValidationSupport myJpaValidationSupport;

	@Qualifier("myDefaultProfileValidationSupport")
	@Autowired
	private IValidationSupport myDefaultProfileValidationSupport;
	@Autowired
	private ITermReadSvc myTerminologyService;

	public JpaValidationSupportChain(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@PreDestroy
	public void flush() {
		invalidateCaches();
	}

	@PostConstruct
	public void postConstruct() {
		addValidationSupport((IValidationSupport) new CommonCodeSystemsTerminologyService(myFhirContext));
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupport);
		addValidationSupport((IValidationSupport) myTerminologyService);
		addValidationSupport((IValidationSupport) new SnapshotGeneratingValidationSupport(myFhirContext));
		addValidationSupport((IValidationSupport) new InMemoryTerminologyServerValidationSupport(myFhirContext));
	}

}
