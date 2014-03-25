package ca.uhn.fhir.narrative;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ObservationStatusEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ThymeleafNarrativeGeneratorTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ThymeleafNarrativeGeneratorTest.class);
	private ThymeleafNarrativeGenerator gen;

	@Before
	public void before() throws IOException {
		gen = new ThymeleafNarrativeGenerator();
		gen.setIgnoreFailures(false);
	}
	
	@Test
	public void testGeneratePatient() throws DataFormatException, InternalErrorException {
		Patient value = new Patient();
		
		value.addIdentifier().setSystem("urn:names").setValue("123456");
		value.addName().addFamily("blow").addGiven("joe").addGiven("john");
		value.getAddressFirstRep().addLine("123 Fake Street").addLine("Unit 1");
		value.getAddressFirstRep().setCity("Toronto").setState("ON").setCountry("Canada");
		
		value.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);
		
		String output = gen.generateNarrative("http://hl7.org/fhir/profiles/Patient", value).getDiv().getValueAsString();

		ourLog.info(output);
	}

	@Test
	public void testGenerateDiagnosticReport() throws DataFormatException, InternalErrorException {
		DiagnosticReport value = new DiagnosticReport();
		value.getName().setText("Some Diagnostic Report");

		value.addResult().setReference("Observation/1");
		value.addResult().setReference("Observation/2");
		value.addResult().setReference("Observation/3");

		String output = gen.generateNarrative("http://hl7.org/fhir/profiles/DiagnosticReport", value).getDiv().getValueAsString();

		ourLog.info(output);
	}

	@Test
	public void testGenerateDiagnosticReportWithObservations() throws DataFormatException, InternalErrorException {
		DiagnosticReport value = new DiagnosticReport();
		value.getName().setText("Some Diagnostic Report");

		Observation obs = new Observation();
		obs.getName().addCoding().setCode("1938HB").setDisplay("Hemoglobin");
		obs.setValue(new QuantityDt(null, 2.223, "mg/L"));		
		obs.addReferenceRange().setLow(new QuantityDt(2.20)).setHigh(new QuantityDt(2.99));
		obs.setStatus(ObservationStatusEnum.FINAL);
		obs.setComments("This is a result comment");
		
		ResourceReferenceDt result = value.addResult();
		result.setResource(obs);

		NarrativeDt generateNarrative = gen.generateNarrative("http://hl7.org/fhir/profiles/DiagnosticReport", value);
		String output = generateNarrative.getDiv().getValueAsString();

		ourLog.info(output);
	}

}
