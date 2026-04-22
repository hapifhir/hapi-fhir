/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-sonnet-4-5-20250929
package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.packages.NpmJpaValidationSupport;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.WorkerContextValidationSupportAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for JpaValidationSupportChain to verify that validators are not added multiple times
 * when the chain is shared between application contexts (GL-8107/SMILE-11306).
 */
@ExtendWith(MockitoExtension.class)
class JpaValidationSupportChainTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Mock(strictness = Mock.Strictness.LENIENT)
	private IValidationSupport myJpaValidationSupport;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private IValidationSupport myDefaultProfileValidationSupport;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private ITermReadSvc myTerminologyService;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private NpmJpaValidationSupport myNpmJpaValidationSupport;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private ITermConceptMappingSvc myConceptMappingSvc;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	@Spy
	private JpaStorageSettings myJpaStorageSettings = new JpaStorageSettings();

	private JpaValidationSupportChain myChain;
	private WorkerContextValidationSupportAdapter myWorkerContextValidationSupportAdapter;

	@BeforeEach
	void setUp() {
		myWorkerContextValidationSupportAdapter = new WorkerContextValidationSupportAdapter();

		// Create chain with constructor parameters that can't be injected
		myChain = new JpaValidationSupportChain(
			ourFhirContext,
			ValidationSupportChain.CacheConfiguration.defaultValues(),
			myWorkerContextValidationSupportAdapter
		);

		// Configure mocks to return FhirContext (required by ValidationSupportChain)
		when(myJpaValidationSupport.getFhirContext()).thenReturn(ourFhirContext);
		when(myDefaultProfileValidationSupport.getFhirContext()).thenReturn(ourFhirContext);
		when(myTerminologyService.getFhirContext()).thenReturn(ourFhirContext);
		when(myNpmJpaValidationSupport.getFhirContext()).thenReturn(ourFhirContext);
		when(myConceptMappingSvc.getFhirContext()).thenReturn(ourFhirContext);
		when(myInMemoryTerminologyServerValidationSupport.getFhirContext()).thenReturn(ourFhirContext);

		// Inject the @Mock and @Spy fields into myChain
		ReflectionTestUtils.setField(myChain, "myJpaValidationSupport", myJpaValidationSupport);
		ReflectionTestUtils.setField(myChain, "myDefaultProfileValidationSupport", myDefaultProfileValidationSupport);
		ReflectionTestUtils.setField(myChain, "myTerminologyService", myTerminologyService);
		ReflectionTestUtils.setField(myChain, "myNpmJpaValidationSupport", myNpmJpaValidationSupport);
		ReflectionTestUtils.setField(myChain, "myConceptMappingSvc", myConceptMappingSvc);
		ReflectionTestUtils.setField(myChain, "myInMemoryTerminologyServerValidationSupport", myInMemoryTerminologyServerValidationSupport);
		ReflectionTestUtils.setField(myChain, "myJpaStorageSettings", myJpaStorageSettings);
	}

	@Test
	void testPostConstruct_firstCall_addsValidators() {
		// Given: Empty chain
		assertThat(getChainSize()).isEqualTo(0);

		// When: postConstruct is called for the first time
		myChain.postConstruct();

		// Then: Validators are added to the chain
		assertThat(getChainSize()).isGreaterThan(0);
	}

	@Test
	void testPostConstruct_secondCall_doesNotAddDuplicates() {
		// Given: Chain has been initialized once
		myChain.postConstruct();
		int initialSize = getChainSize();

		// When: postConstruct is called again (simulating child context using parent's chain)
		myChain.postConstruct();
		int afterSecondCall = getChainSize();

		// Then: No additional validators are added
		assertThat(afterSecondCall).isEqualTo(initialSize);
	}

	/**
	 * Helper method to get the number of validators in the chain using reflection.
	 * This accesses the private myChain field from ValidationSupportChain parent class.
	 */
	private int getChainSize() {
		@SuppressWarnings("unchecked")
		java.util.List<IValidationSupport> chainList =
			(java.util.List<IValidationSupport>) ReflectionTestUtils.getField(myChain, "myChain");
		return chainList != null ? chainList.size() : 0;
	}
}
