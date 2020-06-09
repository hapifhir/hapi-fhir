package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EmpiRuleValidatorTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();

	private EmpiRuleValidator myEmpiRuleValidator = new EmpiRuleValidator(ourFhirContext);

   @Test
   public void testValidate() throws IOException {
		try {
			setScript("bad-rules-bad-url.json");
			fail();
		} catch (ConfigurationException e){
			assertThat(e.getMessage(), is("Enterprise Identifier System (eidSystem) must be a valid URI"));
		}
   }

	@Test
	public void testNonExistentMatchField() throws IOException {
		try {
			setScript("bad-rules-missing-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("There is no matchField with name foo"));
		}
	}

	@Test
	public void testSimilarityHasThreshold() throws IOException {
		try {
			setScript("bad-rules-missing-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("MatchField given-name metric COSINE requires a matchThreshold"));
		}
	}

	@Test
	public void testMatcherUnusedThreshold() throws IOException {
		try {
			setScript("bad-rules-unused-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("MatchField given-name metric EXACT should not have a matchThreshold"));
		}
	}

	@Test
	public void testMatcherBadPath() throws IOException {
		try {
			setScript("bad-rules-bad-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("MatchField given-name resourceType Patient has invalid path 'name.first'.  Unknown child name 'first' in element Patient"));
		}
	}

	private void setScript(String theTheS) throws IOException {
		EmpiSettings empiSettings = new EmpiSettings(myEmpiRuleValidator);
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theTheS);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		empiSettings.setScriptText(json);
	}

}
