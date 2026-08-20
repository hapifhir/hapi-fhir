package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImportLoincJobParametersValidatorTest {

	private final ImportLoincJobParametersValidator mySvc = new ImportLoincJobParametersValidator();

	@Test
	void testValidate_Valid() {
		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		parameters.setVersionId("1.22.3");
		List<String> outcome = mySvc.validate(null, parameters);
		assertThat(outcome).isEmpty();
	}

	@Test
	void testValidate_NoVersion() {
		List<String> outcome = mySvc.validate(null, new ImportTerminologyJobParameters());
		assertThat(outcome).contains("Version ID is required");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"abc123",
		"1.",
		"1.a",
		" 1.2.3 "
	})
	void testValidate_InvalidVersion(String theVersionId) {
		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		parameters.setVersionId(theVersionId);
		List<String> outcome = mySvc.validate(null, parameters);
		assertThat(outcome).contains("Version ID is invalid: " + theVersionId);
	}

}
