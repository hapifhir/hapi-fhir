package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu.resource.Composition;
import ca.uhn.fhir.util.TestUtil;

public class BaseParserTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParserTest.class);

	private AllergyIntolerance createAllergy(String theId, String theIdentifierValue) {
		AllergyIntolerance retVal = new AllergyIntolerance();
		if (theId != null) {
			retVal.setId(theId);
		}
		return retVal.addIdentifier("urn:system", theIdentifierValue);
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources01() {
		Composition comp = new Composition();
		comp.addAuthor().setReference("Practitioner/123");
		comp.getContained().getContainedResources().add(createAllergy("#AAA", "allergy0"));

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, not(containsString("contained")));

		String json = ourCtx.newJsonParser().encodeResourceToString(comp);
		assertThat(json, not(containsString("contained")));
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources02() {
		Composition comp = new Composition();
		comp.addAuthor().setReference("Practitioner/123");
		comp.getContained().getContainedResources().add(createAllergy("#AAA", "allergy0"));
		comp.getContained().getContainedResources().add(createAllergy("#BBB", "allergy1"));
		comp.addSection().getContent().setReference("#AAA");

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, stringContainsInOrder("<contained>", "id=\"AAA\"", "reference value=\"#AAA\""));
		assertThat(xml, not(containsString("BBB")));

		String json = ourCtx.newJsonParser().encodeResourceToString(comp);
		assertThat(json, stringContainsInOrder("\"contained\"", "\"id\":\"AAA\"", "\"reference\":\"#AAA\""));
		assertThat(json, not(containsString("BBB")));
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources03() {
		Composition comp = new Composition();
		comp.addAuthor().setReference("Practitioner/123");
		AllergyIntolerance allergy = createAllergy("#AAA", "allergy0");
		comp.getContained().getContainedResources().add(allergy);
		comp.addSection().getContent().setResource(allergy);

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, stringContainsInOrder("<contained>", "id=\"AAA\"", "reference value=\"#AAA\""));
		assertThat(xml, not(stringContainsInOrder("AAA", ">", "AAA", ">", "AAA"))); // only once in the contained tag, and once in the reference

		String json = ourCtx.newJsonParser().encodeResourceToString(comp);
		assertThat(json, stringContainsInOrder("\"contained\"", "\"id\":\"AAA\"", "\"reference\":\"#AAA\""));
		assertThat(json, not(stringContainsInOrder("AAA", "\"", "AAA", "\"", "AAA"))); // only once in the contained tag, and once in the reference
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources04() {
		Composition comp = new Composition();
		comp.addAuthor().setReference("Practitioner/999");
		AllergyIntolerance allergy = createAllergy(null, "allergy0");
		comp.getContained().getContainedResources().add(allergy);
		comp.addSection().getContent().setResource(allergy);

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, stringContainsInOrder("<contained>", "id=\"1\"", "reference value=\"#1\""));
		assertThat(xml, not(stringContainsInOrder("1", ">", "1", ">", "1"))); // only once in the contained tag, and once in the reference

		String json = ourCtx.newJsonParser().encodeResourceToString(comp);
		assertThat(json, stringContainsInOrder("\"contained\"", "\"id\":\"1\"", "\"reference\":\"#1\""));
		assertThat(json, not(stringContainsInOrder("1", "\"", "1", "\"", "1"))); // only once in the contained tag, and once in the reference
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources05() {
		Composition comp = new Composition();
		comp.addAuthor().setReference("Practitioner/999");
		AllergyIntolerance allergy = createAllergy(null, "allergy0");
		comp.addSection().getContent().setResource(allergy);

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, stringContainsInOrder("<contained>", "id=\"1\"", "reference value=\"#1\""));
		assertThat(xml, not(stringContainsInOrder("1", ">", "1", ">", "1"))); // only once in the contained tag, and once in the reference

		String json = ourCtx.newJsonParser().encodeResourceToString(comp);
		assertThat(json, stringContainsInOrder("\"contained\"", "\"id\":\"1\"", "\"reference\":\"#1\""));
		assertThat(json, not(stringContainsInOrder("1", "\"", "1", "\"", "1"))); // only once in the contained tag, and once in the reference
	}

	/**
	 * See #103
	 */
	@Test
	public void testDontEncodeUnnecessaryContainedResources06() {
		Composition comp = new Composition();
		comp.addSection().getContent().setResource(new AllergyIntolerance().addIdentifier("foo", "bar"));
		comp.addSection().getContent().setResource(new AllergyIntolerance().addIdentifier("foo", "bar"));
		comp.addSection().getContent().setResource(new AllergyIntolerance().addIdentifier("foo", "bar"));

		String string = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(comp);
		ourLog.info(string);

		String xml = ourCtx.newXmlParser().encodeResourceToString(comp);
		assertThat(xml, stringContainsInOrder("<contained>", "id=\"1\"", "reference value=\"#1\""));

	}

	/**
	 * See #120
	 */
	@Test
	public void testParseExtensionWithBoundCodeType() {
		//@formatter:off
		String resText = 
			"<DiagnosticReport xmlns=\"http://hl7.org/fhir\">" +
			"  <extension url=\"http://agfa.com/extensions#workflowAction\">" +
			"    <valueCode value=\"sign-off\"/>" +
			"  </extension>" +
			"</DiagnosticReport>";
		//@formatter:on
		
		MyDiagnosticReportWithBoundCodeExtension res = ourCtx.newXmlParser().parseResource(MyDiagnosticReportWithBoundCodeExtension.class, resText);
		assertEquals(WorkflowActionEnum.SIGNOFF, res.getWorkflowAction().getValueAsEnum());
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
