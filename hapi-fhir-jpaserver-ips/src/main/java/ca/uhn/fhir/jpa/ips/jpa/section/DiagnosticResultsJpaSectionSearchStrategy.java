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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ResourceType;

public class DiagnosticResultsJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.DiagnosticReport.name())) {
			// nothing currently
		} else if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
			theSearchParameterMap.add(
				Observation.SP_CATEGORY,
				new TokenOrListParam()
					.addOr(new TokenParam(
						"http://terminology.hl7.org/CodeSystem/observation-category",
						"laboratory")));
		}
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theCandidate instanceof DiagnosticReport) {
			DiagnosticReport diagnosticReport = (DiagnosticReport) theCandidate;
			if (diagnosticReport.getStatus() == DiagnosticReport.DiagnosticReportStatus.CANCELLED ||
				diagnosticReport.getStatus() == DiagnosticReport.DiagnosticReportStatus.ENTEREDINERROR ||
				diagnosticReport.getStatus() == DiagnosticReport.DiagnosticReportStatus.PRELIMINARY) {
				return false;
			}
		} else if (theCandidate instanceof Observation) {
			// code filtering not yet applied
			Observation observation = (Observation) theCandidate;
			if (observation.getStatus() == Observation.ObservationStatus.CANCELLED ||
				observation.getStatus() == Observation.ObservationStatus.ENTEREDINERROR ||
				observation.getStatus() == Observation.ObservationStatus.PRELIMINARY) {
				return false;
			}
		}

		return true;
	}
}
