package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;

public class PatientMergeProvider extends BaseJpaResourceProvider<Patient> {

	private final FhirContext myFhirContext;
	private final ResourceMergeService myResourceMergeService;

	public PatientMergeProvider(FhirContext theFhirContext, ResourceMergeService theResourceMergeService) {
		myFhirContext = theFhirContext;
		assert myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4;
		myResourceMergeService = theResourceMergeService;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	/**
	 * /Patient/$merge
	 */
	@Operation(
			name = ProviderConstants.OPERATION_MERGE,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-merge")
	public IBaseParameters patientMerge(
			HttpServletRequest theServletRequest,
			HttpServletResponse theServletResponse,
			ServletRequestDetails theRequestDetails,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER)
					List<Identifier> theSourcePatientIdentifier,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER)
					List<Identifier> theTargetPatientIdentifier,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT, max = 1)
					IBaseReference theSourcePatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT, max = 1)
					IBaseReference theTargetPatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_PREVIEW, typeName = "boolean", max = 1)
					IPrimitiveType<Boolean> thePreview,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_DELETE_SOURCE, typeName = "boolean", max = 1)
					IPrimitiveType<Boolean> theDeleteSource,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_RESULT_PATIENT, max = 1)
					IBaseResource theResultPatient,
			@OperationParam(name = ProviderConstants.OPERATION_MERGE_PARAM_BATCH_SIZE, typeName = "unsignedInt")
					IPrimitiveType<Integer> theBatchSize) {

		startRequest(theServletRequest);

		try {
			int batchSize = myStorageSettings.getTransactionWriteBatchSizeFromOperationParameter(theBatchSize);

			BaseMergeOperationInputParameters mergeOperationParameters = buildMergeOperationInputParameters(
					theSourcePatientIdentifier,
					theTargetPatientIdentifier,
					theSourcePatient,
					theTargetPatient,
					thePreview,
					theDeleteSource,
					theResultPatient,
					batchSize);

			MergeOperationOutcome mergeOutcome =
					myResourceMergeService.merge(mergeOperationParameters, theRequestDetails);

			theServletResponse.setStatus(mergeOutcome.getHttpStatusCode());
			return buildMergeOperationOutputParameters(myFhirContext, mergeOutcome, theRequestDetails.getResource());
		} finally {
			endRequest(theServletRequest);
		}
	}

	private IBaseParameters buildMergeOperationOutputParameters(
			FhirContext theFhirContext, MergeOperationOutcome theMergeOutcome, IBaseResource theInputParameters) {

		IBaseParameters retVal = ParametersUtil.newInstance(theFhirContext);
		ParametersUtil.addParameterToParameters(
				theFhirContext, retVal, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT, theInputParameters);

		ParametersUtil.addParameterToParameters(
				theFhirContext,
				retVal,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME,
				theMergeOutcome.getOperationOutcome());

		if (theMergeOutcome.getUpdatedTargetResource() != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext,
					retVal,
					OPERATION_MERGE_OUTPUT_PARAM_RESULT,
					theMergeOutcome.getUpdatedTargetResource());
		}

		if (theMergeOutcome.getTask() != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext,
					retVal,
					ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK,
					theMergeOutcome.getTask());
		}
		return retVal;
	}

	private BaseMergeOperationInputParameters buildMergeOperationInputParameters(
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultPatient,
			int theBatchSize) {
		BaseMergeOperationInputParameters mergeOperationParameters =
				new PatientMergeOperationInputParameters(theBatchSize);
		if (theSourcePatientIdentifier != null) {
			List<CanonicalIdentifier> sourceResourceIdentifiers = theSourcePatientIdentifier.stream()
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			mergeOperationParameters.setSourceResourceIdentifiers(sourceResourceIdentifiers);
		}
		if (theTargetPatientIdentifier != null) {
			List<CanonicalIdentifier> targetResourceIdentifiers = theTargetPatientIdentifier.stream()
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			mergeOperationParameters.setTargetResourceIdentifiers(targetResourceIdentifiers);
		}
		mergeOperationParameters.setSourceResource(theSourcePatient);
		mergeOperationParameters.setTargetResource(theTargetPatient);
		mergeOperationParameters.setPreview(thePreview != null && thePreview.getValue());
		mergeOperationParameters.setDeleteSource(theDeleteSource != null && theDeleteSource.getValue());

		if (theResultPatient != null) {
			// pass in a copy of the result patient as we don't want it to be modified. It will be
			// returned back to the client as part of the response.
			mergeOperationParameters.setResultResource(((Patient) theResultPatient).copy());
		}

		return mergeOperationParameters;
	}
}
