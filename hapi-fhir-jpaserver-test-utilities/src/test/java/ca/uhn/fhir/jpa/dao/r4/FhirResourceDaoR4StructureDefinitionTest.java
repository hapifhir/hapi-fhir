package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"unchecked", "deprecation"})
public class FhirResourceDaoR4StructureDefinitionTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4StructureDefinitionTest.class);

	@AfterEach
	public final void after() {
	}

	@Test
	public void testGenerateSnapshot() throws IOException {
		StructureDefinition differential = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		assertEquals(0, differential.getSnapshot().getElement().size());

		// Create a validation chain that includes default validation support and a
		// snapshot generator
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(myFhirContext);
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirContext);
		ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

		// Generate the snapshot
		StructureDefinition snapshot = (StructureDefinition) chain.generateSnapshot(new ValidationSupportContext(chain), differential, "http://foo", null, "THE BEST PROFILE");

		String url = "http://foo";
		String webUrl = null;
		String name = "Foo Profile";
		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(differential, url, webUrl, name);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertEquals(54, output.getSnapshot().getElement().size());
	}


	/**
	 * Make sure that if one SD extends another SD, and the parent SD hasn't been snapshotted itself, the child can
	 * be snapshotted.
	 */
	@Test
	public void testGenerateSnapshotChained() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient.json");
		myStructureDefinitionDao.update(sd);

		StructureDefinition sd2 = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient-no-phi.json");
		myStructureDefinitionDao.update(sd2);

		StructureDefinition snapshotted = myStructureDefinitionDao.generateSnapshot(sd2, null, null, null);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(snapshotted));

		assertTrue(snapshotted.getSnapshot().getElement().size() > 0);
	}



}
