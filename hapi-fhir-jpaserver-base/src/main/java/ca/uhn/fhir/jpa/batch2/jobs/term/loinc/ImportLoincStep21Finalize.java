package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirPatchBuilder;
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

public class ImportLoincStep21Finalize implements IReductionStepWorker<LoincJobImportParameters, ImportLoincFileSetJson, ImportTerminologyResultJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep21Finalize.class);

	private final TerminologyFileSetJson.RecordsAddedCounter myTotalRecordsAddedCounter = new TerminologyFileSetJson.RecordsAddedCounter();
	private final Map<String, TerminologyFileSetJson.RecordsAddedCounter> myStepIdToRecordsAddedCounter = new HashMap<>();
	private final Set<String> myResourcesToActivate = new HashSet<>();
	private final DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public ImportLoincStep21Finalize(@Nonnull DaoRegistry theDaoRegistry) {
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		myDaoRegistry = theDaoRegistry;
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theChunkDetails) {
		ImportLoincFileSetJson data = theChunkDetails.getData();

		for (Map.Entry<String, TerminologyFileSetJson.RecordsAddedCounter> entry : data.getStepIdToRecordsAdded().entrySet()) {
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
	public RunOutcome run(@Nonnull StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<ImportTerminologyResultJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {

		for (String resourceToActivate : myResourcesToActivate) {

			IIdType resourceId = myDaoRegistry.getFhirContext().getVersion().newIdType(resourceToActivate);

			IPrimitiveType<String> statusCode = (IPrimitiveType<String>) myDaoRegistry.getFhirContext().getElementDefinition("code").newInstance();
			statusCode.setValue("active");

			FhirPatchBuilder patchBuilder = new FhirPatchBuilder(myDaoRegistry.getFhirContext());
			patchBuilder
				.replace()
				.path("status")
				.value(statusCode);
			IBaseParameters patchDocument = patchBuilder.build();

			ourLog.info("Setting status to ACTIVE for resource: {}", resourceId);

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());
			String patchBody = myDaoRegistry.getFhirContext().newJsonParser().encodeResourceToString(patchDocument);
			RequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			dao.patch(resourceId, null, PatchTypeEnum.FHIR_PATCH_JSON, patchBody, patchDocument, requestDetails);

		}

		ImportTerminologyResultJson resultJson = new ImportTerminologyResultJson();

		String report = createReport(theStepExecutionDetails);
		resultJson.setReport(report);

		theDataSink.accept(resultJson);

		return RunOutcome.SUCCESS;
	}

	private String createReport(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		JobDefinition<LoincJobImportParameters> jobDefinition = theStepExecutionDetails.getJobDefinition();

		StringBuilder reportBuilder = new StringBuilder();

		reportBuilder.append("Terminology Import Report\n");
		reportBuilder.append("---------------------------------------------------\n");

		appendCounts(myTotalRecordsAddedCounter, reportBuilder, 0);

		for (JobDefinitionStep<LoincJobImportParameters, ?, ?> step : jobDefinition.getSteps()) {
			if (step.getJobStepWorker() instanceof ITerminologyImportFileHandlerStep) {
				TerminologyFileSetJson.RecordsAddedCounter counter = myStepIdToRecordsAddedCounter.computeIfAbsent(step.getStepId(), k -> new TerminologyFileSetJson.RecordsAddedCounter());

				reportBuilder.append("---------------------------------------------------\n");
				reportBuilder.append("Step: ").append(step.getStepId());
				reportBuilder.append(" (").append(step.getStepDescription()).append(")\n");
				appendCounts(counter, reportBuilder, 3);
			}
		}

		return reportBuilder.toString();
	}

	private void appendCounts(TerminologyFileSetJson.RecordsAddedCounter counter, StringBuilder reportBuilder, int indent) {
		boolean hasAny = false;
		if (counter.getConceptsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("Concepts Added             : ").append(counter.getConceptsAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getConceptLinksAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("Concepts Links Added       : ").append(counter.getConceptLinksAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getDesignationsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("Concept Designations Added : ").append(counter.getDesignationsAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getPropertiesAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("Concept Properties Added   : ").append(counter.getPropertiesAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getConceptMapsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("ConceptMaps Added          : ").append(counter.getConceptMapsAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getConceptMapMappingsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("ConceptMap Mappings Added  : ").append(counter.getConceptMapMappingsAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getValueSetsAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("ValueSets Added            : ").append(counter.getValueSetsAdded()).append("\n");
			hasAny = true;
		}
		if (counter.getValueSetCodesAdded() > 0) {
			indent(reportBuilder, indent);
			reportBuilder.append("ValueSets Codes Added      : ").append(counter.getValueSetCodesAdded()).append("\n");
			hasAny = true;
		}
		if (!hasAny) {
			indent(reportBuilder, indent);
			reportBuilder.append("Nothing added\n");
		}
	}

	private void indent(StringBuilder theReportBuilder, int theIndent) {
		for (int i = 0; i < theIndent; i++) {
			theReportBuilder.append(' ');
		}
	}

	@Override
	public ImportLoincStep21Finalize newInstance() {
		return new ImportLoincStep21Finalize(myDaoRegistry);
	}
}
