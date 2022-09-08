package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoDstu3StructureDefinitionTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3StructureDefinitionTest.class);

	@AfterEach
	public final void after() {
	}

	@Test
	public void testGenerateSnapshot() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		assertEquals(0, sd.getSnapshot().getElement().size());

		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(sd, "http://foo", null, "THE BEST PROFILE");
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(54, output.getSnapshot().getElement().size());
	}


}
