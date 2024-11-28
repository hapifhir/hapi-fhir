package ca.uhn.fhir.jpa.dao.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceMergeServiceTest {

	private static final String MISSING_SOURCE_PARAMS_MSG =
		"There are no source resource parameters provided, include either a source-patient, or a source-patient-identifier parameter.";
	private static final String MISSING_TARGET_PARAMS_MSG =
		"There are no target resource parameters provided, include either a target-patient, or a target-patient-identifier parameter.";
	private static final String BOTH_SOURCE_PARAMS_PROVIDED_MSG =
		"Source resource must be provided either by source-patient or by source-patient-identifier, not both.";
	private static final String BOTH_TARGET_PARAMS_PROVIDED_MSG =
		"Target resource must be provided either by target-patient or by target-patient-identifier, not both.";

	@Mock
	private IFhirResourceDaoPatient<?> myDaoMock;
	@Mock
	RequestDetails myRequestDetailsMock;

	private ResourceMergeService myResourceMergeService;

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@BeforeEach
	void setup() {
		when(myDaoMock.getContext()).thenReturn(myFhirContext);
		myResourceMergeService = new ResourceMergeService(myDaoMock);
	}



	@Test
	void testValidatesInputParameters_MissingSourcePatientParams_ReturnsErrorInOutcomeWith400Status() {
		// Given
		BaseMergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testValidatesInputParameters_MissingTargetPatientParams_ReturnsErrorInOutcomeWith400Status() {
		// Given
		BaseMergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testValidatesInputParameters_MissingBothSourceAndTargetPatientParams_ReturnsErrorsInOutcomeWith400Status() {
		// Given
		BaseMergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testValidatesInputParameters_BothSourceResourceParamsProvided_ReturnsErrorInOutcomeWith400Status() {
		// Given
		BaseMergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testValidatesInputParameters_BothTargetResourceParamsProvided_ReturnsErrorInOutcomeWith400Status() {
		// Given
		BaseMergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setSourceResource(new Reference("Patient/345"));
		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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

}
