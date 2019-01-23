package ca.uhn.fhir.jpa.model.interceptor.executor;

import ca.uhn.fhir.jpa.model.interceptor.api.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
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
@ContextConfiguration(classes = {InterceptorServiceTest.InterceptorRegistryTestCtxConfig.class})
public class InterceptorServiceTest {

	private static boolean ourNext_beforeRestHookDelivery_Return1;
	private static List<String> ourInvocations = new ArrayList<>();
	private static IBaseResource ourLastResourceOne;
	private static IBaseResource ourLastResourceTwoA;
	private static IBaseResource ourLastResourceTwoB;

	@Autowired
	private InterceptorService myInterceptorRegistry;

	@Autowired
	private MyTestInterceptorOne myInterceptorOne;
	@Autowired
	private MyTestInterceptorTwo myInterceptorTwo;
	@Autowired
	private MyTestInterceptorManual myInterceptorManual;

	@Test
	public void testGlobalInterceptorsAreFound() {
		List<Object> globalInterceptors = myInterceptorRegistry.getGlobalInterceptorsForUnitTest();
		assertEquals(2, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyTestInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyTestInterceptorTwo);
	}

	@Test
	public void testManuallyRegisterGlobalInterceptor() {

		// Register the manual interceptor (has @Order right in the middle)
		myInterceptorRegistry.registerInterceptor(myInterceptorManual);
		List<Object> globalInterceptors = myInterceptorRegistry.getGlobalInterceptorsForUnitTest();
		assertEquals(3, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyTestInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyTestInterceptorManual);
		assertTrue(globalInterceptors.get(2).getClass().toString(), globalInterceptors.get(2) instanceof MyTestInterceptorTwo);

		// Try to register again (should have no effect
		myInterceptorRegistry.registerInterceptor(myInterceptorManual);
		globalInterceptors = myInterceptorRegistry.getGlobalInterceptorsForUnitTest();
		assertEquals(3, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyTestInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyTestInterceptorManual);
		assertTrue(globalInterceptors.get(2).getClass().toString(), globalInterceptors.get(2) instanceof MyTestInterceptorTwo);

		// Make sure we have the right invokers in the right order
		List<Object> invokers = myInterceptorRegistry.getInterceptorsWithInvokersForPointcut(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED);
		assertSame(myInterceptorOne, invokers.get(0));
		assertSame(myInterceptorManual, invokers.get(1));
		assertSame(myInterceptorTwo, invokers.get(2));

		// Finally, unregister it
		myInterceptorRegistry.unregisterInterceptor(myInterceptorManual);
		globalInterceptors = myInterceptorRegistry.getGlobalInterceptorsForUnitTest();
		assertEquals(2, globalInterceptors.size());
		assertTrue(globalInterceptors.get(0).getClass().toString(), globalInterceptors.get(0) instanceof MyTestInterceptorOne);
		assertTrue(globalInterceptors.get(1).getClass().toString(), globalInterceptors.get(1) instanceof MyTestInterceptorTwo);

	}

	@Test
	public void testInvokeGlobalInterceptorMethods() {
		Patient patient = new Patient();
		HookParams params = new HookParams()
			.add(IBaseResource.class, patient);
		boolean outcome = myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		assertTrue(outcome);

		assertThat(ourInvocations, contains("MyTestInterceptorOne.beforeRestHookDelivery", "MyTestInterceptorTwo.beforeRestHookDelivery"));
		assertSame(patient, ourLastResourceTwoA);
		assertNull(ourLastResourceTwoB);
		assertSame(patient, ourLastResourceOne);
	}

	@Test
	public void testInvokeGlobalInterceptorMethods_MethodAbortsProcessing() {
		ourNext_beforeRestHookDelivery_Return1 = false;

		Patient patient = new Patient();
		HookParams params = new HookParams()
			.add(IBaseResource.class, patient);
		boolean outcome = myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		assertFalse(outcome);

		assertThat(ourInvocations, contains("MyTestInterceptorOne.beforeRestHookDelivery"));
	}

