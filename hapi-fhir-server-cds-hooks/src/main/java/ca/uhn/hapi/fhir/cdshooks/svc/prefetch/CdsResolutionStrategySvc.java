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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;

import java.util.HashSet;
import java.util.Set;

public class CdsResolutionStrategySvc {

	private final DaoRegistry myDaoRegistry;

	public CdsResolutionStrategySvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	public Set<CdsResolutionStrategyEnum> determineResolutionStrategy(
			ICdsServiceMethod theMethod, CdsServiceRequestJson theRequest) {
		Set<CdsResolutionStrategyEnum> strategies = new HashSet<>();
		strategies.add(CdsResolutionStrategyEnum.NONE);
		if (theRequest.getFhirServer() != null) {
			strategies.add(CdsResolutionStrategyEnum.SERVICE);
			if (theMethod.isAllowAutoFhirClientPrefetch()) {
				strategies.add(CdsResolutionStrategyEnum.FHIR_CLIENT);
			}
		}
		if (myDaoRegistry != null) {
			strategies.add(CdsResolutionStrategyEnum.DAO);
		}
		return strategies;
	}
}
