package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceMethod;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CdsResolutionStrategySvcTest {
	private static final String TEST_FHIR_SERVER = "http://example.com/fhir";
	private final DaoRegistry myDaoRegistry = new DaoRegistry();

	@Test
	public void testScenarios() {
		Set<CdsResolutionStrategyEnum> set = new HashSet<>();
		set.add(CdsResolutionStrategyEnum.NONE);
		assertEquals(set, strategyWith(null, false, false));
		assertEquals(set, strategyWith(null, true, false));
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO));
		assertEquals(set, strategyWith(myDaoRegistry, false, false));
		assertEquals(set, strategyWith(myDaoRegistry, true, false));
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.SERVICE));
		assertEquals(set, strategyWith(null, false, true));
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO, CdsResolutionStrategyEnum.SERVICE));
		assertEquals(set, strategyWith(myDaoRegistry, false, true));
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.SERVICE, CdsResolutionStrategyEnum.FHIR_CLIENT));
		assertEquals(set, strategyWith(null, true, true));
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO, CdsResolutionStrategyEnum.SERVICE, CdsResolutionStrategyEnum.FHIR_CLIENT));
		assertEquals(set, strategyWith(myDaoRegistry, true, true));
		set.clear();
	}

	private Set<CdsResolutionStrategyEnum> strategyWith(DaoRegistry theDaoRegistry, boolean theMethodAllowsAutoFhirClientPrefetch, boolean theRequestHasFhirServer) {
		CdsResolutionStrategySvc svc = new CdsResolutionStrategySvc(theDaoRegistry);
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		if (theRequestHasFhirServer) {
			request.setFhirServer(TEST_FHIR_SERVER);
		}
		CdsServiceMethod method = new CdsServiceMethod(null, null, null, theMethodAllowsAutoFhirClientPrefetch);
		return svc.determineResolutionStrategy(method, request);
	}
}
