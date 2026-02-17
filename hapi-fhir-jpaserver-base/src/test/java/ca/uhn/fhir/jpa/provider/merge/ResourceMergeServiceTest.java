package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.merge.ExtensionBasedLinkService;
import ca.uhn.fhir.merge.MergeResourceHelper;
import ca.uhn.fhir.merge.PatientNativeLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.batch2.jobs.parameters.BatchJobParametersWithTaskId;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.IReplaceReferencesSvc;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceMergeServiceTest {
	private static final Integer PAGE_SIZE = 1024;

	private static final String SUCCESSFUL_SYNC_MERGE_MSG = "Merge operation completed successfully";
	private static final String SUCCESSFUL_ASYNC_MERGE_MSG = "Merge request is accepted, and will be " +
		"processed asynchronously. See task resource returned in this response for details.";

	private static final String SOURCE_PATIENT_TEST_ID = "Patient/123";
	private static final String SOURCE_PATIENT_TEST_ID_WITH_VERSION_1= SOURCE_PATIENT_TEST_ID + "/_history/1";
	private static final String SOURCE_PATIENT_TEST_ID_WITH_VERSION_2= SOURCE_PATIENT_TEST_ID + "/_history/2";
	private static final String TARGET_PATIENT_TEST_ID = "Patient/456";
	private static final String TARGET_PATIENT_TEST_ID_WITH_VERSION_1 = TARGET_PATIENT_TEST_ID + "/_history/1";
	private static final String TARGET_PATIENT_TEST_ID_WITH_VERSION_2 = TARGET_PATIENT_TEST_ID + "/_history/2";
	public static final String PRECONDITION_FAILED_MESSAGE = "bad wolf";

	@Mock
	DaoRegistry myDaoRegistryMock;

	@Mock
	IFhirResourceDaoPatient<Patient> myPatientDaoMock;

	@Mock
	IFhirResourceDaoPatient<Task> myTaskDaoMock;

	@Mock
	IFhirResourceDaoPatient<Provenance> myProvenanceDaoMock;

	@Mock
	IReplaceReferencesSvc myReplaceReferencesSvcMock;

	@Mock
	RequestDetails myRequestDetailsMock;

	@Mock
	IHapiTransactionService myTransactionServiceMock;

	@Mock
	IRequestPartitionHelperSvc myRequestPartitionHelperSvcMock;

	@Mock
	IJobCoordinator myJobCoordinatorMock;

	@Mock
	Batch2TaskHelper myBatch2TaskHelperMock;

	@Mock
	RequestPartitionId myRequestPartitionIdMock;

	@Mock
	private JpaStorageSettings myStorageSettingsMock;

	@Mock
	MergeValidationService myMergeValidationServiceMock;

	private ResourceMergeService myResourceMergeService;

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private Patient myCapturedSourcePatientForUpdate;

	private Patient myCapturedTargetPatientForUpdate;

	@BeforeEach
	void setup() {
		lenient().when(myDaoRegistryMock.getResourceDao("Patient")).thenReturn(myPatientDaoMock);
		when(myDaoRegistryMock.getResourceDao(Task.class)).thenReturn(myTaskDaoMock);
		when(myDaoRegistryMock.getResourceDao("Provenance")).thenReturn(myProvenanceDaoMock);
		when(myDaoRegistryMock.getFhirContext()).thenReturn(myFhirContext);
		lenient().when(myRequestDetailsMock.getResourceName()).thenReturn("Patient");

		PatientNativeLinkService patientNativeLinkService = new PatientNativeLinkService();
		ExtensionBasedLinkService extensionBasedLinkService = new ExtensionBasedLinkService();
		ResourceLinkServiceFactory resourceLinkServiceFactory = new ResourceLinkServiceFactory(patientNativeLinkService, extensionBasedLinkService);

		MergeProvenanceSvc myMergeProvenanceService = new MergeProvenanceSvc(myDaoRegistryMock);
		MergeResourceHelper myMergeResourceHelper = new MergeResourceHelper(myDaoRegistryMock, myMergeProvenanceService, resourceLinkServiceFactory);

		myResourceMergeService = new ResourceMergeService(
			myStorageSettingsMock,
			myDaoRegistryMock,
			myReplaceReferencesSvcMock,
			myTransactionServiceMock,
			myRequestPartitionHelperSvcMock,
			myJobCoordinatorMock,
			myBatch2TaskHelperMock,
			myMergeValidationServiceMock,
			myMergeResourceHelper);
	}

	@Nested
	class SuccessfulMerge {

		@Test
		void testMerge_WithoutResultResource_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		setOriginalInputParameters(mergeOperationParameters);

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);

		//the identifiers should be copied from the source to the target, without creating duplicates on the target
		sourcePatient.addIdentifier(new Identifier().setSystem("sysSource").setValue("valS1"));
		sourcePatient.addIdentifier(new Identifier().setSystem("sysSource").setValue("valS2"));
		sourcePatient.addIdentifier(new Identifier().setSystem("sysCommon").setValue("valCommon"));
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		targetPatient.addIdentifier(new Identifier().setSystem("sysCommon").setValue("valCommon"));
		targetPatient.addIdentifier(new Identifier().setSystem("sysTarget").setValue("valT1"));
		setupValidationMockForSuccess(sourcePatient, targetPatient);
		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		Patient patientReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedSourcePatient();
		// the identifiers copied over from the source should be marked as OLD
		List<Identifier> expectedIdentifiers = List.of(
			new Identifier().setSystem("sysCommon").setValue("valCommon"),
			new Identifier().setSystem("sysTarget").setValue("valT1"),
			new Identifier().setSystem("sysSource").setValue("valS1").setUse(Identifier.IdentifierUse.OLD),
			new Identifier().setSystem("sysSource").setValue("valS2").setUse(Identifier.IdentifierUse.OLD));
		verifyUpdatedTargetPatient(true, expectedIdentifiers);
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}


	@ParameterizedTest
	@CsvSource(value = {
		"true,  true",   // Both explicitly active
		"true,  null",   // Source active, target not set
		"false, true",   // Source inactive, target active
		"false, null",   // Source inactive, target not set
		"null,  true",   // Source not set, target active
		"null,  null"    // Neither explicitly set
		// Note: target.active=false is excluded - tested separately as failure case
	}, nullValues = {"null"})
	void testMerge_WithoutResultResource_VariousActiveStates_Success(
			Boolean isSourceActive, Boolean isTargetActive) {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		setOriginalInputParameters(mergeOperationParameters);

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		if (isSourceActive != null) {
			sourcePatient.setActive(isSourceActive);
		}

		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		if (isTargetActive != null) {
			targetPatient.setActive(isTargetActive);
		}

		setupValidationMockForSuccess(sourcePatient, targetPatient);
		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		Patient patientReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedSourcePatient();
		verifyUpdatedTargetPatient(true, Collections.emptyList());

		// Verify target active field preserved
		boolean activeWasSetExplicitlyOnTarget = (isTargetActive != null);
		verifyTargetActiveFieldPreserved(activeWasSetExplicitlyOnTarget);

		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@Test
	void testMerge_WithResultResource_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
		resultPatient.addLink().setType(Patient.LinkType.REPLACES).setOther(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setResultResource(resultPatient);
		setOriginalInputParameters(mergeOperationParameters);
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		//when result resource exists, the identifiers should not be copied. so we don't expect this identifier when
		//target is updated
		sourcePatient.addIdentifier(new Identifier().setSystem("sysSource").setValue("valS1"));
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);

		setupValidationMockForSuccess(sourcePatient, targetPatient);

		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(resultPatient, patientToBeReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientToBeReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedSourcePatient();
		verifyUpdatedTargetPatient(true, Collections.emptyList());
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}


	@Test
	void testMerge_WithResultResource_ResultHasAllTargetIdentifiers_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")
		));
		Patient resultPatient = createValidResultPatient(false);
		resultPatient.addIdentifier().setSystem("sys").setValue("val1");
		resultPatient.addIdentifier().setSystem("sys").setValue("val2");
		mergeOperationParameters.setResultResource(resultPatient);
		setOriginalInputParameters(mergeOperationParameters);

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);

		setupValidationMockForSuccess(sourcePatient, targetPatient);

		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(resultPatient, patientToBeReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();


		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientToBeReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedSourcePatient();
		List<Identifier> expectedIdentifiers = List.of(
			new Identifier().setSystem("sys").setValue("val1"),
			new Identifier().setSystem("sys").setValue("val2")
		);
		verifyUpdatedTargetPatient(true, expectedIdentifiers);
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@Test
	void testMerge_WithDeleteSourceTrue_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setDeleteSource(true);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		setOriginalInputParameters(mergeOperationParameters);
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		setupValidationMockForSuccess(sourcePatient, targetPatient);

		when(myPatientDaoMock.delete(new IdType(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1), myRequestDetailsMock)).thenReturn(new DaoMethodOutcome());
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientToBeReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();


		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientToBeReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedTargetPatient(false, Collections.emptyList());
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}


	@Test
	void testMerge_WithDeleteSourceTrue_And_WithResultResource_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setDeleteSource(true);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		Patient resultPatient = createValidResultPatient(true);
		mergeOperationParameters.setResultResource(resultPatient);
		setOriginalInputParameters(mergeOperationParameters);
		setupValidationMockForSuccess(sourcePatient, targetPatient);

		when(myPatientDaoMock.delete(new IdType(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1), myRequestDetailsMock)).thenReturn(new DaoMethodOutcome());
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(resultPatient, patientToBeReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();


		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientToBeReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedTargetPatient(false, Collections.emptyList());
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@Test
	void testMerge_WithPreviewTrue_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setPreview(true);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		setupValidationMockForSuccess(sourcePatient, targetPatient);

		when(myReplaceReferencesSvcMock.countResourcesReferencingResource(new IdType(SOURCE_PATIENT_TEST_ID),
			myRequestDetailsMock)).thenReturn(10);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);
		assertThat(mergeOutcome.getUpdatedTargetResource()).isEqualTo(targetPatient);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDetails().getText()).contains("Preview only merge operation - no issues detected");
		assertThat(issue.getDiagnostics()).contains("Merge would update 12 resources");

		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@Test
	void testMerge_ResolvesResourcesByReferenceThatHasVersions_CurrentResourceVersionAreTheSame_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID_WITH_VERSION_2));
		setOriginalInputParameters(mergeOperationParameters);

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupValidationMockForSuccess(sourcePatient, targetPatient);
		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2));
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientToBeReturnedFromDaoAfterTargetUpdate);
		setupTransactionServiceMock();
		setupReplaceReferencesForSuccessForSync();

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySuccessfulOutcomeForSync(mergeOutcome, patientToBeReturnedFromDaoAfterTargetUpdate);
		verifyUpdatedSourcePatient();
		verifyUpdatedTargetPatient(true, Collections.emptyList());
		verifyProvenanceCreated();
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}


	@ParameterizedTest
	@CsvSource({
		"true, false",
		"false, true",
		"true, true",
		"false, false"
	})
	void testMerge_Async_Success(boolean theWithResultResource, boolean theWithDeleteSource) {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		mergeOperationParameters.setDeleteSource(theWithDeleteSource);

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		when(myRequestDetailsMock.isPreferAsync()).thenReturn(true);
		when(myRequestPartitionHelperSvcMock.determineReadPartitionForRequest(eq(myRequestDetailsMock), any())).thenReturn(myRequestPartitionIdMock);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		setupValidationMockForSuccess(sourcePatient, targetPatient);

		Patient resultResource;
		if (theWithResultResource) {
			resultResource = createValidResultPatient(theWithDeleteSource);
			mergeOperationParameters.setResultResource(resultResource);
		}
		setOriginalInputParameters(mergeOperationParameters);

		Task task = new Task();
		setupBatch2JobTaskHelperMock(task);

		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		verifySuccessfulOutcomeForAsync(mergeOutcome, task);
		verifyBatch2JobTaskHelperMockInvocation(mergeOperationParameters);
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	}

	@Nested
	class ValidationFailure {

		@Test
		void testMerge_ValidationReturnsInvalid_ReturnsValidationStatusCodeAndNoMergeExecuted() {
			// Given
			MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
			mergeOperationParameters.setResourceLimit(PAGE_SIZE);
			mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			when(myMergeValidationServiceMock.validate(any(), eq(myRequestDetailsMock), any()))
				.thenReturn(MergeValidationResult.invalidResult(422));

			// When
			MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

			// Then
			assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);
			assertThat(mergeOutcome.getUpdatedTargetResource()).isNull();
			assertThat(mergeOutcome.getTask()).isNull();
			verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
		}
	}

	@Nested
	class ExceptionHandling {

		@ParameterizedTest
		@CsvSource({
			"true, false",
			"false, true",
			"true, true",
			"false, false"
		})
		void testMerge_SyncRequest_ReplaceReferencesThrowsPreconditionFailedException_TheExceptionReturnedToClientInOutcome(boolean theWithResultResource,
																															boolean theWithDeleteSource) {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		mergeOperationParameters.setDeleteSource(theWithDeleteSource);
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
		when(myRequestDetailsMock.isPreferAsync()).thenReturn(false);
		when(myRequestPartitionHelperSvcMock.determineReadPartitionForRequest(eq(myRequestDetailsMock), any())).thenReturn(myRequestPartitionIdMock);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
		setupValidationMockForSuccess(sourcePatient, targetPatient);

		if (theWithResultResource) {
			Patient resultResource = createValidResultPatient(theWithDeleteSource);
			mergeOperationParameters.setResultResource(resultResource);
		}

		when(myReplaceReferencesSvcMock.replaceReferences(any(), any())).thenThrow(new PreconditionFailedException(PRECONDITION_FAILED_MESSAGE));

		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(PreconditionFailedException.STATUS_CODE);
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(operationOutcome.getIssue()).hasSize(1);
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics()).isEqualTo(PRECONDITION_FAILED_MESSAGE);
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testMerge_UnhandledServerResponseExceptionThrown_UsesStatusCodeOfTheException(boolean thePreview) {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setPreview(thePreview);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

		ForbiddenOperationException ex = new ForbiddenOperationException("this is the exception message");
		when(myMergeValidationServiceMock.validate(any(), eq(myRequestDetailsMock), any())).thenThrow(ex);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifyErrorOutcome(mergeOutcome, 403, "this is the exception message", "exception");
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testMerge_UnhandledExceptionThrown_Uses500StatusCode(boolean thePreview) {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new MergeOperationInputParameters();
		mergeOperationParameters.setResourceLimit(PAGE_SIZE);
		mergeOperationParameters.setPreview(thePreview);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

		RuntimeException ex = new RuntimeException("this is the exception message");
		when(myMergeValidationServiceMock.validate(any(), eq(myRequestDetailsMock), any())).thenThrow(ex);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifyErrorOutcome(mergeOutcome, 500, "this is the exception message", "exception");
		verifyNoMoreInteractions(myPatientDaoMock, myTaskDaoMock, myProvenanceDaoMock, myBatch2TaskHelperMock);
	}

	}

	private void setupValidationMockForSuccess(Patient theSourcePatient, Patient theTargetPatient) {
		when(myMergeValidationServiceMock.validate(any(), eq(myRequestDetailsMock), any()))
			.thenReturn(MergeValidationResult.validResult(theSourcePatient, theTargetPatient));
	}

	private void verifySuccessfulOutcomeForSync(MergeOperationOutcome theMergeOutcome, Patient theExpectedTargetResource) {
		assertThat(theMergeOutcome.getHttpStatusCode()).isEqualTo(200);

		OperationOutcome operationOutcome = (OperationOutcome) theMergeOutcome.getOperationOutcome();
		assertThat(theMergeOutcome.getUpdatedTargetResource()).isEqualTo(theExpectedTargetResource);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDetails().getText()).contains(SUCCESSFUL_SYNC_MERGE_MSG);
	}

	private void verifyErrorOutcome(MergeOperationOutcome theMergeOutcome, int theExpectedStatus,
									String theExpectedMessageContains, String theExpectedIssueCode) {
		OperationOutcome operationOutcome = (OperationOutcome) theMergeOutcome.getOperationOutcome();
		assertThat(theMergeOutcome.getHttpStatusCode()).isEqualTo(theExpectedStatus);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(theExpectedMessageContains);
		assertThat(issue.getCode().toCode()).isEqualTo(theExpectedIssueCode);
	}

	private void verifySuccessfulOutcomeForAsync(MergeOperationOutcome theMergeOutcome, Task theExpectedTask) {
		assertThat(theMergeOutcome.getHttpStatusCode()).isEqualTo(202);
		assertThat(theMergeOutcome.getTask()).isEqualTo(theExpectedTask);
		assertThat(theMergeOutcome.getUpdatedTargetResource()).isNull();
		OperationOutcome operationOutcome = (OperationOutcome) theMergeOutcome.getOperationOutcome();
		assertThat(theMergeOutcome.getUpdatedTargetResource()).isNull();
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDetails().getText()).contains(SUCCESSFUL_ASYNC_MERGE_MSG);

	}

	private Patient createPatient(String theId) {
		Patient patient = new Patient();
		patient.setId(theId);
		return patient;
	}

	private Patient createValidResultPatient(boolean theDeleteSource) {

		Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
		if (!theDeleteSource) {
			resultPatient.addLink().setType(Patient.LinkType.REPLACES).setOther(new Reference(SOURCE_PATIENT_TEST_ID));
		}
		return resultPatient;
	}

	private void setupTransactionServiceMock() {
		when(myRequestPartitionHelperSvcMock.determineReadPartitionForRequest(eq(myRequestDetailsMock), any())).thenReturn(myRequestPartitionIdMock);
		IHapiTransactionService.IExecutionBuilder executionBuilderMock =
			mock(IHapiTransactionService.IExecutionBuilder.class);
		when(myTransactionServiceMock.withRequest(myRequestDetailsMock)).thenReturn(executionBuilderMock);
		when(executionBuilderMock.withRequestPartitionId(myRequestPartitionIdMock)).thenReturn(executionBuilderMock);
		doAnswer(invocation -> {
			Runnable runnable = invocation.getArgument(0);
			runnable.run();
			return null;
		}).when(executionBuilderMock).execute(isA(Runnable.class));
	}

	private void verifyUpdatedSourcePatient() {
		assertThat(myCapturedSourcePatientForUpdate.getLink()).hasSize(1);
		assertThat(myCapturedSourcePatientForUpdate.getLinkFirstRep().getType()).isEqualTo(Patient.LinkType.REPLACEDBY);
		assertThat(myCapturedSourcePatientForUpdate.getLinkFirstRep().getOther().getReference()).isEqualTo(TARGET_PATIENT_TEST_ID);

		// Validate source active field set to false after merge
		assertThat(myCapturedSourcePatientForUpdate.getActive()).isFalse();
	}

	private void setupDaoMockForSuccessfulSourcePatientUpdate(Patient thePatientExpectedAsInput,
															  Patient thePatientToReturnInDaoOutcome) {
		DaoMethodOutcome daoMethodOutcome = new DaoMethodOutcome();
		daoMethodOutcome.setResource(thePatientToReturnInDaoOutcome);
		when(myPatientDaoMock.update(thePatientExpectedAsInput, myRequestDetailsMock))
			.thenAnswer(t -> {
				myCapturedSourcePatientForUpdate = t.getArgument(0);

				DaoMethodOutcome outcome = new DaoMethodOutcome();
				outcome.setResource(thePatientToReturnInDaoOutcome);
				thePatientExpectedAsInput.setId(thePatientToReturnInDaoOutcome.getIdElement());
				return outcome;
			});
	}

	private void verifyUpdatedTargetPatient(boolean theExpectLinkToSourcePatient, List<Identifier> theExpectedIdentifiers) {
		if (theExpectLinkToSourcePatient) {
			assertThat(myCapturedTargetPatientForUpdate.getLink()).hasSize(1);
			assertThat(myCapturedTargetPatientForUpdate.getLinkFirstRep().getType()).isEqualTo(Patient.LinkType.REPLACES);
			assertThat(myCapturedTargetPatientForUpdate.getLinkFirstRep().getOther().getReference()).isEqualTo(SOURCE_PATIENT_TEST_ID);
		}
		else {
			assertThat(myCapturedTargetPatientForUpdate.getLink()).isEmpty();
		}


		assertThat(myCapturedTargetPatientForUpdate.getIdentifier()).hasSize(theExpectedIdentifiers.size());
		for (int i = 0; i < theExpectedIdentifiers.size(); i++) {
			Identifier expectedIdentifier = theExpectedIdentifiers.get(i);
			Identifier actualIdentifier = myCapturedTargetPatientForUpdate.getIdentifier().get(i);
			assertThat(expectedIdentifier.equalsDeep(actualIdentifier)).isTrue();
		}

	}

	private void verifyTargetActiveFieldPreserved(boolean theActiveWasSetExplicitly) {
		Patient updatedTarget = myCapturedTargetPatientForUpdate;
		if (theActiveWasSetExplicitly) {
			// If active was set, it must be true (false is failure case tested separately)
			assertThat(updatedTarget.getActive()).isTrue();
		} else {
			// If active was not set, verify it remains unset
			assertThat(updatedTarget.hasActive()).isFalse();
		}
	}

	private void verifyProvenanceCreated() {

		ArgumentCaptor<Provenance> captor = ArgumentCaptor.forClass(Provenance.class);
		verify(myProvenanceDaoMock).create(captor.capture(), eq(myRequestDetailsMock));

		Provenance provenance = captor.getValue();
		//assert targets
		assertThat(provenance.getTarget()).hasSize(2);
		// the first target reference should be the target patient
		String targetPatientReference = provenance.getTarget().get(0).getReference();
		assertThat(targetPatientReference).isEqualTo(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);

		// the second target reference should be the source patient
		String sourcePatientReference = provenance.getTarget().get(1).getReference();
		assertThat(sourcePatientReference).isEqualTo(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2);

		Instant now = Instant.now();
		Instant oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);
		assertThat(provenance.getRecorded()).isBetween(oneMinuteAgo, now, true, true);

		Period period = provenance.getOccurredPeriod();
		// since this is unit test and the test runs fast, the start time could be same as the end time
		assertThat(period.getStart()).isBeforeOrEqualTo(period.getEnd());
		assertThat(period.getStart()).isBetween(oneMinuteAgo, now, true, true);
		assertThat(period.getEnd()).isEqualTo(provenance.getRecorded());

		// validate provenance.reason
		assertThat(provenance.getReason()).hasSize(1);
		Coding reasonCoding = provenance.getReason().get(0).getCodingFirstRep();
		assertThat(reasonCoding).isNotNull();
		assertThat(reasonCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/v3-ActReason");
		assertThat(reasonCoding.getCode()).isEqualTo("PATADMIN");

		//validate provenance.activity
		Coding activityCoding = provenance.getActivity().getCodingFirstRep();
		assertThat(activityCoding).isNotNull();
		assertThat(activityCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle");
		assertThat(activityCoding.getCode()).isEqualTo("merge");
	}



	private void setupReplaceReferencesForSuccessForSync() {
		Parameters parameters = new Parameters();
		Parameters.ParametersParameterComponent outcomeParameter = new Parameters.ParametersParameterComponent();
		outcomeParameter.setName("outcome");
		outcomeParameter.setResource(new Bundle());
		parameters.addParameter(outcomeParameter);

		when(myReplaceReferencesSvcMock.replaceReferences(isA(ReplaceReferencesRequest.class),
			eq(myRequestDetailsMock))).thenReturn(parameters);
	}

	private void setupBatch2JobTaskHelperMock(Task theTaskToReturn) {
		when(myBatch2TaskHelperMock.startJobAndCreateAssociatedTask(
			eq(myTaskDaoMock),
			eq(myRequestDetailsMock),
			eq(myJobCoordinatorMock),
			eq("MERGE"),
			any())).thenReturn(theTaskToReturn);
	}

	private void verifyBatch2JobTaskHelperMockInvocation(MergeOperationInputParameters theInputParameters) {
		ArgumentCaptor<BatchJobParametersWithTaskId> jobParametersCaptor =
			ArgumentCaptor.forClass(BatchJobParametersWithTaskId.class);
		verify(myBatch2TaskHelperMock).startJobAndCreateAssociatedTask(
			eq(myTaskDaoMock),
			eq(myRequestDetailsMock),
			eq(myJobCoordinatorMock),
			eq("MERGE"),
			jobParametersCaptor.capture());

		assertThat(jobParametersCaptor.getValue()).isInstanceOf(MergeJobParameters.class);
		MergeJobParameters capturedJobParams = (MergeJobParameters) jobParametersCaptor.getValue();
		assertThat(capturedJobParams.getBatchSize()).isEqualTo(PAGE_SIZE);
		assertThat(capturedJobParams.getSourceId()).hasToString(SOURCE_PATIENT_TEST_ID);
		assertThat(capturedJobParams.getTargetId()).hasToString(TARGET_PATIENT_TEST_ID);
		assertThat(capturedJobParams.getPartitionId()).isEqualTo(myRequestPartitionIdMock);

		assertThat(capturedJobParams.getOriginalInputParameters()).isEqualTo(myFhirContext.newJsonParser().encodeResourceToString(theInputParameters.getOriginalInputParameters()));

	}

	private void setupDaoMockForSuccessfulTargetPatientUpdate(Patient thePatientExpectedAsInput,
															  Patient thePatientToReturnInDaoOutcome) {
		DaoMethodOutcome daoMethodOutcome = new DaoMethodOutcome();
		daoMethodOutcome.setResource(thePatientToReturnInDaoOutcome);
		when(myPatientDaoMock.update(thePatientExpectedAsInput, myRequestDetailsMock))
			.thenAnswer(t -> {
				myCapturedTargetPatientForUpdate = t.getArgument(0);
				DaoMethodOutcome outcome = new DaoMethodOutcome();
				outcome.setResource(thePatientToReturnInDaoOutcome);
				outcome.setOperationOutcome(new OperationOutcome());
				return outcome;
			});
	}

	private void setOriginalInputParameters(MergeOperationInputParameters theMergeOperationInputParameters) {
		Parameters originalInputParameters = new Parameters();
		if (theMergeOperationInputParameters.getSourceResource() != null) {
			originalInputParameters.addParameter("source-patient", new Reference(theMergeOperationInputParameters.getSourceResource().getReferenceElement()));
		}
		if (theMergeOperationInputParameters.getTargetResource() != null) {
			originalInputParameters.addParameter("target-patient", new Reference(theMergeOperationInputParameters.getTargetResource().getReferenceElement()));
		}
		if (theMergeOperationInputParameters.getSourceIdentifiers() != null) {
			theMergeOperationInputParameters.getSourceIdentifiers().forEach(i ->
				originalInputParameters.addParameter("source-patient-identifier", new Identifier().setSystem(i.getSystemElement().getValue()).setValue(i.getValueElement().getValue())));
		}
		if(theMergeOperationInputParameters.getTargetIdentifiers() != null) {
			theMergeOperationInputParameters.getTargetIdentifiers().forEach(i ->
				originalInputParameters.addParameter("target-patient-identifier", new Identifier().setSystem(i.getSystemElement().getValue()).setValue(i.getValueElement().getValue())));
		}
		if (theMergeOperationInputParameters.getDeleteSource()) {
			originalInputParameters.addParameter("delete-source", new BooleanType(true));
		}
		if (theMergeOperationInputParameters.getResultResource() != null) {
			Parameters.ParametersParameterComponent resultPatientParam = new Parameters.ParametersParameterComponent();
			resultPatientParam.setName("result-patient");
			resultPatientParam.setResource((Resource) theMergeOperationInputParameters.getResultResource());
			originalInputParameters.addParameter(resultPatientParam);
		}
		theMergeOperationInputParameters.setOriginalInputParameters(originalInputParameters);
	}
}
