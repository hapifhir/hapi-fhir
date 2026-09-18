package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * The wrapper is what CachingValidationSupport and HapiToHl7OrgDstu2ValidatingSupportWrapper are built on, and
 * neither overrides validateCode, so a version the wrapper drops is dropped for every chain they sit in.
 */
// Created by Claude Opus 5
@ExtendWith(MockitoExtension.class)
public class BaseValidationSupportWrapperTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private static final String CODE_SYSTEM = "http://example.org/fhir/CodeSystem/colour";
	private static final String CODE_SYSTEM_VERSION = "1.0.0";
	private static final String CODE = "vermilion";
	private static final String DISPLAY = "Vermilion";
	private static final String VALUE_SET_URL = "http://example.org/fhir/ValueSet/colour";

	@Mock
	private IValidationSupport myWrapped;

	private BaseValidationSupportWrapper myWrapper;

	@BeforeEach
	public void setUp() {
		myWrapper = new BaseValidationSupportWrapper(ourCtx, myWrapped) {};
	}

	@Test
	public void validateCode_withCodeSystemVersion_passesTheVersionToTheWrappedSupport() {
		// Test
		myWrapper.validateCode(
			new ValidationSupportContext(myWrapper),
			new ConceptValidationOptions(),
			CODE_SYSTEM,
			CODE_SYSTEM_VERSION,
			CODE,
			DISPLAY,
			VALUE_SET_URL);

		// Verify
		verify(myWrapped)
			.validateCode(any(), any(), eq(CODE_SYSTEM), eq(CODE_SYSTEM_VERSION), eq(CODE), eq(DISPLAY), eq(VALUE_SET_URL));
	}

	/**
	 * An implementation which overrides only the older signature is never called if the wrapper funnels
	 * everything through the version-bearing one, and the test above would not notice.
	 */
	@Test
	public void validateCode_withoutCodeSystemVersion_callsTheWrappedSupportOnTheSameSignature() {
		// Test
		myWrapper.validateCode(
			new ValidationSupportContext(myWrapper),
			new ConceptValidationOptions(),
			CODE_SYSTEM,
			CODE,
			DISPLAY,
			VALUE_SET_URL);

		// Verify
		verify(myWrapped).validateCode(any(), any(), eq(CODE_SYSTEM), eq(CODE), eq(DISPLAY), eq(VALUE_SET_URL));
	}
}
