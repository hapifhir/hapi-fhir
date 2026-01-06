package ca.uhn.fhir.batch2.jobs.export.v2;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.v3.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;

public class ExpandResourceAndWriteBinaryV2Step extends ExpandResourceAndWriteBinaryStep {

	@Override
	protected RequestDetails newRequestDetails(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			BulkExportJobParameters jobParameters) {
		RequestPartitionId partitionId = jobParameters.getPartitionIdForSecurity();
		return new SystemRequestDetails().setRequestPartitionId(partitionId);
	}
}
