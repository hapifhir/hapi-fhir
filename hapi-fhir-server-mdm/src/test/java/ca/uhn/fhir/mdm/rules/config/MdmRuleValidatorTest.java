package ca.uhn.fhir.mdm.rules.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.BaseR4Test;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
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
			assertThat(e.getMessage(), is(Msg.code(1519) + "Enterprise Identifier System (eidSystem) must be a valid URI"));
		}
   }

	@Test
	public void testNonExistentMatchField() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is(Msg.code(1523) + "There is no matchField with name foo"));
		}
	}

	@Test
	public void testSimilarityHasThreshold() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-threshold.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), is(Msg.code(1514) + "MatchField given-name similarity COSINE requires a matchThreshold"));
		}
	}

	@Test
	public void testMatcherBadPath() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1517) + "MatchField given-name resourceType Patient has invalid path 'name.first'.  "+ Msg.code(1700) + "Unknown child name 'first' in element HumanName"));
		}
	}

	@Test
	public void testMatcherBadFhirPath() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-fhirpath.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1518) + "MatchField [given-name] resourceType [Patient] has failed FHIRPath evaluation.  Error in ?? at 1, 1: The name blurst is not a valid function name"));
		}
	}

	@Test
	public void testBadRulesMissingBothPaths() throws IOException {
		try {
			setMdmRuleJson("bad-rules-no-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1516) + "MatchField [given-name] resourceType [Patient] has defined neither a resourcePath or a fhirPath. You must define one of the two."));
		}
	}

	@Test
	public void testBadRulesBothPathsFilled() throws IOException {
		try {
			setMdmRuleJson("bad-rules-both-paths.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1515) + "MatchField [given-name] resourceType [Patient] has defined both a resourcePath and a fhirPath. You must define one of the two."));
		}
	}

	@Test
	public void testMatcherBadSearchParam() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-searchparam.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1511) + "Error in candidateSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testMatcherBadFilter() throws IOException {
		try {
			setMdmRuleJson("bad-rules-bad-filter.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1511) + "Error in candidateFilterSearchParams: Patient does not have a search parameter called 'foo'"));
		}
	}

	@Test
	public void testInvalidMdmType() throws IOException {
		try {
			setMdmRuleJson("bad-rules-missing-mdm-types.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1509) + "mdmTypes must be set to a list of resource types."));
		}
	}

	@Test
	public void testMatcherduplicateName() throws IOException {
		try {
			setMdmRuleJson("bad-rules-duplicate-name.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1512) + "Two MatchFields have the same name 'foo'"));
		}
	}

	@Test
	public void testInvalidPath() throws IOException {
		try {
			setMdmRuleJson("bad-rules-invalid-path.json");
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1517) + "MatchField name-prefix resourceType Organization has invalid path"));
		}
	}

	@Test
	public void testMatcherExtensionJson() throws IOException {
		try {
			setMdmRuleJson("rules-extension-search.json");
		}
		catch (ConfigurationException e){
			fail("Unable to validate extension matcher");
		}
	}

	@Test
	public void testBadEidResourceType() throws IOException {
		try {
			setMdmRuleJson("bad-rules-illegal-resource-type-eid.json");
		}
		catch (ConfigurationException e){
			assertThat(e.getMessage(), is(equalTo(Msg.code(1508) + "not-a-resource is not a valid resource type, but is set in the eidSystems field.")));
		}
	}
	@Test
	public void testBadEidDoesntMatchKnownMdmTypes() throws IOException {
		try {
			setMdmRuleJson("bad-rules-illegal-missing-resource-type.json");
		}
		catch (ConfigurationException e){
			assertThat(e.getMessage(), is(equalTo(Msg.code(1507) + "There is an eidSystem set for [Patient] but that is not one of the mdmTypes. Valid options are [Organization, *].")));
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
