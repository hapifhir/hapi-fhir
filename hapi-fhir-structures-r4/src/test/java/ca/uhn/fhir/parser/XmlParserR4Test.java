package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class XmlParserR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(XmlParserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	private Bundle createBundleWithPatient() {
		Bundle b = new Bundle();
		b.setId("BUNDLEID");
		b.getMeta().addProfile("http://FOO");

		Patient p = new Patient();
		p.setId("PATIENTID");
		p.getMeta().addProfile("http://BAR");
		p.addName().addGiven("GIVEN");
		b.addEntry().setResource(p);
		return b;
	}

	@Test
	public void testParseAndEncodeXmlNumericEntity() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"    <name>\n" +
			"       <family value=\"A &#xA; B\"/>\n" +
			"    </name>\n" +
			"</Patient>";

		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, input);
		assertEquals("A \n B", p.getNameFirstRep().getFamily());

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

	}

	@Test
	public void testExcludeNothing() {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
//		excludes.add("*.id");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, containsString("BUNDLEID"));
		assertThat(encoded, containsString("http://FOO"));
		assertThat(encoded, containsString("PATIENTID"));
		assertThat(encoded, containsString("http://BAR"));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeRootStuff() {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("id");
		excludes.add("meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, (containsString("PATIENTID")));
		assertThat(encoded, (containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeStarDotStuff() {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("*.id");
		excludes.add("*.meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, not(containsString("PATIENTID")));
		assertThat(encoded, not(containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertNotEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testParseAndEncodeExtensionWithValueWithExtension() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"    <extension url=\"https://purl.org/elab/fhir/network/StructureDefinition/1/BirthWeight\">\n" +
			"       <valueDecimal>\n" +
			"          <extension url=\"http://www.hl7.org/fhir/extension-data-absent-reason.html\">\n" +
			"            <valueCoding>\n" +
			"                <system value=\"http://hl7.org/fhir/ValueSet/birthweight\"/>\n" +
			"                <code value=\"Underweight\"/>\n" +
			"                <userSelected value=\"false\"/>\n" +
			"            </valueCoding>\n" +
			"          </extension>\n" +
			"       </valueDecimal>\n" +
			"    </extension>\n" +
			"    <identifier>\n" +
			"       <system value=\"https://purl.org/elab/fhir/network/StructureDefinition/1/EuroPrevallStudySubjects\"/>\n" +
			"       <value value=\"1\"/>\n" +
			"    </identifier>\n" +
			"    <gender value=\"female\"/>\n" +
			"</Patient>";

		IParser xmlParser = ourCtx.newXmlParser();
		IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));
		xmlParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));

		Patient parsed = xmlParser.parseResource(Patient.class, input);

		ourLog.info(jsonParser.setPrettyPrint(true).encodeResourceToString(parsed));
		assertThat(xmlParser.encodeResourceToString(parsed), containsString("Underweight"));
		assertThat(jsonParser.encodeResourceToString(parsed), containsString("Underweight"));

	}

	@Test
	public void testEncodeContainedIdsAreRenumbered() {


		DiagnosticReport dr = new DiagnosticReport();

		// Put a contained that isn't referenced anywhere
		dr.getContained().add(new Specimen().setId("#1"));

		// Add a specimen that is actually referenced
		Specimen spec = new Specimen();
		spec.addNote().setAuthor(new StringType("FOO"));
		dr.addSpecimen().setResource(spec);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(dr);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder(
			"<contained>",
			"<id value=\"1\"/>",
			"</contained>",
			"<reference value=\"#1\"/>"
		));
	}

	/**
	 * See #11
	 */
	@Test
	public void testDuplicateContainedResources() {

		Observation resA = new Observation();
		resA.setComment("A");

		Observation resB = new Observation();
		resB.setComment("B");
		resB.addHasMember(new Reference(resA));
		resB.addHasMember(new Reference(resA));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resB);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "</contained>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "<Observation", "</contained>"))));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
