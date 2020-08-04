package ca.uhn.fhir.empi.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.BaseR4Test;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiRuleValidatorTest extends BaseR4Test {
   @Test
   public void testValidate() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-bad-url.json");
			fail();
		} catch (ConfigurationException e){
			assertThat(e.getMessage(), is("Enterprise Identifier System (eidSystem) must be a valid URI"));
		}
   }

	@Test
	public void testNonExistentMatchField() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-missing-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("There is no matchField with name foo"));
		}
	}

	@Test
	public void testSimilarityHasThreshold() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-missing-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("MatchField given-name metric COSINE requires a matchThreshold"));
		}
	}

	@Test
	public void testMatcherUnusedThreshold() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-unused-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("MatchField given-name metric STRING should not have a matchThreshold"));
		}
	}

	@Test
	public void testMatcherBadPath() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-bad-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("MatchField given-name resourceType Patient has invalid path 'name.first'.  Unknown child name 'first' in element HumanName"));
		}
	}

	@Test
	public void testMatcherBadSearchParam() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-bad-searchparam.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Error in candidateSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testMatcherBadFilter() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-bad-filter.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Error in candidateFilterSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testMatcherduplicateName() throws IOException {
		try {
			setEmpiRuleJson("bad-rules-duplicate-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Two MatchFields have the same name 'foo'"));
		}
	}


	private void setEmpiRuleJson(String theTheS) throws IOException {
		EmpiRuleValidator empiRuleValidator = new EmpiRuleValidator(ourFhirContext, mySearchParamRetriever);
		EmpiSettings empiSettings = new EmpiSettings(empiRuleValidator);
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theTheS);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		empiSettings.setScriptText(json);
	}

}