	@Test
	public void testCallHooksInvokedWithWrongParameters() {
		Integer msg = 123;
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		try {
			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Wrong number of params for pointcut OP_PRECOMMIT_RESOURCE_CREATED - Wanted org.hl7.fhir.instance.model.api.IBaseResource but found [CanonicalSubscription, Integer]", e.getMessage());
		}
	}

	@Test
	public void testValidateParamTypes() {
		HookParams params = new HookParams();
		params.add(IBaseResource.class, new Patient());
		params.add(IBaseResource.class, new Patient());
		boolean validated = myInterceptorRegistry.haveAppropriateParams(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED, params);
		assertTrue(validated);
	}

	@Test
	public void testValidateParamTypesMissingParam() {
		HookParams params = new HookParams();
		params.add(IBaseResource.class, new Patient());
		try {
			myInterceptorRegistry.haveAppropriateParams(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Wrong number of params for pointcut OP_PRECOMMIT_RESOURCE_UPDATED - Wanted org.hl7.fhir.instance.model.api.IBaseResource,org.hl7.fhir.instance.model.api.IBaseResource but found [Patient]", e.getMessage());
		}
	}

	@Test
	public void testValidateParamTypesExtraParam() {
		HookParams params = new HookParams();
		params.add(IBaseResource.class, new Patient());
		params.add(IBaseResource.class, new Patient());
		params.add(IBaseResource.class, new Patient());
		try {
			myInterceptorRegistry.haveAppropriateParams(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Wrong number of params for pointcut OP_PRECOMMIT_RESOURCE_UPDATED - Wanted org.hl7.fhir.instance.model.api.IBaseResource,org.hl7.fhir.instance.model.api.IBaseResource but found [Patient, Patient, Patient]", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testValidateParamTypesWrongParam() {
		HookParams params = new HookParams();
		Class clazz = IBaseResource.class;
		params.add(clazz, "AAA");
		params.add(clazz, "BBB");
		try {
			myInterceptorRegistry.haveAppropriateParams(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid params for pointcut OP_PRECOMMIT_RESOURCE_UPDATED - class java.lang.String is not of type interface org.hl7.fhir.instance.model.api.IBaseResource", e.getMessage());
		}
	}

	@Before
	public void before() {
		ourNext_beforeRestHookDelivery_Return1 = true;
		ourLastResourceOne = null;
		ourLastResourceTwoA = null;
		ourLastResourceTwoB = null;
		ourInvocations.clear();
	}

	@Configuration
	@ComponentScan(basePackages = "ca.uhn.fhir.jpa.model")
	static class InterceptorRegistryTestCtxConfig {

		@Autowired
		private IInterceptorRegistry myInterceptorRegistry;

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorTwo interceptor1() {
			MyTestInterceptorTwo retVal = new MyTestInterceptorTwo();
			myInterceptorRegistry.registerInterceptor(retVal);
			return retVal;
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorOne interceptor2() {
			MyTestInterceptorOne retVal = new MyTestInterceptorOne();
			myInterceptorRegistry.registerInterceptor(retVal);
			return retVal;
		}

		@Bean
		public MyTestInterceptorManual interceptorManual() {
			return new MyTestInterceptorManual();
		}

	}

	@Interceptor
	@Order(100)
	public static class MyTestInterceptorOne {

		public MyTestInterceptorOne() {
			super();
		}

		@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
		public boolean beforeRestHookDelivery(IBaseResource theResource) {
			ourLastResourceOne = theResource;
			ourInvocations.add("MyTestInterceptorOne.beforeRestHookDelivery");
			return ourNext_beforeRestHookDelivery_Return1;
		}

	}

	@Interceptor
	@Order(300)
	public static class MyTestInterceptorTwo {
		@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
		public void beforeRestHookDelivery(IBaseResource theResource0, IBaseResource theResource1) {
			ourLastResourceTwoA = theResource0;
			ourLastResourceTwoB = theResource1;
			ourInvocations.add("MyTestInterceptorTwo.beforeRestHookDelivery");
		}
	}

	@Interceptor(manualRegistration = true)
	@Order(200)
	public static class MyTestInterceptorManual {
		@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
		public void beforeRestHookDelivery() {
			ourInvocations.add("MyTestInterceptorManual.beforeRestHookDelivery");
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
