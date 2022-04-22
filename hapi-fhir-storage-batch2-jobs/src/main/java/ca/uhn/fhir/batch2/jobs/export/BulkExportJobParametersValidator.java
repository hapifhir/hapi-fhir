package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.model.BulkExportJobInfo;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BulkExportJobParametersValidator implements IJobParametersValidator<BulkExportJobParameters> {

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Nullable
	@Override
	public List<String> validate(@Nonnull BulkExportJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();

		// initial validation
		String jobUUID = theParameters.getJobId();
		if (jobUUID == null || jobUUID.isBlank()) {
			errorMsgs.add("JobId is required to start an export job.");
		}
		if (theParameters.getResourceTypes() == null || theParameters.getResourceTypes().isEmpty()) {
			errorMsgs.add("Resource Types are required for an export job.");
		}

		if (!errorMsgs.isEmpty()) {
			// we can't do the rest of the validation, so return now
			return errorMsgs;
		}

		// fetch info
		BulkExportJobInfo info = myBulkExportProcessor.getJobInfo(jobUUID);
		if (info == null || info.getJobId() == null || info.getResourceTypes() == null) {
			errorMsgs.add("Invalid jobId " + jobUUID);
			return errorMsgs;
		}

		// validate the resource types
		if (theParameters.getResourceTypes().size() != info.getResourceTypes().size()) {
			errorMsgs.add("Resource types for job " + jobUUID + " do not match input parameters.");
		}
		for (String resourceType : theParameters.getResourceTypes()) {
			if (resourceType.equalsIgnoreCase("Binary")) {
				errorMsgs.add("Bulk export of Binary resources is verboten");
			}
			if (!info.getResourceTypes().contains(resourceType)) {
				errorMsgs.add("Job must include resource type " + resourceType);
			}
		}

		// validate the output format
		if (!Constants.CT_FHIR_NDJSON.equalsIgnoreCase(theParameters.getOutputFormat())) {
			errorMsgs.add("The only allowed format for Bulk Export is currently " + Constants.CT_FHIR_NDJSON);
		}

		// validate for group
		BulkDataExportOptions.ExportStyle style = theParameters.getExportStyle();
		if (style == null) {
			errorMsgs.add("Export style is required");
		}
		else {
			switch (style) {
				case GROUP:
					if (theParameters.getGroupId() == null || theParameters.getGroupId().isEmpty()) {
						errorMsgs.add("Group export requires a group id, but none provided for job " + jobUUID);
					}
					break;
				case SYSTEM:
				case PATIENT:
				default:
					break;
			}
		}

		return errorMsgs;
	}
}
