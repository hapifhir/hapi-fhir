package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by Claude Opus 5.5
@ExtendWith(MockitoExtension.class)
class ValueSetCodeSystemFinderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String CS_URL = "http://example.com/cs";
	private static final String OTHER_CS_URL = "http://example.com/other-cs";
	private static final String VS_URL = "http://example.com/vs";
	private static final String INCLUDED_VS_URL = "http://example.com/included-vs";

	@Mock
	private IValidationSupport myValidationSupport;

	private ValueSetCodeSystemFinder myFinder;

	@BeforeEach
	void beforeEach() {
		lenient().when(myValidationSupport.getFhirContext()).thenReturn(ourCtx);
		myFinder = new ValueSetCodeSystemFinder(new ValidationSupportContext(myValidationSupport));
	}

	@Test
	void findUnknownSystems_resultNamesUnknownSystems_returnsThem() {
		CodeValidationResult result = notInValueSet();
		result.addUnknownSystem(OTHER_CS_URL);

		assertThat(myFinder.findUnknownSystems(result, VS_URL, CS_URL)).containsExactly(OTHER_CS_URL);
		verify(myValidationSupport, never()).fetchValueSet(any());
	}

	@Test
	void findUnknownSystems_codeInValueSet_returnsEmpty() {
		CodeValidationResult result = new CodeValidationResult().setCode("code0");

		assertThat(myFinder.findUnknownSystems(result, VS_URL, CS_URL)).isEmpty();
		verify(myValidationSupport, never()).fetchValueSet(any());
	}

	@Test
	void findUnknownSystems_codingHasNoSystem_returnsEmpty() {
		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, null)).isEmpty();
		verify(myValidationSupport, never()).fetchValueSet(any());
	}

	@Test
	void findUnknownSystems_includeOfUnsupportedSystem_returnsTheSystem() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(CS_URL);
		givenSupported(CS_URL, null, false);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).containsExactly(CS_URL);
	}

	@Test
	void findUnknownSystems_includeOfSupportedSystem_returnsEmpty() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(CS_URL);
		givenSupported(CS_URL, null, true);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).isEmpty();
	}

	@Test
	void findUnknownSystems_excludeOfUnsupportedSystem_returnsTheSystem() {
		givenValueSet(VS_URL).getCompose().addExclude().setSystem(CS_URL);
		givenSupported(CS_URL, null, false);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).containsExactly(CS_URL);
	}

	@Test
	void findUnknownSystems_includeNamesUnsupportedVersionOfSupportedSystem_returnsTheSystem() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(CS_URL).setVersion("2.0");
		givenSupported(CS_URL, "2.0", false);
		lenient().when(myValidationSupport.isCodeSystemSupported(any(), eq(CS_URL), isNull())).thenReturn(true);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).containsExactly(CS_URL);
	}

	@Test
	void findUnknownSystems_includePacksUnsupportedVersionIntoSystem_returnsTheSystem() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(CS_URL + "|2.0");
		givenSupported(CS_URL, "2.0", false);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).containsExactly(CS_URL);
	}

	@Test
	void findUnknownSystems_unsupportedSystemIsNotTheCodingsSystem_returnsEmpty() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(OTHER_CS_URL);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).isEmpty();
	}

	@Test
	void findUnknownSystems_includedValueSetUsesUnsupportedSystem_returnsTheSystem() {
		givenValueSet(VS_URL).getCompose().addInclude().addValueSet(INCLUDED_VS_URL);
		givenValueSet(INCLUDED_VS_URL).getCompose().addInclude().setSystem(CS_URL);
		givenSupported(CS_URL, null, false);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).containsExactly(CS_URL);
	}

	@Test
	void findUnknownSystems_valueSetsIncludeEachOtherOverSupportedSystem_returnsEmpty() {
		ValueSet outer = givenValueSet(VS_URL);
		outer.getCompose().addInclude().setSystem(CS_URL);
		outer.getCompose().addInclude().addValueSet(INCLUDED_VS_URL);
		givenValueSet(INCLUDED_VS_URL).getCompose().addInclude().addValueSet(VS_URL);
		givenSupported(CS_URL, null, true);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).isEmpty();
	}

	@Test
	void findUnknownSystems_valueSetIncludesItself_returnsEmpty() {
		givenValueSet(VS_URL).getCompose().addInclude().addValueSet(VS_URL);

		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).isEmpty();
	}

	@Test
	void findUnknownSystems_valueSetNotFound_returnsEmpty() {
		assertThat(myFinder.findUnknownSystems(notInValueSet(), VS_URL, CS_URL)).isEmpty();
	}

	@Test
	void usesSystem_includeOrExcludeOfSystem_isTrueInAnyVersionAndNotAsking() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(CS_URL).setVersion("2.0");
		givenValueSet(INCLUDED_VS_URL).getCompose().addExclude().setSystem(CS_URL + "|1.0");

		assertThat(myFinder.usesSystem(VS_URL, CS_URL)).isTrue();
		assertThat(myFinder.usesSystem(INCLUDED_VS_URL, CS_URL)).isTrue();
		verify(myValidationSupport, never()).isCodeSystemSupported(any(), any(), any());
	}

	@Test
	void usesSystem_includedValueSetUsesSystem_isTrue() {
		givenValueSet(VS_URL).getCompose().addInclude().addValueSet(INCLUDED_VS_URL);
		givenValueSet(INCLUDED_VS_URL).getCompose().addInclude().setSystem(CS_URL);

		assertThat(myFinder.usesSystem(VS_URL, CS_URL)).isTrue();
	}

	@Test
	void usesSystem_onlyOtherSystemOrValueSetNotFound_isFalse() {
		givenValueSet(VS_URL).getCompose().addInclude().setSystem(OTHER_CS_URL);

		assertThat(myFinder.usesSystem(VS_URL, CS_URL)).isFalse();
		assertThat(myFinder.usesSystem(INCLUDED_VS_URL, CS_URL)).isFalse();
	}

	private static CodeValidationResult notInValueSet() {
		return new CodeValidationResult().setSeverity(IssueSeverity.ERROR).setMessage("Unknown code");
	}

	private ValueSet givenValueSet(String theUrl) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(theUrl);
		when(myValidationSupport.fetchValueSet(theUrl)).thenReturn(valueSet);
		return valueSet;
	}

	private void givenSupported(String theSystem, String theVersion, boolean theSupported) {
		when(myValidationSupport.isCodeSystemSupported(
						any(), eq(theSystem), theVersion == null ? isNull() : eq(theVersion)))
				.thenReturn(theSupported);
	}
}
