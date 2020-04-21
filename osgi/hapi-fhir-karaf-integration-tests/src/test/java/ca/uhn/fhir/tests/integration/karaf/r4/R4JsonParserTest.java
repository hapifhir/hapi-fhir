package ca.uhn.fhir.tests.integration.karaf.r4;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_R4;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;


/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class R4JsonParserTest {

	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(R4JsonParserTest.class);
	private FhirContext ourCtx = FhirContext.forR4();

   @Configuration
   public Option[] config() throws IOException {
      return options(
      	KARAF.option(),
			WRAP.option(),
			HAPI_FHIR_R4.option(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			when(false)
         	.useOptions(
            	debugConfiguration("5005", true))
      );
   }

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

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwice() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertNotEquals(-1, idx);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwiceWithManualIds() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		mr.setId("#MR");
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		med.setId("#MED");
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertNotEquals(-1, idx);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	@Test
	public void testExcludeNothing() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
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


	/**
	 * Test that long JSON strings don't get broken up
	 */
	@Test
	public void testNoBreakInLongString() {
		String longString = StringUtils.leftPad("", 100000, 'A');

		Patient p = new Patient();
		p.addName().setFamily(longString);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);

		assertThat(encoded, containsString(longString));
	}


	@Test
	public void testSetDontEncodeResourcesWithMetaSubPath() {
		Patient p = new Patient();
		p.setId("AAA");
		p.getMeta().setVersionId("BBB");
		p.getMeta().setLastUpdatedElement(new InstantType("2011-01-01T00:00:00.000Z"));
		p.getMeta().addTag().setSystem("SYS").setCode("CODE");
		p.addName().setFamily("FAMILY");

		IParser parser = ourCtx.newJsonParser();
		parser.setDontEncodeElements(Sets.newHashSet("id", "*.meta.versionId", "*.meta.lastUpdated"));
		String output = parser.encodeResourceToString(p);

		assertThat(output, containsString("FAMILY"));
		assertThat(output, containsString("SYS"));
		assertThat(output, containsString("CODE"));
		assertThat(output, not(containsString("AAA")));
		assertThat(output, not(containsString("BBB")));
		assertThat(output, not(containsString("2011")));
	}

	@Test
	public void testParseAndEncodeExtensionWithValueWithExtension() {
		String input = "{\n" +
			"  \"resourceType\": \"Patient\",\n" +
			"  \"extension\": [\n" +
			"    {\n" +
			"      \"url\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/BirthWeight\",\n" +
			"      \"_valueDecimal\": {\n" +
			"        \"extension\": [\n" +
			"          {\n" +
			"            \"url\": \"http://www.hl7.org/fhir/extension-data-absent-reason.html\",\n" +
			"            \"valueCoding\": {\n" +
			"              \"system\": \"http://hl7.org/fhir/ValueSet/birthweight\",\n" +
			"              \"code\": \"Underweight\",\n" +
			"              \"userSelected\": false\n" +
			"            }\n" +
			"          }\n" +
			"        ]\n" +
			"      }\n" +
			"    }\n" +
			"  ],\n" +
			"  \"identifier\": [\n" +
			"    {\n" +
			"      \"system\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/EuroPrevallStudySubjects\",\n" +
			"      \"value\": \"1\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"gender\": \"female\"\n" +
			"}";

		IParser jsonParser = ourCtx.newJsonParser();
		IParser xmlParser = ourCtx.newXmlParser();
		jsonParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));
		xmlParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));

		Patient parsed = jsonParser.parseResource(Patient.class, input);

		ourLog.info(jsonParser.setPrettyPrint(true).encodeResourceToString(parsed));
		assertThat(xmlParser.encodeResourceToString(parsed), containsString("Underweight"));
		assertThat(jsonParser.encodeResourceToString(parsed), containsString("Underweight"));

	}

	@Test
	public void testParseExtensionOnPrimitive() throws IOException {
		String input = IOUtils.toString(R4JsonParserTest.class.getResourceAsStream("/extension-on-line.txt"));
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Patient pt = parser.parseResource(Patient.class, input);

		StringType line0 = pt.getAddressFirstRep().getLine().get(0);
		assertEquals("535 Sheppard Avenue West, Unit 1907", line0.getValue());
		Extension houseNumberExt = line0.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber");
		assertEquals("535", ((StringType) houseNumberExt.getValue()).getValue());
	}

}
