package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.util.BundleBuilder;
import org.hamcrest.Matcher;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("Duplicates")
public class ResourceProviderMeaningfulOutcomeMessageR4Test extends BaseResourceProviderR4Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		HapiLocalizer.setOurFailOnMissingMessage(true);
	}

	@Test
	public void testCreateUpdateDelete() {

		// Initial Create-with-client-assigned-ID

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		OperationOutcome oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateAsCreate", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Update with change

		p.setId("Patient/A");
		p.setActive(false);
		oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdate", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Update with no change

		p.setId("Patient/A");
		oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Initial create: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateNoChange", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete

		oo = (OperationOutcome) myClient
			.delete()
			.resourceById("Patient", "A")
			.execute()
			.getOperationOutcome();
		ourLog.info("Delete: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete with no change

		oo = (OperationOutcome) myClient
			.delete()
			.resourceById("Patient", "A")
			.execute()
			.getOperationOutcome();
		ourLog.info("Delete: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("deleteResourceAlreadyDeleted"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE_ALREADY_DELETED.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testCreateUpdateDelete_InTransaction() {

		// Initial Create-with-client-assigned-ID

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Initial create: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateAsCreate", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Update with change

		p.setId("Patient/A");
		p.setActive(false);
		input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.andThen()
			.getBundle();
		output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdate"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Update with no change

		p.setId("Patient/A");
		input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.andThen()
			.getBundle();
		output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateNoChange"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete

		input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionDeleteEntry("Patient", "A")
			.andThen()
			.getBundle();
		output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete With No Change

		input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionDeleteEntry("Patient", "A")
			.andThen()
			.getBundle();
		output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("deleteResourceAlreadyDeleted"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE_ALREADY_DELETED.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testCreate_InTransaction() {

		Patient p = new Patient();
		p.setActive(true);

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionCreateEntry(p)
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulCreate", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CREATE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalCreate_NoMatch_InTransaction() {

		Patient p = new Patient();
		p.setActive(true);

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionCreateEntry(p)
			.conditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(),
			matchesPattern("Successfully conditionally created resource \".*\". No existing resources matched URL \"Patient\\?active=true\". Took [0-9]+ms."));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalCreate_WithMatch_InTransaction() {
		createPatient(withActiveTrue());

		Patient p = new Patient();
		p.setActive(true);

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionCreateEntry(p)
			.conditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulCreateConditionalWithMatch"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_NoMatch() {
		Patient p = new Patient();
		p.setActive(true);

		OperationOutcome oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.conditionalByUrl("Patient?active=true")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalNoMatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_WithMatchAndChange() {
		createPatient(withActiveTrue());

		Patient p = new Patient();
		p.setActive(true);
		p.addName().setFamily("Test");

		OperationOutcome oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.conditionalByUrl("Patient?active=true")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalWithMatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_WithMatchNoChange() {
		createPatient(withActiveTrue());

		Patient p = new Patient();
		p.setActive(true);

		OperationOutcome oo = (OperationOutcome) myClient
			.update()
			.resource(p)
			.conditionalByUrl("Patient?active=true")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalNoChangeWithMatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_NoMatch_InTransaction() {
		Patient p = new Patient();
		p.setActive(true);

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.conditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalNoMatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_WithMatchAndChange_InTransaction() {
		createPatient(withActiveTrue());

		Patient p = new Patient();
		p.setActive(true);
		p.addName().setFamily("Test");

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.conditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalWithMatch"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testConditionalUpdate_WithMatchNoChange_InTransaction() {
		createPatient(withActiveTrue());

		Patient p = new Patient();
		p.setActive(true);

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionUpdateEntry(p)
			.conditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulUpdateConditionalNoChangeWithMatch"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_WithChanges() {
		createPatient(withId("A"), withActiveTrue());

		Parameters patch = createPatchToSetPatientActiveFalse();

		OperationOutcome oo = (OperationOutcome) myClient
			.patch()
			.withFhirPatch(patch)
			.withId("Patient/A")
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulPatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_NoChanges() {
		createPatient(withId("A"), withActiveTrue());

		Parameters patch = createPatchToSetPatientActiveFalse();

		OperationOutcome oo = (OperationOutcome) myClient
			.patch()
			.withFhirPatch(patch)
			.withId("Patient/A")
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulPatchNoChange", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_PATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testMultiDelete_NoneFound() {

		OperationOutcome oo = (OperationOutcome) myClient
			.delete()
			.resourceConditionalByUrl("Patient?active=true")
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("unableToDeleteNotFound"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testMultiDelete_SomeFound() {

		createPatient(withActiveTrue());
		createPatient(withActiveTrue());
		createPatient(withActiveTrue());

		OperationOutcome oo = (OperationOutcome) myClient
			.delete()
			.resourceConditionalByUrl("Patient?active=true")
			.execute()
			.getOperationOutcome();
		ourLog.info("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testMultiDelete_SomeFound_InTransaction() {
		createPatient(withActiveTrue());
		createPatient(withActiveTrue());
		createPatient(withActiveTrue());

		Bundle input = (Bundle) new BundleBuilder(myFhirContext)
			.addTransactionDeleteEntryConditional("Patient?active=true")
			.andThen()
			.getBundle();
		Bundle output = myClient
			.transaction()
			.withBundle(input)
			.execute();
		ourLog.info("Delete {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	private static Parameters createPatchToSetPatientActiveFalse() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));
		return patch;
	}


	private Matcher<String> matchesHapiMessage(String... theMessageKey) {
		StringBuilder joinedPattern = new StringBuilder();

		for (var next : theMessageKey) {
			String qualifiedKey = BaseStorageDao.class.getName() + "." + next;
			String pattern = myFhirContext.getLocalizer().getFormatString(qualifiedKey);
			assertTrue(isNotBlank(pattern));
			pattern = pattern
				.replace("\"", "\\\"")
				.replace("(", "\\(")
				.replace(")", "\\)")
				.replace("[", "\\[")
				.replace("]", "\\]")
				.replace(".", "\\.")
				.replaceAll("\\{[0-9]+}", ".*");

			if (joinedPattern.length() > 0) {
				joinedPattern.append(' ');
			}
			joinedPattern.append(pattern);

		}

		return matchesPattern(joinedPattern.toString());
	}

}
