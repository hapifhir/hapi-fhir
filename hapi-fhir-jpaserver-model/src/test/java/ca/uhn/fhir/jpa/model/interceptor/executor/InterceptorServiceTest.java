package ca.uhn.fhir.jpa.model.interceptor.executor;

import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

	private static final Logger ourLog = LoggerFactory.getLogger(InterceptorServiceTest.class);
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
	public void testRegisterHookFails() {
		int initialSize = myInterceptorRegistry.getGlobalInterceptorsForUnitTest().size();

		try {
			myInterceptorRegistry.registerInterceptor(new InterceptorThatFailsOnRegister());
			fail();
		} catch (InternalErrorException e) {
			// good
		}

		assertEquals(initialSize, myInterceptorRegistry.getGlobalInterceptorsForUnitTest().size());

	}

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
	public void testInvokeUsingSupplierArg() {
		Patient patient = new Patient();
		HookParams params = new HookParams()
			.addSupplier(IBaseResource.class, () -> patient);
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

	@Test
	public void testThreadLocalHookInterceptor() {
		myInterceptorRegistry.setThreadlocalInvokersEnabled(true);

		Patient patient = new Patient();
		HookParams params = new HookParams().add(IBaseResource.class, patient);

		@Interceptor
		@Order(100)
		class LocalInterceptor {

			private int myCount = 0;

			@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
			public boolean beforeRestHookDelivery(IBaseResource theResource) {
				myCount++;
				return true;
			}

		}
		LocalInterceptor interceptor = new LocalInterceptor();
		myInterceptorRegistry.registerThreadLocalInterceptor(interceptor);
		try {

			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			assertEquals(5, interceptor.myCount);

		} finally {
			myInterceptorRegistry.unregisterThreadLocalInterceptor(interceptor);
		}

		// Call some more - The interceptor is removed so the count shouldn't change
		myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
		assertEquals(5, interceptor.myCount);

	}

	/**
	 * <pre>
	 * JA 20190321 On my MBP 2018
	 *    ThreadLocalEnabled=true - Performed 500000 loops in 8383.0ms - 0.017ms / loop
	 *    ThreadLocalEnabled=false - Performed 500000 loops in 3743.0ms - 0.007ms / loop
	 *    ThreadLocalEnabled=true - Performed 500000 loops in 6163.0ms - 0.012ms / loop
	 *    ThreadLocalEnabled=false - Performed 500000 loops in 3487.0ms - 0.007ms / loop
	 *    ThreadLocalEnabled=true - Performed 1000000 loops in 00:00:12.458 - 0.012ms / loop
	 *    ThreadLocalEnabled=false - Performed 1000000 loops in 7046.0ms - 0.007ms / loop
	 * </pre>
	 */
	@Test
	@Ignore("Performance test - Not needed normally")
	public void testThreadLocalHookInterceptorMicroBenchmark() {
		threadLocalMicroBenchmark(true, 500000);
		threadLocalMicroBenchmark(false, 500000);
		threadLocalMicroBenchmark(true, 500000);
		threadLocalMicroBenchmark(false, 500000);
		threadLocalMicroBenchmark(true, 1000000);
		threadLocalMicroBenchmark(false, 1000000);
	}

	private void threadLocalMicroBenchmark(boolean theThreadlocalInvokersEnabled, int theCount) {
		myInterceptorRegistry.setThreadlocalInvokersEnabled(theThreadlocalInvokersEnabled);

		Patient patient = new Patient();
		HookParams params = new HookParams().add(IBaseResource.class, patient);

		@Interceptor
		@Order(100)
		class LocalInterceptor {

			private int myCount = 0;

			@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
			public boolean beforeRestHookDelivery(IBaseResource theResource) {
				myCount++;
				return true;
			}

		}

		StopWatch sw = new StopWatch();
		for (int i = 0; i < theCount; i++) {

			LocalInterceptor interceptor = new LocalInterceptor();
			myInterceptorRegistry.registerThreadLocalInterceptor(interceptor);
			try {
				myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
				myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
				myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
				myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
				myInterceptorRegistry.callHooks(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, params);
			} finally {
				myInterceptorRegistry.unregisterThreadLocalInterceptor(interceptor);
			}

		}

		ourLog.info("ThreadLocalEnabled={} - Performed {} loops in {} - {} / loop", theThreadlocalInvokersEnabled, theCount, sw.toString(), sw.formatMillisPerOperation(theCount));
	}

	@Before
	public void before() {
		ourNext_beforeRestHookDelivery_Return1 = true;
		ourLastResourceOne = null;
		ourLastResourceTwoA = null;
		ourLastResourceTwoB = null;
		ourInvocations.clear();
	}

	@After
	public void after() {
		myInterceptorRegistry.setThreadlocalInvokersEnabled(new InterceptorService().isThreadlocalInvokersEnabled());
	}

	@Configuration
	static class InterceptorRegistryTestCtxConfig {

		@Bean
		public InterceptorService interceptorService() {
			return new InterceptorService("test");
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorTwo interceptor1() {
			MyTestInterceptorTwo retVal = new MyTestInterceptorTwo();
			interceptorService().registerInterceptor(retVal);
			return retVal;
		}

		/**
		 * Note: Orders are deliberately reversed to make sure we get the orders right
		 * using the @Order annotation
		 */
		@Bean
		public MyTestInterceptorOne interceptor2() {
			MyTestInterceptorOne retVal = new MyTestInterceptorOne();
			interceptorService().registerInterceptor(retVal);
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

	@Interceptor(manualRegistration = true)
	public static class InterceptorThatFailsOnRegister {

		@Hook(Pointcut.REGISTERED)
		public void start() throws Exception {
			throw new Exception("InterceptorThatFailsOnRegister FAILED!");
		}

	}

}
