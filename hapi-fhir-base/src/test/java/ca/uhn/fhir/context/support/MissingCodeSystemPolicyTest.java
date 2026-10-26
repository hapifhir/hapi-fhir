package ca.uhn.fhir.context.support;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class MissingCodeSystemPolicyTest {

	@ParameterizedTest
	@CsvSource({"REJECT, false", "USE_INSTALLED_VERSION, false", "ACCEPT_LISTED_CODES, true"})
	void isListedCodesAccepted_eachPolicy_isTrueOnlyForAcceptListedCodes(
			MissingCodeSystemPolicy thePolicy, boolean theExpected) {
		assertThat(thePolicy.isListedCodesAccepted()).isEqualTo(theExpected);
	}
}
