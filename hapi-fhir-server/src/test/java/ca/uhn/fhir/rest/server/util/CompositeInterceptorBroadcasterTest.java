package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeInterceptorBroadcasterTest {

	private List<Integer> myOrders = new ArrayList<>();

	@ParameterizedTest
	@CsvSource({
		"0, 1, 2",
		"1, 0, 2",
		"2, 0, 1"
	})
	public void testCompositeBroadcasterBroadcastsInOrder(int theIndex0, int theIndex1, int theIndex2) {
		// Setup
		InterceptorService svc0 = new InterceptorService();
		svc0.registerInterceptor(new Interceptor0());
		InterceptorService svc1 = new InterceptorService();
		svc1.registerInterceptor(new Interceptor1());
		InterceptorService svc2 = new InterceptorService();
		svc2.registerInterceptor(new Interceptor2());
		InterceptorService[] services = new InterceptorService[] {svc0, svc1, svc2};

		List<IInterceptorBroadcaster> serviceList = List.of(
			services[theIndex0], services[theIndex1], services[theIndex2]
		);
		IInterceptorBroadcaster compositeBroadcaster = CompositeInterceptorBroadcaster.newCompositeBroadcaster(serviceList);

		assertTrue(compositeBroadcaster.hasHooks(Pointcut.TEST_RO));
		assertFalse(compositeBroadcaster.hasHooks(Pointcut.TEST_RB));

		// Test
		HookParams hookParams = new HookParams()
			.add(String.class, "PARAM_A")
			.add(String.class, "PARAM_B");
		Object outcome = compositeBroadcaster.callHooksAndReturnObject(Pointcut.TEST_RO, hookParams);

		// Verify
		assertNull(outcome);
		assertThat(myOrders).asList().containsExactly(
			-2, -1, 0, 1, 2, 3
		);
	}


	@Interceptor
	private class Interceptor0 {

		@Hook(value=Pointcut.TEST_RO, order = 0)
		public BaseServerResponseException hook0() {
			myOrders.add(0);
			return null;
		}

		@Hook(value=Pointcut.TEST_RO, order = 2)
		public BaseServerResponseException hook2() {
			myOrders.add(2);
			return null;
		}

	}

	@Interceptor
	private class Interceptor1 {

		@Hook(value=Pointcut.TEST_RO, order = 1)
		public BaseServerResponseException hook1() {
			myOrders.add(1);
			return null;
		}

		@Hook(value=Pointcut.TEST_RO, order = 3)
		public BaseServerResponseException hook3() {
			myOrders.add(3);
			return null;
		}

	}

	@Interceptor
	private class Interceptor2 {

		@Hook(value=Pointcut.TEST_RO, order = -1)
		public BaseServerResponseException hookMinus1() {
			myOrders.add(-1);
			return null;
		}

		@Hook(value=Pointcut.TEST_RO, order = -2)
		public BaseServerResponseException hookMinus2() {
			myOrders.add(-2);
			return null;
		}

	}

}
