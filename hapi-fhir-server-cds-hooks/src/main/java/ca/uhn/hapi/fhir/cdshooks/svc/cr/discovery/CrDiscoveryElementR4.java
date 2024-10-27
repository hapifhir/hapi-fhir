/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.hl7.fhir.r4.model.TriggerDefinition;

import java.util.stream.Collectors;

public class CrDiscoveryElementR4 implements ICrDiscoveryElement {
	protected PlanDefinition myPlanDefinition;
	protected PrefetchUrlList myPrefetchUrlList;

	public CrDiscoveryElementR4(PlanDefinition thePlanDefinition, PrefetchUrlList thePrefetchUrlList) {
		myPlanDefinition = thePlanDefinition;
		myPrefetchUrlList = thePrefetchUrlList;
	}

	public CdsServiceJson getCdsServiceJson() {
		if (myPlanDefinition == null
				|| !myPlanDefinition.hasAction()
				|| myPlanDefinition.getAction().stream().noneMatch(a -> a.hasTrigger())) {
			return null;
		}

		var triggerDefs = myPlanDefinition.getAction().stream()
				.filter(a -> a.hasTrigger())
				.flatMap(a -> a.getTrigger().stream())
				.filter(t -> t.getType().equals(TriggerDefinition.TriggerType.NAMEDEVENT))
				.collect(Collectors.toList());
		if (triggerDefs == null || triggerDefs.isEmpty()) {
			return null;
		}

		var service = new CdsServiceJson()
				.setId(myPlanDefinition.getIdElement().getIdPart())
				.setTitle(myPlanDefinition.getTitle())
				.setDescription(myPlanDefinition.getDescription())
				.setHook(triggerDefs.get(0).getName());

		if (myPrefetchUrlList == null) {
			myPrefetchUrlList = new PrefetchUrlList();
		}

		int itemNo = 0;
		if (!myPrefetchUrlList.stream()
				.anyMatch(p -> p.equals("Patient/{{context.patientId}}")
						|| p.equals("Patient?_id={{context.patientId}}")
						|| p.equals("Patient?_id=Patient/{{context.patientId}}"))) {
			String key = getKey(++itemNo);
			service.addPrefetch(key, "Patient?_id={{context.patientId}}");
			service.addSource(key, CdsResolutionStrategyEnum.FHIR_CLIENT);
		}

		for (String item : myPrefetchUrlList) {
			String key = getKey(++itemNo);
			service.addPrefetch(key, item);
			service.addSource(key, CdsResolutionStrategyEnum.FHIR_CLIENT);
		}

		return service;
	}
}
