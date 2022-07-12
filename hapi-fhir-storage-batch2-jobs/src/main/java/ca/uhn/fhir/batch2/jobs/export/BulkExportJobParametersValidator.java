package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
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
	private DaoRegistry myDaoRegistry;

	@Nullable
	@Override
	public List<String> validate(@Nonnull BulkExportJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();

		// initial validation

		if (theParameters.getResourceTypes() == null || theParameters.getResourceTypes().isEmpty()) {
			errorMsgs.add("Resource Types are required for an export job.");
		}
		else {
			for (String resourceType : theParameters.getResourceTypes()) {
				if (resourceType.equalsIgnoreCase("Binary")) {
					errorMsgs.add("Bulk export of Binary resources is forbidden");
				} else if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
					errorMsgs.add("Resource type " + resourceType + " is not a supported resource type!");
				}
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
						errorMsgs.add("Group export requires a group id, but none provided.");
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
