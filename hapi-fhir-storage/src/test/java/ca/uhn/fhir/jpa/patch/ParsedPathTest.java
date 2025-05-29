package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import org.junit.jupiter.api.Test;

public class ParsedPathTest {
	org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParsedPathTest.class);

	FhirContext myContext = FhirContext.forR4Cached();

	@Test
	public void test() throws Exception {
		String path = "Appointment.participant.actor.where(reference.startsWith('Patient/')).first()";

		ParsedPath parsedPath = ParsedPath.parse(path);

		ourLog.info("done");
	}
}
