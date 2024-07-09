package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MdmSubmitJobParametersValidatorTest {

	@InjectMocks
	MdmSubmitJobParametersValidator myValidator;
	@Mock
	private FhirContext myFhirContext;
	@Mock
	private IMdmSettings myMdmSettings;
	@Mock
	private MatchUrlService myMatchUrlService;

	@BeforeEach
	public void before() {
		myFhirContext = FhirContext.forR4Cached();
		myValidator = new MdmSubmitJobParametersValidator(myMdmSettings, myMatchUrlService, myFhirContext);
	}

	@Test
	public void testUnusedMdmResourceTypesAreNotAccepted() {
		when(myMdmSettings.isSupportedMdmType(anyString())).thenReturn(false);

		MdmSubmitJobParameters parameters = new MdmSubmitJobParameters();
		parameters.addUrl("Practitioner?name=foo");
		List<String> errors = myValidator.validate(null, parameters);
		assertThat(errors).hasSize(1);
		assertEquals("Resource type Practitioner is not supported by MDM. Check your MDM settings", errors.get(0));
	}

	@Test
	public void testMissingSearchParameter() {
		when(myMdmSettings.isSupportedMdmType(anyString())).thenReturn(true);
		when(myMatchUrlService.translateMatchUrl(anyString(), any())).thenThrow(new InvalidRequestException("Can't find death-date!"));
		MdmSubmitJobParameters parameters = new MdmSubmitJobParameters();
		parameters.addUrl("Practitioner?death-date=foo");
		List<String> errors = myValidator.validate(null, parameters);
		assertThat(errors).hasSize(1);
		assertEquals("Invalid request detected: Can't find death-date!", errors.get(0));
	}

}
