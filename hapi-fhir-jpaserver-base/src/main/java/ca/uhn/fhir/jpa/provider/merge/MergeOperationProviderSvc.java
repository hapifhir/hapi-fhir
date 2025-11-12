// Created by claude-sonnet-4-5
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
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IMergeOperationProviderSvc;
import ca.uhn.fhir.jpa.interceptor.ProvenanceAgentsPointcutUtil;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
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
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;

/**
 * Service that handles the common logic for merge operations across different resource types.
 * This service extracts the shared merge operation logic that was previously duplicated
 * in resource-specific providers.
 */
public class MergeOperationProviderSvc implements IMergeOperationProviderSvc {

	private final FhirContext myFhirContext;
	private final ResourceMergeService myResourceMergeService;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final JpaStorageSettings myStorageSettings;

	public MergeOperationProviderSvc(
			FhirContext theFhirContext,
			ResourceMergeService theResourceMergeService,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			JpaStorageSettings theStorageSettings) {
		myFhirContext = theFhirContext;
		myResourceMergeService = theResourceMergeService;
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
	 * @param theResourceLimit     Optional batch size limit
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

			FhirTerser terser = myFhirContext.newTerser();
			IBaseResource resultResourceCopy = theResultResource != null ? terser.clone(theResultResource) : null;
			IBaseResource originalParametersCopy = terser.clone(theRequestDetails.getResource());

			MergeOperationInputParameters mergeOperationParameters = MergeOperationInputParameters.from(
					theSourceIdentifiers,
					theTargetIdentifiers,
					theSourceReference,
					theTargetReference,
					thePreview,
					theDeleteSource,
					resultResourceCopy,
					resourceLimit,
					provenanceAgents,
					originalParametersCopy);

			MergeOperationOutcome mergeOutcome =
					myResourceMergeService.merge(mergeOperationParameters, theRequestDetails);
			servletResponse.setStatus(mergeOutcome.getHttpStatusCode());
			return buildMergeOperationOutputParameters(mergeOutcome, theRequestDetails.getResource());
		} finally {
			endRequest(servletRequest);
		}
	}

	private IBaseParameters buildMergeOperationOutputParameters(
			MergeOperationOutcome theMergeOutcome, IBaseResource theInputParameters) {

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParameters(
				myFhirContext,
				retVal,
				ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT,
				theInputParameters);

		ParametersUtil.addParameterToParameters(
				myFhirContext,
				retVal,
				ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME,
				theMergeOutcome.getOperationOutcome());

		if (theMergeOutcome.getUpdatedTargetResource() != null) {
			ParametersUtil.addParameterToParameters(
					myFhirContext,
					retVal,
					OPERATION_MERGE_OUTPUT_PARAM_RESULT,
					theMergeOutcome.getUpdatedTargetResource());
		}

		if (theMergeOutcome.getTask() != null) {
			ParametersUtil.addParameterToParameters(
					myFhirContext,
					retVal,
					ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK,
					theMergeOutcome.getTask());
		}
		return retVal;
	}
}
