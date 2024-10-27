package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.hl7.fhir.dstu3.hapi.ctx.FhirDstu3;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.FhirR4;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceHistoryCalculatorTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceHistoryCalculatorTest.class);

	private static final FhirContext CONTEXT = FhirContext.forR4Cached();

	private static final ResourceHistoryCalculator CALCULATOR_ORACLE = new ResourceHistoryCalculator(CONTEXT, true);
	private static final ResourceHistoryCalculator CALCULATOR_NON_ORACLE = new ResourceHistoryCalculator(CONTEXT, false);

	private static final LocalDate TODAY = LocalDate.of(2024, Month.JANUARY, 25);
	private static final String ENCODED_RESOURCE_1 = "1234";
	private static final String ENCODED_RESOURCE_2 = "abcd";
	private static final String RESOURCE_TEXT_VC = "resourceTextVc";
	private static final List<String> EXCLUDED_ELEMENTS_1 = List.of("id");
	private static final List<String> EXCLUDED_ELEMENTS_2 = List.of("resourceType", "birthDate");
	private static final HashFunction SHA_256 = Hashing.sha256();

	private static Stream<Arguments> calculateResourceHistoryStateArguments() {
		return Stream.of(
			Arguments.of(FhirContext.forDstu3Cached(), true, ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), false, ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), true, ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), false, ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), true, ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), false, ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), true, ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), false, ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), true, ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), false, ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), true, ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), false, ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), true, ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), false, ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), true, ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), false, ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_1)
		);
	}

	/**
	 * The purpose of this test is to ensure that the conditional logic to pre-calculate resource history text or binaries
	 * is respected.
	 * If this is for Oracle, the resource text will be driven off a binary with a given encoding with the
	 * resource text effectively ignored.
	 * If this is not Oracle, it will be driven off a JSON encoded text field with
	 * the binary effectively ignored.
	 */
	@ParameterizedTest
	@MethodSource("calculateResourceHistoryStateArguments")
	void calculateResourceHistoryState(FhirContext theFhirContext, boolean theIsOracle, ResourceEncodingEnum theResourceEncoding, List<String> theExcludedElements) {
		final IBaseResource patient = getPatient(theFhirContext);

		final ResourceHistoryCalculator calculator = getCalculator(theFhirContext, theIsOracle);
		final ResourceHistoryState result = calculator.calculateResourceHistoryState(patient, theResourceEncoding, theExcludedElements);

		if (theIsOracle) {
			assertNotNull(result.getResourceBinary()); // On Oracle: We use the resource binary to serve up the resource content
			assertNull(result.getResourceText()); // On Oracle: We do NOT use the resource text to serve up the resource content
			assertEquals(theResourceEncoding, result.getEncoding()); // On Oracle, the resource encoding is what we used to encode the binary
			assertEquals(SHA_256.hashBytes(result.getResourceBinary()), result.getHashCode()); // On Oracle, the SHA 256 hash is of the binary
		} else {
			assertNull(result.getResourceBinary()); // Non-Oracle: We do NOT use the resource binary to serve up the resource content
			assertNotNull(result.getResourceText()); // Non-Oracle: We use the resource text to serve up the resource content
			assertEquals(ResourceEncodingEnum.JSON, result.getEncoding()); // Non-Oracle, since we didn't encode a binary this is always JSON.
			final HashCode expectedHashCode = SHA_256.hashUnencodedChars(calculator.encodeResource(patient, theResourceEncoding, theExcludedElements)); // Non-Oracle, the SHA 256 hash is of the parsed resource object
			assertEquals(expectedHashCode, result.getHashCode());
		}
	}


	private static Stream<Arguments> conditionallyAlterHistoryEntityArguments() {
		return Stream.of(
			Arguments.of(true, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_2)
		);
	}

	@ParameterizedTest
	@MethodSource("conditionallyAlterHistoryEntityArguments")
	void conditionallyAlterHistoryEntity_usesVarcharForOracle(boolean theIsOracle, ResourceEncodingEnum theResourceEncoding, String theResourceText) {
		final ResourceTable resourceTable = new ResourceTable();
		resourceTable.setId(123L);

		final ResourceHistoryTable resourceHistoryTable = new ResourceHistoryTable();
		resourceHistoryTable.setVersion(1);
		resourceHistoryTable.setResource("resource".getBytes(StandardCharsets.UTF_8));
		resourceHistoryTable.setEncoding(theResourceEncoding);
		resourceHistoryTable.setResourceTextVc(RESOURCE_TEXT_VC);

		final boolean isChanged =
			getCalculator(theIsOracle).conditionallyAlterHistoryEntity(resourceTable, resourceHistoryTable, theResourceText);

		if (theIsOracle) {
			assertFalse(isChanged);
			assertNotNull(resourceHistoryTable.getResource());
			assertEquals(RESOURCE_TEXT_VC, resourceHistoryTable.getResourceTextVc());
			assertEquals(resourceHistoryTable.getEncoding(), resourceHistoryTable.getEncoding());
		} else {
			assertTrue(isChanged);
			assertNull(resourceHistoryTable.getResource());
			assertEquals(theResourceText, resourceHistoryTable.getResourceTextVc());
			assertEquals(resourceHistoryTable.getEncoding(), ResourceEncodingEnum.JSON);
		}
	}

	private static Stream<Arguments> encodeResourceArguments() {
		return Stream.of(
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forDstu3Cached(), ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.JSONC, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.DEL, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.ESR, EXCLUDED_ELEMENTS_2),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_1),
			Arguments.of(FhirContext.forR4Cached(), ResourceEncodingEnum.JSON, EXCLUDED_ELEMENTS_2)
		);
	}

	@ParameterizedTest
	@MethodSource("encodeResourceArguments")
	void encodeResource_ensureFhirVersionSpecificAndIntendedElementsExcluded(FhirContext theFhirContext, ResourceEncodingEnum theResourceEncoding, List<String> theExcludedElements) {
		final IBaseResource patient = getPatient(theFhirContext);
		final String encodedResource = getCalculator(theFhirContext, true).encodeResource(patient, theResourceEncoding, theExcludedElements);

		final String expectedEncoding =
			theResourceEncoding.newParser(theFhirContext).setDontEncodeElements(theExcludedElements).encodeResourceToString(patient);

		assertEquals(expectedEncoding, encodedResource);
	}

	private static Stream<Arguments> getResourceBinaryArguments() {
		return Stream.of(
			Arguments.of(ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_1),
			Arguments.of(ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_2),
			Arguments.of(ResourceEncodingEnum.DEL, ENCODED_RESOURCE_1),
			Arguments.of(ResourceEncodingEnum.DEL, ENCODED_RESOURCE_2),
			Arguments.of(ResourceEncodingEnum.ESR, ENCODED_RESOURCE_1),
			Arguments.of(ResourceEncodingEnum.ESR, ENCODED_RESOURCE_2),
			Arguments.of(ResourceEncodingEnum.JSON, ENCODED_RESOURCE_1),
			Arguments.of(ResourceEncodingEnum.JSON, ENCODED_RESOURCE_2)
		);
	}

	@ParameterizedTest
	@MethodSource("getResourceBinaryArguments")
	void getResourceBinary(ResourceEncodingEnum theResourceEncoding, String theEncodedResource) {
		final byte[] resourceBinary = ResourceHistoryCalculator.getResourceBinary(theResourceEncoding, theEncodedResource);

		switch (theResourceEncoding) {
			case JSON:
				assertArrayEquals(theEncodedResource.getBytes(StandardCharsets.UTF_8), resourceBinary);
				break;
			case JSONC:
				assertArrayEquals(GZipUtil.compress(theEncodedResource), resourceBinary);
				break;
			case DEL :
			case ESR :
			default:
				assertArrayEquals(new byte[0], resourceBinary);
		}

		ourLog.info("resourceBinary: {}", resourceBinary);
	}

	private static Stream<Arguments> isResourceHistoryChangedArguments() {
		return Stream.of(
			Arguments.of(true, ENCODED_RESOURCE_1.getBytes(StandardCharsets.UTF_8), ENCODED_RESOURCE_1),
			Arguments.of(false, ENCODED_RESOURCE_1.getBytes(StandardCharsets.UTF_8), ENCODED_RESOURCE_1),
			Arguments.of(true, ENCODED_RESOURCE_2.getBytes(StandardCharsets.UTF_8), ENCODED_RESOURCE_2),
			Arguments.of(false, ENCODED_RESOURCE_2.getBytes(StandardCharsets.UTF_8), ENCODED_RESOURCE_2)
		);
	}

	@ParameterizedTest
	@MethodSource("isResourceHistoryChangedArguments")
	void isResourceHistoryChanged(boolean theIsOracle, byte[] theNewBinary, String theNewResourceText) {
		final String existngResourceText = ENCODED_RESOURCE_1;
		final byte[] existingBytes = existngResourceText.getBytes(StandardCharsets.UTF_8);

		final ResourceHistoryTable resourceHistoryTable = new ResourceHistoryTable();
		resourceHistoryTable.setResource(existingBytes);
		resourceHistoryTable.setResourceTextVc(existngResourceText);

		final boolean isChanged = getCalculator(theIsOracle).isResourceHistoryChanged(resourceHistoryTable, theNewBinary, theNewResourceText);

		if (theIsOracle) {
			final boolean expectedResult = !Arrays.equals(existingBytes, theNewBinary);
			assertEquals(expectedResult, isChanged);
		} else {
			final boolean expectedResult = ! existngResourceText.equals(theNewResourceText);
			assertEquals(expectedResult, isChanged);
		}
	}

	private static Stream<Arguments> populateEncodedResourceArguments() {
		return Stream.of(
			Arguments.of(true, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.JSONC, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.DEL, ENCODED_RESOURCE_1),
			Arguments.of(true, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_1),
			Arguments.of(false, ResourceEncodingEnum.ESR, ENCODED_RESOURCE_2),
			Arguments.of(true, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_2),
			Arguments.of(false, ResourceEncodingEnum.JSON, ENCODED_RESOURCE_1)
		);
	}

	@ParameterizedTest
	@MethodSource("populateEncodedResourceArguments")
	void populateEncodedResource(boolean theIsOracle, ResourceEncodingEnum theResourceEncoding, String theEncodedResourceString) {
		final EncodedResource encodedResource = new EncodedResource();
		final byte[] resourceBinary = theEncodedResourceString.getBytes(StandardCharsets.UTF_8);

		getCalculator(theIsOracle)
			.populateEncodedResource(encodedResource, theEncodedResourceString, resourceBinary, theResourceEncoding);

		if (theIsOracle) {
			assertEquals(resourceBinary, encodedResource.getResourceBinary());
			assertNull(encodedResource.getResourceText());
			assertEquals(theResourceEncoding, encodedResource.getEncoding());
		} else {
			assertNull(encodedResource.getResourceBinary());
			assertEquals(theEncodedResourceString, encodedResource.getResourceText());
			assertEquals(ResourceEncodingEnum.JSON, encodedResource.getEncoding());
		}
	}

	private ResourceHistoryCalculator getCalculator(boolean theIsOracle) {
		return theIsOracle ? CALCULATOR_ORACLE : CALCULATOR_NON_ORACLE;
	}

	private ResourceHistoryCalculator getCalculator(FhirContext theFhirContext, boolean theIsOracle) {
		return new ResourceHistoryCalculator(theFhirContext, theIsOracle);
	}

	private IBaseResource getPatient(FhirContext theFhirContext) {
		if (theFhirContext.getVersion() instanceof FhirR4) {
			return getPatientR4();
		}

		if (theFhirContext.getVersion() instanceof FhirDstu3) {
			return getPatientDstu3();
		}

		return null;
	}

	private org.hl7.fhir.dstu3.model.Patient getPatientDstu3() {
		final org.hl7.fhir.dstu3.model.Patient patient = new org.hl7.fhir.dstu3.model.Patient();

		patient.setId("123");
		patient.setBirthDate(Date.from(TODAY.atStartOfDay(ZoneId.of("America/Toronto")).toInstant()));

		return patient;
	}

	private Patient getPatientR4() {
		final Patient patient = new Patient();

		patient.setId("123");
		patient.setBirthDate(Date.from(TODAY.atStartOfDay(ZoneId.of("America/Toronto")).toInstant()));

		return patient;
	}
}
