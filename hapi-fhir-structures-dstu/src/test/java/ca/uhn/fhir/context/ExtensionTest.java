package ca.uhn.fhir.context;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.MyPatient;

public class ExtensionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExtensionTest.class);

	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testExtensionType() {
		
		DummyPatientWithExtensions patient = new DummyPatientWithExtensions();
		patient.setPetName(new StringDt("Fido"));
		patient.getImportantDates().add(new DateTimeDt("2010-01-02"));
		patient.getImportantDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");

		String messageString = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info(messageString);
		assertThat(messageString, containsString("<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2010-01-02\"/></modifierExtension>"));
	}

	@Test
	public void testEmptyExtensionType() {
		DummyPatientWithExtensions patient = new DummyPatientWithExtensions();
		patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");
		String messageString = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info(messageString);
		
		assertThat(messageString, not(containsString("xtension")));

	}


	@Test
	public void testEmptyExtensionTypeJson() {
		DummyPatientWithExtensions patient = new DummyPatientWithExtensions();
		patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");
		String messageString = ourCtx.newJsonParser().encodeResourceToString(patient);
		ourLog.info(messageString);
		
		assertThat(messageString, not(containsString("xtension")));

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws DataFormatException, IOException {
		// START SNIPPET: patientUse
		MyPatient patient = new MyPatient();
		patient.setPetName(new StringDt("Fido"));
		patient.getImportantDates().add(new DateTimeDt("2010-01-02"));
		patient.getImportantDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");

		IParser p = FhirContext.forDstu1().newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(patient);

		System.out.println(messageString);
		// END SNIPPET: patientUse

		// START SNIPPET: patientParse
		IParser parser = FhirContext.forDstu1().newXmlParser();
		MyPatient newPatient = parser.parseResource(MyPatient.class, messageString);
		// END SNIPPET: patientParse

		{
			FhirContext ctx2 = FhirContext.forDstu1();
			RuntimeResourceDefinition def = ctx2.getResourceDefinition(patient);
			System.out.println(ctx2.newXmlParser().setPrettyPrint(true).encodeResourceToString(def.toProfile("http://foo.org/fhir")));
		}
	}

}
