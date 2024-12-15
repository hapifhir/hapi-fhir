package ca.uhn.fhir.jpa.replacereferences;

import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesTestHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesTestHelper.class);

	static final Identifier pat1IdentifierA =
			new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB =
			new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA =
			new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB =
			new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC =
			new Identifier().setSystem("SYSC").setValue("VALC");
	public static final int TOTAL_EXPECTED_PATCHES = 23;
	public static final int SMALL_BATCH_SIZE = 5;
	public static final int EXPECTED_SMALL_BATCHES = (TOTAL_EXPECTED_PATCHES + SMALL_BATCH_SIZE - 1) / SMALL_BATCH_SIZE;
	private final IFhirResourceDaoPatient<Patient> myPatientDao;
	private final IFhirResourceDao<Task> myTaskDao;
	private final IFhirResourceDao<Organization> myOrganizationDao;
	private final IFhirResourceDao<Encounter> myEncounterDao;
	private final IFhirResourceDao<CarePlan> myCarePlanDao;
	private final IFhirResourceDao<Observation> myObservationDao;

	private IIdType myOrgId;
	private IIdType mySourcePatientId;
	private IIdType mySourceCarePlanId;
	private IIdType mySourceEncId1;
	private IIdType mySourceEncId2;
	private ArrayList<IIdType> mySourceObsIds;
	private IIdType myTargetPatientId;
	private IIdType myTargetEnc1;
	private Patient myResultPatient;

	private final FhirContext myFhirContext;
	private final SystemRequestDetails mySrd = new SystemRequestDetails();

	public ReplaceReferencesTestHelper(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myPatientDao = (IFhirResourceDaoPatient<Patient>) theDaoRegistry.getResourceDao(Patient.class);
		myTaskDao = theDaoRegistry.getResourceDao(Task.class);
		myOrganizationDao = theDaoRegistry.getResourceDao(Organization.class);
		myEncounterDao = theDaoRegistry.getResourceDao(Encounter.class);
		myCarePlanDao = theDaoRegistry.getResourceDao(CarePlan.class);
		myObservationDao = theDaoRegistry.getResourceDao(Observation.class);
	}

	public void beforeEach() throws Exception {

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatientId = myPatientDao.create(patient1, mySrd).getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatientId = myPatientDao.create(patient2, mySrd).getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(Encounter.EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatientId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId1 = myEncounterDao.create(enc1, mySrd).getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatientId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId2 = myEncounterDao.create(enc2, mySrd).getId().toUnqualifiedVersionless();

		CarePlan carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setReferenceElement(mySourcePatientId);
		mySourceCarePlanId = myCarePlanDao.create(carePlan, mySrd).getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(Encounter.EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatientId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myEncounterDao.create(targetEnc1, mySrd).getId().toUnqualifiedVersionless();

		mySourceObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatientId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			IIdType obsId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			mySourceObsIds.add(obsId);
		}

		myResultPatient = new Patient();
		myResultPatient.setIdElement((IdType) myTargetPatientId);
		myResultPatient.addIdentifier(pat1IdentifierA);
	}

	public void setSourceAndTarget(PatientMergeInputParameters inParams) {
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatient = new Reference().setReferenceElement(myTargetPatientId);
	}

	public void setResultPatient(PatientMergeInputParameters theInParams, boolean theWithDelete) {
		if (!theWithDelete) {
			// add the link only if we are not deleting the source
			Patient.PatientLinkComponent link = myResultPatient.addLink();
			link.setOther(new Reference(mySourcePatientId));
			link.setType(Patient.LinkType.REPLACES);
		}
		theInParams.resultPatient = myResultPatient;
	}

	public Patient readSourcePatient() {
		return myPatientDao.read(mySourcePatientId, mySrd);
	}

	public Patient readTargetPatient() {
		return myPatientDao.read(myTargetPatientId, mySrd);
	}

	public IIdType getTargetPatientId() {
		return myTargetPatientId;
	}

	private Set<IIdType> getTargetEverythingResourceIds() {
		PatientEverythingParameters everythingParams = new PatientEverythingParameters();
		everythingParams.setCount(new IntegerType(100));

		IBundleProvider bundleProvider =
				myPatientDao.patientInstanceEverything(null, mySrd, everythingParams, myTargetPatientId);

		assertNull(bundleProvider.getNextPageId());

		return bundleProvider.getAllResources().stream()
				.map(IBaseResource::getIdElement)
				.map(IIdType::toUnqualifiedVersionless)
				.collect(Collectors.toSet());
	}

	public Boolean taskCompleted(IdType theTaskId) {
		Task updatedTask = myTaskDao.read(theTaskId, mySrd);
		ourLog.info("Task {} status is {}", theTaskId, updatedTask.getStatus());
		return updatedTask.getStatus() == Task.TaskStatus.COMPLETED;
	}

	public Parameters callReplaceReferences(IGenericClient theFhirClient, boolean theIsAsync) {
		return callReplaceReferencesWithBatchSize(theFhirClient, theIsAsync, null);
	}

	public Parameters callReplaceReferencesWithBatchSize(
			IGenericClient theFhirClient, boolean theIsAsync, Integer theBatchSize) {
		IOperationUntypedWithInputAndPartialOutput<Parameters> request = theFhirClient
				.operation()
				.onServer()
				.named(OPERATION_REPLACE_REFERENCES)
				.withParameter(
						Parameters.class,
						ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID,
						new StringType(mySourcePatientId.getValue()))
				.andParameter(
						ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID,
						new StringType(myTargetPatientId.getValue()));
		if (theBatchSize != null) {
			request.andParameter(
					ProviderConstants.OPERATION_REPLACE_REFERENCES_BATCH_SIZE, new IntegerType(theBatchSize));
		}

		if (theIsAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request.returnResourceType(Parameters.class).execute();
	}

	public void assertAllReferencesUpdated() {
		assertAllReferencesUpdated(false);
	}

	public void assertAllReferencesUpdated(boolean theWithDelete) {

		Set<IIdType> actual = getTargetEverythingResourceIds();

		ourLog.info("Found IDs: {}", actual);

		if (theWithDelete) {
			assertThat(actual).doesNotContain(mySourcePatientId);
		}
		assertThat(actual).contains(mySourceEncId1);
		assertThat(actual).contains(mySourceEncId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).contains(mySourceCarePlanId);
		assertThat(actual).containsAll(mySourceObsIds);
		assertThat(actual).contains(myTargetPatientId);
		assertThat(actual).contains(myTargetEnc1);
	}

	public void assertNothingChanged() {
		Set<IIdType> actual = getTargetEverythingResourceIds();

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).doesNotContain(mySourcePatientId);
		assertThat(actual).doesNotContain(mySourceEncId1);
		assertThat(actual).doesNotContain(mySourceEncId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).doesNotContain(mySourceCarePlanId);
		assertThat(actual).doesNotContainAnyElementsOf(mySourceObsIds);
		assertThat(actual).contains(myTargetPatientId);
		assertThat(actual).contains(myTargetEnc1);
	}

	public PatientMergeInputParameters buildMultipleTargetMatchParameters(
			boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatientIdentifier = patBothIdentifierC;
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public PatientMergeInputParameters buildMultipleSourceMatchParameters(
			boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatientIdentifier = patBothIdentifierC;
		inParams.targetPatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public IIdType getSourcePatientId() {
		return mySourcePatientId;
	}

	public static class PatientMergeInputParameters {
		public Type sourcePatient;
		public Type sourcePatientIdentifier;
		public Type targetPatient;
		public Type targetPatientIdentifier;
		public Patient resultPatient;
		public Boolean preview;
		public Boolean deleteSource;

		public Parameters asParametersResource() {
			Parameters inParams = new Parameters();
			if (sourcePatient != null) {
				inParams.addParameter().setName("source-patient").setValue(sourcePatient);
			}
			if (sourcePatientIdentifier != null) {
				inParams.addParameter().setName("source-patient-identifier").setValue(sourcePatientIdentifier);
			}
			if (targetPatient != null) {
				inParams.addParameter().setName("target-patient").setValue(targetPatient);
			}
			if (targetPatientIdentifier != null) {
				inParams.addParameter().setName("target-patient-identifier").setValue(targetPatientIdentifier);
			}
			if (resultPatient != null) {
				inParams.addParameter().setName("result-patient").setResource(resultPatient);
			}
			if (preview != null) {
				inParams.addParameter().setName("preview").setValue(new BooleanType(preview));
			}
			if (deleteSource != null) {
				inParams.addParameter().setName("delete-source").setValue(new BooleanType(deleteSource));
			}
			return inParams;
		}
	}

	public static void validatePatchResultBundle(
			Bundle patchResultBundle, int theTotalExpectedPatches, List<String> theExpectedResourceTypes) {
		String resourceMatchString = "(" + String.join("|", theExpectedResourceTypes) + ")";
		Pattern expectedPatchIssuePattern =
				Pattern.compile("Successfully patched resource \"" + resourceMatchString + "/\\d+/_history/\\d+\".");
		assertThat(patchResultBundle.getEntry())
				.hasSize(theTotalExpectedPatches)
				.allSatisfy(entry -> assertThat(entry.getResponse().getOutcome())
						.isInstanceOf(OperationOutcome.class)
						.extracting(OperationOutcome.class::cast)
						.extracting(OperationOutcome::getIssue)
						.satisfies(issues -> assertThat(issues)
								.hasSize(1)
								.element(0)
								.extracting(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
								.satisfies(
										diagnostics -> assertThat(diagnostics).matches(expectedPatchIssuePattern))));
	}

	public Bundle validateCompletedTask(JobInstance theJobInstance, IIdType theTaskId) {
		validateJobReport(theJobInstance, theTaskId);

		Bundle patchResultBundle;
		Task taskWithOutput = myTaskDao.read(theTaskId, mySrd);
		assertThat(taskWithOutput.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
		ourLog.info(
				"Complete Task: {}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

		Task.TaskOutputComponent taskOutput = taskWithOutput.getOutputFirstRep();

		// Assert on the output type
		Coding taskType = taskOutput.getType().getCodingFirstRep();
		assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
		assertEquals("Bundle", taskType.getCode());

		List<Resource> containedResources = taskWithOutput.getContained();
		assertThat(containedResources).hasSize(1).element(0).isInstanceOf(Bundle.class);

		Bundle containedBundle = (Bundle) containedResources.get(0);

		Reference outputRef = (Reference) taskOutput.getValue();
		patchResultBundle = (Bundle) outputRef.getResource();
		//		ourLog.info("containedBundle: {}",
		// myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(containedBundle));
		//		ourLog.info("patchResultBundle: {}",
		// myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patchResultBundle));
		assertTrue(containedBundle.equalsDeep(patchResultBundle));
		return patchResultBundle;
	}

	private void validateJobReport(JobInstance theJobInstance, IIdType theTaskId) {
		String report = theJobInstance.getReport();
		ReplaceReferenceResultsJson replaceReferenceResultsJson =
				JsonUtil.deserialize(report, ReplaceReferenceResultsJson.class);
		IdDt resultTaskId = replaceReferenceResultsJson.getTaskId().asIdDt();
		assertEquals(theTaskId.getIdPart(), resultTaskId.getIdPart());
	}
}
