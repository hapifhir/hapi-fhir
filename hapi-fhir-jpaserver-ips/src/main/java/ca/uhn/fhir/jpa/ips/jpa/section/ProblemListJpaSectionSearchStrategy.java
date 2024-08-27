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
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Condition;

public class ProblemListJpaSectionSearchStrategy extends JpaSectionSearchStrategy<Condition> {

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(
			@Nonnull IpsSectionContext<Condition> theIpsSectionContext, @Nonnull Condition theCandidate) {
		if (theCandidate
						.getClinicalStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
				|| theCandidate
						.getClinicalStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
				|| theCandidate
						.getVerificationStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
			return false;
		}

		return true;
	}
}
