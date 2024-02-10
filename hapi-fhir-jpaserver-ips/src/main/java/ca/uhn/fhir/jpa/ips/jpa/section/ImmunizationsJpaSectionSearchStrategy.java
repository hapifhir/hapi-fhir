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
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Immunization;

public class ImmunizationsJpaSectionSearchStrategy extends JpaSectionSearchStrategy<Immunization> {

	@Override
	public void massageResourceSearch(
			@Nonnull IpsSectionContext<Immunization> theIpsSectionContext,
			@Nonnull SearchParameterMap theSearchParameterMap) {
		theSearchParameterMap.setSort(new SortSpec(Immunization.SP_DATE).setOrder(SortOrderEnum.DESC));
		theSearchParameterMap.addInclude(Immunization.INCLUDE_MANUFACTURER);
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(
			@Nonnull IpsSectionContext<Immunization> theIpsSectionContext, @Nonnull Immunization theCandidate) {
		if (theCandidate.getStatus() == Immunization.ImmunizationStatus.ENTEREDINERROR) {
			return false;
		}

		return true;
	}
}
