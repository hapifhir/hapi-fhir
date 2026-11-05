package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link CodeValidationResult#toParameters(FhirContext)} for each FHIR version. Lives in this module rather than
 * beside the class because the structures are here.
 */
// Created by Claude Opus 5.5
class CodeValidationResultToParametersTest {

	@ParameterizedTest
	@CsvSource({"DSTU3, uri", "R4, canonical", "R5, canonical"})
	void toParameters_unknownSystems_returnsOneCausedByParameterEachOfTheVersionsType(
			FhirVersionEnum theVersion, String theExpectedType) {
		// Setup
		FhirContext ctx = FhirContext.forCached(theVersion);
		CodeValidationResult result = new CodeValidationResult()
				.setMessage("not found")
				.addUnknownSystem("http://example.org/cs|2.0")
				.addUnknownSystem("http://example.org/other");

		// Test
		IBaseParameters parameters = result.toParameters(ctx);

		// Verify
		assertThat(ParametersUtil.getNamedParameterValuesAsString(
						ctx, parameters, CodeValidationResult.CAUSED_BY_UNKNOWN_SYSTEM))
				.containsExactly("http://example.org/cs|2.0", "http://example.org/other");
		assertThat(ParametersUtil.getNamedParameters(ctx, parameters, CodeValidationResult.CAUSED_BY_UNKNOWN_SYSTEM))
				.allSatisfy(t -> assertThat(ctx.newTerser()
								.getSingleValueOrNull(t, "value[x]", IBase.class)
								.fhirType())
						.isEqualTo(theExpectedType));
	}
}
