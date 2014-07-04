package ca.uhn.fhir.narrative;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.util.Date;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class DefaultThymeleafNarrativeGeneratorTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultThymeleafNarrativeGeneratorTest.class);
	private DefaultThymeleafNarrativeGenerator gen;

	@Before
	public void before() {
		gen = new DefaultThymeleafNarrativeGenerator();
		gen.setUseHapiServerConformanceNarrative(true);
		gen.setIgnoreFailures(false);
		gen.setIgnoreMissingTemplates(false);
	}

	@Test
	public void testGeneratePatient() throws DataFormatException {
		Patient value = new Patient();

		value.addIdentifier().setSystem("urn:names").setValue("123456");
		value.addName().addFamily("blow").addGiven("joe").addGiven("john");
		value.getAddressFirstRep().addLine("123 Fake Street").addLine("Unit 1");
		value.getAddressFirstRep().setCity("Toronto").setState("ON").setCountry("Canada");

		value.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);

		String output = gen.generateNarrative(value).getDiv().getValueAsString();
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> joe john <b>BLOW </b></div>"));
		
		String title = gen.generateTitle(value);
		assertEquals("joe john BLOW (123456)", title);
		ourLog.info(title);
	}

	@Test
	public void testGenerateServerConformance() throws DataFormatException {
		Conformance value = new FhirContext().newXmlParser().parseResource(Conformance.class, new InputStreamReader(getClass().getResourceAsStream("/server-conformance-statement.xml")));
		
		String output = gen.generateNarrative(value).getDiv().getValueAsString();

		ourLog.info(output);
	}

	
	@Test
	public void testGenerateDiagnosticReport() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();
		value.getName().setText("Some Diagnostic Report");

		value.addResult().setReference("Observation/1");
		value.addResult().setReference("Observation/2");
		value.addResult().setReference("Observation/3");

		String output = gen.generateNarrative("http://hl7.org/fhir/profiles/DiagnosticReport", value).getDiv().getValueAsString();

		ourLog.info(output);
	}

	@Test
	public void testGenerateDiagnosticReportWithObservations() throws DataFormatException {
		DiagnosticReport value = new DiagnosticReport();

		value.getIssued().setValueAsString("2011-02-22T11:13:00");
		value.setStatus(DiagnosticReportStatusEnum.FINAL);

		value.getName().setText("Some Diagnostic Report");
		{
			Observation obs = new Observation();
			obs.getName().addCoding().setCode("1938HB").setDisplay("Hemoglobin");
			obs.setValue(new QuantityDt(null, 2.223, "mg/L"));
			obs.addReferenceRange().setLow(new QuantityDt(2.20)).setHigh(new QuantityDt(2.99));
			obs.setStatus(ObservationStatusEnum.FINAL);
			obs.setComments("This is a result comment");

			ResourceReferenceDt result = value.addResult();
			result.setResource(obs);
		}
		{
			Observation obs = new Observation();
			obs.setValue(new StringDt("HELLO!"));
			value.addResult().setResource(obs);
		}
		NarrativeDt generateNarrative = gen.generateNarrative("http://hl7.org/fhir/profiles/DiagnosticReport", value);
		String output = generateNarrative.getDiv().getValueAsString();

		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> Some Diagnostic Report </div>"));

		String title = gen.generateTitle(value);
		ourLog.info(title);
		assertEquals("Some Diagnostic Report - final - 2 observations", title);

		
		// Now try it with the parser

		FhirContext context = new FhirContext();
		context.setNarrativeGenerator(gen);
		output = context.newXmlParser().setPrettyPrint(true).encodeResourceToString(value);
		ourLog.info(output);
		assertThat(output, StringContains.containsString("<div class=\"hapiHeaderText\"> Some Diagnostic Report </div>"));

	}

}
