package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoDstu3StructureDefinitionTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3StructureDefinitionTest.class);

	@AfterEach
	public final void after() {
	}

	@Test
	public void testGenerateSnapshot() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		assertThat(sd.getSnapshot().getElement()).isEmpty();

		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(sd, "http://foo", null, "THE BEST PROFILE");
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getSnapshot().getElement()).hasSize(54);
	}


}
