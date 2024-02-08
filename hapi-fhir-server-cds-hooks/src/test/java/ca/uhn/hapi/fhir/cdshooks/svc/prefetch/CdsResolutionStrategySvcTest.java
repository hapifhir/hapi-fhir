package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceMethod;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class CdsResolutionStrategySvcTest {
	private static final String TEST_FHIR_SERVER = "http://example.com/fhir";
	private final DaoRegistry myDaoRegistry = new DaoRegistry();

	@Test
	public void testScenarios() {
		Set<CdsResolutionStrategyEnum> set = new HashSet<>();
		set.add(CdsResolutionStrategyEnum.NONE);
		assertThat(strategyWith(null, false, false)).isEqualTo(set);
		assertThat(strategyWith(null, true, false)).isEqualTo(set);
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO));
		assertThat(strategyWith(myDaoRegistry, false, false)).isEqualTo(set);
		assertThat(strategyWith(myDaoRegistry, true, false)).isEqualTo(set);
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.SERVICE));
		assertThat(strategyWith(null, false, true)).isEqualTo(set);
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO, CdsResolutionStrategyEnum.SERVICE));
		assertThat(strategyWith(myDaoRegistry, false, true)).isEqualTo(set);
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.SERVICE, CdsResolutionStrategyEnum.FHIR_CLIENT));
		assertThat(strategyWith(null, true, true)).isEqualTo(set);
		set.clear();
		set.addAll(Arrays.asList(CdsResolutionStrategyEnum.NONE, CdsResolutionStrategyEnum.DAO, CdsResolutionStrategyEnum.SERVICE, CdsResolutionStrategyEnum.FHIR_CLIENT));
		assertThat(strategyWith(myDaoRegistry, true, true)).isEqualTo(set);
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
