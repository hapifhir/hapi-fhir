/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
