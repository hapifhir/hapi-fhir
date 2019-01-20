package ca.uhn.fhir.jpa.interceptor.test;

import ca.uhn.fhir.jpa.model.interceptor.executor.InterceptorRegistry;
import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {InterceptorRegistryTest.InterceptorRegistryTestCtxConfig.class})
public class InterceptorRegistryTest {

	private static boolean ourNext_beforeRestHookDelivery_Return2;
	private static boolean ourNext_beforeRestHookDelivery_Return1;
	private static List<String> ourInvocations = new ArrayList<>();
	private static CanonicalSubscription ourLastCanonicalSubscription;
	private static ResourceDeliveryMessage ourLastResourceDeliveryMessage0;
	private static ResourceDeliveryMessage ourLastResourceDeliveryMessage1;

	@Autowired
	private InterceptorRegistry myInterceptorRegistry;

	@Test
	public void testGlobalInterceptorsAreFound() {
		List<Object> globalInterceptors = myInterceptorRegistry.getGlobalInterceptorsForUnitTest();
		assertEquals(2, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyTestInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyTestInterceptorTwo);
	}

	@Test
	public void testInvokeGlobalInterceptorMethods() {
		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		boolean outcome = myInterceptorRegistry.callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, params);
		assertTrue(outcome);

		assertThat(ourInvocations, contains("MyTestInterceptorOne.beforeRestHookDelivery", "MyTestInterceptorTwo.beforeRestHookDelivery"));
		assertSame(msg, ourLastResourceDeliveryMessage0);
		assertNull(ourLastResourceDeliveryMessage1);
		assertSame(subs, ourLastCanonicalSubscription);
	}

	@Test
	public void testInvokeGlobalInterceptorMethods_MethodAbortsProcessing() {
		ourNext_beforeRestHookDelivery_Return1 = false;

		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		boolean outcome = myInterceptorRegistry.callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, params);
		assertFalse(outcome);

		assertThat(ourInvocations, contains("MyTestInterceptorOne.beforeRestHookDelivery"));
	}

	@Test
	public void testCallHooksInvokedWithWrongParameters() {
		Integer msg = 123;
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		try {
			myInterceptorRegistry.callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, params);
			fail();
		} catch (AssertionError e) {
			assertEquals("Wrong hook parameters, wanted [CanonicalSubscription, ResourceDeliveryMessage] and found [CanonicalSubscription, Integer]", e.getMessage());
		}
	}


	@Before
	public void before() {
		ourNext_beforeRestHookDelivery_Return1 = true;
		ourNext_beforeRestHookDelivery_Return2 = true;
		ourLastCanonicalSubscription = null;
		ourLastResourceDeliveryMessage0 = null;
		ourLastResourceDeliveryMessage1 = null;
		ourInvocations.clear();
	}

	@Configuration
	@ComponentScan(basePackages = "ca.uhn.fhir.jpa.model")
	static class InterceptorRegistryTestCtxConfig {

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorTwo interceptor1() {
			return new MyTestInterceptorTwo();
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorOne interceptor2() {
			return new MyTestInterceptorOne();
		}

	}

	@Interceptor
	@Order(100)
	public static class MyTestInterceptorOne {

		public MyTestInterceptorOne() {
			super();
		}

		@Hook(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY)
		public boolean beforeRestHookDelivery(CanonicalSubscription theCanonicalSubscription) {
			ourLastCanonicalSubscription = theCanonicalSubscription;
			ourInvocations.add("MyTestInterceptorOne.beforeRestHookDelivery");
			return ourNext_beforeRestHookDelivery_Return1;
		}

	}

	@Interceptor
	@Order(200)
	public static class MyTestInterceptorTwo {
		@Hook(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY)
		public boolean beforeRestHookDelivery(ResourceDeliveryMessage theResourceDeliveryMessage0, ResourceDeliveryMessage theResourceDeliveryMessage1) {
			ourLastResourceDeliveryMessage0 = theResourceDeliveryMessage0;
			ourLastResourceDeliveryMessage1 = theResourceDeliveryMessage1;
			ourInvocations.add("MyTestInterceptorTwo.beforeRestHookDelivery");
			return ourNext_beforeRestHookDelivery_Return2;
		}
	}

	/**
	 * Just a make-believe version of this class for the unit test
	 */
	private static class CanonicalSubscription {
	}

	/**
	 * Just a make-believe version of this class for the unit test
	 */
	private static class ResourceDeliveryMessage {
	}
}
