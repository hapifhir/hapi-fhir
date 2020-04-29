package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(myFhirCtx);
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirCtx);
		ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

		// Generate the snapshot
		StructureDefinition snapshot = (StructureDefinition) chain.generateSnapshot(chain, differential, "http://foo", null, "THE BEST PROFILE");

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
