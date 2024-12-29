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

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrUtils;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.TriggerDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;

import java.util.stream.Collectors;

public class CrDiscoveryServiceDstu3 implements ICrDiscoveryService {

	protected final Repository myRepository;
	protected final IIdType myPlanDefinitionId;
	protected final PrefetchTemplateBuilderDstu3 myPrefetchTemplateBuilder;

	public CrDiscoveryServiceDstu3(IIdType thePlanDefinitionId, Repository theRepository) {
		myPlanDefinitionId = thePlanDefinitionId;
		myRepository = theRepository;
		myPrefetchTemplateBuilder = new PrefetchTemplateBuilderDstu3(myRepository);
	}

	public CdsServiceJson resolveService() {
		return resolveService(
				CdsCrUtils.readPlanDefinitionFromRepository(FhirVersionEnum.DSTU3, myRepository, myPlanDefinitionId));
	}

	protected CdsServiceJson resolveService(IBaseResource thePlanDefinition) {
		if (thePlanDefinition instanceof PlanDefinition) {
			PlanDefinition planDef = (PlanDefinition) thePlanDefinition;
			String triggerEvent = getTriggerEvent(planDef);
			if (triggerEvent != null) {
				PrefetchUrlList prefetchUrlList =
						isEca(planDef) ? myPrefetchTemplateBuilder.getPrefetchUrlList(planDef) : new PrefetchUrlList();
				return new CrDiscoveryElementDstu3(planDef, prefetchUrlList).getCdsServiceJson();
			}
		}
		return null;
	}

	protected String getTriggerEvent(PlanDefinition thePlanDefinition) {
		if (thePlanDefinition == null
				|| !thePlanDefinition.hasAction()
				|| thePlanDefinition.getAction().stream().noneMatch(a -> a.hasTriggerDefinition())) {
			return null;
		}

		var triggerDefs = thePlanDefinition.getAction().stream()
				.filter(a -> a.hasTriggerDefinition())
				.flatMap(a -> a.getTriggerDefinition().stream())
				.filter(t -> t.getType().equals(TriggerDefinition.TriggerType.NAMEDEVENT))
				.collect(Collectors.toList());
		if (triggerDefs == null || triggerDefs.isEmpty()) {
			return null;
		}

		return triggerDefs.get(0).getEventName();
	}

	protected boolean isEca(PlanDefinition thePlanDefinition) {
		if (thePlanDefinition.hasType() && thePlanDefinition.getType().hasCoding()) {
			for (Coding coding : thePlanDefinition.getType().getCoding()) {
				if (coding.getCode().equals("eca-rule")) {
					return true;
				}
			}
		}
		return false;
	}
}
