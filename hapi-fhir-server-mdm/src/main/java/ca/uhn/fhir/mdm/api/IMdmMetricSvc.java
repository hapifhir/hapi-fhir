package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.parameters.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;

public interface IMdmMetricSvc {

	/**
	 * Generates the metrics for the provided parameters.
	 * This implementation is persistence dependent.
	 */
	MdmLinkMetrics generateLinkMetrics(GenerateMdmLinkMetricParameters theParameters);

	MdmResourceMetrics generateResourceMetrics(GenerateMdmResourceMetricsParameters theParameters);
}
