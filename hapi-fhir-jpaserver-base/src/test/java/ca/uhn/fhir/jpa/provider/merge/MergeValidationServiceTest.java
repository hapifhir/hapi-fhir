package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.merge.ExtensionBasedLinkService;
import ca.uhn.fhir.merge.PatientNativeLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-6
@ExtendWith(MockitoExtension.class)
class MergeValidationServiceTest {

	private static final String MISSING_SOURCE_PARAMS_MSG =
		"There are no source resource parameters provided, include either a 'source-patient', or a 'source-patient-identifier' parameter.";
	private static final String MISSING_TARGET_PARAMS_MSG =
		"There are no target resource parameters provided, include either a 'target-patient', or a 'target-patient-identifier' parameter.";
	private static final String BOTH_SOURCE_PARAMS_PROVIDED_MSG =
		"Source resource must be provided either by 'source-patient' or by 'source-patient-identifier', not both.";
	private static final String BOTH_TARGET_PARAMS_PROVIDED_MSG =
		"Target resource must be provided either by 'target-patient' or by 'target-patient-identifier', not both.";

	private static final String SOURCE_PATIENT_TEST_ID = "Patient/123";
	private static final String SOURCE_PATIENT_TEST_ID_WITH_VERSION_1 = SOURCE_PATIENT_TEST_ID + "/_history/1";
	private static final String SOURCE_PATIENT_TEST_ID_WITH_VERSION_2 = SOURCE_PATIENT_TEST_ID + "/_history/2";
	private static final String TARGET_PATIENT_TEST_ID = "Patient/456";
	private static final String TARGET_PATIENT_TEST_ID_WITH_VERSION_1 = TARGET_PATIENT_TEST_ID + "/_history/1";
	private static final String TARGET_PATIENT_TEST_ID_WITH_VERSION_2 = TARGET_PATIENT_TEST_ID + "/_history/2";
	private static final Integer PAGE_SIZE = 1024;

	@Mock
	DaoRegistry myDaoRegistryMock;

	@Mock
	IFhirResourceDao<IBaseResource> myPatientDaoMock;

	@Mock
	RequestDetails myRequestDetailsMock;

	@Mock
	ISearchParamExtractor mySearchParamExtractorMock;

	@Mock
	IFhirResourceDao<IBaseResource> myOrganizationDaoMock;

	@Mock
	IFhirResourceDao<IBaseResource> myObservationDaoMock;

	private MergeValidationService myMergeValidationService;

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@BeforeEach
	void setup() {
		lenient().when(myDaoRegistryMock.getResourceDao("Patient")).thenReturn(myPatientDaoMock);
		lenient().when(myRequestDetailsMock.getResourceName()).thenReturn("Patient");

		PatientNativeLinkService patientNativeLinkService = new PatientNativeLinkService();
		ExtensionBasedLinkService extensionBasedLinkService = new ExtensionBasedLinkService();
		ResourceLinkServiceFactory resourceLinkServiceFactory =
			new ResourceLinkServiceFactory(patientNativeLinkService, extensionBasedLinkService);

		myMergeValidationService = new MergeValidationService(
			myFhirContext, myDaoRegistryMock, resourceLinkServiceFactory, mySearchParamExtractorMock);
	}

	@Nested
	class InputParameterValidation {

		@Test
		void testValidate_MissingSourcePatientParams_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400, MISSING_SOURCE_PARAMS_MSG, "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_MissingTargetPatientParams_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400, MISSING_TARGET_PARAMS_MSG, "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_MissingBothSourceAndTargetPatientParams_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(2);

			OperationOutcome.OperationOutcomeIssueComponent issue1 = operationOutcome.getIssue().get(0);
			OperationOutcome.OperationOutcomeIssueComponent issue2 = operationOutcome.getIssue().get(1);
			assertThat(issue1.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue1.getDiagnostics()).contains(MISSING_SOURCE_PARAMS_MSG);
			assertThat(issue1.getCode().toCode()).isEqualTo("required");
			assertThat(issue2.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue2.getDiagnostics()).contains(MISSING_TARGET_PARAMS_MSG);
			assertThat(issue2.getCode().toCode()).isEqualTo("required");

			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_BothSourceResourceAndSourceIdentifierParamsProvided_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setSourceResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val")));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400, BOTH_SOURCE_PARAMS_PROVIDED_MSG, "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_BothTargetResourceAndTargetIdentifiersParamsProvided_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			params.setTargetResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val")));
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400, BOTH_TARGET_PARAMS_PROVIDED_MSG, "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_SourceResourceParamHasNoReferenceElement_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference());
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400,
				"Reference specified in 'source-patient' parameter does not have a reference element.", "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}

