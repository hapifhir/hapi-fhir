package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import io.opentelemetry.instrumentation.testing.LibraryTestRunner;
import io.opentelemetry.sdk.metrics.data.Data;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationSupportChainTest extends BaseTest {

	public static final String CODE_SYSTEM_URL_0 = "http://code-system-url-0";
	public static final String CODE_0 = "code-0";
	public static final String DISPLAY_0 = "display-0";
	public static final String VALUE_SET_URL_0 = "http://value-set-url-0";
	private static final Logger ourLog = LoggerFactory.getLogger(ValidationSupportChainTest.class);
	@Mock(strictness = Mock.Strictness.LENIENT)
	private IValidationSupport myValidationSupport0;
	@Mock(strictness = Mock.Strictness.LENIENT)
	private IValidationSupport myValidationSupport1;
	@Mock(strictness = Mock.Strictness.LENIENT)
	private IValidationSupport myValidationSupport2;

	@Test
	public void testVersionCheck() {
		DefaultProfileValidationSupport ctx3 = new DefaultProfileValidationSupport(FhirContext.forDstu3Cached());
		DefaultProfileValidationSupport ctx4 = new DefaultProfileValidationSupport(FhirContext.forR4Cached());

		try {
			new ValidationSupportChain(ctx3, ctx4);
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(709) + "Trying to add validation support of version R4 to chain with 1 entries of version DSTU3", e.getMessage());
		}
	}


	@Test
	public void testFetchIndividualStructureDefinitionThenAll() {
		DefaultProfileValidationSupport ctx = new DefaultProfileValidationSupport(FhirContext.forR4Cached());
		ValidationSupportChain chain = new ValidationSupportChain(ctx);

		assertNotNull(chain.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Patient"));
		assertEquals(649, chain.fetchAllStructureDefinitions().size());
	}


	@Test
	public void testMissingContext() {
		when(myValidationSupport0.getFhirContext()).thenReturn(null);

		try {
			new ValidationSupportChain(myValidationSupport0);
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(708) + "Can not add validation support: getFhirContext() returns null", e.getMessage());
		}
	}

	@Test
	public void fetchBinary_normally_returnsExpectedBinaries() {
		// Setup
		final byte[] EXPECTED_BINARY_CONTENT_1 = "dummyBinaryContent1".getBytes();
		final byte[] EXPECTED_BINARY_CONTENT_2 = "dummyBinaryContent2".getBytes();
		final String EXPECTED_BINARY_KEY_1 = "dummyBinaryKey1";
		final String EXPECTED_BINARY_KEY_2 = "dummyBinaryKey2";
		prepareMock(myValidationSupport0);
		prepareMock(myValidationSupport1);
		createMockValidationSupportWithSingleBinary(myValidationSupport0, EXPECTED_BINARY_KEY_1, EXPECTED_BINARY_CONTENT_1);
		createMockValidationSupportWithSingleBinary(myValidationSupport1, EXPECTED_BINARY_KEY_2, EXPECTED_BINARY_CONTENT_2);

		// Test
		ValidationSupportChain validationSupportChain = new ValidationSupportChain(myValidationSupport0, myValidationSupport1);
		final byte[] actualBinaryContent1 = validationSupportChain.fetchBinary(EXPECTED_BINARY_KEY_1);
		final byte[] actualBinaryContent2 = validationSupportChain.fetchBinary(EXPECTED_BINARY_KEY_2);

		// Verify
		assertThat(actualBinaryContent1).containsExactly(EXPECTED_BINARY_CONTENT_1);
		assertThat(actualBinaryContent2).containsExactly(EXPECTED_BINARY_CONTENT_2);
		assertNull(validationSupportChain.fetchBinary("nonExistentKey"));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testValidateCode_WithValueSetUrl(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(false);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Test
		IValidationSupport.CodeValidationResult result = chain.validateCode(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, VALUE_SET_URL_0);

		// Verify
		verify(myValidationSupport0, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
		verify(myValidationSupport1, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
		verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
		verify(myValidationSupport0, never()).validateCode(any(), any(), any(), any(), any(), any());
		verify(myValidationSupport1, times(1)).validateCode(any(), any(), any(), any(), any(), any());
		verify(myValidationSupport2, never()).validateCode(any(), any(), any(), any(), any(), any());

		// Setup for second execution (should use cache this time)
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(false);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Test again (should use cache)
		IValidationSupport.CodeValidationResult result2 = chain.validateCode(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, VALUE_SET_URL_0);

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verifyNoInteractions(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		} else {
			assertNotSame(result, result2);
			verify(myValidationSupport0, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport1, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport0, never()).validateCode(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport1, times(1)).validateCode(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport2, never()).validateCode(any(), any(), any(), any(), any(), any());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testValidateCode_WithoutValueSetUrl(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(false);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Test
		IValidationSupport.CodeValidationResult result = chain.validateCode(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, null);

		// Verify
		verify(myValidationSupport0, times(1)).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
		verify(myValidationSupport1, times(1)).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
		verify(myValidationSupport2, never()).isCodeSystemSupported(any(), any());
		verify(myValidationSupport0, never()).validateCode(any(), any(), any(), any(), any(), any());
		verify(myValidationSupport1, times(1)).validateCode(any(), any(), any(), any(), any(), any());
		verify(myValidationSupport2, never()).validateCode(any(), any(), any(), any(), any(), any());

		// Setup for second execution (should use cache this time)
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(false);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Test again (should use cache)
		IValidationSupport.CodeValidationResult result2 = chain.validateCode(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, null);

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verifyNoInteractions(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		} else {
			assertNotSame(result, result2);
			verify(myValidationSupport0, times(1)).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
			verify(myValidationSupport1, times(1)).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
			verify(myValidationSupport2, never()).isCodeSystemSupported(any(), any());
			verify(myValidationSupport0, never()).validateCode(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport1, times(1)).validateCode(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport2, never()).validateCode(any(), any(), any(), any(), any(), any());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"true,  true",
		"true,  false",
		"false, true",
		"false, false",
	})
	public void testValidateCodeInValueSet(boolean theUseCache, boolean theValueSetHasUrl) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(false);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCodeInValueSet(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		ValueSet inputValueSet = new ValueSet();
		if (theValueSetHasUrl) {
			inputValueSet.setUrl(VALUE_SET_URL_0);
		}

		// Test
		IValidationSupport.CodeValidationResult result = chain.validateCodeInValueSet(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, inputValueSet);

		// Verify
		if (theValueSetHasUrl) {
			verify(myValidationSupport0, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport1, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport0, never()).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport1, times(1)).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport2, never()).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
		} else {
			verify(myValidationSupport0, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport1, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
			verify(myValidationSupport0, times(1)).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport1, times(1)).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport2, never()).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
		}

		// Setup for second execution (should use cache this time)
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(false);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCodeInValueSet(any(), any(), any(), any(), any(), any())).thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Test again (should use cache)
		IValidationSupport.CodeValidationResult result2 = chain.validateCodeInValueSet(newValidationCtx(chain), new ConceptValidationOptions(), CODE_SYSTEM_URL_0, CODE_0, DISPLAY_0, inputValueSet);

		// Verify
		if (theUseCache && theValueSetHasUrl) {
			assertSame(result, result2);
			if (theValueSetHasUrl) {
				verify(myValidationSupport0, times(1)).getFhirContext();
			}
			verifyNoMoreInteractions(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		} else {
			assertNotSame(result, result2);
			if (theValueSetHasUrl) {
				verify(myValidationSupport0, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport1, times(1)).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport0, never()).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			} else {
				verify(myValidationSupport0, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport1, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport2, never()).isValueSetSupported(any(), eq(VALUE_SET_URL_0));
				verify(myValidationSupport0, times(1)).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			}

			verify(myValidationSupport1, times(1)).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
			verify(myValidationSupport2, never()).validateCodeInValueSet(any(), any(), any(), any(), any(), any());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"true,  true",
		"true,  false",
		"false, true",
		"false, false",
	})
	public void testExpandValueSet_ValueSetParam(boolean theUseCache, boolean theValueSetHasUrl) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport0.expandValueSet(any(), any(), any(IBaseResource.class))).thenReturn(null);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.expandValueSet(any(), any(), any(IBaseResource.class))).thenAnswer(t -> new IValidationSupport.ValueSetExpansionOutcome(new ValueSet()));

		ValueSet valueSetToExpand = new ValueSet();
		if (theValueSetHasUrl) {
			valueSetToExpand.setId("123");
			valueSetToExpand.setUrl("http://foo");
		}

		// Test
		IValidationSupport.ValueSetExpansionOutcome result = chain.expandValueSet(newValidationCtx(chain), new ValueSetExpansionOptions(), valueSetToExpand);

		// Verify
		verify(myValidationSupport0, times(1)).expandValueSet(any(), any(), any(IBaseResource.class));
		verify(myValidationSupport1, times(1)).expandValueSet(any(), any(), any(IBaseResource.class));
		verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(IBaseResource.class));

		// Test again (should use cache)
		IValidationSupport.ValueSetExpansionOutcome result2 = chain.expandValueSet(newValidationCtx(chain), new ValueSetExpansionOptions(), valueSetToExpand);

		// Verify
		if (theUseCache && theValueSetHasUrl) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).expandValueSet(any(), any(), any(IBaseResource.class));
			verify(myValidationSupport1, times(1)).expandValueSet(any(), any(), any(IBaseResource.class));
			verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(IBaseResource.class));
		} else {
			verify(myValidationSupport0, times(2)).expandValueSet(any(), any(), any(IBaseResource.class));
			verify(myValidationSupport1, times(2)).expandValueSet(any(), any(), any(IBaseResource.class));
			verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(IBaseResource.class));
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testExpandValueSet_StringParam(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport0.expandValueSet(any(), any(), any(String.class))).thenReturn(null);
		when(myValidationSupport1.isValueSetSupported(any(), eq(VALUE_SET_URL_0))).thenReturn(true);
		when(myValidationSupport1.expandValueSet(any(), any(), any(String.class))).thenAnswer(t -> new IValidationSupport.ValueSetExpansionOutcome(new ValueSet()));

		// Test
		IValidationSupport.ValueSetExpansionOutcome result = chain.expandValueSet(newValidationCtx(chain), new ValueSetExpansionOptions(), VALUE_SET_URL_0);

		// Verify
		verify(myValidationSupport0, times(1)).expandValueSet(any(), any(), any(String.class));
		verify(myValidationSupport1, times(1)).expandValueSet(any(), any(), any(String.class));
		verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(String.class));

		// Test again (should use cache)
		IValidationSupport.ValueSetExpansionOutcome result2 = chain.expandValueSet(newValidationCtx(chain), new ValueSetExpansionOptions(), VALUE_SET_URL_0);

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).expandValueSet(any(), any(), any(String.class));
			verify(myValidationSupport1, times(1)).expandValueSet(any(), any(), any(String.class));
			verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(String.class));
		} else {
			verify(myValidationSupport0, times(2)).expandValueSet(any(), any(), any(String.class));
			verify(myValidationSupport1, times(2)).expandValueSet(any(), any(), any(String.class));
			verify(myValidationSupport2, times(0)).expandValueSet(any(), any(), any(String.class));
		}
	}

	@Test
	public void testFetchAllNonBaseStructureDefinitions() {
		// Setup
		prepareMock(myValidationSupport0);

		StructureDefinition sd0 = (StructureDefinition) new StructureDefinition().setId("SD0");
		StructureDefinition sd1 = (StructureDefinition) new StructureDefinition().setId("SD1");
		StructureDefinition sd2 = (StructureDefinition) new StructureDefinition().setId("SD2");
		List<IBaseResource> responses = Collections.synchronizedList(Lists.newArrayList(
			sd0, sd1, sd2
		));

		// Each time this is called it will return a slightly shorter list
		when(myValidationSupport0.fetchAllNonBaseStructureDefinitions()).thenAnswer(t -> {
			Thread.sleep(1000);
			return new ArrayList<>(responses);
		});

		final ValidationSupportChain.CacheConfiguration cacheTimeouts = ValidationSupportChain.CacheConfiguration
			.defaultValues()
			.setCacheTimeout(Duration.ofMillis(500));
		ValidationSupportChain chain = new ValidationSupportChain(cacheTimeouts, myValidationSupport0);

		// First call should return the full list
		assertEquals(3, chain.fetchAllNonBaseStructureDefinitions().size());
		assertEquals(3, chain.fetchAllNonBaseStructureDefinitions().size());

		// Remove one from the backing list and wait for the cache to expire
		responses.remove(0);
		TestUtil.sleepAtLeast(750);

		// The cache is expired, but we should still return the old list and
		// start a background job to update the backing list
		assertEquals(3, chain.fetchAllNonBaseStructureDefinitions().size());

		// Eventually we should refresh
		await().until(() -> chain.fetchAllNonBaseStructureDefinitions().size(), t -> t == 2);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testFetchAllSearchParameters(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.fetchAllSearchParameters()).thenReturn(List.of(
			new SearchParameter().setId("01"),
			new SearchParameter().setId("02")
		));
		when(myValidationSupport1.fetchAllSearchParameters()).thenReturn(List.of(
			new SearchParameter().setId("11"),
			new SearchParameter().setId("12")
		));
		when(myValidationSupport2.fetchAllSearchParameters()).thenReturn(null);
		ValidationSupportChain.CacheConfiguration cache = theUseCache ? ValidationSupportChain.CacheConfiguration.defaultValues() : ValidationSupportChain.CacheConfiguration.disabled();
		ValidationSupportChain chain = new ValidationSupportChain(cache, myValidationSupport0, myValidationSupport1, myValidationSupport2);

		// Test
		List<IBaseResource> actual = chain.fetchAllSearchParameters();

		// Verify
		assert actual != null;
		assertThat(actual.stream().map(t -> t.getIdElement().getIdPart()).toList()).asList().containsExactly(
			"01", "02", "11", "12"
		);
		verify(myValidationSupport0, times(1)).fetchAllSearchParameters();
		verify(myValidationSupport1, times(1)).fetchAllSearchParameters();
		verify(myValidationSupport2, times(1)).fetchAllSearchParameters();

		// Test a second time
		actual = chain.fetchAllSearchParameters();

		// Verify
		assert actual != null;
		assertThat(actual.stream().map(t -> t.getIdElement().getIdPart()).toList()).asList().containsExactly(
			"01", "02", "11", "12"
		);
		if (theUseCache) {
			verify(myValidationSupport0, times(1)).fetchAllSearchParameters();
			verify(myValidationSupport1, times(1)).fetchAllSearchParameters();
			verify(myValidationSupport2, times(1)).fetchAllSearchParameters();
		} else {
			verify(myValidationSupport0, times(2)).fetchAllSearchParameters();
			verify(myValidationSupport1, times(2)).fetchAllSearchParameters();
			verify(myValidationSupport2, times(2)).fetchAllSearchParameters();
		}

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testLookupCode(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport0.lookupCode(any(), any(LookupCodeRequest.class))).thenReturn(null);
		when(myValidationSupport1.lookupCode(any(), any(LookupCodeRequest.class))).thenAnswer(t -> new IValidationSupport.LookupCodeResult());

		// Test
		IValidationSupport.LookupCodeResult result = chain.lookupCode(newValidationCtx(chain), new LookupCodeRequest(CODE_SYSTEM_URL_0, CODE_0));

		// Verify
		verify(myValidationSupport0, times(1)).lookupCode(any(), any(LookupCodeRequest.class));
		verify(myValidationSupport1, times(1)).lookupCode(any(), any(LookupCodeRequest.class));
		verify(myValidationSupport2, times(0)).lookupCode(any(), any(LookupCodeRequest.class));

		// Test again (should use cache)
		IValidationSupport.LookupCodeResult result2 = chain.lookupCode(newValidationCtx(chain), new LookupCodeRequest(CODE_SYSTEM_URL_0, CODE_0));

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).lookupCode(any(), any(LookupCodeRequest.class));
			verify(myValidationSupport1, times(1)).lookupCode(any(), any(LookupCodeRequest.class));
			verify(myValidationSupport2, times(0)).lookupCode(any(), any(LookupCodeRequest.class));
		} else {
			verify(myValidationSupport0, times(2)).lookupCode(any(), any(LookupCodeRequest.class));
			verify(myValidationSupport1, times(2)).lookupCode(any(), any(LookupCodeRequest.class));
			verify(myValidationSupport2, times(0)).lookupCode(any(), any(LookupCodeRequest.class));
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testFetchValueSet(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.fetchValueSet(any())).thenReturn(null);
		when(myValidationSupport1.fetchValueSet(any())).thenAnswer(t -> new ValueSet());

		// Test
		IBaseResource result = chain.fetchValueSet(VALUE_SET_URL_0);

		// Verify
		verify(myValidationSupport0, times(1)).fetchValueSet(any());
		verify(myValidationSupport1, times(1)).fetchValueSet(any());
		verify(myValidationSupport2, times(0)).fetchValueSet(any());

		// Test again (should use cache)
		IBaseResource result2 = chain.fetchValueSet(VALUE_SET_URL_0);

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).fetchValueSet(any());
			verify(myValidationSupport1, times(1)).fetchValueSet(any());
			verify(myValidationSupport2, times(0)).fetchValueSet(any());
		} else {
			verify(myValidationSupport0, times(2)).fetchValueSet(any());
			verify(myValidationSupport1, times(2)).fetchValueSet(any());
			verify(myValidationSupport2, times(0)).fetchValueSet(any());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testResource_ValueSet(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.fetchValueSet(any())).thenReturn(null);
		when(myValidationSupport1.fetchValueSet(any())).thenAnswer(t -> new ValueSet());

		// Test
		IBaseResource result = chain.fetchResource(ValueSet.class, VALUE_SET_URL_0);

		// Verify
		verify(myValidationSupport0, times(1)).fetchValueSet( any());
		verify(myValidationSupport1, times(1)).fetchValueSet( any());
		verify(myValidationSupport2, times(0)).fetchValueSet(any());

		// Test again (should use cache)
		IBaseResource result2 = chain.fetchResource(ValueSet.class, VALUE_SET_URL_0);

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).fetchValueSet( any());
			verify(myValidationSupport1, times(1)).fetchValueSet( any());
			verify(myValidationSupport2, times(0)).fetchValueSet( any());
		} else {
			verify(myValidationSupport0, times(2)).fetchValueSet( any());
			verify(myValidationSupport1, times(2)).fetchValueSet( any());
			verify(myValidationSupport2, times(0)).fetchValueSet(any());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testResource_Arbitrary(boolean theUseCache) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.fetchResource(any(), any())).thenReturn(null);
		when(myValidationSupport1.fetchResource(any(), any())).thenAnswer(t -> new ListResource());

		// Test
		IBaseResource result = chain.fetchResource(ListResource.class, "http://foo");

		// Verify
		verify(myValidationSupport0, times(1)).fetchResource(any(), any());
		verify(myValidationSupport1, times(1)).fetchResource(any(), any());
		verify(myValidationSupport2, times(0)).fetchResource(any(), any());

		// Test again (should use cache)
		IBaseResource result2 = chain.fetchResource(ListResource.class, "http://foo");

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).fetchResource(any(), any());
			verify(myValidationSupport1, times(1)).fetchResource(any(), any());
			verify(myValidationSupport2, times(0)).fetchResource(any(), any());
		} else {
			verify(myValidationSupport0, times(2)).fetchResource(any(), any());
			verify(myValidationSupport1, times(2)).fetchResource(any(), any());
			verify(myValidationSupport2, times(0)).fetchResource(any(), any());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"false, false",
		"false, true",
		"true, true",
		"true, false"})
	public void testTranslateCode(boolean theUseCache, boolean theWithCodingInInput) {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);

		when(myValidationSupport0.translateConcept(any())).thenReturn(null);
		TranslateConceptResults backingResult1 = new TranslateConceptResults();
		backingResult1.setMessage("Message 1");
		backingResult1.setResult(true);
		backingResult1.getResults().add(new TranslateConceptResult().setCode("A"));
		when(myValidationSupport1.translateConcept(any())).thenReturn(backingResult1);
		TranslateConceptResults backingResult2 = new TranslateConceptResults();
		backingResult2.setMessage("Message 2");
		backingResult2.setResult(true);
		backingResult2.getResults().add(new TranslateConceptResult().setCode("B"));
		when(myValidationSupport2.translateConcept(any())).thenReturn(backingResult2);

		// Test

		TranslateConceptResults result = chain.translateConcept(createTranslateCodeRequest(theWithCodingInInput));

		// Verify
		assertEquals("Message 1", result.getMessage());
		assertTrue(result.getResult());
		assertEquals(2, result.getResults().size());
		verify(myValidationSupport0, times(1)).translateConcept(any());
		verify(myValidationSupport1, times(1)).translateConcept(any());
		verify(myValidationSupport2, times(1)).translateConcept(any());

		// Test again
		TranslateConceptResults result2 = chain.translateConcept(createTranslateCodeRequest(theWithCodingInInput));

		// Verify
		if (theUseCache) {
			assertSame(result, result2);
			verify(myValidationSupport0, times(1)).translateConcept(any());
			verify(myValidationSupport1, times(1)).translateConcept(any());
			verify(myValidationSupport2, times(1)).translateConcept(any());
		} else {
			assertNotSame(result, result2);
			verify(myValidationSupport0, times(2)).translateConcept(any());
			verify(myValidationSupport1, times(2)).translateConcept(any());
			verify(myValidationSupport2, times(2)).translateConcept(any());
		}
	}

	private  IValidationSupport.TranslateCodeRequest createTranslateCodeRequest(boolean theWithCodingInInput) {
		IValidationSupport.TranslateCodeRequest request;
		if (theWithCodingInInput) {
			Coding coding = new Coding()
				.setSystem("coding_system")
				.setCode("coding_code");

			request = new IValidationSupport.TranslateCodeRequest(List.of(coding), CODE_SYSTEM_URL_0);
		}
		else {
			request = new IValidationSupport.TranslateCodeRequest(List.of(), CODE_SYSTEM_URL_0);
		}
		return request;
	}

	/**
	 * Verify that OpenTelemetry metrics are generated correctly
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testMetrics(boolean theUseCache) {
		LibraryTestRunner libraryTestRunner = LibraryTestRunner.instance();

		/*
		 * As of version 2.10.0 of the opentelemetry-testing-common library,
		 * the following doesn't actually clear the stored metrics. Hopefully
		 * this will be fixed in a future release.
		 */
		libraryTestRunner.clearAllExportedData();

		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.fetchStructureDefinition("http://foo")).thenReturn(new StructureDefinition().setUrl("http://foo"));
		ValidationSupportChain chain = new ValidationSupportChain(newCacheConfiguration(theUseCache), myValidationSupport0, myValidationSupport1, myValidationSupport2);
		chain.setName("FOO_NAME");

		chain.start();
		try {
			if (theUseCache) {
				assertEquals(5000L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.EXPIRING_CACHE_MAXIMUM_SIZE));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.EXPIRING_CACHE_CURRENT_ENTRIES));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.NON_EXPIRING_CACHE_CURRENT_ENTRIES));
			} else {
				assertEquals(0L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.EXPIRING_CACHE_MAXIMUM_SIZE));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.EXPIRING_CACHE_CURRENT_ENTRIES));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, ValidationSupportChainMetrics.NON_EXPIRING_CACHE_CURRENT_ENTRIES));
			}

			// Test
			assertNotNull(chain.fetchStructureDefinition("http://foo"));

			// Verify
			if (theUseCache) {
				assertEquals(5000L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.expiring_cache.maximum_size"));
				assertEquals(1L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.expiring_cache.current_entries"));
				assertEquals(1L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.non_expiring_cache.current_entries"));
			} else {
				assertEquals(0L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.expiring_cache.maximum_size"));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.expiring_cache.current_entries"));
				assertEquals(0L, getLastMetricValue(libraryTestRunner, "io.hapifhir.validation_support_chain.non_expiring_cache.current_entries"));
			}
		} finally {
			chain.stop();
		}
	}


	@Test
	public void testModifyingServiceInvalidatesCache() {
		// Setup
		prepareMock(myValidationSupport0, myValidationSupport1, myValidationSupport2);
		when(myValidationSupport0.isCodeSystemSupported(any(), eq("http://foo"))).thenReturn(true);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq("http://foo"))).thenReturn(true);

		ValidationSupportChain svc = new ValidationSupportChain(myValidationSupport0, myValidationSupport1);
		assertTrue(svc.isCodeSystemSupported(newValidationCtx(svc), "http://foo"));

		// Test
		svc.addValidationSupport(myValidationSupport2);
		when(myValidationSupport0.isCodeSystemSupported(any(), eq("http://foo"))).thenReturn(false);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq("http://foo"))).thenReturn(false);
		when(myValidationSupport2.isCodeSystemSupported(any(), eq("http://foo"))).thenReturn(false);
		boolean actual = svc.isCodeSystemSupported(newValidationCtx(svc), "http://foo");

		// Verify
		assertFalse(actual);
	}

	@Test
	void testRemoveValidationSupport_removesValidatorAndInvalidatesCache() {
		// Setup with caching enabled
		prepareMock(myValidationSupport0, myValidationSupport1);
		ValidationSupportChain chain = new ValidationSupportChain(
			ValidationSupportChain.CacheConfiguration.defaultValues(),
			myValidationSupport0,
			myValidationSupport1
		);

		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(false);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any()))
			.thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// First validation call - should use support1 and cache result
		IValidationSupport.CodeValidationResult result1 = chain.validateCode(
			newValidationCtx(chain),
			new ConceptValidationOptions(),
			CODE_SYSTEM_URL_0,
			CODE_0,
			DISPLAY_0,
			null
		);
		assertNotNull(result1);
		verify(myValidationSupport1, times(1)).validateCode(any(), any(), any(), any(), any(), any());

		// Second validation call - should hit cache
		prepareMock(myValidationSupport0, myValidationSupport1);
		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(false);
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);

		IValidationSupport.CodeValidationResult result2 = chain.validateCode(
			newValidationCtx(chain),
			new ConceptValidationOptions(),
			CODE_SYSTEM_URL_0,
			CODE_0,
			DISPLAY_0,
			null
		);

		// Should be same instance from cache
		assertSame(result1, result2);
		verifyNoInteractions(myValidationSupport0, myValidationSupport1);

		// Remove myValidationSupport1 - this should invalidate caches AND remove from chain
		chain.removeValidationSupport(myValidationSupport1);

		// Setup BOTH validators to support the code system - only support0 should be called
		// since support1 was removed from the chain
		prepareMock(myValidationSupport0, myValidationSupport1);
		when(myValidationSupport0.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport0.validateCode(any(), any(), any(), any(), any(), any()))
			.thenAnswer(t -> new IValidationSupport.CodeValidationResult());
		when(myValidationSupport1.isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0))).thenReturn(true);
		when(myValidationSupport1.validateCode(any(), any(), any(), any(), any(), any()))
			.thenAnswer(t -> new IValidationSupport.CodeValidationResult());

		// Third validation call - cache invalidated, should only call support0
		IValidationSupport.CodeValidationResult result3 = chain.validateCode(
			newValidationCtx(chain),
			new ConceptValidationOptions(),
			CODE_SYSTEM_URL_0,
			CODE_0,
			DISPLAY_0,
			null
		);

		// Verify cache was invalidated (different instance)
		assertNotSame(result1, result3);

		// Verify support0 was called (it's still in the chain)
		verify(myValidationSupport0, times(1)).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
		verify(myValidationSupport0, times(1)).validateCode(any(), any(), any(), any(), any(), any());

		// Verify support1 was NOT called (it was removed from the chain)
		verify(myValidationSupport1, never()).isCodeSystemSupported(any(), eq(CODE_SYSTEM_URL_0));
		verify(myValidationSupport1, never()).validateCode(any(), any(), any(), any(), any(), any());
	}


	private static long getLastMetricValue(LibraryTestRunner libraryTestRunner, String metricName) {
		List<MetricData> metrics = libraryTestRunner.getExportedMetrics();
		List<MetricData> metricsList = metrics.stream().filter(t -> t.getName().equals(metricName)).toList();
		ourLog.info("Have metrics {}\n * {}", metricName, metricsList.stream().map(t -> t.getData().getPoints().toString()).collect(Collectors.joining("\n * ")));
		MetricData metric = metricsList.get(metricsList.size() - 1);
		assertEquals("io.hapifhir.validation_support_chain", metric.getInstrumentationScopeInfo().getName());
		Data<?> data = metric.getData();
		ArrayList<?> dataPoints = new ArrayList<>(data.getPoints());
		assertEquals(1, dataPoints.size());
		LongPointData pointData = (LongPointData) dataPoints.get(0);
		return pointData.getValue();
	}


	private static ValidationSupportChain.CacheConfiguration newCacheConfiguration(boolean theUseCache) {
		return theUseCache ? ValidationSupportChain.CacheConfiguration.defaultValues() : ValidationSupportChain.CacheConfiguration.disabled();
	}

	@Nonnull
	private static ValidationSupportContext newValidationCtx(ValidationSupportChain validationSupportChain) {
		return new ValidationSupportContext(validationSupportChain);
	}


	private static void prepareMock(IValidationSupport... theMock) {
		reset(theMock);
		for (var mock : theMock) {
			when(mock.getFhirContext()).thenReturn(FhirContext.forR4Cached());
		}
	}

	private static void createMockValidationSupportWithSingleBinary(IValidationSupport theValidationSupport, String theExpectedBinaryKey, byte[] theExpectedBinaryContent) {
		when(theValidationSupport.fetchBinary(theExpectedBinaryKey)).thenReturn(theExpectedBinaryContent);
	}

}
