package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.parameters.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateScoreMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkDataMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;

public interface IMdmMetricSvc {

	/**
	 * Generates the metrics for the provided parameters.
	 * This implementation is persistence dependent.
	 * MdmLinkMetrics are metrics related to the links themselves
	 * (counts of links and the like)
	 */
	MdmLinkMetrics generateLinkMetrics(GenerateMdmLinkMetricParameters theParameters);

	MdmResourceMetrics generateResourceMetrics(GenerateMdmResourceMetricsParameters theParameters);

	MdmLinkDataMetrics generateLinkScoreMetrics(GenerateScoreMetricsParameters theParameters);
}
