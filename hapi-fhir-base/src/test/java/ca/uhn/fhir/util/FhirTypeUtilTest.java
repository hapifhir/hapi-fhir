package ca.uhn.fhir.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class FhirTypeUtilTest {
	@ParameterizedTest
	@ValueSource(
			strings = {
				"string",
				"code",
				"markdown",
				"id",
				"uri",
				"url",
				"canonical",
				"oid",
				"uuid",
				"boolean",
				"unsignedInt",
				"positiveInt",
				"decimal",
				"integer64",
				"integer",
				"date",
				"dateTime",
				"time",
				"instant",
				"base64Binary"
			})
	void isPrimitiveType_primitiveTypeName_returnsTrue(String theFhirType) {
		assertThat(FhirTypeUtil.isPrimitiveType(theFhirType)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {"Patient", "CodeableConcept", "Extension", "unknownType", ""})
	void isPrimitiveType_nonPrimitiveOrUnknownTypeName_returnsFalse(String theFhirType) {
		assertThat(FhirTypeUtil.isPrimitiveType(theFhirType)).isFalse();
	}
}
