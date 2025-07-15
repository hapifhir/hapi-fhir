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

import ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.interceptor.ProvenanceAgentsPointcutUtil;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.model.api.IProvenanceAgent;
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
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;

public class PatientMergeProvider extends BaseJpaResourceProvider<Patient> {

	private final FhirContext myFhirContext;
	private final ResourceMergeService myResourceMergeService;
	private final ResourceUndoMergeService myResourceUndoMergeService;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public PatientMergeProvider(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			ResourceMergeService theResourceMergeService,
			ResourceUndoMergeService theResourceUndoMergeService,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		super(theDaoRegistry.getResourceDao("Patient"));
		myFhirContext = theFhirContext;
		assert myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4;
		myResourceMergeService = theResourceMergeService;
		myResourceUndoMergeService = theResourceUndoMergeService;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
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
					IPrimitiveType<Integer> theResourceLimit) {

		startRequest(theServletRequest);

		try {
			int resourceLimit = MergeResourceHelper.setResourceLimitFromParameter(myStorageSettings, theResourceLimit);

			List<IProvenanceAgent> provenanceAgents =
					ProvenanceAgentsPointcutUtil.ifHasCallHooks(theRequestDetails, myInterceptorBroadcaster);

			MergeOperationInputParameters mergeOperationParameters = buildMergeOperationInputParameters(
					theSourcePatientIdentifier,
					theTargetPatientIdentifier,
					theSourcePatient,
					theTargetPatient,
					thePreview,
					theDeleteSource,
					theResultPatient,
					resourceLimit,
					provenanceAgents,
					(Parameters) theRequestDetails.getResource());

			MergeOperationOutcome mergeOutcome =
					myResourceMergeService.merge(mergeOperationParameters, theRequestDetails);

			theServletResponse.setStatus(mergeOutcome.getHttpStatusCode());
			return buildMergeOperationOutputParameters(myFhirContext, mergeOutcome, theRequestDetails.getResource());
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$merge
	 */
	@Operation(
			name = ProviderConstants.OPERATION_UNDO_MERGE,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-merge")
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

	private UndoMergeOperationInputParameters buildUndoMergeOperationInputParameters(
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient) {

		UndoMergeOperationInputParameters undoMergeOperationParameters =
				new UndoMergeOperationInputParameters();

		setCommonMergeOperationInputParameters(
				undoMergeOperationParameters,
				theSourcePatientIdentifier,
				theTargetPatientIdentifier,
				theSourcePatient,
				theTargetPatient);

		return undoMergeOperationParameters;
	}

	private void setCommonMergeOperationInputParameters(
			MergeOperationsCommonInputParameters theMergeOperationParameters,
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient) {
		if (theSourcePatientIdentifier != null) {
			List<CanonicalIdentifier> sourceResourceIdentifiers = theSourcePatientIdentifier.stream()
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			theMergeOperationParameters.setSourceResourceIdentifiers(sourceResourceIdentifiers);
		}
		if (theTargetPatientIdentifier != null) {
			List<CanonicalIdentifier> targetResourceIdentifiers = theTargetPatientIdentifier.stream()
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			theMergeOperationParameters.setTargetResourceIdentifiers(targetResourceIdentifiers);
		}
		theMergeOperationParameters.setSourceResource(theSourcePatient);
		theMergeOperationParameters.setTargetResource(theTargetPatient);
		theMergeOperationParameters.setSourceResource(theSourcePatient);
		theMergeOperationParameters.setTargetResource(theTargetPatient);
	}

	private MergeOperationInputParameters buildMergeOperationInputParameters(
			List<Identifier> theSourcePatientIdentifier,
			List<Identifier> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultPatient,
			int theResourceLimit,
			List<IProvenanceAgent> theProvenanceAgents,
			Parameters theOriginalInputParameters) {

		MergeOperationInputParameters mergeOperationParameters =
				new MergeOperationInputParameters(theResourceLimit);

		setCommonMergeOperationInputParameters(
				mergeOperationParameters,
				theSourcePatientIdentifier,
				theTargetPatientIdentifier,
				theSourcePatient,
				theTargetPatient);

		mergeOperationParameters.setPreview(thePreview != null && thePreview.getValue());
		mergeOperationParameters.setDeleteSource(theDeleteSource != null && theDeleteSource.getValue());

		if (theResultPatient != null) {
			// pass in a copy of the result patient as we don't want it to be modified. It will be
			// returned back to the client as part of the response.
			mergeOperationParameters.setResultResource(((Patient) theResultPatient).copy());
		}

		mergeOperationParameters.setProvenanceAgents(theProvenanceAgents);
		mergeOperationParameters.setOriginalInputParameters(theOriginalInputParameters.copy());
		return mergeOperationParameters;
	}
}
