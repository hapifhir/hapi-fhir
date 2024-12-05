package ca.uhn.fhir.jpa.dao.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.IReplaceReferencesSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceMergeServiceTest {
	private static final Integer PAGE_SIZE = 1024;
	
	private static final String MISSING_SOURCE_PARAMS_MSG =
		"There are no source resource parameters provided, include either a 'source-patient', or a 'source-patient-identifier' parameter.";
	private static final String MISSING_TARGET_PARAMS_MSG =
		"There are no target resource parameters provided, include either a 'target-patient', or a 'target-patient-identifier' parameter.";
	private static final String BOTH_SOURCE_PARAMS_PROVIDED_MSG =
		"Source resource must be provided either by 'source-patient' or by 'source-patient-identifier', not both.";
	private static final String BOTH_TARGET_PARAMS_PROVIDED_MSG =
		"Target resource must be provided either by 'target-patient' or by 'target-patient-identifier', not both.";
	private static final String SUCCESSFUL_MERGE_MSG = "Merge operation completed successfully";
	private static final String SOURCE_PATIENT_TEST_ID = "Patient/123";
	private static final String TARGET_PATIENT_TEST_ID = "Patient/456";

	@Mock
	private IFhirResourceDaoPatient<Patient> myDaoMock;

	@Mock
	IReplaceReferencesSvc myReplaceReferencesSvcMock;

	@Mock
	RequestDetails myRequestDetailsMock;

	private ResourceMergeService myResourceMergeService;

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@BeforeEach
	void setup() {
		when(myDaoMock.getContext()).thenReturn(myFhirContext);
		myResourceMergeService = new ResourceMergeService(myDaoMock, myReplaceReferencesSvcMock);
	}

	// SUCCESS CASES
	@Test
	void testMerge_WithoutResultResource_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, new Patient());
		Patient patientReturnedFromDaoAfterTargetUpdate = new Patient();
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientReturnedFromDaoAfterTargetUpdate, true);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);
		assertThat(mergeOutcome.getUpdatedTargetResource()).isEqualTo(patientReturnedFromDaoAfterTargetUpdate);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains(SUCCESSFUL_MERGE_MSG);

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_WithResultResource_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
		resultPatient.addLink().setType(Patient.LinkType.REPLACES).setOther(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setResultResource(resultPatient);
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);

		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		setupDaoMockForSuccessfulSourcePatientUpdate(sourcePatient, new Patient());
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = new Patient();
		setupDaoMockForSuccessfulTargetPatientUpdate(resultPatient, patientToBeReturnedFromDaoAfterTargetUpdate, true);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);
		assertThat(mergeOutcome.getUpdatedTargetResource()).isEqualTo(patientToBeReturnedFromDaoAfterTargetUpdate);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains(SUCCESSFUL_MERGE_MSG);

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_WithDeleteSourceTrue_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setDeleteSource(true);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		when(myDaoMock.delete(new IdType(SOURCE_PATIENT_TEST_ID), myRequestDetailsMock)).thenReturn(new DaoMethodOutcome());
		Patient patientToBeReturnedFromDaoAfterTargetUpdate = new Patient();
		setupDaoMockForSuccessfulTargetPatientUpdate(targetPatient, patientToBeReturnedFromDaoAfterTargetUpdate, false);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);
		assertThat(mergeOutcome.getUpdatedTargetResource()).isEqualTo(patientToBeReturnedFromDaoAfterTargetUpdate);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains(SUCCESSFUL_MERGE_MSG);

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_WithPreviewTrue_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setPreview(true);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);
		assertThat(mergeOutcome.getUpdatedTargetResource()).isEqualTo(targetPatient);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains("Preview only merge operation - no issues detected");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesResourcesByReferenceThatHasVersions_CurrentResourceVersionAreTheSame_Success() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123/_history/2"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345/_history/2"));
		Patient sourcePatient = createPatient("Patient/123/_history/2");
		Patient targetPatient = createPatient("Patient/345/_history/2");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);
		when(myDaoMock.update(any(), eq(myRequestDetailsMock))).thenReturn(new DaoMethodOutcome());

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains(SUCCESSFUL_MERGE_MSG);

		verifyNoMoreInteractions(myDaoMock);
	}

	//  ERROR CASES

	@Test
	void testMerge_UnhandledServerResponseExceptionThrown_UsesStatusCodeOfTheException() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));

		ForbiddenOperationException ex = new ForbiddenOperationException("this is the exception message");
		when(myDaoMock.read(any(), eq(myRequestDetailsMock))).thenThrow(ex);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(403);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("this is the exception message");
		assertThat(issue.getCode().toCode()).isEqualTo("exception");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_UnhandledExceptionThrown_Uses500StatusCode() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));

		RuntimeException ex = new RuntimeException("this is the exception message");
		when(myDaoMock.read(any(), eq(myRequestDetailsMock))).thenThrow(ex);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(500);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("this is the exception message");
		assertThat(issue.getCode().toCode()).isEqualTo("exception");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ValidatesInputParameters_MissingSourcePatientParams_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);
		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(MISSING_SOURCE_PARAMS_MSG);
		assertThat(issue.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesInputParameters_MissingTargetPatientParams_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(MISSING_TARGET_PARAMS_MSG);
		assertThat(issue.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ValidatesInputParameters_MissingBothSourceAndTargetPatientParams_ReturnsErrorsWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);
		assertThat(operationOutcome.getIssue()).hasSize(2);

		OperationOutcome.OperationOutcomeIssueComponent issue1 = operationOutcome.getIssue().get(0);
		OperationOutcome.OperationOutcomeIssueComponent issue2 = operationOutcome.getIssue().get(1);
		assertThat(issue1.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue1.getDiagnostics()).contains(MISSING_SOURCE_PARAMS_MSG);
		assertThat(issue1.getCode().toCode()).isEqualTo("required");
		assertThat(issue2.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue2.getDiagnostics()).contains(MISSING_TARGET_PARAMS_MSG);
		assertThat(issue2.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ValidatesInputParameters_BothSourceResourceAndSourceIdentifierParamsProvided_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(BOTH_SOURCE_PARAMS_PROVIDED_MSG);
		assertThat(issue.getCode().toCode()).isEqualTo("required");


		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesInputParameters_BothTargetResourceAndTargetIdentifiersParamsProvided_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setSourceResource(new Reference("Patient/345"));
		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(BOTH_TARGET_PARAMS_PROVIDED_MSG);
		assertThat(issue.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesInputParameters_SourceResourceParamHasNoReferenceElement_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference());
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Reference specified in 'source-patient' parameter does not have a reference element.");
		assertThat(issue.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesInputParameters_TargetResourceParamHasNoReferenceElement_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference());

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);

		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Reference specified in 'target-patient' parameter does not have " +
			"a reference element.");
		assertThat(issue.getCode().toCode()).isEqualTo("required");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesSourceResourceByReference_ResourceNotFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		when(myDaoMock.read(new IdType("Patient/123"), myRequestDetailsMock)).thenThrow(ResourceNotFoundException.class);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Resource not found for the reference specified in 'source-patient'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesTargetResourceByReference_ResourceNotFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123");
		setupDaoMockForSuccessfulRead(sourcePatient);
		when(myDaoMock.read(new IdType("Patient/345"), myRequestDetailsMock)).thenThrow(ResourceNotFoundException.class);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Resource not found for the reference specified in 'target-patient'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesSourceResourceByIdentifiers_NoMatchFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		setupDaoMockSearchForIdentifiers(List.of(Collections.emptyList()));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1","sys|val2")));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("No resources found matching the identifier(s) specified in 'source-patient-identifier'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ResolvesSourceResourceByIdentifiers_MultipleMatchesFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		setupDaoMockSearchForIdentifiers(List.of(
			List.of(
				createPatient("Patient/match-1"),
				createPatient("Patient/match-2"))
		));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1")));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Multiple resources found matching the identifier(s) specified in" +
			" 'source-patient-identifier'");
		assertThat(issue.getCode().toCode()).isEqualTo("multiple-matches");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ResolvesTargetResourceByIdentifiers_NoMatchFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		setupDaoMockSearchForIdentifiers(List.of(Collections.emptyList()));
		Patient sourcePatient = createPatient("Patient/123");
		setupDaoMockForSuccessfulRead(sourcePatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1", "sys|val2")));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("No resources found matching the identifier(s) specified in " +
			"'target-patient-identifier'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesTargetResourceByIdentifiers_MultipleMatchesFound_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
		setupDaoMockSearchForIdentifiers(List.of(
			List.of(
				createPatient("Patient/match-1"),
				createPatient("Patient/match-2"))
		));

		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		setupDaoMockForSuccessfulRead(sourcePatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1")));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Multiple resources found matching the identifier(s) specified in 'target-patient-identifier'");
		assertThat(issue.getCode().toCode()).isEqualTo("multiple-matches");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesSourceResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123/_history/1"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123/_history/2");
		setupDaoMockForSuccessfulRead(sourcePatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("The reference in 'source-patient' parameter has a version specified, but it is not the latest version of the resource");
		assertThat(issue.getCode().toCode()).isEqualTo("conflict");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ResolvesTargetResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345/_history/1"));
		Patient sourcePatient = createPatient("Patient/123");
		Patient targetPatient = createPatient("Patient/345/_history/2");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("The reference in 'target-patient' parameter has a version " +
			"specified, but it is not the latest version of the resource");
		assertThat(issue.getCode().toCode()).isEqualTo("conflict");

		verifyNoMoreInteractions(myDaoMock);
	}





	@Test
	void testMerge_SourceAndTargetResolvesToSameResource_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		Patient sourcePatient = createPatient("Patient/123");
		Patient targetPatient = createPatient("Patient/123");
		setupDaoMockSearchForIdentifiers(List.of(List.of(sourcePatient), List.of(targetPatient)));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then

		verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1"), List.of("sys|val2")));
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Source and target resources are the same resource.");

		//TODO: enable this
		//verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_TargetResourceIsInactive_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123");
		Patient targetPatient = createPatient("Patient/345");
		targetPatient.setActive(false);
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Target resource is not active, it must be active to be the target of a merge operation");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_TargetResourceWasPreviouslyReplacedByAnotherResource_ReturnsErrorWith422Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123");
		Patient targetPatient = createPatient("Patient/345");
		addReplacedByLink(targetPatient, "Patient/678");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("Target resource was previously replaced by another resource, it is not a suitable target for merging.");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ValidatesResultResource_ResultResourceHasDifferentIdThanTargetResource_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient resultPatient = createPatient("Patient/678");
		addReplacesLink(resultPatient, "Patient/123");
		mergeOperationParameters.setResultResource(resultPatient);

		Patient sourcePatient = createPatient("Patient/123/_history/1");
		Patient targetPatient = createPatient("Patient/345/_history/1");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("'result-patient' must have the same versionless id as the actual resolved target resource. The actual resolved target resource's id is: 'Patient/345'");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesResultResource_ResultResourceDoesNotHaveAllIdentifiersProvidedInTargetIdentifiers_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")
		));

		// the result patient has only one of the identifiers that were provided in the target identifiers
		Patient resultPatient = createPatient("Patient/345");
		resultPatient.addIdentifier().setSystem("sys").setValue("val");
		addReplacesLink(resultPatient, "Patient/123");
		mergeOperationParameters.setResultResource(resultPatient);
		Patient sourcePatient = createPatient("Patient/123/_history/1");
		Patient targetPatient = createPatient("Patient/345/_history/1");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockSearchForIdentifiers(List.of(List.of(targetPatient)));

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("'result-patient' must have all the identifiers provided in target-patient-identifier");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesResultResource_ResultResourceHasNoReplacesLink_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));

		Patient resultPatient = createPatient("Patient/345");
		mergeOperationParameters.setResultResource(resultPatient);
		Patient sourcePatient = createPatient("Patient/123/_history/1");
		Patient targetPatient = createPatient("Patient/345/_history/1");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("'result-patient' must have a 'replaces' link to the source resource.");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testMerge_ValidatesResultResource_ResultResourceHasReplacesLinkAndDeleteSourceIsTrue_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters();
		mergeOperationParameters.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
		mergeOperationParameters.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
		mergeOperationParameters.setDeleteSource(true);

		Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
		addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);
		mergeOperationParameters.setResultResource(resultPatient);
		Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
		Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("'result-patient' must not have a 'replaces' link to the source resource when the source resource will be deleted, as the link may prevent deleting the source resource.");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testMerge_ValidatesResultResource_ResultResourceHasRedundantReplacesLinksToSource_ReturnsErrorWith400Status() {
		// Given
		MergeOperationInputParameters mergeOperationParameters = new PatientMergeOperationInputParameters(PAGE_SIZE);
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));

		Patient resultPatient = createPatient("Patient/345");
		//add the link twice
		addReplacesLink(resultPatient, "Patient/123");
		addReplacesLink(resultPatient, "Patient/123");

		mergeOperationParameters.setResultResource(resultPatient);
		Patient sourcePatient = createPatient("Patient/123/_history/1");
		Patient targetPatient = createPatient("Patient/345/_history/1");
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		MergeOperationOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("'result-patient' has multiple 'replaces' links to the source resource. There should be only one.");

		verifyNoMoreInteractions(myDaoMock);
	}

	private Patient createPatient(String theId) {
		Patient patient = new Patient();
		patient.setId(theId);
		return patient;
	}

	private void addReplacedByLink(Patient thePatient, String theReplacingResourceId) {
		thePatient.addLink().setType(Patient.LinkType.REPLACEDBY).setOther(new Reference(theReplacingResourceId));
	}

	private void addReplacesLink(Patient patient, String theReplacedResourceId) {
		patient.addLink().setType(Patient.LinkType.REPLACES).setOther(new Reference(theReplacedResourceId));
	}

	private void setupDaoMockForSuccessfulRead(Patient resource) {
		assertThat(resource.getIdElement()).isNotNull();
		//dao reads the versionless id
		when(myDaoMock.read(resource.getIdElement().toVersionless(), myRequestDetailsMock)).thenReturn(resource);
	}


	/**
	 * Sets up the dao mock to return the given list of resources for each invocation of the search method
	 * @param theMatchingResourcesOnInvocations list containing the list of resources the search should return on each
	 *                                          invocation of the search method, i.e. one list per invocation
	 */
	private void setupDaoMockSearchForIdentifiers(List<List<IBaseResource>> theMatchingResourcesOnInvocations) {

		OngoingStubbing<IBundleProvider> ongoingStubbing = null;
		for (List<IBaseResource> matchingResources : theMatchingResourcesOnInvocations) {
			IBundleProvider bundleProviderMock = mock(IBundleProvider.class);
			when(bundleProviderMock.getAllResources()).thenReturn(matchingResources);
			if (ongoingStubbing == null) {
				ongoingStubbing = when(myDaoMock.search(any(), eq(myRequestDetailsMock))).thenReturn(bundleProviderMock);
			}
			else {
				ongoingStubbing.thenReturn(bundleProviderMock);
			}

		}
	}


	private void setupDaoMockForSuccessfulSourcePatientUpdate(Patient thePatientExpectedAsInput,
															  Patient thePatientToReturnInDaoOutcome) {
		DaoMethodOutcome daoMethodOutcome = new DaoMethodOutcome();
		daoMethodOutcome.setResource(thePatientToReturnInDaoOutcome);
		when(myDaoMock.update(thePatientExpectedAsInput, myRequestDetailsMock))
			.thenAnswer(t -> {
				Patient capturedSourcePatient = t.getArgument(0);
				assertThat(capturedSourcePatient.getLink()).hasSize(1);
				assertThat(capturedSourcePatient.getLinkFirstRep().getType()).isEqualTo(Patient.LinkType.REPLACEDBY);
				assertThat(capturedSourcePatient.getLinkFirstRep().getOther().getReference()).isEqualTo(TARGET_PATIENT_TEST_ID);

				DaoMethodOutcome outcome = new DaoMethodOutcome();
				outcome.setResource(thePatientToReturnInDaoOutcome);
				return outcome;
			});
	}

	private void setupDaoMockForSuccessfulTargetPatientUpdate(Patient thePatientExpectedAsInput,
															  Patient thePatientToReturnInDaoOutcome,
															  boolean theExpectLinkToSourcePatient) {
		DaoMethodOutcome daoMethodOutcome = new DaoMethodOutcome();
		daoMethodOutcome.setResource(thePatientToReturnInDaoOutcome);
		when(myDaoMock.update(thePatientExpectedAsInput, myRequestDetailsMock))
			.thenAnswer(t -> {
				Patient capturedTargetPatient = t.getArgument(0);
				if (theExpectLinkToSourcePatient) {
					assertThat(capturedTargetPatient.getLink()).hasSize(1);
					assertThat(capturedTargetPatient.getLinkFirstRep().getType()).isEqualTo(Patient.LinkType.REPLACES);
					assertThat(capturedTargetPatient.getLinkFirstRep().getOther().getReference()).isEqualTo(SOURCE_PATIENT_TEST_ID);
				}
				else {
					assertThat(capturedTargetPatient.getLink()).isEmpty();
				}
				DaoMethodOutcome outcome = new DaoMethodOutcome();
				outcome.setResource(thePatientToReturnInDaoOutcome);
				return outcome;
			});
	}

	private void verifySearchParametersOnDaoSearchInvocations(List<List<String>> theExpectedIdentifierParams) {
		ArgumentCaptor<SearchParameterMap> captor = ArgumentCaptor.forClass(SearchParameterMap.class);
		verify(myDaoMock, times(theExpectedIdentifierParams.size())).search(captor.capture(), eq(myRequestDetailsMock));
		List<SearchParameterMap> maps = captor.getAllValues();
		assertThat(maps).hasSameSizeAs(theExpectedIdentifierParams);
		for (int i = 0; i < maps.size(); i++) {
			verifySearchParameterOnSingleDaoSearchInvocation(maps.get(i), theExpectedIdentifierParams.get(i));
		}

	}

	private void verifySearchParameterOnSingleDaoSearchInvocation(SearchParameterMap capturedMap,
																  List<String> theExpectedIdentifierParams) {
		List<List<IQueryParameterType>> actualIdentifierParams = capturedMap.get("identifier");
		assertThat(actualIdentifierParams).hasSameSizeAs(theExpectedIdentifierParams);
		for (int i = 0; i < theExpectedIdentifierParams.size(); i++) {
			assertThat(actualIdentifierParams.get(i)).hasSize(1);
			assertThat(actualIdentifierParams.get(i).get(0).getValueAsQueryToken(myFhirContext)).isEqualTo(theExpectedIdentifierParams.get(i));
		}
	}
}