		@Test
		void testValidate_TargetResourceParamHasNoReferenceElement_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference());

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 400,
				"Reference specified in 'target-patient' parameter does not have a reference element.", "required");
			verifyNoMoreInteractions(myPatientDaoMock);
		}
	}

	@Nested
	class ResourceResolution {

		@Test
		void testValidate_SourceResourceByReference_ResourceNotFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			when(myPatientDaoMock.read(new IdType(SOURCE_PATIENT_TEST_ID), myRequestDetailsMock))
				.thenThrow(ResourceNotFoundException.class);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Resource not found for the reference specified in 'source-patient'", "not-found");
		}

		@Test
		void testValidate_TargetResourceByReference_ResourceNotFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			setupDaoMockForSuccessfulRead(sourcePatient);
			when(myPatientDaoMock.read(new IdType(TARGET_PATIENT_TEST_ID), myRequestDetailsMock))
				.thenThrow(ResourceNotFoundException.class);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Resource not found for the reference specified in 'target-patient'", "not-found");
		}

		@Test
		void testValidate_SourceResourceByIdentifiers_NoMatchFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResourceIdentifiers(List.of(
				new CanonicalIdentifier().setSystem("sys").setValue("val1"),
				new CanonicalIdentifier().setSystem("sys").setValue("val2")));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			setupDaoMockSearchForIdentifiers(List.of(Collections.emptyList()));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1", "sys|val2")));
			verifyValidationFailure(result, mergeOutcome, 422,
				"No resources found matching the identifier(s) specified in 'source-patient-identifier'", "not-found");
		}

		@Test
		void testValidate_SourceResourceByIdentifiers_MultipleMatchesFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			setupDaoMockSearchForIdentifiers(List.of(
				List.of(
					createPatient("Patient/match-1"),
					createPatient("Patient/match-2"))));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1")));
			verifyValidationFailure(result, mergeOutcome, 422,
				"Multiple resources found matching the identifier(s) specified in 'source-patient-identifier'",
				"multiple-matches");
		}

		@Test
		void testValidate_TargetResourceByIdentifiers_NoMatchFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResourceIdentifiers(List.of(
				new CanonicalIdentifier().setSystem("sys").setValue("val1"),
				new CanonicalIdentifier().setSystem("sys").setValue("val2")));
			setupDaoMockSearchForIdentifiers(List.of(Collections.emptyList()));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			setupDaoMockForSuccessfulRead(sourcePatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1", "sys|val2")));
			verifyValidationFailure(result, mergeOutcome, 422,
				"No resources found matching the identifier(s) specified in 'target-patient-identifier'", "not-found");
		}

		@Test
		void testValidate_TargetResourceByIdentifiers_MultipleMatchesFound_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
			setupDaoMockSearchForIdentifiers(List.of(
				List.of(
					createPatient("Patient/match-1"),
					createPatient("Patient/match-2"))));

			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			setupDaoMockForSuccessfulRead(sourcePatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1")));
			verifyValidationFailure(result, mergeOutcome, 422,
				"Multiple resources found matching the identifier(s) specified in 'target-patient-identifier'",
				"multiple-matches");
		}

		@Test
		void testValidate_SourceResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			//make resolved patient has a more recent version than the one specified in the reference
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_2);
			setupDaoMockForSuccessfulRead(sourcePatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"The reference in 'source-patient' parameter has a version specified, but it is not the latest version of the resource",
				"conflict");
		}

		@Test
		void testValidate_TargetResourceByReferenceThatHasVersion_CurrentResourceVersionIsDifferent_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID_WITH_VERSION_1));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			// make resolved target patient has a more recent version than the one specified in the reference
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_2);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"The reference in 'target-patient' parameter has a version specified, but it is not the latest version of the resource",
				"conflict");
		}
	}

	@Nested
	class ResourceStateValidation {

		@Test
		void testValidate_SourceAndTargetResolvesToSameResource_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val1")));
			params.setTargetResourceIdentifiers(
				List.of(new CanonicalIdentifier().setSystem("sys").setValue("val2")));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(SOURCE_PATIENT_TEST_ID);
			setupDaoMockSearchForIdentifiers(List.of(List.of(sourcePatient), List.of(targetPatient)));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifySearchParametersOnDaoSearchInvocations(List.of(List.of("sys|val1"), List.of("sys|val2")));
			verifyValidationFailure(result, mergeOutcome, 422,
				"Source and target resources are the same resource.", "invalid");
		}

		@Test
		void testValidate_TargetResourceIsInactive_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
			targetPatient.setActive(false);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Target resource is not active, it must be active to be the target of a merge operation", "invalid");
		}

		@Test
		void testValidate_TargetResourceWasPreviouslyReplacedByAnotherResource_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
			addReplacedByLink(targetPatient, "Patient/replacing-res-id");
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Target resource was previously replaced by a resource with reference 'Patient/replacing-res-id', it is not a suitable target for merging.",
				"invalid");
		}

		@Test
		void testValidate_SourceResourceWasPreviouslyReplacedByAnotherResource_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
			addReplacedByLink(sourcePatient, "Patient/replacing-res-id");
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Source resource was previously replaced by a resource with reference 'Patient/replacing-res-id', it is not a suitable source for merging.",
				"invalid");
		}

		@Test
		void testValidate_ResourceTypeWithoutIdentifierElement_ReturnsInvalidWith422Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference("OperationOutcome/source-id"));
			params.setTargetResource(new Reference("OperationOutcome/target-id"));
			when(myRequestDetailsMock.getResourceName()).thenReturn("OperationOutcome");

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			verifyValidationFailure(result, mergeOutcome, 422,
				"Merge operation cannot be performed on resource type 'OperationOutcome' because it does not have an 'identifier' element.",
				"invalid");
			verifyNoMoreInteractions(myPatientDaoMock);
		}
	}

	@Nested
	class ResultResourceValidation {

		@Test
		void testValidate_ResultResourceHasDifferentIdThanTargetResource_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient resultPatient = createPatient("Patient/not-the-target-id");
			addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);
			params.setResultResource(resultPatient);

			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' must have the same versionless id as the actual" +
					" resolved target resource 'Patient/not-the-target-id'. The actual resolved target resource's id is: '"
					+ TARGET_PATIENT_TEST_ID + "'");
		}

		@Test
		void testValidate_ResultResourceDoesNotHaveAllIdentifiersProvidedInTargetIdentifiers_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResourceIdentifiers(List.of(
				new CanonicalIdentifier().setSystem("sysA").setValue("val1"),
				new CanonicalIdentifier().setSystem("sysB").setValue("val2")));

			// the result patient has only one of the identifiers that were provided in the target identifiers
			Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
			resultPatient.addIdentifier().setSystem("sysA").setValue("val1");
			resultPatient.addIdentifier().setSystem("sysC").setValue("val2");
			addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);
			params.setResultResource(resultPatient);
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockSearchForIdentifiers(List.of(List.of(targetPatient)));

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' must have all the identifiers provided in target-patient-identifier");
		}

		@Test
		void testValidate_ResultResourceHasNoReplacesLinkAtAll_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
			params.setResultResource(resultPatient);
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' must have a 'replaces' link to the source resource.");
		}

		@Test
		void testValidate_ResultResourceHasNoReplacesLinkToSource_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
			addReplacesLink(resultPatient, "Patient/not-the-source-id");

			params.setResultResource(resultPatient);
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' must have a 'replaces' link to the source resource.");
		}

		@Test
		void testValidate_ResultResourceHasReplacesLinkAndDeleteSourceIsTrue_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			params.setDeleteSource(true);

			Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
			addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);
			params.setResultResource(resultPatient);
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' must not have a 'replaces' link to the source resource when the source resource will be deleted, as the link may prevent deleting the source resource.");
		}

		@Test
		void testValidate_ResultResourceHasRedundantReplacesLinksToSource_ReturnsInvalidWith400Status() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));

			Patient resultPatient = createPatient(TARGET_PATIENT_TEST_ID);
			//add the link twice
			addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);
			addReplacesLink(resultPatient, SOURCE_PATIENT_TEST_ID);

			params.setResultResource(resultPatient);
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID_WITH_VERSION_1);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID_WITH_VERSION_1);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isFalse();
			assertThat(result.httpStatusCode).isEqualTo(400);

			OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
			assertThat(operationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
			assertThat(issue.getDiagnostics()).contains(
				"'result-patient' has multiple 'replaces' links to the source resource. There should be only one.");
		}
	}

	@Nested
	class PatientCompartmentValidation {

		@Test
		void testValidate_PatientResources_CompartmentCheckSkipped_ReturnsValid() {
			// Given
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference(SOURCE_PATIENT_TEST_ID));
			params.setTargetResource(new Reference(TARGET_PATIENT_TEST_ID));
			Patient sourcePatient = createPatient(SOURCE_PATIENT_TEST_ID);
			Patient targetPatient = createPatient(TARGET_PATIENT_TEST_ID);
			setupDaoMockForSuccessfulRead(sourcePatient);
			setupDaoMockForSuccessfulRead(targetPatient);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isTrue();
			verifyNoInteractions(mySearchParamExtractorMock);
		}

		@Test
		void testValidate_NonCompartmentResourceType_CompartmentCheckSkipped_ReturnsValid() {
			// Given
			when(myRequestDetailsMock.getResourceName()).thenReturn("Organization");
			when(myDaoRegistryMock.getResourceDao("Organization")).thenReturn(myOrganizationDaoMock);

			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference("Organization/ORG1"));
			params.setTargetResource(new Reference("Organization/ORG2"));
			IBaseResource sourceOrg = createOrganization("Organization/ORG1");
			IBaseResource targetOrg = createOrganization("Organization/ORG2");
			setupDaoMockForSuccessfulRead(myOrganizationDaoMock, sourceOrg);
			setupDaoMockForSuccessfulRead(myOrganizationDaoMock, targetOrg);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isTrue();
		}

		static Stream<Arguments> compartmentCases() {
			BaseSearchParamExtractor.IValueExtractor refP01 = () -> List.of(new Reference("Patient/P01"));
			BaseSearchParamExtractor.IValueExtractor refP02 = () -> List.of(new Reference("Patient/P02"));
			BaseSearchParamExtractor.IValueExtractor empty = Collections::emptyList;
			return Stream.of(
				Arguments.of("same patient compartment",      refP01, refP01, true),
				Arguments.of("different patient compartments", refP01, refP02, false),
				Arguments.of("source has no patient ref",     empty,  refP01, true),
				Arguments.of("target has no patient ref",     refP01, empty,  true)
			);
		}

		@ParameterizedTest(name = "{0}")
		@MethodSource("compartmentCases")
		void testValidate_ResourceInPatientCompartment(
				String theCaseName,
				BaseSearchParamExtractor.IValueExtractor theSourceExtractor,
				BaseSearchParamExtractor.IValueExtractor theTargetExtractor,
				boolean theExpectedValid) {
			// Given
			when(myRequestDetailsMock.getResourceName()).thenReturn("Observation");
			when(myDaoRegistryMock.getResourceDao("Observation")).thenReturn(myObservationDaoMock);
			IBaseResource sourceObs = createObservation("Observation/OBS1");
			IBaseResource targetObs = createObservation("Observation/OBS2");
			setupDaoMockForSuccessfulRead(myObservationDaoMock, sourceObs);
			setupDaoMockForSuccessfulRead(myObservationDaoMock, targetObs);
			MergeOperationInputParameters params = new MergeOperationInputParameters();
			params.setResourceLimit(PAGE_SIZE);
			params.setSourceResource(new Reference("Observation/OBS1"));
			params.setTargetResource(new Reference("Observation/OBS2"));
			when(mySearchParamExtractorMock.getPathValueExtractor(eq(sourceObs), anyString()))
				.thenReturn(theSourceExtractor);
			when(mySearchParamExtractorMock.getPathValueExtractor(eq(targetObs), anyString()))
				.thenReturn(theTargetExtractor);

			// When
			MergeOperationOutcome mergeOutcome = createMergeOutcome();
			MergeValidationResult result = myMergeValidationService.validate(params, myRequestDetailsMock, mergeOutcome);

			// Then
			assertThat(result.isValid).isEqualTo(theExpectedValid);
			if (!theExpectedValid) {
				assertThat(result.httpStatusCode).isEqualTo(422);
				OperationOutcome operationOutcome = (OperationOutcome) mergeOutcome.getOperationOutcome();
				OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
				assertThat(issue.getDiagnostics()).contains("Source and target resources belong to different patients");
				assertThat(issue.getCode().toCode()).isEqualTo("invalid");
			}
		}
	}

	private MergeOperationOutcome createMergeOutcome() {
		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		mergeOutcome.setOperationOutcome(OperationOutcomeUtil.newInstance(myFhirContext));
		return mergeOutcome;
	}

	private void setupDaoMockForSuccessfulRead(Patient theResource) {
		setupDaoMockForSuccessfulRead(myPatientDaoMock, theResource);
	}

	private void setupDaoMockForSuccessfulRead(IFhirResourceDao<IBaseResource> theDao, IBaseResource theResource) {
		assertThat(theResource.getIdElement()).isNotNull();
		when(theDao.read(theResource.getIdElement().toVersionless(), myRequestDetailsMock))
			.thenReturn(theResource);
	}

	private void setupDaoMockSearchForIdentifiers(List<List<IBaseResource>> theMatchingResourcesOnInvocations) {
		OngoingStubbing<IBundleProvider> ongoingStubbing = null;
		for (List<IBaseResource> matchingResources : theMatchingResourcesOnInvocations) {
			IBundleProvider bundleProviderMock = mock(IBundleProvider.class);
			when(bundleProviderMock.getAllResources()).thenReturn(matchingResources);
			if (ongoingStubbing == null) {
				ongoingStubbing = when(myPatientDaoMock.search(any(), eq(myRequestDetailsMock)))
					.thenReturn(bundleProviderMock);
			} else {
				ongoingStubbing.thenReturn(bundleProviderMock);
			}
		}
	}

	private void verifyValidationFailure(MergeValidationResult theResult,
										  MergeOperationOutcome theMergeOutcome,
										  int theExpectedStatus,
										  String theExpectedDiagnostics,
										  String theExpectedIssueCode) {
		assertThat(theResult.isValid).isFalse();
		assertThat(theResult.httpStatusCode).isEqualTo(theExpectedStatus);
		OperationOutcome operationOutcome = (OperationOutcome) theMergeOutcome.getOperationOutcome();
		assertThat(operationOutcome.getIssue()).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = operationOutcome.getIssueFirstRep();
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		assertThat(issue.getDiagnostics()).contains(theExpectedDiagnostics);
		assertThat(issue.getCode().toCode()).isEqualTo(theExpectedIssueCode);
	}

	private void verifySearchParametersOnDaoSearchInvocations(List<List<String>> theExpectedIdentifierParams) {
		org.mockito.ArgumentCaptor<SearchParameterMap> captor =
			org.mockito.ArgumentCaptor.forClass(SearchParameterMap.class);
		verify(myPatientDaoMock, times(theExpectedIdentifierParams.size()))
			.search(captor.capture(), eq(myRequestDetailsMock));
		List<SearchParameterMap> maps = captor.getAllValues();
		assertThat(maps).hasSameSizeAs(theExpectedIdentifierParams);
		for (int i = 0; i < maps.size(); i++) {
			verifySearchParameterOnSingleDaoSearchInvocation(maps.get(i), theExpectedIdentifierParams.get(i));
		}
	}

	private void verifySearchParameterOnSingleDaoSearchInvocation(SearchParameterMap theCapturedMap,
																  List<String> theExpectedIdentifierParams) {
		List<List<IQueryParameterType>> actualIdentifierParams = theCapturedMap.get("identifier");
		assertThat(actualIdentifierParams).hasSameSizeAs(theExpectedIdentifierParams);
		for (int i = 0; i < theExpectedIdentifierParams.size(); i++) {
			assertThat(actualIdentifierParams.get(i)).hasSize(1);
			assertThat(actualIdentifierParams.get(i).get(0).getValueAsQueryToken())
				.isEqualTo(theExpectedIdentifierParams.get(i));
		}
	}

	private static Patient createPatient(String theId) {
		Patient patient = new Patient();
		patient.setId(theId);
		return patient;
	}

	private static Observation createObservation(String theId) {
		Observation observation = new Observation();
		observation.setId(theId);
		return observation;
	}

	private static Organization createOrganization(String theId) {
		Organization organization = new Organization();
		organization.setId(theId);
		return organization;
	}

	private static void addReplacesLink(Patient thePatient, String theReplacedResourceId) {
		thePatient.addLink().setType(Patient.LinkType.REPLACES).setOther(new Reference(theReplacedResourceId));
	}

	private static void addReplacedByLink(Patient thePatient, String theReplacingResourceId) {
		thePatient.addLink().setType(Patient.LinkType.REPLACEDBY).setOther(new Reference(theReplacingResourceId));
	}

}
