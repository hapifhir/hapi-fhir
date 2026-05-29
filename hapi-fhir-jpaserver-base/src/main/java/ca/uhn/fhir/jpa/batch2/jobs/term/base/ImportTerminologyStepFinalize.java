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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.BatchInstanceStepStatisticsDTO;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirPatchBuilder;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ImportTerminologyStepFinalize<PT extends BaseTerminologyImportParameters> extends BaseImportTerminologyStep
		implements IReductionStepWorker<PT, TerminologyFileSetJson, ImportTerminologyResultJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportTerminologyStepFinalize.class);

	private final TerminologyFileSetJson.RecordsAddedCounter myTotalRecordsAddedCounter =
			new TerminologyFileSetJson.RecordsAddedCounter();
	private final Map<String, TerminologyFileSetJson.RecordsAddedCounter> myStepIdToRecordsAddedCounter =
			new HashMap<>();
	private final Set<String> myResourcesToActivate = new HashSet<>();
	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IHapiTransactionService myTxService;

	/**
	 * Constructor
	 */
	public ImportTerminologyStepFinalize(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc,
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull IHapiTransactionService theTxService) {
		super(theJobPersistence);

		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		Validate.notNull(theTermCodeSystemStorageSvc, "theTermCodeSystemStorageSvc must not be null");
		Validate.notNull(theTxService, "theTxService must not be null");
		myDaoRegistry = theDaoRegistry;
		myTermCodeSystemStorageSvc = theTermCodeSystemStorageSvc;
		myTxService = theTxService;
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PT, TerminologyFileSetJson> theChunkDetails) {
		TerminologyFileSetJson data = theChunkDetails.getData();

		for (Map.Entry<String, TerminologyFileSetJson.RecordsAddedCounter> entry :
				data.getStepIdToRecordsAdded().entrySet()) {
			TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter = entry.getValue();
			myTotalRecordsAddedCounter.copyFrom(recordsAddedCounter);

			myStepIdToRecordsAddedCounter
					.computeIfAbsent(entry.getKey(), k -> new TerminologyFileSetJson.RecordsAddedCounter())
					.copyFrom(recordsAddedCounter);
		}

		myResourcesToActivate.addAll(data.getResourcesToActivate());

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ImportTerminologyResultJson> theDataSink)
			throws JobExecutionFailedException, ReductionStepFailureException {

		ImportTerminologyMetadataAttachmentJson jobMetadata =
				getJobMetadata(theStepExecutionDetails.getInstance().getInstanceId());
		String codeSystemUrl = jobMetadata.getCodeSystem().getUrl();
		String stagingVersionId = jobMetadata.getCodeSystemStagingVersionId();

		for (String resourceToActivate : myResourcesToActivate) {
			updateResourceStatusToActive(theStepExecutionDetails, resourceToActivate);
		}

		boolean makeCurrent =
				!Boolean.TRUE.equals(theStepExecutionDetails.getParameters().getDontMakeCurrent());

		myTermCodeSystemStorageSvc.activateStagingCodeSystemVersion(codeSystemUrl, stagingVersionId, makeCurrent);

		ImportTerminologyResultJson resultJson = new ImportTerminologyResultJson();

		String report = createReport(theStepExecutionDetails);
		resultJson.setReport(report);

		theDataSink.accept(resultJson);

		return RunOutcome.SUCCESS;
	}

	private void updateResourceStatusToActive(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			String resourceToActivate) {
		IIdType resourceId = myDaoRegistry.getFhirContext().getVersion().newIdType(resourceToActivate);

		@SuppressWarnings("unchecked")
		IPrimitiveType<String> statusCode = (IPrimitiveType<String>)
				requireNonNull(myDaoRegistry.getFhirContext().getElementDefinition("code"))
						.newInstance();
		statusCode.setValue("active");

		FhirPatchBuilder patchBuilder = new FhirPatchBuilder(myDaoRegistry.getFhirContext());
		patchBuilder.replace().path("status").value(statusCode);
		IBaseParameters patchDocument = patchBuilder.build();

		ourLog.info("Setting status to ACTIVE for resource: {}", resourceId);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());
		String patchBody = myDaoRegistry.getFhirContext().newJsonParser().encodeResourceToString(patchDocument);
		RequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
		dao.patch(resourceId, null, PatchTypeEnum.FHIR_PATCH_JSON, patchBody, patchDocument, requestDetails);
	}

	private String createReport(
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {
		JobDefinition<PT> jobDefinition = theStepExecutionDetails.getJobDefinition();

		BatchInstanceStepStatisticsDTO allStepStatistics = calculateStepStatistics(theStepExecutionDetails);

		StringBuilder reportBuilder = new StringBuilder();

		reportBuilder.append("Terminology Import Report\n");
		reportBuilder.append("---------------------------------------------------\n");

		appendCounts(myTotalRecordsAddedCounter, reportBuilder, 0);

		for (JobDefinitionStep<PT, ?, ?> step : jobDefinition.getSteps()) {
			if (step.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep) {
				TerminologyFileSetJson.RecordsAddedCounter counter = myStepIdToRecordsAddedCounter.computeIfAbsent(
						step.getStepId(), k -> new TerminologyFileSetJson.RecordsAddedCounter());

				reportBuilder.append("---------------------------------------------------\n");
				reportBuilder.append("Step: ").append(step.getStepId());
				reportBuilder.append(" (").append(step.getStepDescription()).append(")\n");

				BatchInstanceStepStatisticsDTO.StepStatistics stepStatistics = allStepStatistics.get(step.getStepId());
				if (stepStatistics != null) {
					indent(reportBuilder, 3);
					reportBuilder
							.append("Total Work Chunks          : ")
							.append(stepStatistics.chunkCount())
							.append("\n");
					indent(reportBuilder, 3);
					reportBuilder
							.append("Total Processing Time      : ")
							.append(StopWatch.formatMillis(stepStatistics.millisElapsed()))
							.append("\n");
				}

				appendCounts(counter, reportBuilder, 3);
			}
		}

		return reportBuilder.toString();
	}

	private BatchInstanceStepStatisticsDTO calculateStepStatistics(
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {
		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			String stepId = theStepExecutionDetails.getInstance().getInstanceId();
			return myJobPersistence.calculateStepStatistics(stepId);
		});
	}

	private void appendCounts(
			TerminologyFileSetJson.RecordsAddedCounter counter, StringBuilder reportBuilder, int indent) {
		boolean hasAny = false;
		if (counter.getConceptsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("Concepts Added             : ")
					.append(counter.getConceptsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getConceptLinksAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("Concepts Links Added       : ")
					.append(counter.getConceptLinksAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getDesignationsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("Concept Designations Added : ")
					.append(counter.getDesignationsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getPropertiesAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("Concept Properties Added   : ")
					.append(counter.getPropertiesAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getConceptMapsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("ConceptMaps Added          : ")
					.append(counter.getConceptMapsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getConceptMapMappingsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("ConceptMap Mappings Added  : ")
					.append(counter.getConceptMapMappingsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getValueSetsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("ValueSets Added            : ")
					.append(counter.getValueSetsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getValueSetInclusionsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("ValueSets Inclusions Added : ")
					.append(counter.getValueSetInclusionsAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getValueSetCodesAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("ValueSets Codes Added      : ")
					.append(counter.getValueSetCodesAdded())
					.append("\n");
			hasAny = true;
		}
		if (counter.getOtherChanges() > 0) {
			indent(reportBuilder, indent);
			reportBuilder
					.append("Other Changes Count        : ")
					.append(counter.getOtherChanges())
					.append("\n");
			hasAny = true;
		}
		if (!hasAny) {
			indent(reportBuilder, indent);
			reportBuilder.append("Nothing changed\n");
		}
	}

	private void indent(StringBuilder theReportBuilder, int theIndent) {
		theReportBuilder.append(" ".repeat(Math.max(0, theIndent)));
	}

	@Override
	public ImportTerminologyStepFinalize newInstance() {
		return new ImportTerminologyStepFinalize(myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}
}
