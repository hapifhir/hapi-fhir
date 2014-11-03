package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Appointment;
import ca.uhn.fhir.model.primitive.DateTimeDt;


public class MixedResourcesTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MixedResourcesTest.class);
	@Test
	public void testMixedResources() {
		
		// dstu
		Appointment a1 = new Appointment();
		a1.getSchedule().addEvent().setStart(new DateTimeDt("2001-01-02T12:00:00"));
		
		ca.uhn.fhir.model.dev.resource.Appointment a2 = new ca.uhn.fhir.model.dev.resource.Appointment();
		a2.getStartElement().setValueAsString("2001-01-02T12:00:00");
		
		IParser parser = new FhirContext().newXmlParser();
		String string = parser.encodeResourceToString(a1);
		ourLog.info(string);
		assertEquals("<Appointment xmlns=\"http://hl7.org/fhir\"><schedule><event><start value=\"2001-01-02T12:00:00\"/></event></schedule></Appointment>", string);

		string = parser.encodeResourceToString(a2);
		ourLog.info(string);
		assertEquals("<Appointment xmlns=\"http://hl7.org/fhir\"><start value=\"2001-01-02T12:00:00\"/></Appointment>", string);

	}
}
