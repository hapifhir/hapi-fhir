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
		MergeOperationParameters mergeOperationParameters = new MergeOperationParameters();
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);
		assertThat(operationOutcome.getIssue()).hasSize(1);
		assertThat(operationOutcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics()).contains("There are no source resource parameters provided, include either a source-patient, " +
			"source-patient-identifier parameter.");

		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testValidatesInputParameters_MissingTargetPatientParams_ReturnsErrorInOutcomeWith400Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new MergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		assertThat(operationOutcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics()).contains("There are no target resource " +
			"parameters provided, include either a target-patient, target-patient-identifier parameter.");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testValidatesInputParameters_MissingBothSourceAndTargetPatientParams_ReturnsErrorsInOutcomeWith400Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new MergeOperationParameters();

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);
		assertThat(operationOutcome.getIssue()).hasSize(2);
		assertThat(operationOutcome.getIssue().get(0).getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssue().get(0).getDiagnostics()).contains("There are no source resource " +
			"parameters provided, include either a source-patient, source-patient-identifier parameter.");
		assertThat(operationOutcome.getIssue().get(1).getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssue().get(1).getDiagnostics()).contains("There are no target resource " +
			"parameters provided, include either a target-patient, target-patient-identifier parameter.");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testValidatesInputParameters_BothSourceResourceParamsProvided_ReturnsErrorInOutcomeWith400Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new MergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		assertThat(operationOutcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics()).contains("Source patient must be provided " +
			"either by source-patient-identifier or by source-resource, not both.");


		verifyNoMoreInteractions(myDaoMock);
	}


	@Test
	void testValidatesInputParameters_BothTargetResourceParamsProvided_ReturnsErrorInOutcomeWith400Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new MergeOperationParameters();
		mergeOperationParameters.setTargetResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue( "val")));
		mergeOperationParameters.setSourceResource(new Reference("Patient/345"));
		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(400);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		assertThat(operationOutcome.getIssueFirstRep().getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics()).contains("Target patient must be provided " +
			"either by target-patient-identifier or by target-resource, not both.");

		verifyNoMoreInteractions(myDaoMock);
	}

}
