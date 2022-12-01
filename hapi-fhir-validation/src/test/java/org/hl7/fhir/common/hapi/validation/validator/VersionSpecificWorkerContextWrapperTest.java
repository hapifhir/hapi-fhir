package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r5.model.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VersionSpecificWorkerContextWrapperTest {

	final byte[] EXPECTED_BINARY_CONTENT_1 = "dummyBinaryContent1".getBytes();
	final byte[] EXPECTED_BINARY_CONTENT_2 = "dummyBinaryContent2".getBytes();
	final String EXPECTED_BINARY_KEY_1 = "dummyBinaryKey1";
	final String EXPECTED_BINARY_KEY_2 = "dummyBinaryKey2";
	final String NON_EXISTENT_BINARY_KEY = "nonExistentBinaryKey";

	@Test
	public void hasBinaryKey_normally_returnsExpected() {

		IValidationSupport validationSupport = mockValidationSupportWithTwoBinaries();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		assertTrue(wrapper.hasBinaryKey(EXPECTED_BINARY_KEY_1), "wrapper should have binary key " + EXPECTED_BINARY_KEY_1);
		assertTrue(wrapper.hasBinaryKey(EXPECTED_BINARY_KEY_2), "wrapper should have binary key " + EXPECTED_BINARY_KEY_1);
		assertFalse(wrapper.hasBinaryKey(NON_EXISTENT_BINARY_KEY), "wrapper should not have binary key " + NON_EXISTENT_BINARY_KEY);

	}

	@Test
	public void getBinaryForKey_normally_returnsExpected() {

		IValidationSupport validationSupport = mockValidationSupportWithTwoBinaries();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		assertArrayEquals(EXPECTED_BINARY_CONTENT_1, wrapper.getBinaryForKey(EXPECTED_BINARY_KEY_1));
		assertArrayEquals(EXPECTED_BINARY_CONTENT_2, wrapper.getBinaryForKey(EXPECTED_BINARY_KEY_2));
		assertNull(wrapper.getBinaryForKey(NON_EXISTENT_BINARY_KEY), "wrapper should return null for binary key " + NON_EXISTENT_BINARY_KEY);
	}

	@Test
	public void cacheResource_normally_executesWithoutException() {

		IValidationSupport validationSupport = mockValidationSupport();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		wrapper.cacheResource(mock(Resource.class));
	}

	private IValidationSupport mockValidationSupportWithTwoBinaries() {
		IValidationSupport validationSupport;
		validationSupport = mockValidationSupport();
		when(validationSupport.fetchBinary(EXPECTED_BINARY_KEY_1)).thenReturn(EXPECTED_BINARY_CONTENT_1);
		when(validationSupport.fetchBinary(EXPECTED_BINARY_KEY_2)).thenReturn(EXPECTED_BINARY_CONTENT_2);
		return validationSupport;
	}


	private static ValidationSupportContext mockValidationSupportContext(IValidationSupport validationSupport) {
		ValidationSupportContext mockContext;
		mockContext = mock(ValidationSupportContext.class);
		when(mockContext.getRootValidationSupport()).thenReturn(validationSupport);
		return mockContext;
	}


	private static IValidationSupport mockValidationSupport() {
		IValidationSupport mockValidationSupport;
		mockValidationSupport = mock(IValidationSupport.class);
		FhirContext mockFhirContext = mock(FhirContext.class);
		when(mockFhirContext.getLocalizer()).thenReturn(new HapiLocalizer());
		when(mockFhirContext.getVersion()).thenReturn(FhirVersionEnum.R4.getVersionImplementation());
		when(mockValidationSupport.getFhirContext()).thenReturn(mockFhirContext);
		return mockValidationSupport;
	}
}
