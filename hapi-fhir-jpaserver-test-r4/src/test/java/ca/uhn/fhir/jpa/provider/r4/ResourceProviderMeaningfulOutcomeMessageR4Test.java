package ca.uhn.fhir.jpa.provider.r4;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("Duplicates")
public class ResourceProviderMeaningfulOutcomeMessageR4Test extends BaseResourceProviderR4Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		HapiLocalizer.setOurFailOnMissingMessage(true);
		myStorageSettings.setAllowMultipleDelete(true);
	}

	@AfterEach
	@Override
	public void after() {
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateAsCreate", "successfulTimingSuffix"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdate", "successfulTimingSuffix"));
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
		ourLog.debug("Initial create: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateNoChange", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete

		oo = (OperationOutcome) myClient
			.delete()
			.resourceById("Patient", "A")
			.execute()
			.getOperationOutcome();
		ourLog.debug("Delete: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_DELETE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

		// Delete with no change

		oo = (OperationOutcome) myClient
			.delete()
			.resourceById("Patient", "A")
			.execute()
			.getOperationOutcome();
		ourLog.debug("Delete: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("deleteResourceAlreadyDeleted"));
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
		ourLog.debug("Initial create: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateAsCreate", "successfulTimingSuffix"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdate"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateNoChange"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("deleteResourceAlreadyDeleted"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulCreate", "successfulTimingSuffix"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches("Successfully conditionally created resource \".*\". No existing resources matched URL \"Patient\\?active=true\". Took [0-9]+ms.");
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulCreateConditionalWithMatch"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalNoMatch", "successfulTimingSuffix"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalWithMatch", "successfulTimingSuffix"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalNoChangeWithMatch", "successfulTimingSuffix"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalNoMatch", "successfulTimingSuffix"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalWithMatch"));
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
		ourLog.debug("Create {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulUpdateConditionalNoChangeWithMatch"));
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
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatch", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_PATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_NoChanges() {
		createPatient(withId("A"), withActiveFalse());

		Parameters patch = createPatchToSetPatientActiveFalse();

		OperationOutcome oo = (OperationOutcome) myClient
			.patch()
			.withFhirPatch(patch)
			.withId("Patient/A")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchNoChange", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_PATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}


	@Test
	public void testPatch_Conditional_MatchWithChanges() {
		createPatient(withId("A"), withActiveTrue(), withBirthdate("2022-01-01"));

		Parameters patch = createPatchToSetPatientActiveFalse();

		OperationOutcome oo = (OperationOutcome) myClient
			.patch()
			.withFhirPatch(patch)
			.conditionalByUrl("Patient?birthdate=2022-01-01")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchConditional", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_Conditional_MatchNoChanges() {
		createPatient(withId("A"), withActiveFalse(), withBirthdate("2022-01-01"));

		Parameters patch = createPatchToSetPatientActiveFalse();

		OperationOutcome oo = (OperationOutcome) myClient
			.patch()
			.withFhirPatch(patch)
			.conditionalByUrl("Patient?birthdate=2022-01-01")
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute()
			.getOperationOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchConditionalNoChange", "successfulTimingSuffix"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}


	@Test
	public void testPatch_WithChanges_InTransaction() {
		createPatient(withId("A"), withActiveTrue());

		Parameters patch = createPatchToSetPatientActiveFalse();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(new IdType("Patient/A"), patch);

		Bundle response = myClient
			.transaction()
			.withBundle((Bundle)bb.getBundle())
			.execute();
		OperationOutcome oo = (OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatch"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_PATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_NoChanges_InTransaction() {
		createPatient(withId("A"), withActiveFalse());

		Parameters patch = createPatchToSetPatientActiveFalse();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(new IdType("Patient/A"), patch);

		Bundle response = myClient
			.transaction()
			.withBundle((Bundle)bb.getBundle())
			.execute();
		OperationOutcome oo = (OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchNoChange"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_PATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}


	@Test
	public void testPatch_Conditional_MatchWithChanges_InTransaction() {
		createPatient(withId("A"), withActiveTrue(), withBirthdate("2022-01-01"));

		Parameters patch = createPatchToSetPatientActiveFalse();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(patch).conditional("Patient?birthdate=2022-01-01");

		Bundle response = myClient
			.transaction()
			.withBundle((Bundle)bb.getBundle())
			.execute();
		OperationOutcome oo = (OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchConditional"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}

	@Test
	public void testPatch_Conditional_MatchNoChanges_InTransaction() {
		createPatient(withId("A"), withActiveFalse(), withBirthdate("2022-01-01"));

		Parameters patch = createPatchToSetPatientActiveFalse();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(patch).conditional("Patient?birthdate=2022-01-01");

		Bundle response = myClient
			.transaction()
			.withBundle((Bundle)bb.getBundle())
			.execute();
		OperationOutcome oo = (OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulPatchConditionalNoChange"));
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH_NO_CHANGE.name(), oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
		assertEquals(StorageResponseCodeEnum.SYSTEM, oo.getIssueFirstRep().getDetails().getCodingFirstRep().getSystem());

	}
	@Test
	public void testMultiDelete_NoneFound() {

		OperationOutcome oo = (OperationOutcome) myClient
			.delete()
			.resourceConditionalByUrl("Patient?active=true")
			.execute()
			.getOperationOutcome();
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("unableToDeleteNotFound"));
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
		ourLog.debug("Update: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
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
		ourLog.debug("Delete {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		OperationOutcome oo = (OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(matchesHapiMessage("successfulDeletes", "successfulTimingSuffix"));
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

	private Predicate<String> matchesHapiMessage(String... theMessageKey) {
		StringBuilder joinedPattern = new StringBuilder();

		for (var next : theMessageKey) {
			String qualifiedKey = BaseStorageDao.class.getName() + "." + next;
			String pattern = myFhirContext.getLocalizer().getFormatString(qualifiedKey);
			assertThat(pattern).isNotBlank();
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

		Pattern compiledPattern = Pattern.compile(joinedPattern.toString());
		return compiledPattern.asPredicate();
	}

}
