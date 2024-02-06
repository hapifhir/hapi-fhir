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
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ResourceType;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;

public class PregnancyJpaSectionSearchStrategy extends JpaSectionSearchStrategy {

	@Override
	public void massageResourceSearch(IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
			theSearchParameterMap.add(
				Observation.SP_CODE,
				new TokenOrListParam()
					.addOr(new TokenParam(LOINC_URI, "82810-3"))
					.addOr(new TokenParam(LOINC_URI, "11636-8"))
					.addOr(new TokenParam(LOINC_URI, "11637-6"))
					.addOr(new TokenParam(LOINC_URI, "11638-4"))
					.addOr(new TokenParam(LOINC_URI, "11639-2"))
					.addOr(new TokenParam(LOINC_URI, "11640-0"))
					.addOr(new TokenParam(LOINC_URI, "11612-9"))
					.addOr(new TokenParam(LOINC_URI, "11613-7"))
					.addOr(new TokenParam(LOINC_URI, "11614-5"))
					.addOr(new TokenParam(LOINC_URI, "33065-4")));
		}
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		if (theCandidate instanceof Observation) {
			// code filtering not yet applied
			Observation observation = (Observation) theCandidate;
			if (observation.getStatus() == Observation.ObservationStatus.PRELIMINARY) {
				return false;
			}
		}

		return true;
	}
}
