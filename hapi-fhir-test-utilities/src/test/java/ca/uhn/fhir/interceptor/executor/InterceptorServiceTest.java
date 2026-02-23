package ca.uhn.fhir.interceptor.executor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static ca.uhn.fhir.interceptor.executor.BaseInterceptorService.haveAppropriateParams;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InterceptorServiceTest {
	final InterceptorService myInterceptorService = new InterceptorService();

	@RegisterExtension
	private LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension();

	private final List<String> myInvocations = new ArrayList<>();

	@ParameterizedTest
	@CsvSource(textBlock = """
		java.lang.NullPointerException                            , true
		java.lang.Exception                                       , true
		ca.uhn.fhir.rest.server.exceptions.InternalErrorException , false
		ca.uhn.fhir.rest.server.exceptions.AuthenticationException, false
		"""
	)
	void testExceptionResultsInStackTraceLogging(Class<? extends Exception> theExceptionClass, boolean theExpectStackTraceToBeLogged) {
		class TestInterceptor {
			@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED)
			public void test(HookParams theParams) throws Exception {
				Exception exception = ReflectionUtil.newInstance(theExceptionClass, String.class, "This is the error message");
				throw exception;
			}
		}

		myInterceptorService.registerInterceptor(new TestInterceptor());

		HookParams params = new HookParams();
		params.add(HttpServletRequest.class, null);
		params.add(HttpServletResponse.class, null);

		// Test
		assertThatThrownBy(()->myInterceptorService.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED, params));

		// Verify
		ILoggingEvent logEvent = myLogbackTestExtension.getLogEvents().get(myLogbackTestExtension.getLogEvents().size() - 1);
		assertEquals("Exception thrown by interceptor for pointcut SERVER_INCOMING_REQUEST_PRE_PROCESSED: " + theExceptionClass.getName() + ": This is the error message", logEvent.getFormattedMessage());
		assertEquals(logEvent.getThrowableProxy() != null, theExpectStackTraceToBeLogged);
	}

	@Test
	void testInterceptorWithAnnotationDefinedOnInterface() {

		InterceptorService svc = new InterceptorService();
		TestInterceptorWithAnnotationDefinedOnInterface_Class interceptor = new TestInterceptorWithAnnotationDefinedOnInterface_Class();
		svc.registerInterceptor(interceptor);

		assertEquals(1, interceptor.getRegisterCount());
	}

	@Test
	void testInterceptorThrowsException() {

		class InterceptorThrowingException {
			@Hook(Pointcut.TEST_RB)
			public void test(String theValue) {
				throw new AuthenticationException(theValue);
			}
		}

		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(new InterceptorThrowingException());

		try {
			svc.callHooks(Pointcut.TEST_RB, new HookParams("A MESSAGE", "B"));
			fail();
		} catch (AuthenticationException e) {
			assertEquals("A MESSAGE", e.getMessage());
		}

	}

	@Test
	void testInterceptorReturnsClass() {

		class InterceptorReturningClass {

			private BaseServerResponseException myNextResponse;

			@Hook(Pointcut.TEST_RO)
			public BaseServerResponseException hook() {
				return myNextResponse;
			}

		}

		InterceptorReturningClass interceptor0 = new InterceptorReturningClass();
		InterceptorReturningClass interceptor1 = new InterceptorReturningClass();

		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(interceptor0);
		svc.registerInterceptor(interceptor1);

		interceptor0.myNextResponse = new InvalidRequestException("0");
		interceptor1.myNextResponse = new InvalidRequestException("1");
		Object response = svc.callHooksAndReturnObject(Pointcut.TEST_RO, new HookParams("", ""));
		assertEquals("0", ((InvalidRequestException) response).getMessage());

		interceptor0.myNextResponse = null;
		response = svc.callHooksAndReturnObject(Pointcut.TEST_RO, new HookParams("", ""));
		assertEquals("1", ((InvalidRequestException) response).getMessage());
	}


	/**
	 * Hook methods with private access are ignored
	 */
	@Test
	void testInterceptorWithPrivateAccessHookMethod() {

		class InterceptorThrowingException {
			@Hook(Pointcut.TEST_RB)
			private void test(String theValue) {
				throw new AuthenticationException(theValue);
			}
		}

		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(new InterceptorThrowingException());

		assertThatNoException().isThrownBy(()->svc.callHooks(Pointcut.TEST_RB, new HookParams("A MESSAGE", "B")));
	}

	@Test
	void testInterceptorWithDefaultAccessHookMethod() {

		class InterceptorThrowingException {
			@Hook(Pointcut.TEST_RB)
			void test(String theValue) {
				throw new AuthenticationException(theValue);
			}
		}

		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(new InterceptorThrowingException());

		try {
			svc.callHooks(Pointcut.TEST_RB, new HookParams("A MESSAGE", "B"));
			fail();
		} catch (AuthenticationException e) {
			assertEquals("A MESSAGE", e.getMessage());
		}

	}

	@Test
	void testInterceptorWithInheritedHookMethod() {

		class InterceptorThrowingException {
			@Hook(Pointcut.TEST_RB)
			void test(String theValue) {
				throw new AuthenticationException(theValue);
			}
		}

		class InterceptorThrowingException2 extends InterceptorThrowingException {
			// nothing
		}

		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(new InterceptorThrowingException2());

		try {
			svc.callHooks(Pointcut.TEST_RB, new HookParams("A MESSAGE", "B"));
			fail();
		} catch (AuthenticationException e) {
			assertEquals("A MESSAGE", e.getMessage());
		}

	}

	@Test
	void testInterceptorWithNoHooks() {

		class InterceptorWithNoHooks {
			// nothing
		}

		InterceptorService svc = new InterceptorService();
		svc.setWarnOnInterceptorWithNoHooks(false);
		boolean outcome = svc.registerInterceptor(new InterceptorWithNoHooks());
		assertFalse(outcome);
	}

	@Test
	void testRegisterHookFails() {
		InterceptorService svc = new InterceptorService();
		int initialSize = svc.getGlobalInterceptorsForUnitTest().size();

		try {
			svc.registerInterceptor(new InterceptorThatFailsOnRegister());
			fail();
		} catch (InternalErrorException e) {
			// good
		}

		assertThat(svc.getGlobalInterceptorsForUnitTest()).hasSize(initialSize);

	}

	@Test
	void testManuallyRegisterInterceptor() {
		InterceptorService svc = new InterceptorService();

		// Registered in opposite order to verify that the order on the annotation is used
		MyTestInterceptorTwo interceptor1 = new MyTestInterceptorTwo();
		MyTestInterceptorOne interceptor0 = new MyTestInterceptorOne();
		assertFalse(svc.hasHooks(Pointcut.TEST_RB));
		svc.registerInterceptor(interceptor1);
		assertTrue(svc.hasHooks(Pointcut.TEST_RB));
		svc.registerInterceptor(interceptor0);
		assertTrue(svc.hasHooks(Pointcut.TEST_RB));

		// Register the manual interceptor (has Order right in the middle)
		MyTestInterceptorManual myInterceptorManual = new MyTestInterceptorManual();
		svc.registerInterceptor(myInterceptorManual);
		List<Object> globalInterceptors = svc.getGlobalInterceptorsForUnitTest();
		assertThat(globalInterceptors).hasSize(3);
        assertInstanceOf(MyTestInterceptorOne.class, globalInterceptors.get(0), globalInterceptors.get(0).getClass().toString());
        assertInstanceOf(MyTestInterceptorManual.class, globalInterceptors.get(1), globalInterceptors.get(1).getClass().toString());
        assertInstanceOf(MyTestInterceptorTwo.class, globalInterceptors.get(2), globalInterceptors.get(2).getClass().toString());

		// Try to register again (should have no effect
		svc.registerInterceptor(myInterceptorManual);
		globalInterceptors = svc.getGlobalInterceptorsForUnitTest();
		assertThat(globalInterceptors).hasSize(3);
        assertInstanceOf(MyTestInterceptorOne.class, globalInterceptors.get(0), globalInterceptors.get(0).getClass().toString());
        assertInstanceOf(MyTestInterceptorManual.class, globalInterceptors.get(1), globalInterceptors.get(1).getClass().toString());
        assertInstanceOf(MyTestInterceptorTwo.class, globalInterceptors.get(2), globalInterceptors.get(2).getClass().toString());

		// Make sure we have the right invokers in the right order
		List<Object> invokers = svc.getInterceptorsWithInvokersForPointcut(Pointcut.TEST_RB);
		assertThat(invokers.get(0)).isSameAs(interceptor0);
		assertThat(invokers.get(1)).isSameAs(myInterceptorManual);
		assertThat(invokers.get(2)).isSameAs(interceptor1);

		// Finally, unregister it
		svc.unregisterInterceptor(myInterceptorManual);
		globalInterceptors = svc.getGlobalInterceptorsForUnitTest();
		assertThat(globalInterceptors).hasSize(2);
        assertInstanceOf(MyTestInterceptorOne.class, globalInterceptors.get(0), globalInterceptors.get(0).getClass().toString());
        assertInstanceOf(MyTestInterceptorTwo.class, globalInterceptors.get(1), globalInterceptors.get(1).getClass().toString());

		// Unregister the two others
		assertTrue(svc.hasHooks(Pointcut.TEST_RB));
		svc.unregisterInterceptor(interceptor1);
		assertTrue(svc.hasHooks(Pointcut.TEST_RB));
		svc.unregisterInterceptor(interceptor0);
		assertFalse(svc.hasHooks(Pointcut.TEST_RB));
	}

	@Test
	void testInvokeGlobalInterceptorMethods() {
		InterceptorService svc = new InterceptorService();

		// Registered in opposite order to verify that the order on the annotation is used
		MyTestInterceptorTwo interceptor1 = new MyTestInterceptorTwo();
		MyTestInterceptorOne interceptor0 = new MyTestInterceptorOne();
		svc.registerInterceptor(interceptor1);
		svc.registerInterceptor(interceptor0);

		if (svc.hasHooks(Pointcut.TEST_RB)) {
			boolean outcome = svc.callHooks(Pointcut.TEST_RB, new HookParams("A", "B"));
			assertTrue(outcome);
		}

		assertThat(myInvocations).containsExactly("MyTestInterceptorOne.testRb", "MyTestInterceptorTwo.testRb");
		assertThat(interceptor0.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString1).isSameAs("B");
	}

	@Test
	void testInvokeAnonymousInterceptorMethods() {
		InterceptorService svc = new InterceptorService();

		MyTestAnonymousInterceptorOne interceptor0 = new MyTestAnonymousInterceptorOne();
		MyTestAnonymousInterceptorTwo interceptor1 = new MyTestAnonymousInterceptorTwo();
		svc.registerAnonymousInterceptor(Pointcut.TEST_RB, interceptor0);
		svc.registerAnonymousInterceptor(Pointcut.TEST_RB, interceptor1);

		if (svc.hasHooks(Pointcut.TEST_RB)) {
			boolean outcome = svc.callHooks(Pointcut.TEST_RB, new HookParams("A", "B"));
			assertTrue(outcome);
		}

		assertThat(myInvocations).containsExactly("MyTestAnonymousInterceptorOne.testRb", "MyTestAnonymousInterceptorTwo.testRb");
		assertThat(interceptor0.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString1).isSameAs("B");
	}

	@Test
	void testInvokeUsingSupplierArg() {
		InterceptorService svc = new InterceptorService();

		MyTestInterceptorOne interceptor0 = new MyTestInterceptorOne();
		MyTestInterceptorTwo interceptor1 = new MyTestInterceptorTwo();
		svc.registerInterceptor(interceptor0);
		svc.registerInterceptor(interceptor1);

		boolean outcome = svc.callHooks(Pointcut.TEST_RB, new HookParams("A", "B"));
		assertTrue(outcome);

		assertThat(myInvocations).containsExactly("MyTestInterceptorOne.testRb", "MyTestInterceptorTwo.testRb");
		assertThat(interceptor0.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString1).isSameAs("B");
	}

	@Test
	void testInvokeGlobalInterceptorMethods_MethodAbortsProcessing() {
		InterceptorService svc = new InterceptorService();

		MyTestInterceptorOne interceptor0 = new MyTestInterceptorOne();
		MyTestInterceptorTwo interceptor1 = new MyTestInterceptorTwo();
		svc.registerInterceptor(interceptor0);
		svc.registerInterceptor(interceptor1);

		interceptor0.myNextReturn = false;

		boolean outcome = svc.callHooks(Pointcut.TEST_RB, new HookParams("A", "B"));
		assertFalse(outcome);

		assertThat(myInvocations).containsExactly("MyTestInterceptorOne.testRb");
		assertThat(interceptor0.myLastString0).isSameAs("A");
		assertThat(interceptor1.myLastString0).isSameAs(null);
		assertThat(interceptor1.myLastString1).isSameAs(null);
	}

	@Test
	void testCallHooksInvokedWithNullParameters() {
		InterceptorService svc = new InterceptorService();

		class NullParameterInterceptor {
			private String myValue0 = "";
			private String myValue1 = "";

			@Hook(Pointcut.TEST_RB)
			public void hook(String theValue0, String theValue1) {
				myValue0 = theValue0;
				myValue1 = theValue1;
			}
		}

		NullParameterInterceptor interceptor;
		HookParams params;

		// Both null
		interceptor = new NullParameterInterceptor();
		svc.registerInterceptor(interceptor);
		params = new HookParams()
			.add(String.class, null)
			.add(String.class, null);
		svc.callHooks(Pointcut.TEST_RB, params);
		assertNull(interceptor.myValue0);
		assertNull(interceptor.myValue1);
		svc.unregisterAllInterceptors();

		// First null
		interceptor = new NullParameterInterceptor();
		svc.registerInterceptor(interceptor);
		params = new HookParams()
			.add(String.class, null)
			.add(String.class, "A");
		svc.callHooks(Pointcut.TEST_RB, params);
		assertNull(interceptor.myValue0);
		assertEquals("A", interceptor.myValue1);
		svc.unregisterAllInterceptors();

		// Second null
		interceptor = new NullParameterInterceptor();
		svc.registerInterceptor(interceptor);
		params = new HookParams()
			.add(String.class, "A")
			.add(String.class, null);
		svc.callHooks(Pointcut.TEST_RB, params);
		assertEquals("A", interceptor.myValue0);
		assertNull(interceptor.myValue1);
		svc.unregisterAllInterceptors();

	}

	@Test
	void testCallHooksLogAndSwallowException() {
		InterceptorService svc = new InterceptorService();

		class LogAndSwallowInterceptor0 {
			private boolean myHit;

			@Hook(Pointcut.TEST_RB)
			public void hook(String theValue0, String theValue1) {
				myHit = true;
				throw new IllegalStateException();
			}
		}
		LogAndSwallowInterceptor0 interceptor0 = new LogAndSwallowInterceptor0();
		svc.registerInterceptor(interceptor0);

		class LogAndSwallowInterceptor1 {
			private boolean myHit;

			@Hook(Pointcut.TEST_RB)
			public void hook(String theValue0, String theValue1) {
				myHit = true;
				throw new IllegalStateException();
			}
		}
		LogAndSwallowInterceptor1 interceptor1 = new LogAndSwallowInterceptor1();
		svc.registerInterceptor(interceptor1);

		class LogAndSwallowInterceptor2 {
			private boolean myHit;

			@Hook(Pointcut.TEST_RB)
			public void hook(String theValue0, String theValue1) {
				myHit = true;
				throw new NullPointerException("AAA");
			}
		}
		LogAndSwallowInterceptor2 interceptor2 = new LogAndSwallowInterceptor2();
		svc.registerInterceptor(interceptor2);

		HookParams params = new HookParams()
			.add(String.class, null)
			.add(String.class, null);

		try {
			svc.callHooks(Pointcut.TEST_RB, params);
			fail();
		} catch (NullPointerException e) {
			assertEquals("AAA", e.getMessage());
		}

		assertTrue(interceptor0.myHit);
		assertTrue(interceptor1.myHit);
		assertTrue(interceptor2.myHit);
	}


	@Test
	void testCallHooksInvokedWithWrongParameters() {
		InterceptorService svc = new InterceptorService();

		Integer msg = 123;
		CanonicalSubscription subs = new CanonicalSubscription();
		HookParams params = new HookParams(msg, subs);
		try {
			svc.callHooks(Pointcut.TEST_RB, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("Invalid params for pointcut " + Pointcut.TEST_RB + " - Wanted java.lang.String,java.lang.String but found ");
		}
	}

	@Test
	void testValidateParamTypes() {

		HookParams params = new HookParams();
		params.add(String.class, "A");
		params.add(String.class, "B");
		boolean validated = haveAppropriateParams(Pointcut.TEST_RB, params);
		assertTrue(validated);
	}

	@Test
	void testValidateParamTypesMissingParam() {

		HookParams params = new HookParams();
		params.add(String.class, "A");
		try {
			haveAppropriateParams(Pointcut.TEST_RB, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1909) + "Wrong number of params for pointcut " + Pointcut.TEST_RB + " - Wanted java.lang.String,java.lang.String but found [String]", e.getMessage());
		}
	}

	@Test
	void testValidateParamTypesExtraParam() {

		HookParams params = new HookParams();
		params.add(String.class, "A");
		params.add(String.class, "B");
		params.add(String.class, "C");
		params.add(String.class, "D");
		params.add(String.class, "E");
		params.add(String.class, "F");
		params.add(String.class, "G");
		try {
			haveAppropriateParams(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1909) + "Wrong number of params for pointcut STORAGE_PRECOMMIT_RESOURCE_UPDATED - Wanted ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum,ca.uhn.fhir.rest.api.server.RequestDetails,ca.uhn.fhir.rest.api.server.storage.TransactionDetails,ca.uhn.fhir.rest.server.servlet.ServletRequestDetails,org.hl7.fhir.instance.model.api.IBaseResource,org.hl7.fhir.instance.model.api.IBaseResource but found [String, String, String, String, String, String, String]", e.getMessage());
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	void testValidateParamTypesWrongParam() {

		HookParams params = new HookParams();
		params.add((Class) String.class, 1);
		params.add((Class) String.class, 2);
		params.add((Class) String.class, 3);
		params.add((Class) String.class, 4);
		params.add((Class) String.class, 5);
		params.add((Class) String.class, 6);
		try {
			haveAppropriateParams(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, params);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid params for pointcut " + Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED + " - class java.lang.Integer is not of type class java.lang.String", e.getMessage());
		}
	}

	/**
	 * Verify the ifPresent methods match the base behaviour.
	 */
	@Nested
	class SupplierDefaultMethods {
		HookParams params = new HookParams("1", "2");

		@Test
		void testBooleanWithNoHooks_returnsTrue() {
			InterceptorService svc = new InterceptorService();

			assertTrue(svc.callHooks(Pointcut.TEST_RB, params));
			assertTrue(svc.ifHasCallHooks(Pointcut.TEST_RB, () -> params));
		}

		@Test
		void testBooleanWithAllHooksReturnTrue_returnsTrue() {
			InterceptorService svc = new InterceptorService();
			svc.registerInterceptor(new BooleanHook(true));
			svc.registerInterceptor(new BooleanHook(true));

			assertTrue(svc.callHooks(Pointcut.TEST_RB, params));
			assertTrue(svc.ifHasCallHooks(Pointcut.TEST_RB, () -> params));
		}

		@Test
		void testBooleanWithAHookReturnFalse_returnsFalse() {
			InterceptorService svc = new InterceptorService();
			svc.registerInterceptor(new BooleanHook(true));
			svc.registerInterceptor(new BooleanHook(false));
			svc.registerInterceptor(new BooleanHook(true));

			assertFalse(svc.callHooks(Pointcut.TEST_RB, params));
			assertFalse(svc.ifHasCallHooks(Pointcut.TEST_RB, () -> params));
		}


		@Test
		void testObjectWithNoHooks_returnsNull() {
			InterceptorService svc = new InterceptorService();

			assertNull(svc.callHooksAndReturnObject(Pointcut.TEST_RO, params));
			assertNull(svc.ifHasCallHooksAndReturnObject(Pointcut.TEST_RO, () -> params));
		}

		@Test
		void testObjectWithAllHooksReturnNull_returnsNull() {
			InterceptorService svc = new InterceptorService();
			svc.registerInterceptor(new ObjectHook<>(null));
			svc.registerInterceptor(new ObjectHook<>(null));

			assertNull(svc.callHooksAndReturnObject(Pointcut.TEST_RO, params));
			assertNull(svc.ifHasCallHooksAndReturnObject(Pointcut.TEST_RO, () -> params));
		}

		@Test
		void testObjectWithAHookReturnValue_returnsFirstValue() {
			InterceptorService svc = new InterceptorService();
			svc.registerInterceptor(new ObjectHook<>(null));
			svc.registerInterceptor(new ObjectHook<>(new ResourceNotFoundException("first")));
			svc.registerInterceptor(new ObjectHook<>(new ResourceNotFoundException("second")));

			assertEquals("first", ((BaseServerResponseException) svc.callHooksAndReturnObject(Pointcut.TEST_RO, params)).getMessage());
			assertEquals("first", ((BaseServerResponseException) svc.ifHasCallHooksAndReturnObject(Pointcut.TEST_RO, () -> params)).getMessage());
		}

		static class BooleanHook {

			final boolean myResult;

            BooleanHook(boolean theResult) {
                myResult = theResult;
            }

            @Hook(Pointcut.TEST_RB)
			boolean doIt() {
				return myResult;
			}
		}

		static class ObjectHook<T extends BaseServerResponseException> {
			final T myResult;

            ObjectHook(T theResult) {
                myResult = theResult;
            }

            @Hook(Pointcut.TEST_RO)
			T doIt() {
				return myResult;
			}

		}
	}


	@Nested
	class FilterHooks {
		List<String> mySupplierLog = new ArrayList<>();
		HookParams myParams = new HookParams("1");
		MySupplier mySupplier = new MySupplier();

		@Test
		void testNoHooks_callsSupplierNormally() {
		    // given no interceptors registered

		    // when
			myInterceptorService.runWithFilterHooks(Pointcut.TEST_FILTER, myParams, ()->{
				mySupplierLog.add("I run!");
			});

		    // then
			assertThat(mySupplierLog).containsExactly("I run!");
		}


		@Test
		void testNoHooks_callsSupplierNormallyAndReturnsValue() {
			// given no interceptors registered

			// when
			Integer result = myInterceptorService.runWithFilterHooks(Pointcut.TEST_FILTER, myParams, mySupplier);

			// then
			assertThat(mySupplierLog).containsExactly("MySupplier runs!");
			assertThat(result).isEqualTo(42);
		}
		@Test
		void testOneHook_calledAroundSupplier() {
			// given no interceptors registered
			myInterceptorService.registerInterceptor(new MyFilterHookInterceptor("filter1"));

			// when
			Integer result = myInterceptorService.runWithFilterHooks(Pointcut.TEST_FILTER, myParams, mySupplier);

			// then
			assertThat(result).isEqualTo(42);
			assertThat(mySupplierLog).containsExactly(
				"filter1-before",
				"MySupplier runs!",
				"filter1-after"
			);
		}

		@Test
		void testTwoHooks_calledInOrderAroundSupplier() {
			// given no interceptors registered
			myInterceptorService.registerInterceptor(new MyFilterHookInterceptor("filter1"));
			myInterceptorService.registerInterceptor(new MyFilterHookInterceptor("filter2"));

			// when
			Integer result = myInterceptorService.runWithFilterHooks(Pointcut.TEST_FILTER, myParams, mySupplier);

			// then
			assertThat(result).isEqualTo(42);
			assertThat(mySupplierLog).containsExactly(
				"filter1-before",
				"filter2-before",
				"MySupplier runs!",
				"filter2-after",
				"filter1-after"
			);
		}


		@Test
		void testFilterHookWhichDoesNotCallSupplier_throwsError() {
			// given no interceptors registered
			myInterceptorService.registerInterceptor(new Object() {
				@Hook(Pointcut.TEST_FILTER)
				public IInterceptorFilterHook testFilter(String theString) {
					// return a filter that does not call the runnable
					return theContinuation ->{
						mySupplierLog.add("bad-boy ran but didn't call continuation");
						// theContinuation.run(); // not called
					};
				}

			});

			// when
			assertThatThrownBy(() -> myInterceptorService.runWithFilterHooks(Pointcut.TEST_FILTER, myParams, mySupplier))
				.hasMessageContaining("Supplier was not executed in filter")
				.hasMessageContaining("InterceptorServiceTest$FilterHooks$1.testFilter");

			assertThat(mySupplierLog).containsExactly(
				"bad-boy ran but didn't call continuation"
			);
		}

		class MySupplier implements Supplier<Integer> {
			public Integer get() {
				mySupplierLog.add("MySupplier runs!");
				return 42;
			}
		}

		class MyFilterHookInterceptor {
			final String myName;

			MyFilterHookInterceptor(String theName) {
				myName = theName;
			}

			@Hook(Pointcut.TEST_FILTER)
			public IInterceptorFilterHook testFilter(String theParam) {
				return new MyFilter();
			}

			private class MyFilter implements IInterceptorFilterHook {
				@Override
				public void wrapCall(Runnable theContinuation) {
					mySupplierLog.add(myName + "-before");
					try {
						theContinuation.run();
					} finally {
						mySupplierLog.add(myName + "-after");
					}
				}
			}
		}
	}

	@BeforeEach
	void before() {
		myInvocations.clear();
	}

	interface TestInterceptorWithAnnotationDefinedOnInterface_Interface {

		@Hook(Pointcut.INTERCEPTOR_REGISTERED)
		void registered();

	}

	@Interceptor(order = 100)
	public class MyTestInterceptorOne {

		private String myLastString0;
		private boolean myNextReturn = true;

		public MyTestInterceptorOne() {
			super();
		}

		@Hook(Pointcut.TEST_RB)
		public boolean testRb(String theString0) {
			myLastString0 = theString0;
			myInvocations.add("MyTestInterceptorOne.testRb");
			return myNextReturn;
		}

	}

	@Interceptor(order = 300)
	public class MyTestInterceptorTwo {
		private String myLastString0;
		private String myLastString1;

		@Hook(Pointcut.TEST_RB)
		public boolean testRb(String theString0, String theString1) {
			myLastString0 = theString0;
			myLastString1 = theString1;
			myInvocations.add("MyTestInterceptorTwo.testRb");
			return true;
		}
	}

	public class MyTestAnonymousInterceptorOne implements IAnonymousInterceptor {
		private String myLastString0;
		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs) {
			myLastString0 = theArgs.get(String.class, 0);
			myInvocations.add("MyTestAnonymousInterceptorOne.testRb");
		}
	}

	public class MyTestAnonymousInterceptorTwo implements IAnonymousInterceptor {
		private String myLastString0;
		private String myLastString1;

		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs) {
			myLastString0 = theArgs.get(String.class, 0);
			myLastString1 = theArgs.get(String.class, 1);
			myInvocations.add("MyTestAnonymousInterceptorTwo.testRb");
		}
	}

	@Interceptor(order = 200)
	public class MyTestInterceptorManual {
		@Hook(Pointcut.TEST_RB)
		public void testRb() {
			myInvocations.add("MyTestInterceptorManual.testRb");
		}
	}

	public static class TestInterceptorWithAnnotationDefinedOnInterface_Class implements TestInterceptorWithAnnotationDefinedOnInterface_Interface {

		private int myRegisterCount = 0;

		public int getRegisterCount() {
			return myRegisterCount;
		}

		@Override
		public void registered() {
			myRegisterCount++;
		}
	}

	/**
	 * Just a make-believe version of this class for the unit test
	 */
	private static class CanonicalSubscription {
	}

	@Interceptor()
	public static class InterceptorThatFailsOnRegister {

		@Hook(Pointcut.INTERCEPTOR_REGISTERED)
		public void start() throws Exception {
			throw new Exception("InterceptorThatFailsOnRegister FAILED!");
		}

	}

	static class BaseHook {
		@Hook(Pointcut.JPA_PERFTRACE_INFO)
		public void hook1() {
		}
	}

	static class HookClass1 extends BaseHook {
	}

	@Test
	void testHookDescription() {
	    // given
		InterceptorService svc = new InterceptorService();
		svc.registerInterceptor(new HookClass1());

	    // when
		List<IBaseInterceptorBroadcaster.IInvoker> invokers = svc.getInvokersForPointcut(Pointcut.JPA_PERFTRACE_INFO);

		// then
		assertThat(invokers).hasSize(1);
		assertThat(invokers.get(0).getHookDescription()).isEqualTo("ca.uhn.fhir.interceptor.executor.InterceptorServiceTest$HookClass1.hook1");
	}
	

}
