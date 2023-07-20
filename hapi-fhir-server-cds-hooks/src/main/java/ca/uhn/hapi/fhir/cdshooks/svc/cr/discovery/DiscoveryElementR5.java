/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import org.hl7.fhir.r5.model.PlanDefinition;

public class DiscoveryElementR5 implements IDiscoveryElement {
	private PlanDefinition planDefinition;
	private PrefetchUrlList prefetchUrlList;

	public DiscoveryElementR5(PlanDefinition planDefinition, PrefetchUrlList prefetchUrlList) {
		this.planDefinition = planDefinition;
		this.prefetchUrlList = prefetchUrlList;
	}

	public CdsServiceJson getCdsServiceJson() {
		if (planDefinition != null) {
			var service = new CdsServiceJson()
					.setId(planDefinition.getIdElement().getIdPart())
					.setTitle(planDefinition.getTitle())
					.setDescription(planDefinition.getDescription());

			if (planDefinition.hasAction()) {
				// TODO - this needs some work - too naive
				if (planDefinition.getActionFirstRep().hasTrigger()) {
					if (planDefinition.getActionFirstRep().getTriggerFirstRep().hasName()) {
						service.setHook(planDefinition
								.getActionFirstRep()
								.getTriggerFirstRep()
								.getName());
					}
				}
			}

			if (prefetchUrlList == null) {
				prefetchUrlList = new PrefetchUrlList();
			}

			int itemNo = 0;
			if (!prefetchUrlList.stream()
					.anyMatch(p -> p.equals("Patient/{{context.patientId}}")
							|| p.equals("Patient?_id={{context.patientId}}")
							|| p.equals("Patient?_id=Patient/{{context.patientId}}"))) {
				service.addPrefetch("item1", "Patient?_id={{context.patientId}}");
				++itemNo;
			}

			for (String item : prefetchUrlList) {
				service.addPrefetch("item" + Integer.toString(++itemNo), item);
			}

			return service;
		}

		return null;
	}
}
