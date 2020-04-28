package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.config.TestEmpiConfig;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@ContextConfiguration(classes = {TestEmpiConfig.class})
public class EmpiRuleValidatorTest {
	private EmpiRuleValidator myEmpiRuleValidator = new EmpiRuleValidator();

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
