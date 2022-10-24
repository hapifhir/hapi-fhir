package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
		MdmRulesJson rules = new MdmRulesJson();
		rules.setMdmTypes(List.of("Patient"));
		when(myMdmSettings.getMdmRules()).thenReturn(rules);


		MdmSubmitJobParameters parameters = new MdmSubmitJobParameters();
		parameters.addUrl("Practitioner?name=foo");
		myValidator.validate(parameters);

	}

}
