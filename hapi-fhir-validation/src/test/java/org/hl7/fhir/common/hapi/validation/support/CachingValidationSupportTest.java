package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidateCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CachingValidationSupport is deprecated and is just a passthrough now. This
 * test verifies that it works that way.
 */
@SuppressWarnings("removal")
@ExtendWith(MockitoExtension.class)
public class CachingValidationSupportTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private static final String CODE_SYSTEM = "http://example.org/fhir/CodeSystem/colour";
	private static final String CODE_SYSTEM_VERSION = "1.0.0";
	private static final String CODE = "vermilion";
	private static final String DISPLAY = "Vermilion";
	private static final String VALUE_SET_URL = "http://example.org/fhir/ValueSet/colour";

	@Mock
	private IValidationSupport myValidationSupport0;

	@Test
	public void testNoCaching() {
		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		when(myValidationSupport0.fetchStructureDefinition(any())).thenAnswer(t->new StructureDefinition());

		CachingValidationSupport support = new CachingValidationSupport(myValidationSupport0);

		IBaseResource actual0 = support.fetchStructureDefinition("http://foo");
		IBaseResource actual1 = support.fetchStructureDefinition("http://foo");
		assertNotSame(actual0, actual1);
	}

	@Test
	public void testEnabledValidationForCodingsLogicalAnd() {
		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		CachingValidationSupport support = new CachingValidationSupport(myValidationSupport0, true);
		assertTrue(support.isCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid());
	}

	/**
	 * CachingValidationSupport and HapiToHl7OrgDstu2ValidatingSupportWrapper both inherit validateCode from
	 * BaseValidationSupportWrapper, so a code system version the wrapper drops is dropped for every chain
	 * they sit in.
	 */
	// Created by Claude Opus 5
	@Test
	public void validateCode_withCodeSystemVersion_passesTheVersionToTheWrappedSupport() {
		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		CachingValidationSupport support = new CachingValidationSupport(myValidationSupport0);

		support.validateCode(
			new ValidationSupportContext(support),
			new ConceptValidationOptions(),
			new ValidateCodeRequest(CODE_SYSTEM, CODE_SYSTEM_VERSION, CODE, DISPLAY, VALUE_SET_URL));

		verify(myValidationSupport0)
			.validateCode(any(), any(), eq(new ValidateCodeRequest(CODE_SYSTEM, CODE_SYSTEM_VERSION, CODE, DISPLAY, VALUE_SET_URL)));
	}

	/**
	 * An implementation which overrides only the older signature is never called if the wrapper funnels
	 * everything through the version-bearing one, and the test above would not notice.
	 */
	// Created by Claude Opus 5
	@Test
	public void validateCode_withoutCodeSystemVersion_callsTheWrappedSupportOnTheSameSignature() {
		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		CachingValidationSupport support = new CachingValidationSupport(myValidationSupport0);

		support.validateCode(
			new ValidationSupportContext(support),
			new ConceptValidationOptions(),
			CODE_SYSTEM,
			CODE,
			DISPLAY,
			VALUE_SET_URL);

		verify(myValidationSupport0).validateCode(any(), any(), eq(CODE_SYSTEM), eq(CODE), eq(DISPLAY), eq(VALUE_SET_URL));
	}
}
