/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencePatchOutcomeJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateTaskReducerStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Parameters;

import java.util.ArrayList;
import java.util.List;

public class MergeUpdateTaskReducerStep extends ReplaceReferenceUpdateTaskReducerStep<MergeJobParameters> {
	private final IHapiTransactionService myHapiTransactionService;
	private final MergeResourceHelper myMergeResourceHelper;
	private final MergeProvenanceSvc myMergeProvenanceSvc;

	public MergeUpdateTaskReducerStep(
			DaoRegistry theDaoRegistry,
			IHapiTransactionService theHapiTransactionService,
			MergeResourceHelper theMergeResourceHelper,
			MergeProvenanceSvc theMergeProvenanceSvc) {
		super(theDaoRegistry, theMergeProvenanceSvc);
		this.myHapiTransactionService = theHapiTransactionService;
		myMergeResourceHelper = theMergeResourceHelper;
		myMergeProvenanceSvc = theMergeProvenanceSvc;
	}

	@Override
	public IReductionStepWorker<MergeJobParameters, ReplaceReferencePatchOutcomeJson, ReplaceReferenceResultsJson>
			newInstance() {
		return new MergeUpdateTaskReducerStep(
				myDaoRegistry, myHapiTransactionService, myMergeResourceHelper, myMergeProvenanceSvc);
	}

	@Override
	protected void performOperationSpecificActions(
			StepExecutionDetails<MergeJobParameters, ReplaceReferencePatchOutcomeJson> theStepExecutionDetails,
			RequestDetails theRequestDetails) {
		MergeJobParameters mergeJobParameters = theStepExecutionDetails.getParameters();

		// Get resource type from source ID (needed for dynamic DAO lookup and parsing)
		String resourceType = mergeJobParameters.getSourceId().getResourceType();

		// Initialize parameter names based on operation type
		String operationName = mergeJobParameters.getOperationName();
		if (operationName == null) {
			// Backward compatibility: old jobs (pre-8.8) don't have operationName field
			// Default to Patient merge parameter names
			operationName = ProviderConstants.OPERATION_MERGE;
		}
		AbstractMergeOperationInputParameterNames parameterNames =
				AbstractMergeOperationInputParameterNames.forOperation(operationName);

		IBaseResource resultResource;
		boolean deleteSource;

		// we store the original input parameters and the operation outcome of updating target as
		// contained resources in the provenance. undo-merge service uses these to contained resources.
		List<IBaseResource> containedResourcesForProvenance = new ArrayList<>();

		Parameters originalInputParameters = null;
		if (mergeJobParameters.getOriginalInputParameters() != null) {
			originalInputParameters = myFhirContext
					.newJsonParser()
					.parseResource(Parameters.class, mergeJobParameters.getOriginalInputParameters());
			resultResource = getResultResource(originalInputParameters, parameterNames);
			deleteSource = isDeleteSource(originalInputParameters, parameterNames);
			containedResourcesForProvenance.add(originalInputParameters);
		} else {
			// This else part is just for backward compatibility in case there were jobs in-flight during upgrade
			// this could be removed in the future
			if (mergeJobParameters.getResultResource() != null) {
				Class<? extends IBaseResource> resourceClass =
						myFhirContext.getResourceDefinition(resourceType).getImplementingClass();
				resultResource = myFhirContext
						.newJsonParser()
						.parseResource(resourceClass, mergeJobParameters.getResultResource());
			} else {
				resultResource = null;
			}
			deleteSource = mergeJobParameters.getDeleteSource();
		}

		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			// Get DAO dynamically based on resource type
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);

			IBaseResource sourceResource =
					dao.read(mergeJobParameters.getSourceId().asIdDt(), theRequestDetails);
			IBaseResource targetResource =
					dao.read(mergeJobParameters.getTargetId().asIdDt(), theRequestDetails);

			DaoMethodOutcome outcome = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
					sourceResource, targetResource, resultResource, deleteSource, theRequestDetails);
			IBaseResource updatedTargetResource = outcome.getResource();

			String sourceVersionForProvenance = sourceResource.getIdElement().getVersionIdPart();
			if (deleteSource) {
				sourceVersionForProvenance =
						Long.toString(sourceResource.getIdElement().getVersionIdPartAsLong() + 1);
			}

			mergeJobParameters.setSourceVersionForProvenance(sourceVersionForProvenance);
			mergeJobParameters.setTargetVersionForProvenance(
					updatedTargetResource.getIdElement().getVersionIdPart());
			containedResourcesForProvenance.add(outcome.getOperationOutcome());
			createProvenance(theStepExecutionDetails, theRequestDetails, containedResourcesForProvenance);

			if (deleteSource) {
				dao.delete(sourceResource.getIdElement(), theRequestDetails);
			}
		});
	}

	private boolean isDeleteSource(
			Parameters originalInputParameters, AbstractMergeOperationInputParameterNames theParameterNames) {
		boolean deleteSource = false;
		String deleteSourceParamName = theParameterNames.getDeleteSourceParameterName();
		if (originalInputParameters.hasParameter(deleteSourceParamName)) {
			deleteSource = originalInputParameters.getParameterBool(deleteSourceParamName);
		}
		return deleteSource;
	}

	private IBaseResource getResultResource(
			Parameters theOriginalInputParameters, AbstractMergeOperationInputParameterNames theParameterNames) {
		IBaseResource resultResource = null;
		String resultResourceParamName = theParameterNames.getResultResourceParameterName();
		if (theOriginalInputParameters.hasParameter(resultResourceParamName)) {
			resultResource = theOriginalInputParameters
					.getParameter(resultResourceParamName)
					.getResource();
		}
		return resultResource;
	}
}
