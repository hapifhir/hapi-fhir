package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@SuppressWarnings({"unchecked", "deprecation"})
public class FhirResourceDaoR4StructureDefinitionTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4StructureDefinitionTest.class);

	@After
	public final void after() {
	}

	@Test
	public void testGenerateSnapshot() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		assertEquals(0, sd.getSnapshot().getElement().size());

		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(sd, "http://foo", null, "THE BEST PROFILE");
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(51, output.getSnapshot().getElement().size());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
