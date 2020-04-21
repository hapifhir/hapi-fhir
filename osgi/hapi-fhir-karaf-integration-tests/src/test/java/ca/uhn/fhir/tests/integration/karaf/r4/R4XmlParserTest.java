package ca.uhn.fhir.tests.integration.karaf.r4;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.google.common.collect.Sets;
import org.hamcrest.core.IsNot;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;


/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class R4XmlParserTest {

	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(R4XmlParserTest.class);
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
	public void testExcludeStarDotStuff() {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("*.id");
		excludes.add("*.meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, IsNot.not(containsString("BUNDLEID")));
		assertThat(encoded, IsNot.not(containsString("http://FOO")));
		assertThat(encoded, IsNot.not(containsString("PATIENTID")));
		assertThat(encoded, IsNot.not(containsString("http://BAR")));
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

}
