package ca.uhn.fhir.mdm.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.BaseR4Test;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MdmRuleValidatorTest extends BaseR4Test {

	@BeforeEach
	public void before() {
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("AllergyIntolerance", "identifier")).thenReturn(null);
		when(mySearchParamRetriever.getActiveSearchParam("Organization", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Organization", "active")).thenReturn(mock(RuntimeSearchParam.class));
	}

   @Test
   public void testValidate() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-url.json");
			fail();
		} catch (ConfigurationException e){
			assertThat(e.getMessage(), is("Enterprise Identifier System (eidSystem) must be a valid URI"));
		}
   }

	@Test
	public void testNonExistentMatchField() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("There is no matchField with name foo"));
		}
	}

	@Test
	public void testSimilarityHasThreshold() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is("MatchField given-name similarity COSINE requires a matchThreshold"));
		}
	}

	@Test
	public void testMatcherBadPath() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("MatchField given-name resourceType Patient has invalid path 'name.first'.  Unknown child name 'first' in element HumanName"));
		}
	}

	@Test
	public void testMatcherBadSearchParam() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-searchparam.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Error in candidateSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testMatcherBadFilter() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-filter.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Error in candidateFilterSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testInvalidMdmType() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-mdm-types.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("mdmTypes must be set to a list of resource types."));
		}
	}

	@Test
	public void testMatcherduplicateName() throws IOException {
		try {
			setMdmRuleJson("bad-rules-duplicate-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("Two MatchFields have the same name 'foo'"));
		}
	}

	@Test
	public void testInvalidPath() throws IOException {
		try {
			setMdmRuleJson("bad-rules-invalid-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith("MatchField name-prefix resourceType Organization has invalid path"));
		}
	}

	private void setMdmRuleJson(String theTheS) throws IOException {
		MdmRuleValidator mdmRuleValidator = new MdmRuleValidator(ourFhirContext, mySearchParamRetriever);
		MdmSettings mdmSettings = new MdmSettings(mdmRuleValidator);
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(theTheS);
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		mdmSettings.setScriptText(json);
	}

}
