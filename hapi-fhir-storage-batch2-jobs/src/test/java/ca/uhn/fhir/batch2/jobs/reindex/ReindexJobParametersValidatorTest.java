package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ReindexJobParametersValidatorTest {

	@Mock
	private UrlListValidator myListValidator;

	@InjectMocks
	private ReindexJobParametersValidator myValidator;

	@Test
	public void validate_urlWithSpace_fails() {
		List<String> errors = runTestWithUrl("Patient, Task");

		// verify
		assertFalse(errors.isEmpty());
		assertTrue(errors.get(0).contains("Invalid URL. URL cannot contain spaces"));
	}

	private List<String> runTestWithUrl(String theUrl) {
		// setup
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl(theUrl);

		// test
		List<String> errors = myValidator.validate(parameters);

		return errors;
	}
}
