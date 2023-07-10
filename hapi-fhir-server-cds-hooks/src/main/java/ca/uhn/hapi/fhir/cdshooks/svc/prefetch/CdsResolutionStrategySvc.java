/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
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

	public Set<CdsResolutionStrategyEnum> determineResolutionStrategy(ICdsServiceMethod theMethod, CdsServiceRequestJson theRequest) {
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
