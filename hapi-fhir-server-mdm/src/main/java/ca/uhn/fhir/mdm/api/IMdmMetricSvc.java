package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.params.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.params.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.params.GenerateScoreMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkDataMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;

public interface IMdmMetricSvc {

	/**
	 * Generates the metrics for the provided parameters.
	 *
	 * MdmLinkMetrics are metrics related to the links themselves
	 * (Counts of links by match_type (match, no_match, etc) and link_source (auto, manual))
	 */
	MdmLinkMetrics generateLinkMetrics(GenerateMdmLinkMetricParameters theParameters);

	/**
	 * Generates the metrics for the provided parameters.
	 *
	 * MdmResourceMetrics are metrics related to resource counts
	 * (counts of GoldenResources, SourceResources, BlockedResources)
	 */
	MdmResourceMetrics generateResourceMetrics(GenerateMdmResourceMetricsParameters theParameters);

	/**
	 * Generates the metrics for the provided parameters.
	 *
	 * These metrics are unique value counts of MdmLink score.
	 */
	MdmLinkDataMetrics generateLinkScoreMetrics(GenerateScoreMetricsParameters theParameters);
}
