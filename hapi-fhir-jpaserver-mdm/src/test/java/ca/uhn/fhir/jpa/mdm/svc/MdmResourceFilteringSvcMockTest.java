package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MdmResourceFilteringSvcMockTest {

	@MockBean
	private IMdmSettings myMdmSettings;
	@MockBean
	MdmSearchParamSvc myMdmSearchParamSvc;
	@MockBean
	FhirContext myFhirContext;
	@Autowired
	private MdmResourceFilteringSvc myMdmResourceFilteringSvc;

	@Configuration
	static class SpringConfig {
		@Bean
		MdmResourceFilteringSvc mdmResourceFilteringSvc() {
			return new MdmResourceFilteringSvc();
		}
	}

	@Test
	public void testEmptyCriteriaShouldBeProcessed() {
		when(myMdmSettings.getMdmRules()).thenReturn(new MdmRulesJson());
		assertTrue(myMdmResourceFilteringSvc.shouldBeProcessed(new Patient()));
	}
}
