/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

public class PatientMergeProvider extends BaseJpaResourceProvider<Patient> {

	private final FhirContext myFhirContext;
	private final MergeOperationProviderSvc myMergeOperationProviderSvc;
	private final ResourceUndoMergeService myResourceUndoMergeService;

	public PatientMergeProvider(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			MergeOperationProviderSvc theMergeOperationProviderSvc,
			ResourceUndoMergeService theResourceUndoMergeService) {
		super(theDaoRegistry.getResourceDao("Patient"));
		myFhirContext = theFhirContext;
		assert myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4;
		myMergeOperationProviderSvc = theMergeOperationProviderSvc;
		myResourceUndoMergeService = theResourceUndoMergeService;
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
			@OperationParam(
							name = ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER,
							typeName = "Identifier")
					List<IBase> theSourcePatientIdentifier,
			@OperationParam(
							name = ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER,
							typeName = "Identifier")
					List<IBase> theTargetPatientIdentifier,
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
					IPrimitiveType<Integer> theResourceLimit) {

		return myMergeOperationProviderSvc.merge(
				theSourcePatientIdentifier,
				theTargetPatientIdentifier,
				theSourcePatient,
				theTargetPatient,
				thePreview,
				theDeleteSource,
				theResultPatient,
				theResourceLimit,
				theRequestDetails,
				theServletRequest,
				theServletResponse);
	}

	/**
	 * /Patient/$hapi.fhir.undo-merge
	 */
	@Operation(name = ProviderConstants.OPERATION_UNDO_MERGE)
	public IBaseParameters patientUndoMerge(
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
					IBaseReference theTargetPatient) {

		startRequest(theServletRequest);

		try {
			// create input parameters
			UndoMergeOperationInputParameters inputParameters = buildUndoMergeOperationInputParameters(
					theSourcePatientIdentifier, theTargetPatientIdentifier, theSourcePatient, theTargetPatient);

			// now call the undo service with parameters
			OperationOutcomeWithStatusCode undomergeOutcome =
					myResourceUndoMergeService.undoMerge(inputParameters, theRequestDetails);
			theServletResponse.setStatus(undomergeOutcome.getHttpStatusCode());
			IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);

			ParametersUtil.addParameterToParameters(
					myFhirContext,
					retVal,
					ProviderConstants.OPERATION_UNDO_MERGE_OUTCOME,
					undomergeOutcome.getOperationOutcome());
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}

	private UndoMergeOperationInputParameters buildUndoMergeOperationInputParameters(
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient) {

		int resourceLimit = myStorageSettings.getInternalSynchronousSearchSize();

		UndoMergeOperationInputParameters undoMergeOperationParameters =
				new UndoMergeOperationInputParameters(resourceLimit);

		MergeOperationsCommonInputParameters.setParameters(
				undoMergeOperationParameters,
				(List) theSourcePatientIdentifier,
				(List) theTargetPatientIdentifier,
				theSourcePatient,
				theTargetPatient);

		return undoMergeOperationParameters;
	}
}
