package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("removal")
@ExtendWith(MockitoExtension.class)
public class CachingValidationSupportTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Mock
	private IValidationSupport myValidationSupport0;
	@Mock
	private IValidationSupport myValidationSupport1;

	@ParameterizedTest
	@NullSource
	@ValueSource(booleans = {true, false})
	public void testAsyncBackgroundLoading(Boolean theIsEnabledValidationForCodingsLogicalAnd) {
		StructureDefinition sd0 = (StructureDefinition) new StructureDefinition().setId("SD0");
		StructureDefinition sd1 = (StructureDefinition) new StructureDefinition().setId("SD1");
		StructureDefinition sd2 = (StructureDefinition) new StructureDefinition().setId("SD2");
		List<StructureDefinition> responses = Collections.synchronizedList(Lists.newArrayList(
			sd0, sd1, sd2
		));

		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		when(myValidationSupport0.fetchAllNonBaseStructureDefinitions()).thenAnswer(t -> {
			Thread.sleep(2000);
			return Collections.singletonList(responses.remove(0));
		});

		final CachingValidationSupport.CacheTimeouts cacheTimeouts = CachingValidationSupport.CacheTimeouts
			.defaultValues()
			.setMiscMillis(1000);
		final CachingValidationSupport support = getSupport(cacheTimeouts, theIsEnabledValidationForCodingsLogicalAnd);

		assertThat(responses).hasSize(3);
		List<IBaseResource> fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertThat(fetched.get(0)).isSameAs(sd0);
		assertThat(responses).hasSize(2);

		sleepAtLeast(1200);
		fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertThat(fetched.get(0)).isSameAs(sd0);
		assertThat(responses).hasSize(2);

		await().until(() -> responses.size() == 1);
		assertThat(responses).hasSize(1);
		fetched = support.fetchAllNonBaseStructureDefinitions();
		assert fetched != null;
		assertThat(fetched.get(0)).isSameAs(sd1);
		assertThat(responses).hasSize(1);

		assertEquals(theIsEnabledValidationForCodingsLogicalAnd != null && theIsEnabledValidationForCodingsLogicalAnd, support.isEnabledValidationForCodingsLogicalAnd());
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(booleans = {true, false})
	public void fetchBinary_normally_accessesSuperOnlyOnce(Boolean theIsEnabledValidationForCodingsLogicalAnd) {
		final byte[] EXPECTED_BINARY = "dummyBinaryContent".getBytes();
		final String EXPECTED_BINARY_KEY = "dummyBinaryKey";
		when(myValidationSupport0.getFhirContext()).thenReturn(ourCtx);
		when(myValidationSupport0.fetchBinary(EXPECTED_BINARY_KEY)).thenReturn(EXPECTED_BINARY);

		final CachingValidationSupport support = getSupport(null, theIsEnabledValidationForCodingsLogicalAnd);

		final byte[] firstActualBinary = support.fetchBinary(EXPECTED_BINARY_KEY);
		assertEquals(EXPECTED_BINARY, firstActualBinary);
		verify(myValidationSupport0, times(1)).fetchBinary(EXPECTED_BINARY_KEY);

		final byte[] secondActualBinary = support.fetchBinary(EXPECTED_BINARY_KEY);
		assertEquals(EXPECTED_BINARY, secondActualBinary);
		verify(myValidationSupport0, times(1)).fetchBinary(EXPECTED_BINARY_KEY);

		assertEquals(theIsEnabledValidationForCodingsLogicalAnd != null && theIsEnabledValidationForCodingsLogicalAnd, support.isEnabledValidationForCodingsLogicalAnd());
	}

	@Nonnull
	private CachingValidationSupport getSupport(@Nullable CachingValidationSupport.CacheTimeouts theCacheTimeouts, @Nullable Boolean theIsEnabledValidationForCodingsLogicalAnd) {
		if (theCacheTimeouts == null) {
			if (theIsEnabledValidationForCodingsLogicalAnd == null) {
				return new CachingValidationSupport(myValidationSupport0);
			}

			return new CachingValidationSupport(myValidationSupport0, theIsEnabledValidationForCodingsLogicalAnd);
		}

		if (theIsEnabledValidationForCodingsLogicalAnd == null) {
			return new CachingValidationSupport(myValidationSupport0, theCacheTimeouts);
		}

		return new CachingValidationSupport(myValidationSupport0, theCacheTimeouts, theIsEnabledValidationForCodingsLogicalAnd);
	}
}
