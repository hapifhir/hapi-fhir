package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;

import java.util.ArrayList;

public class BulkExportUtils {
	private BulkExportUtils() {}

	/**
	 * Converts the BulkDataExportOptions -> BulkExportParameters
	 */
	public static BulkExportParameters createBulkExportJobParametersFromExportOptions(BulkDataExportOptions theOptions) {
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);

		parameters.setStartDate(theOptions.getSince());
		parameters.setOutputFormat(theOptions.getOutputFormat());
		parameters.setExportStyle(theOptions.getExportStyle());
		if (theOptions.getFilters() != null) {
			parameters.setFilters(new ArrayList<>(theOptions.getFilters()));
		}
		if (theOptions.getGroupId() != null) {
			parameters.setGroupId(theOptions.getGroupId().getValue());
		}
		if (theOptions.getResourceTypes() != null) {
			parameters.setResourceTypes(new ArrayList<>(theOptions.getResourceTypes()));
		}
		parameters.setExpandMdm(theOptions.isExpandMdm());

		return parameters;
	}
}
