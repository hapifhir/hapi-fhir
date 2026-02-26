// Created by claude-sonnet-4-5
/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IMergeOperationProviderSvc;
import ca.uhn.fhir.jpa.interceptor.ProvenanceAgentsPointcutUtil;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.GenericMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.MergeResourceHelper;
import ca.uhn.fhir.merge.PatientMergeOperationInputParameterNames;
import ca.uhn.fhir.model.api.IProvenanceAgent;
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

import java.util.List;

import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.endRequest;
import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.startRequest;

/**
 * Service that consolidates the provider logic for merge operations across different endpoints.
 * This service implements the shared merge logic used by:
 * <ul>
 *   <li>The FHIR standard Patient/$merge operation</li>
 *   <li>The HAPI FHIR generic [ResourceType]/$hapi.fhir.merge operation for any resource type</li>
 * </ul>
 * By centralizing this logic, the service eliminates duplication and provides consistent
 * merge implementation across all resource types and operation endpoints.
 */
public class MergeOperationProviderSvc implements IMergeOperationProviderSvc {

	private final FhirContext myFhirContext;
	private final ResourceMergeService myResourceMergeService;
	private final ResourceUndoMergeService myResourceUndoMergeService;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final JpaStorageSettings myStorageSettings;

	public MergeOperationProviderSvc(
			FhirContext theFhirContext,
			ResourceMergeService theResourceMergeService,
			ResourceUndoMergeService theResourceUndoMergeService,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			JpaStorageSettings theStorageSettings) {
		myFhirContext = theFhirContext;
		myResourceMergeService = theResourceMergeService;
		myResourceUndoMergeService = theResourceUndoMergeService;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Executes a merge operation for any resource type.
	 *
	 * @param theSourceIdentifiers Source resource identifiers
	 * @param theTargetIdentifiers Target resource identifiers
	 * @param theSourceReference   Source resource reference
	 * @param theTargetReference   Target resource reference
	 * @param thePreview           Preview mode flag
	 * @param theDeleteSource      Delete source flag
	 * @param theResultResource    Optional result resource provided by client
	 * @param theResourceLimit     Optional resource limit
	 * @param theRequestDetails    Servlet request details containing HTTP request/response and context
	 * @return Parameters resource containing merge operation results
	 */
	@Override
	public IBaseParameters merge(
			List<IBase> theSourceIdentifiers,
			List<IBase> theTargetIdentifiers,
			IBaseReference theSourceReference,
			IBaseReference theTargetReference,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultResource,
			IPrimitiveType<Integer> theResourceLimit,
			ServletRequestDetails theRequestDetails) {

		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();

		startRequest(servletRequest);
		try {
			int resourceLimit = MergeResourceHelper.setResourceLimitFromParameter(myStorageSettings, theResourceLimit);

			List<IProvenanceAgent> provenanceAgents =
					ProvenanceAgentsPointcutUtil.ifHasCallHooks(theRequestDetails, myInterceptorBroadcaster);

			MergeOperationInputParameters mergeOperationParameters =
					MergeOperationParametersUtil.inputParamsFromOperationParams(
							myFhirContext,
							theSourceIdentifiers,
							theTargetIdentifiers,
							theSourceReference,
							theTargetReference,
							thePreview,
							theDeleteSource,
							theResultResource,
							provenanceAgents,
							theRequestDetails.getResource(),
							resourceLimit);

			MergeOperationOutcome mergeOutcome =
					myResourceMergeService.merge(mergeOperationParameters, theRequestDetails);

			servletResponse.setStatus(mergeOutcome.getHttpStatusCode());
			return MergeOperationParametersUtil.buildMergeOperationOutputParameters(
					myFhirContext, mergeOutcome, theRequestDetails.getResource());
		} finally {
			endRequest(servletRequest);
		}
	}

	/**
	 * Executes an undo-merge operation for any resource type.
	 *
	 * @param theSourceIdentifiers Source resource identifiers
	 * @param theTargetIdentifiers Target resource identifiers
	 * @param theSourceReference   Source resource reference
	 * @param theTargetReference   Target resource reference
	 * @param theRequestDetails    Servlet request details containing HTTP request/response and context
	 * @return Parameters resource containing undo-merge operation results
	 */
	@Override
	public IBaseParameters undoMerge(
			List<IBase> theSourceIdentifiers,
			List<IBase> theTargetIdentifiers,
			IBaseReference theSourceReference,
			IBaseReference theTargetReference,
			ServletRequestDetails theRequestDetails) {

		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();

		startRequest(servletRequest);
		try {
			int resourceLimit = myStorageSettings.getInternalSynchronousSearchSize();

			UndoMergeOperationInputParameters undoMergeOperationParameters;
			AbstractMergeOperationInputParameterNames parameterNames;

			// Check if all parameters are null
			boolean allParamsNull = theSourceIdentifiers == null
					&& theTargetIdentifiers == null
					&& theSourceReference == null
					&& theTargetReference == null;

			if (allParamsNull
					&& "Patient".equals(theRequestDetails.getResourceName())
					&& theRequestDetails.getResource() instanceof IBaseParameters requestBody) {
				// Backward compatibility: Patient resources could call this with patient specific parameter names
				// so check for them if all generic parameters are null.
				parameterNames = new PatientMergeOperationInputParameterNames();
				undoMergeOperationParameters = MergeOperationParametersUtil.undoInputParametersFromParameters(
						myFhirContext, requestBody, parameterNames, resourceLimit);

			} else {
				// Standard case: use generic parameter names for all resource types
				// if all parameters are null the service validator will reject the request, so no need to reject it
				// here.
				parameterNames = new GenericMergeOperationInputParameterNames();
				undoMergeOperationParameters = MergeOperationParametersUtil.undoInputParametersFromOperationParameters(
						theSourceIdentifiers,
						theTargetIdentifiers,
						theSourceReference,
						theTargetReference,
						resourceLimit);
			}

			OperationOutcomeWithStatusCode undomergeOutcome = myResourceUndoMergeService.undoMerge(
					undoMergeOperationParameters, theRequestDetails, parameterNames);

			servletResponse.setStatus(undomergeOutcome.getHttpStatusCode());
			IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);

			ParametersUtil.addParameterToParameters(
					myFhirContext,
					retVal,
					ProviderConstants.OPERATION_UNDO_MERGE_OUTCOME,
					undomergeOutcome.getOperationOutcome());
			return retVal;
		} finally {
			endRequest(servletRequest);
		}
	}
}
