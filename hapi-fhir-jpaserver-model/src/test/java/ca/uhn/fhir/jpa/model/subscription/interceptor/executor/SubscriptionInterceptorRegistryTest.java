package ca.uhn.fhir.jpa.model.subscription.interceptor.executor;

import ca.uhn.fhir.jpa.model.subscription.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.subscription.interceptor.api.SubscriptionHook;
import ca.uhn.fhir.jpa.model.subscription.interceptor.api.SubscriptionInterceptor;
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
@ContextConfiguration(classes = {SubscriptionInterceptorRegistryTest.MyCtxConfig.class})
public class SubscriptionInterceptorRegistryTest {

	private static boolean ourNext_beforeRestHookDelivery_Return2;
	private static boolean ourNext_beforeRestHookDelivery_Return1;
	private static List<String> ourInvocations = new ArrayList<>();
	private static CanonicalSubscription ourLastCanonicalSubscription;
	private static ResourceDeliveryMessage ourLastResourceDeliveryMessage;

	@Autowired
	private SubscriptionInterceptorRegistry mySubscriptionInterceptorRegistry;

	@Test
	public void testGlobalInterceptorsAreFound() {
		List<Object> globalInterceptors = mySubscriptionInterceptorRegistry.getGlobalInterceptors();
		assertEquals(2, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyInterceptorTwo);
	}

	@Test
	public void testInvokeGlobalInterceptorMethods() {
		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		boolean outcome = mySubscriptionInterceptorRegistry.callHooks(Pointcut.BEFORE_REST_HOOK_DELIVERY, params);
		assertTrue(outcome);

		assertThat(ourInvocations, contains("MyInterceptorOne.beforeRestHookDelivery", "MyInterceptorTwo.beforeRestHookDelivery"));
		assertSame(msg, ourLastResourceDeliveryMessage);
		assertSame(subs, ourLastCanonicalSubscription);
	}

	@Test
	public void testInvokeGlobalInterceptorMethods_MethodAbortsProcessing() {
		ourNext_beforeRestHookDelivery_Return1 = false;

		ResourceDeliveryMessage msg = new ResourceDeliveryMessage();
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		boolean outcome = mySubscriptionInterceptorRegistry.callHooks(Pointcut.BEFORE_REST_HOOK_DELIVERY, params);
		assertFalse(outcome);

		assertThat(ourInvocations, contains("MyInterceptorOne.beforeRestHookDelivery"));
	}

	@Test
	public void testCallHooksInvokedWithWrongParameters() {
		Integer msg = 123;
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		try {
			mySubscriptionInterceptorRegistry.callHooks(Pointcut.BEFORE_REST_HOOK_DELIVERY, params);
			fail();
		} catch (AssertionError e) {
			// good
		}
	}


	@Before
	public void before() {
		ourNext_beforeRestHookDelivery_Return1 = true;
		ourNext_beforeRestHookDelivery_Return2 = true;
		ourLastCanonicalSubscription = null;
		ourLastResourceDeliveryMessage = null;
		ourInvocations.clear();
	}

	@Configuration
	@ComponentScan(basePackages = "ca.uhn.fhir.jpa.model")
	public static class MyCtxConfig {

		@Bean
		public SubscriptionInterceptorRegistry subscriptionInterceptorRegistry() {
			return new SubscriptionInterceptorRegistry();
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyInterceptorTwo interceptor1() {
			return new MyInterceptorTwo();
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyInterceptorOne interceptor2() {
			return new MyInterceptorOne();
		}

	}

	@SubscriptionInterceptor
	@Order(100)
	public static class MyInterceptorOne {

		@SubscriptionHook(Pointcut.BEFORE_REST_HOOK_DELIVERY)
		public boolean beforeRestHookDelivery(CanonicalSubscription theCanonicalSubscription) {
			ourLastCanonicalSubscription = theCanonicalSubscription;
			ourInvocations.add("MyInterceptorOne.beforeRestHookDelivery");
			return ourNext_beforeRestHookDelivery_Return1;
		}

	}

	@SubscriptionInterceptor
	@Order(200)
	public static class MyInterceptorTwo {
		@SubscriptionHook(Pointcut.BEFORE_REST_HOOK_DELIVERY)
		public boolean beforeRestHookDelivery(ResourceDeliveryMessage theResourceDeliveryMessage) {
			ourLastResourceDeliveryMessage = theResourceDeliveryMessage;
			ourInvocations.add("MyInterceptorTwo.beforeRestHookDelivery");
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
