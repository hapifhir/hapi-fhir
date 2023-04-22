package ca.uhn.fhir.jpa.testutil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BulkExportBatch2TestUtils {


	public static BulkExportParameters getBulkExportParametersFromOptions(FhirContext theCtx,
																			BulkDataExportOptions theOptions) {
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);

		parameters.setSince(theOptions.getSince());
		parameters.setOutputFormat(theOptions.getOutputFormat());
		parameters.setExportStyle(theOptions.getExportStyle());
		if (theOptions.getFilters() != null) {
			parameters.setFilters(new ArrayList<>(theOptions.getFilters()));
		}
		if (theOptions.getGroupId() != null) {
			parameters.setGroupId(theOptions.getGroupId().getValue());
		}
		parameters.setExpandMdm(theOptions.isExpandMdm());

		// resource types are special
		// if none are provided, the job submitter adds them
		// but we cannot manually start the job without correct parameters
		// so we "correct" them here
		if (CollectionUtils.isEmpty(theOptions.getResourceTypes())) {
			addAllResourceTypes(parameters, theCtx);
		}
		else {
			parameters.setResourceTypes(new ArrayList<>(theOptions.getResourceTypes()));
		}

		return parameters;
	}

	private static void addAllResourceTypes(BulkExportParameters theOptions, FhirContext theCtx) {
		Set<String> rts = theCtx.getResourceTypes();
		if (theOptions.getExportStyle() == BulkDataExportOptions.ExportStyle.SYSTEM) {
			// everything
			List<String> resourceTypes = rts.stream()
				.filter(rt -> !rt.equalsIgnoreCase("Binary"))
				.collect(Collectors.toList());
			theOptions.setResourceTypes(resourceTypes);
		}
		else if (theOptions.getExportStyle() != null) {
			// patients
			List<String> patientRts = rts.stream()
				.filter(rt -> SearchParameterUtil.isResourceTypeInPatientCompartment(theCtx, rt))
				.collect(Collectors.toList());
			theOptions.setResourceTypes(patientRts);
		}
	}
}
