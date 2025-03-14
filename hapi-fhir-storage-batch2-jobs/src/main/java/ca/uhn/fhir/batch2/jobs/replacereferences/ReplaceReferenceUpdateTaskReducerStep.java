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
package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ReplaceReferenceUpdateTaskReducerStep<PT extends ReplaceReferencesJobParameters>
		implements IReductionStepWorker<PT, ReplaceReferencePatchOutcomeJson, ReplaceReferenceResultsJson> {
	public static final String RESOURCE_TYPES_SYSTEM = "http://hl7.org/fhir/ValueSet/resource-types";

	protected final FhirContext myFhirContext;
	protected final DaoRegistry myDaoRegistry;
	private final IFhirResourceDao<Task> myTaskDao;
	private final ReplaceReferencesProvenanceSvc myProvenanceSvc;

	private List<Bundle> myPatchOutputBundles = new ArrayList<>();

	public ReplaceReferenceUpdateTaskReducerStep(
			DaoRegistry theDaoRegistry, ReplaceReferencesProvenanceSvc theProvenanceSvc) {
		myDaoRegistry = theDaoRegistry;
		myTaskDao = myDaoRegistry.getResourceDao(Task.class);
		myFhirContext = theDaoRegistry.getFhirContext();
		myProvenanceSvc = theProvenanceSvc;
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<PT, ReplaceReferencePatchOutcomeJson> theChunkDetails) {
		ReplaceReferencePatchOutcomeJson result = theChunkDetails.getData();
		Bundle patchOutputBundle =
				myFhirContext.newJsonParser().parseResource(Bundle.class, result.getPatchResponseBundle());
		myPatchOutputBundles.add(patchOutputBundle);
		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ReplaceReferencePatchOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferenceResultsJson> theDataSink)
			throws JobExecutionFailedException {
		return run(theStepExecutionDetails, theDataSink, true);
	}

	protected RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ReplaceReferencePatchOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferenceResultsJson> theDataSink,
			boolean theCreateProvenance)
			throws JobExecutionFailedException {

		try {
			ReplaceReferencesJobParameters params = theStepExecutionDetails.getParameters();
			SystemRequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(params.getPartitionId());

			updateTask(params.getTaskId(), requestDetails);

			if (theCreateProvenance) {
				myProvenanceSvc.createProvenance(
						params.getTargetId().asIdDt(),
						params.getSourceId().asIdDt(),
						myPatchOutputBundles,
						theStepExecutionDetails.getInstance().getStartTime(),
						requestDetails);
			}

			ReplaceReferenceResultsJson result = new ReplaceReferenceResultsJson();
			result.setTaskId(params.getTaskId());
			theDataSink.accept(result);

			return new RunOutcome(myPatchOutputBundles.size());
		} finally {
			// Reusing the same reducer for all jobs feels confusing and dangerous to me. We need to fix this.
			// See https://github.com/hapifhir/hapi-fhir/pull/6551
			// TODO KHS add new methods to the api called init() and cleanup() that are called by the api so we can move
			// this finally block out
			myPatchOutputBundles.clear();
		}
	}

	protected void updateTask(FhirIdJson theTaskId, RequestDetails theRequestDetails) {
		Task task = myTaskDao.read(theTaskId.asIdDt(), theRequestDetails);
		task.setStatus(Task.TaskStatus.COMPLETED);

		// TODO KHS this Task will probably be too large for large jobs. Revisit this model once we support
		// Provenance resources.
		myPatchOutputBundles.forEach(outputBundle -> {
			Task.TaskOutputComponent output = task.addOutput();
			Coding coding = output.getType().getCodingFirstRep();
			coding.setSystem(RESOURCE_TYPES_SYSTEM);
			coding.setCode("Bundle");
			Reference outputBundleReference =
					new Reference("#" + outputBundle.getIdElement().getIdPart());
			output.setValue(outputBundleReference);
			task.addContained(outputBundle);
		});

		myTaskDao.update(task, theRequestDetails);
	}

	protected List<Bundle> getPatchOutputBundles() {
		return myPatchOutputBundles;
	}
}
