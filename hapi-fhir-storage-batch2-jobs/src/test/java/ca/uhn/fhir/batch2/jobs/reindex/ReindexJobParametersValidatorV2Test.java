package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.v2.ReindexJobParametersValidatorV2;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReindexJobParametersValidatorV2Test {

	@Mock
	private UrlListValidator myListValidator;

	@InjectMocks
	private ReindexJobParametersValidatorV2 myValidator;

	@ParameterizedTest
	@ValueSource(strings = { "\n", " ", "\t" })
	public void validate_urlWithSpace_fails(String theWhiteSpaceChar) {
		List<String> errors = runTestWithUrl("Patient," + theWhiteSpaceChar + "Practitioner");

		// verify
		assertThat(errors).isNotEmpty();
		assertThat(errors.get(0)).contains("Invalid URL. URL cannot contain spaces");
	}

	@Test
	public void correctCurrentVersionCantBeCombinedWithOptimisticLocking() {
		ReindexJobParameters params = new ReindexJobParameters();
		params.setOptimisticLock(true);
		params.setCorrectCurrentVersion(ReindexParameters.CorrectCurrentVersionModeEnum.ALL);
		List<String> outcome = myValidator.validate(new SystemRequestDetails(), params);
		assertThat(outcome).containsExactly("Optimistic locking cannot be enabled when correcting current versions");
	}

	private List<String> runTestWithUrl(String theUrl) {
		// setup
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl(theUrl);

		// test
		return myValidator.validate(null, parameters);
	}
}
