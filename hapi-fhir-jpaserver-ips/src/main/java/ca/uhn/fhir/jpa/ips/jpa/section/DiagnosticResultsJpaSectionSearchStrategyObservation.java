/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.jpa.section;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Observation;

public class DiagnosticResultsJpaSectionSearchStrategyObservation extends JpaSectionSearchStrategy<Observation> {

	@Override
	public void massageResourceSearch(
			@Nonnull IpsSectionContext<Observation> theIpsSectionContext,
			@Nonnull SearchParameterMap theSearchParameterMap) {
		theSearchParameterMap.add(
				Observation.SP_CATEGORY,
				new TokenOrListParam()
						.addOr(new TokenParam(
								"http://terminology.hl7.org/CodeSystem/observation-category", "laboratory")));
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(
			@Nonnull IpsSectionContext<Observation> theIpsSectionContext, @Nonnull Observation theCandidate) {
		// code filtering not yet applied
		if (theCandidate.getStatus() == Observation.ObservationStatus.CANCELLED
				|| theCandidate.getStatus() == Observation.ObservationStatus.ENTEREDINERROR
				|| theCandidate.getStatus() == Observation.ObservationStatus.PRELIMINARY) {
			return false;
		}

		return true;
	}
}
