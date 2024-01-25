package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResourceHistoryCalculatorTest {
	private static final FhirContext CONTEXT = FhirContext.forR4Cached();

	private static final ResourceHistoryCalculator CALCULATOR_ORACLE = new ResourceHistoryCalculator(CONTEXT, true);
	private static final ResourceHistoryCalculator CALCULATOR_NON_ORACLE = new ResourceHistoryCalculator(CONTEXT, false);

	private static Stream<Arguments> arguments() {
		return Stream.of(
			Arguments.of(true, ResourceEncodingEnum.JSONC),
			Arguments.of(false, ResourceEncodingEnum.JSONC),
			Arguments.of(true, ResourceEncodingEnum.DEL),
			Arguments.of(false, ResourceEncodingEnum.DEL),
			Arguments.of(true, ResourceEncodingEnum.ESR),
			Arguments.of(false, ResourceEncodingEnum.ESR),
			Arguments.of(true, ResourceEncodingEnum.JSON),
			Arguments.of(false, ResourceEncodingEnum.JSON)
		);
	}

	@ParameterizedTest
	@MethodSource("arguments")
	void calculate(boolean theIsOracle, ResourceEncodingEnum theResourceEncoding) {
		final Patient patient = getPatient();
		final List<String> excludeElements = List.of("id", "Patient.meta");

		final ResourceHistoryState result = getCalculator(theIsOracle)
			.calculate(patient, theResourceEncoding, excludeElements);

		if (theIsOracle) {
			assertNotNull(result.myResourceBinary());
			assertNull(result.myResourceText());
			assertEquals(theResourceEncoding, result.myEncoding());
		} else {
			assertNull(result.myResourceBinary());
			assertNotNull(result.myResourceText());
			assertEquals(ResourceEncodingEnum.JSON, result.myEncoding());
		}
	}

	private ResourceHistoryCalculator getCalculator(boolean theIsOracle) {
		return theIsOracle ? CALCULATOR_ORACLE : CALCULATOR_NON_ORACLE;
	}

	private Patient getPatient() {
		return new Patient();
	}
}
