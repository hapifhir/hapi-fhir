package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.config.TestEmpiConfig;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestEmpiConfig.class})
public class EmpiRuleValidatorImplTest {

	@Autowired
	private IEmpiRuleValidator myEmpiRuleValidator;

   @Test
   public void testValidate() {
   	String invalidUri = "invalid uri";
		EmpiRulesJson sampleEmpiRulesJson = new EmpiRulesJson();
		sampleEmpiRulesJson.setEnterpriseEIDSystem(invalidUri);

		try {
			myEmpiRuleValidator.validate(sampleEmpiRulesJson);
			fail();
		} catch (ConfigurationException e){
			assertThat(e.getMessage(), is(equalTo("Enterprise Identifier System (eidSystem) must be a valid URI")));

		}

   }
}
