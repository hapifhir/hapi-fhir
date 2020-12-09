package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
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
class EmpiResourceFilteringSvcMockTest {
	@MockBean
	private IEmpiSettings myEmpiSettings;
	@MockBean
	EmpiSearchParamSvc myEmpiSearchParamSvc;
	@MockBean
	FhirContext myFhirContext;
	@Autowired
	private EmpiResourceFilteringSvc myEmpiResourceFilteringSvc;

	@Configuration
	static class SpringConfig {
		@Bean EmpiResourceFilteringSvc empiResourceFilteringSvc() {
			return new EmpiResourceFilteringSvc();
		}
	}

	@Test
	public void testEmptyCriteriaShouldBeProcessed() {
		when(myEmpiSettings.getEmpiRules()).thenReturn(new EmpiRulesJson());
		assertTrue(myEmpiResourceFilteringSvc.shouldBeProcessed(new Patient()));
	}
}
