/*-
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.util.ValueSetTestUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Covers ValueSets whose {@code compose} includes other ValueSets ({@code compose.include.valueSet}): what their
 * expansion holds, which codes validate against them, and how their stored expansion follows changes to the
 * ValueSets they include. {@link ValueSetExpansionR4Test} covers {@code $expand} generally.
 */
// Created by Claude Opus 5.5
class ValueSetIncludingValueSetR4Test extends BaseTermR4Test {

	private static final String INNER_VS_URL = "http://example.org/ValueSet/inner";
	private static final String OUTER_VS_URL = "http://example.org/ValueSet/outer";

	private final ValueSetTestUtil myValueSetTestUtil = new ValueSetTestUtil(FhirVersionEnum.R4);

	/**
	 * An include naming both a system and a ValueSet stands for the codes of that system that are also in the
	 * ValueSet.
	 */
	@Test
	void includeWithSystemAndValueSet_holdsOnlyCodesInBoth() {
		myStorageSettings.setPreExpandValueSets(false);

		// Given a CodeSystem with A, B and C, a ValueSet holding A and B, and a ValueSet including the system and that
		// ValueSet in one include
		createCodeSystemWithCodes("A", "B", "C");
		myValueSetDao.create(newValueSetListingCodes(INNER_VS_URL, "A", "B"), mySrd);
		ValueSet outer = newValueSet(OUTER_VS_URL);
		outer.getCompose().addInclude().setSystem(CS_URL).addValueSet(INNER_VS_URL);
		myValueSetDao.create(outer, mySrd);

		// When it is expanded, and C is validated against it
		ValueSet expansion = myTermSvc.expandValueSet(null, outer);
		IValidationSupport.CodeValidationResult validationOfC = validateCode(OUTER_VS_URL, "C");

		// Then it holds only A and B, and C is rejected
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactlyInAnyOrder("A", "B");
		assertThat(validationOfC.isOk()).as(validationOfC.getMessage()).isFalse();
	}

	/**
	 * A stored expansion of a ValueSet holds the codes of the ValueSets it includes, so it must not outlive a change
	 * to one of them.
	 */
	@Test
	void preExpansion_includedValueSetUpdated_outerHoldsTheNewCodes() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a pre-expanded ValueSet including another that holds A
		createCodeSystemWithCodes("A", "B");
		ValueSet inner = newValueSetListingCodes(INNER_VS_URL, "A");
		inner.setId("inner");
		myValueSetDao.update(inner, mySrd);
		ValueSet outer = newValueSet(OUTER_VS_URL);
		outer.getCompose().addInclude().addValueSet(INNER_VS_URL);
		myValueSetDao.create(outer, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();
		assertEquals(
				TermValueSetPreExpansionStatusEnum.EXPANDED,
				runInTransaction(() -> myTermValueSetDao
						.findTermValueSetByUrlAndNullVersion(OUTER_VS_URL)
						.orElseThrow()
						.getExpansionStatus()));

		// When the included ValueSet is changed to hold B instead
		ValueSet updatedInner = newValueSetListingCodes(INNER_VS_URL, "B");
		updatedInner.setId("inner");
		myValueSetDao.update(updatedInner, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then the including ValueSet expands to B, accepts B and rejects A
		ValueSet expansion = myValueSetDao.expandByIdentifier(OUTER_VS_URL, null);
		IValidationSupport.CodeValidationResult validationOfA = validateCode(OUTER_VS_URL, "A");
		IValidationSupport.CodeValidationResult validationOfB = validateCode(OUTER_VS_URL, "B");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("B");
		assertThat(validationOfB.isOk()).as(validationOfB.getMessage()).isTrue();
		assertThat(validationOfA.isOk()).as(validationOfA.getMessage()).isFalse();
	}

	/**
	 * ValueSets that include each other cannot be expanded; the server must say so instead of recursing until the
	 * stack runs out.
	 */
	@Test
	void valueSetsIncludingEachOther_expandAndValidate_failWithAClearError() {
		myStorageSettings.setPreExpandValueSets(false);

		// Given two ValueSets that include each other
		createCodeSystemWithCodes("A");
		ValueSet inner = newValueSetListingCodes(INNER_VS_URL, "A");
		inner.getCompose().addInclude().addValueSet(OUTER_VS_URL);
		myValueSetDao.create(inner, mySrd);
		ValueSet outer = newValueSet(OUTER_VS_URL);
		outer.getCompose().addInclude().addValueSet(INNER_VS_URL);
		myValueSetDao.create(outer, mySrd);

		// When one is expanded, or a code validated against it, then the request fails with an error, not a
		// StackOverflowError
		assertThatThrownBy(() -> myTermSvc.expandValueSet(null, outer))
				.isInstanceOf(BaseServerResponseException.class);
		assertThatThrownBy(() -> validateCode(OUTER_VS_URL, "A")).isInstanceOf(BaseServerResponseException.class);
	}

	private void createCodeSystemWithCodes(String... theCodes) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		for (String code : theCodes) {
			codeSystem.addConcept().setCode(code).setDisplay("Code " + code);
		}
		myCodeSystemDao.create(codeSystem, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private ValueSet newValueSetListingCodes(String theUrl, String... theCodes) {
		ValueSet valueSet = newValueSet(theUrl);
		ValueSet.ConceptSetComponent include = valueSet.getCompose().addInclude().setSystem(CS_URL);
		for (String code : theCodes) {
			include.addConcept().setCode(code);
		}
		return valueSet;
	}

	private ValueSet newValueSet(String theUrl) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(theUrl);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return valueSet;
	}

	private IValidationSupport.CodeValidationResult validateCode(String theValueSetUrl, String theCode) {
		return myValueSetDao.validateCode(
				new CodeType(theValueSetUrl), null, new CodeType(theCode), new CodeType(CS_URL), null, null, null, mySrd);
	}
}
