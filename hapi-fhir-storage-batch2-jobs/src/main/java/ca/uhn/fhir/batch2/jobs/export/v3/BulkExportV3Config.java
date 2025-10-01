package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.BulkExportCreateReportStep;
import ca.uhn.fhir.batch2.jobs.export.BulkExportJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourcesStep;
import ca.uhn.fhir.batch2.jobs.export.WriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.MdmExpandedPatientIds;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.batch2.jobs.export.BulkExportAppCtx.WRITE_TO_BINARIES;

@Configuration
public class BulkExportV3Config {

	@Bean
	public JobDefinition bulkExportJobV3Definition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(3);

		JobDefinition def = builder.setParametersType(BulkExportJobParameters.class)
			// validator
			.setParametersValidator(bulkExportJobParametersValidator())
			.gatedExecution()
			.addFirstStep(
				"mdm-expand-if-necessary",
				"Expand out patient ids if necessary",
				MdmExpandedPatientIds.class,
				mdmExpansionStep()
			)
			// load in (all) ids and create id chunks of 1000 each
			.addIntermediateStep(
				"fetch-resources",
				"Fetches resource PIDs for exporting",
				ResourceIdList.class,
				fetchResourceIdsV3Step())
			// expand out - fetch resources
			// and write binaries and save to db
			.addIntermediateStep(
				WRITE_TO_BINARIES,
				"Writes the expanded resources to the binaries and saves",
				BulkExportBinaryFileId.class,
				expandResourceAndWriteBinaryStep())
			// finalize the job (set to complete)
			.addFinalReducerStep(
				"create-report-step",
				"Creates the output report from a bulk export job",
				BulkExportJobResults.class,
				createReportStep())
			.build();

		return def;
	}

	@Bean
	public BulkExportJobParametersValidator bulkExportJobParametersValidator() {
		return new BulkExportJobParametersValidator();
	}

	@Bean
	public FetchIdsV3Step fetchResourceIdsV3Step() {
		return new FetchIdsV3Step();
	}

	/**
	 * pre-expands patients lists (if necessary)
	 */
	@Bean
	public MdmExpansionStep mdmExpansionStep() {
		return new MdmExpansionStep();
	}

	/**
	 * Note, this bean is only used for version 1 of the bulk export job definition
	 */
	@Bean
	public ExpandResourcesStep expandResourcesStep() {
		return new ExpandResourcesStep();
	}

	/**
	 * Note, this bean is only used for version 1 of the bulk export job definition
	 */
	@Bean
	public WriteBinaryStep writeBinaryStep() {
		return new WriteBinaryStep();
	}

	/**
	 * Note, this bean is only used for version 2 of the bulk export job definition
	 */
	@Bean
	public ExpandResourceAndWriteBinaryStep expandResourceAndWriteBinaryStep() {
		return new ExpandResourceAndWriteBinaryStep();
	}

	@Bean
	public BulkExportCreateReportStep createReportStep() {
		return new BulkExportCreateReportStep();
	}
}
