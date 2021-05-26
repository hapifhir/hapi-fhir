package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig.JOB_PARAM_URL_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteExpungeJobParameterValidatorTest {
	static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Mock
	MatchUrlService myMatchUrlService;

	DeleteExpungeJobParameterValidator mySvc;

	@BeforeEach
	public void initMocks() {
		mySvc = new DeleteExpungeJobParameterValidator(ourFhirContext, myMatchUrlService);
	}

	@Test
	public void testValidate() throws JobParametersInvalidException, JsonProcessingException {
		// setup
		JobParameters parameters = buildJobParameters("Patient?address=memory", "Patient?name=smith");
		when(myMatchUrlService.translateMatchUrl(any(), any())).thenReturn(new SearchParameterMap());

		// execute
		mySvc.validate(parameters);
		// verify
		verify(myMatchUrlService, times(2)).translateMatchUrl(any(), any());
	}

	@Test
	public void testValidateBadType() throws JobParametersInvalidException, JsonProcessingException {
		JobParameters parameters = buildJobParameters("Buffy?vampire=false");

		try {
			mySvc.validate(parameters);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"Buffy\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Nonnull
	private JobParameters buildJobParameters(String... theUrls) {
		Map<String, JobParameter> map = new HashMap<>();
		UrlListJson urlListJson = UrlListJson.fromUrlStrings(theUrls);
		map.put(JOB_PARAM_URL_LIST, new JobParameter(urlListJson.toString()));
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}
}
