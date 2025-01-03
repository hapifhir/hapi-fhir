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

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencePatchOutcomeJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateStep;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesErrorHandler;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesQueryIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import org.hl7.fhir.r4.model.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MergeAppCtx {
	public static final String JOB_MERGE = "MERGE";

	@Bean
	public JobDefinition<MergeJobParameters> merge(
			ReplaceReferencesQueryIdsStep<MergeJobParameters> theMergeQueryIds,
			ReplaceReferenceUpdateStep<MergeJobParameters> theMergeUpdateStep,
			MergeUpdateTaskReducerStep theMergeUpdateTaskReducerStep,
			ReplaceReferencesErrorHandler<MergeJobParameters> theMergeErrorHandler) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_MERGE)
				.setJobDescription("Merge Resources")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(MergeJobParameters.class)
				.addFirstStep(
						"query-ids",
						"Query IDs of resources that link to the source resource",
						FhirIdListWorkChunkJson.class,
						theMergeQueryIds)
				.addIntermediateStep(
						"replace-references",
						"Update all references from pointing to source to pointing to target",
						ReplaceReferencePatchOutcomeJson.class,
						theMergeUpdateStep)
				.addFinalReducerStep(
						"update-task",
						"Waits for replace reference work to complete and updates Task.",
						ReplaceReferenceResultsJson.class,
						theMergeUpdateTaskReducerStep)
				.errorHandler(theMergeErrorHandler)
				.build();
	}

	@Bean
	public ReplaceReferencesQueryIdsStep<MergeJobParameters> mergeQueryIdsStep(
			HapiTransactionService theHapiTransactionService, IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReplaceReferencesQueryIdsStep<>(theHapiTransactionService, theBatch2DaoSvc);
	}

	@Bean
	public ReplaceReferenceUpdateStep<MergeJobParameters> mergeUpdateStep(
			FhirContext theFhirContext, ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc) {
		return new ReplaceReferenceUpdateStep<>(theFhirContext, theReplaceReferencesPatchBundleSvc);
	}

	@Bean
	public MergeProvenanceSvc mergeProvenanceSvc(DaoRegistry theDaoRegistry) {
		return new MergeProvenanceSvc(theDaoRegistry);
	}

	@Bean
	public MergeResourceHelper mergeResourceHelper(
			DaoRegistry theDaoRegistry, MergeProvenanceSvc theMergeProvenanceSvc) {

		return new MergeResourceHelper(theDaoRegistry, theMergeProvenanceSvc);
	}

	@Bean
	public MergeUpdateTaskReducerStep mergeUpdateTaskStep(
			DaoRegistry theDaoRegistry,
			IHapiTransactionService theHapiTransactionService,
			MergeResourceHelper theMergeResourceHelper,
			MergeProvenanceSvc theMergeProvenanceSvc) {
		return new MergeUpdateTaskReducerStep(
				theDaoRegistry, theHapiTransactionService, theMergeResourceHelper, theMergeProvenanceSvc);
	}

	@Bean
	public ReplaceReferencesErrorHandler<MergeJobParameters> mergeErrorHandler(
			DaoRegistry theDaoRegistry, Batch2TaskHelper theBatch2TaskHelper) {
		IFhirResourceDao<Task> taskDao = theDaoRegistry.getResourceDao(Task.class);
		return new ReplaceReferencesErrorHandler<>(theBatch2TaskHelper, taskDao);
	}
}
