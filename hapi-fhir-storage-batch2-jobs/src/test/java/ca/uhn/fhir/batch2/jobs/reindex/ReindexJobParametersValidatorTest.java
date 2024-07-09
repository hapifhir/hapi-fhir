package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReindexJobParametersValidatorTest {

	@Mock
	private UrlListValidator myListValidator;

	@InjectMocks
	private ReindexJobParametersValidator myValidator;

	@ParameterizedTest
	@ValueSource(strings = { "\n", " ", "\t" })
	public void validate_urlWithSpace_fails(String theWhiteSpaceChar) {
		List<String> errors = runTestWithUrl("Patient," + theWhiteSpaceChar + "Practitioner");

		// verify
		assertThat(errors).isNotEmpty();
		assertThat(errors.get(0)).contains("Invalid URL. URL cannot contain spaces");
	}

	private List<String> runTestWithUrl(String theUrl) {
		// setup
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl(theUrl);

		// test
		return myValidator.validate(null, parameters);
	}
}
