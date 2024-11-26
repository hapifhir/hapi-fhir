package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
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

}
