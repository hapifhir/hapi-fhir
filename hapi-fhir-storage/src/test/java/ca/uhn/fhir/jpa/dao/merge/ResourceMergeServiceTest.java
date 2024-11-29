package ca.uhn.fhir.jpa.dao.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
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

	private static final String MISSING_SOURCE_PARAMS_MSG =
		"There are no source resource parameters provided, include either a 'source-patient', or a 'source-patient-identifier' parameter.";
	private static final String MISSING_TARGET_PARAMS_MSG =
		"There are no target resource parameters provided, include either a 'target-patient', or a 'target-patient-identifier' parameter.";
	private static final String BOTH_SOURCE_PARAMS_PROVIDED_MSG =
		"Source resource must be provided either by 'source-patient' or by 'source-patient-identifier', not both.";
	private static final String BOTH_TARGET_PARAMS_PROVIDED_MSG =
		"Target resource must be provided either by 'target-patient' or by 'target-patient-identifier', not both.";
	private static final String SUCCESSFUL_MERGE_MSG = "Merge operation completed successfully";

	@Mock
	private IFhirResourceDaoPatient<Patient> myDaoMock;
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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();

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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
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

	@Test
	void testResolvesSourceResourceByReference_ResourceNotFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		when(myDaoMock.read(new IdType("Patient/123"), myRequestDetailsMock)).thenThrow(ResourceNotFoundException.class);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testResolvesTargetResourceByReference_ResourceNotFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123", Collections.emptyList());
		setupDaoMockForSuccessfulRead(sourcePatient);
		when(myDaoMock.read(new IdType("Patient/345"), myRequestDetailsMock)).thenThrow(ResourceNotFoundException.class);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testResolvesSourceResourceByIdentifierInReference_NoMatchFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference().setIdentifier(new Identifier().setSystem("sys").setValue("val")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		setupDaoMockSearchForIdentifiers(Collections.emptyList());

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParameters(List.of("sys|val"));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("No resources found matching the identifier(s) specified in 'source-patient'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testResolvesTargetResourceByIdentifierInReference_NoMatchFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference().setIdentifier(new Identifier().setSystem("sys").setValue("val")));
		setupDaoMockForSuccessfulRead(createPatient("Patient/123", Collections.emptyList()));
		setupDaoMockSearchForIdentifiers(Collections.emptyList());

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParameters(List.of("sys|val"));

		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(422);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains("No resources found matching the identifier(s) specified in " +
			"'target-patient'");
		assertThat(issue.getCode().toCode()).isEqualTo("not-found");

		verifyNoMoreInteractions(myDaoMock);
	}

	@Test
	void testResolvesSourceResourceByIdentifiers_NoMatchFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		setupDaoMockSearchForIdentifiers(Collections.emptyList());

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParameters(List.of("sys|val1", "sys|val2"));

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
	void testResolvesTargetResourceByIdentifiers_NoMatchFound_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(
			new CanonicalIdentifier().setSystem("sys").setValue("val1"),
			new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		setupDaoMockSearchForIdentifiers(Collections.emptyList());
		Patient sourcePatient = createPatient("Patient/123", Collections.emptyList());
		setupDaoMockForSuccessfulRead(sourcePatient);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		verifySearchParameters(List.of("sys|val1", "sys|val2"));

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
	void testResolvesSourceResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123/_history/1"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345"));
		Patient sourcePatient = createPatient("Patient/123/_history/2", Collections.emptyList());
		setupDaoMockForSuccessfulRead(sourcePatient);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
	void testResolvesTargetResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsErrorInOutcomeWith422Status() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345/_history/1"));
		Patient sourcePatient = createPatient("Patient/123", Collections.emptyList());
		Patient targetPatient = createPatient("Patient/345/_history/2", Collections.emptyList());
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

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
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
		mergeOperationParameters.setTargetResourceIdentifiers(List.of(new CanonicalIdentifier().setSystem("sys").setValue("val2")));
		Patient sourcePatient = createPatient("Patient/123", Collections.emptyList());
		Patient targetPatient = createPatient("Patient/123", Collections.emptyList());
		setupDaoMockSearchForIdentifiers(List.of(sourcePatient), List.of(targetPatient));

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then

		verifySearchParameters(List.of("sys|val1"), List.of("sys|val2"));
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
	void testMerge_ResolvesResourcesByReferenceThatHasVersions_CurrentResourceVersionAreTheSame_NoErrorsReturned() {
		// Given
		MergeOperationParameters mergeOperationParameters = new PatientMergeOperationParameters();
		mergeOperationParameters.setSourceResource(new Reference("Patient/123/_history/2"));
		mergeOperationParameters.setTargetResource(new Reference("Patient/345/_history/2"));
		Patient sourcePatient = createPatient("Patient/123/_history/2", Collections.emptyList());
		Patient targetPatient = createPatient("Patient/345/_history/2", Collections.emptyList());
		setupDaoMockForSuccessfulRead(sourcePatient);
		setupDaoMockForSuccessfulRead(targetPatient);

		// When
		ResourceMergeService.MergeOutcome mergeOutcome = myResourceMergeService.merge(mergeOperationParameters, myRequestDetailsMock);

		// Then
		OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
		assertThat(mergeOutcome.getHttpStatusCode()).isEqualTo(200);

		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
		assertThat(issue.getDiagnostics()).contains(SUCCESSFUL_MERGE_MSG);

		//TODO: enable this
		//verifyNoMoreInteractions(myDaoMock);
	}




	private Patient createPatient(String theId, List<String> replacedByLinks) {
		Patient patient = new Patient();
		patient.setId(theId);
		for (String replacedByLink : replacedByLinks) {
			patient.addLink().setType(Patient.LinkType.REPLACEDBY).setOther(new Reference(replacedByLink));
		}
		return patient;
	}

	private void setupDaoMockForSuccessfulRead(Patient resource) {
		assertThat(resource.getIdElement()).isNotNull();
		//dao reads the versionless id
		when(myDaoMock.read(resource.getIdElement().toVersionless(), myRequestDetailsMock)).thenReturn(resource);
	}


	private void setupDaoMockSearchForIdentifiers(List<IBaseResource>... theMatchingResourcesOnInvocations) {

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



	private void verifySearchParameters(List<String>... theExpectedIdentifierParams) {
		ArgumentCaptor<SearchParameterMap> captor = ArgumentCaptor.forClass(SearchParameterMap.class);
		verify(myDaoMock, times(theExpectedIdentifierParams.length)).search(captor.capture(), eq(myRequestDetailsMock));
		List<SearchParameterMap> maps = captor.getAllValues();
		assertThat(maps).hasSize(theExpectedIdentifierParams.length);
		for (int i = 0; i < maps.size(); i++) {
			verifySearchParameterSingleInvocation(maps.get(i), theExpectedIdentifierParams[i]);
		}

	}

	private void verifySearchParameterSingleInvocation(SearchParameterMap capturedMap,
													   List<String> theExpectedIdentifierParams) {
		List<List<IQueryParameterType>> actualIdentifierParams = capturedMap.get("identifier");
		assertThat(actualIdentifierParams).hasSameSizeAs(theExpectedIdentifierParams);
		for (int i = 0; i < theExpectedIdentifierParams.size(); i++) {
			assertThat(actualIdentifierParams.get(i)).hasSize(1);
			assertThat(actualIdentifierParams.get(i).get(0).getValueAsQueryToken(myFhirContext)).isEqualTo(theExpectedIdentifierParams.get(i));
		}
	}
}
