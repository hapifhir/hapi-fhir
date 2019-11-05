package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
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
		StructureDefinition differential = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		assertEquals(0, differential.getSnapshot().getElement().size());

		// Create a validation chain that includes default validation support and a
		// snapshot generator
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirCtx, defaultSupport);
		ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

		// Generate the snapshot
		StructureDefinition snapshot = chain.generateSnapshot(differential, "http://foo", null, "THE BEST PROFILE");

		String url = "http://foo";
		String webUrl = null;
		String name = "Foo Profile";
		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(differential, url, webUrl, name);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(51, output.getSnapshot().getElement().size());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
