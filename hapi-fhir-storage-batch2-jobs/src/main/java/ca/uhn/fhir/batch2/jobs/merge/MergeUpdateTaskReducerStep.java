/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencePatchOutcomeJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateTaskReducerStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.Nullable;

public class MergeUpdateTaskReducerStep extends ReplaceReferenceUpdateTaskReducerStep<MergeJobParameters> {
	private final IHapiTransactionService myHapiTransactionService;
	private final MergeResourceHelper myMergeResourceHelper;
	private final MergeProvenanceSvc myMergeProvenanceSvc;
	private final IFhirResourceDao<Patient> myPatientDao;
	private final MergeOperationInputParameterNames myMergeOperationInputParameterNames;

	public MergeUpdateTaskReducerStep(
			DaoRegistry theDaoRegistry,
			IHapiTransactionService theHapiTransactionService,
			MergeResourceHelper theMergeResourceHelper,
			MergeProvenanceSvc theMergeProvenanceSvc) {
		super(theDaoRegistry, theMergeProvenanceSvc);
		this.myHapiTransactionService = theHapiTransactionService;
		myMergeResourceHelper = theMergeResourceHelper;
		myMergeProvenanceSvc = theMergeProvenanceSvc;
		myPatientDao = theDaoRegistry.getResourceDao(Patient.class);
		myMergeOperationInputParameterNames = new MergeOperationInputParameterNames();
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

		Patient resultResource;
		boolean deleteSource;

		Parameters originalInputParameters = null;
		if (mergeJobParameters.getOriginalInputParameters() != null) {
			originalInputParameters = myFhirContext.newJsonParser().parseResource(Parameters.class, mergeJobParameters.getOriginalInputParameters());
			resultResource = getResultResource(originalInputParameters);
			deleteSource = isDeleteSource(originalInputParameters);
		}
		else {
			// This else part is just for backward compatibility could be removed in the future
			if (mergeJobParameters.getResultResource() != null) {
				resultResource = myFhirContext.newJsonParser().parseResource(Patient.class, mergeJobParameters.getResultResource());
			} else {
				resultResource = null;
			}
			deleteSource = mergeJobParameters.getDeleteSource();
		}

		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			Patient sourceResource =
					myPatientDao.read(mergeJobParameters.getSourceId().asIdDt(), theRequestDetails);
			Patient targetResource =
					myPatientDao.read(mergeJobParameters.getTargetId().asIdDt(), theRequestDetails);

			Patient updatedTarget = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
					sourceResource,
					targetResource,
					resultResource,
					mergeJobParameters.getDeleteSource(),
					theRequestDetails);

			String sourceVersionForProvenance = sourceResource.getIdElement().getVersionIdPart();
			if (mergeJobParameters.getDeleteSource()) {
				sourceVersionForProvenance =
						Long.toString(sourceResource.getIdElement().getVersionIdPartAsLong() + 1);
			}

			mergeJobParameters.setSourceVersionForProvenance(sourceVersionForProvenance);
			mergeJobParameters.setTargetVersionForProvenance(
					updatedTarget.getIdElement().getVersionIdPart());

			createProvenance(theStepExecutionDetails, theRequestDetails);

			if (deleteSource) {
				myPatientDao.delete(sourceResource.getIdElement(), theRequestDetails);
			}
		});
	}

	private boolean isDeleteSource(Parameters originalInputParameters) {
		boolean deleteSource = false;
		String deleteSourceParamName = myMergeOperationInputParameterNames.getDeleteSourceParameterName();
		if (originalInputParameters.hasParameter(deleteSourceParamName)) {
			deleteSource = originalInputParameters.getParameterBool(deleteSourceParamName);
		}
		return deleteSource;
	}

	private @Nullable Patient getResultResource(Parameters originalInputParameters) {
		Patient resultResource = null;
		String resultResourceParamName = myMergeOperationInputParameterNames.getResultResourceParameterName();
		if(originalInputParameters.hasParameter(resultResourceParamName)) {
			resultResource = (Patient) originalInputParameters.getParameter(resultResourceParamName).getResource();
		}
		return resultResource;
	}
}
