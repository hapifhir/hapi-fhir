package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import com.google.common.collect.MultimapBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class CompositeInterceptorBroadcasterTest {

	@SuppressWarnings("rawtypes")
	private static final Class BOOLEAN_CLASS = boolean.class;
	private final List<Integer> myOrders = new ArrayList<>();
	@Mock
	private IInterceptorBroadcaster myModuleBroadcasterMock;
	@Mock
	private IInterceptorBroadcaster myReqDetailsBroadcasterMock;
	@Mock
	private IBaseInterceptorBroadcaster.IInvoker myModuleBroadcasterInvokerMock;
	@Mock
	private IBaseInterceptorBroadcaster.IInvoker myReqDetailsInvokerMock;
	@Mock
	private Pointcut myPointcutMock;
	@Mock
	private HookParams myHookParamsMock;
	@Mock
	private RequestDetails myRequestDetailsMock;

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_RequestDetailsBroadcasterReturnsTrue_ThenReturnsTrue() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myReqDetailsBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myReqDetailsBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myReqDetailsInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());
		when(myModuleBroadcasterInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(true);
		when(myReqDetailsInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(true);

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, myRequestDetailsMock);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterInvokerMock, times(1)).invoke(eq(myHookParamsMock));
		verify(myReqDetailsInvokerMock, times(1)).invoke(eq(myHookParamsMock));
	}

	@SuppressWarnings("unchecked")
	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_RequestDetailsBroadcasterReturnsFalse_ThenReturnsFalse() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myReqDetailsBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myReqDetailsBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myReqDetailsInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());
		when(myModuleBroadcasterInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(true);
		when(myReqDetailsInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(false);

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, myRequestDetailsMock);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterInvokerMock).invoke(eq(myHookParamsMock));
		verify(myReqDetailsInvokerMock).invoke(eq(myHookParamsMock));
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_ThenSkipsBroadcasterInRequestDetails_And_ReturnsFalse() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myReqDetailsBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myReqDetailsBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myReqDetailsInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());
		when(myModuleBroadcasterInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(false);

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, myRequestDetailsMock);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterInvokerMock, times(1)).invoke(eq(myHookParamsMock));
		verify(myReqDetailsInvokerMock, never()).invoke(eq(myHookParamsMock));
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_NullRequestDetailsBroadcaster_ThenReturnsTrue() {

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, myRequestDetailsMock);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterInvokerMock, times(1)).invoke(eq(myHookParamsMock));
		verify(myReqDetailsInvokerMock, never()).invoke(eq(myHookParamsMock));
	}

	@SuppressWarnings("unchecked")
	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_And_NullRequestDetailsBroadcaster_ThenReturnsFalse() {

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());
		when(myModuleBroadcasterInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(false);

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, myRequestDetailsMock);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterInvokerMock, times(1)).invoke(eq(myHookParamsMock));
	}

	@SuppressWarnings("unchecked")
	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_NullRequestDetails_ThenReturnsTrue() {

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, null);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterInvokerMock, times(1)).invoke(eq(myHookParamsMock));
		verify(myReqDetailsInvokerMock, never()).invoke(eq(myHookParamsMock));
	}

	@SuppressWarnings("unchecked")
	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_And_NullRequestDetails_ThenReturnsFalse() {

		when(myPointcutMock.getReturnType()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getBooleanReturnTypeForEnum()).thenReturn(BOOLEAN_CLASS);
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());
		when(myModuleBroadcasterMock.hasHooks(eq(myPointcutMock))).thenReturn(true);
		when(myModuleBroadcasterMock.getInvokersForPointcut(eq(myPointcutMock))).thenReturn(List.of(myModuleBroadcasterInvokerMock));
		when(myModuleBroadcasterInvokerMock.invoke(eq(myHookParamsMock))).thenReturn(false);

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster(myModuleBroadcasterMock, null);
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterInvokerMock).invoke(eq(myHookParamsMock));
	}

	@Test
	void doCallHooks_WhenNullModuleBroadcaster_And_NullRequestDetails_ThenReturnsTrue() {
		when(myPointcutMock.getParameterTypes()).thenReturn(List.of());
		when(myHookParamsMock.getParamsForType()).thenReturn(MultimapBuilder.hashKeys().arrayListValues().build());

		IInterceptorBroadcaster interceptorBroadcaster = CompositeInterceptorBroadcaster
			.newCompositeBroadcaster();
		boolean retVal = interceptorBroadcaster.callHooks(myPointcutMock, myHookParamsMock);

		assertThat(retVal).isTrue();
	}

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
		InterceptorService[] services = new InterceptorService[]{svc0, svc1, svc2};

		List<IInterceptorBroadcaster> serviceList = List.of(
			services[theIndex0], services[theIndex1], services[theIndex2]
		);
		IInterceptorBroadcaster compositeBroadcaster = CompositeInterceptorBroadcaster.newCompositeBroadcaster(serviceList.toArray(new IInterceptorBroadcaster[0]));

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

		@Hook(value = Pointcut.TEST_RO, order = 0)
		public BaseServerResponseException hook0() {
			myOrders.add(0);
			return null;
		}

		@Hook(value = Pointcut.TEST_RO, order = 2)
		public BaseServerResponseException hook2() {
			myOrders.add(2);
			return null;
		}

	}

	@Interceptor
	private class Interceptor1 {

		@Hook(value = Pointcut.TEST_RO, order = 1)
		public BaseServerResponseException hook1() {
			myOrders.add(1);
			return null;
		}

		@Hook(value = Pointcut.TEST_RO, order = 3)
		public BaseServerResponseException hook3() {
			myOrders.add(3);
			return null;
		}

	}

	@Interceptor
	private class Interceptor2 {

		@Hook(value = Pointcut.TEST_RO, order = -1)
		public BaseServerResponseException hookMinus1() {
			myOrders.add(-1);
			return null;
		}

		@Hook(value = Pointcut.TEST_RO, order = -2)
		public BaseServerResponseException hookMinus2() {
			myOrders.add(-2);
			return null;
		}

	}

}
