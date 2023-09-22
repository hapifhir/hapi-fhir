package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;

public interface IMdmMetricSvc {

	/**
	 * Generates metrics on MDM Links.
	 * Metrics include:
	 * * breakdowns of counts of MATCH_RESULT types by LINK_SOURCE types.
	 * * counts of resources of each type
	 * * a histogram of score 'buckets' with the appropriate counts.
	 * @param theParameters - Parameters defining resource type of interest,
	 *                      as well as MatchResult and LinkSource filters.
	 * @return The metrics in a JSON format.
	 */
	MdmMetrics generateMdmMetrics(GenerateMdmMetricsParameters theParameters);
}
