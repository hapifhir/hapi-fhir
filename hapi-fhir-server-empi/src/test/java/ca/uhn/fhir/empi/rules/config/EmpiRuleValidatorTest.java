package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
			assertThat(e.getMessage(), is("Enterprise Identifier System (eidSystem) must be a valid URI"));
		}
   }
   
   @Test
	public void testNonExistentMatchField() throws IOException {
		EmpiSettings empiSettings = new EmpiSettings();
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("bad-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		try {
			empiSettings.setScriptText(json);
			fail();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("There is no matchField with name foo"));
		}
	}
}
