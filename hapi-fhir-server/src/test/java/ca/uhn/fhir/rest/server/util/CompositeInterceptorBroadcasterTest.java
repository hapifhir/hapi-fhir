package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompositeInterceptorBroadcasterTest {

	@Mock
	private IInterceptorBroadcaster myModuleBroadcasterMock;
	@Mock
	private IInterceptorBroadcaster myReqDetailsBroadcasterMock;
	@Mock
	private Pointcut myPointcutMock;
	@Mock
	private HookParams myHookParamsMock;
	@Mock
	private RequestDetails myRequestDetailsMock;


	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_RequestDetailsBroadcasterReturnsTrue_ThenReturnsTrue() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(true);
		when(myReqDetailsBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(true);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, myRequestDetailsMock,
			myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
		verify(myReqDetailsBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_RequestDetailsBroadcasterReturnsFalse_ThenReturnsFalse() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(true);
		when(myReqDetailsBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(false);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, myRequestDetailsMock,
			myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
		verify(myReqDetailsBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_ThenSkipsBroadcasterInRequestDetails_And_ReturnsFalse() {
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(myReqDetailsBroadcasterMock);

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(false);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, myRequestDetailsMock,
			myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
		verify(myReqDetailsBroadcasterMock, never()).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_NullRequestDetailsBroadcaster_ThenReturnsTrue() {

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(true);
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(null);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, myRequestDetailsMock, myPointcutMock,
			myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_And_NullRequestDetailsBroadcaster_ThenReturnsFalse() {

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(false);
		when(myRequestDetailsMock.getInterceptorBroadcaster()).thenReturn(null);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, myRequestDetailsMock, myPointcutMock,
			myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsTrue_And_NullRequestDetails_ThenReturnsTrue() {

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(true);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, null, myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isTrue();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenModuleBroadcasterReturnsFalse_And_NullRequestDetails_ThenReturnsFalse() {

		when(myModuleBroadcasterMock.callHooks(myPointcutMock, myHookParamsMock)).thenReturn(false);

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(myModuleBroadcasterMock, null, myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isFalse();

		verify(myModuleBroadcasterMock).callHooks(myPointcutMock, myHookParamsMock);
	}

	@Test
	void doCallHooks_WhenNullModuleBroadcaster_And_NullRequestDetails_ThenReturnsTrue() {

		boolean retVal = CompositeInterceptorBroadcaster.doCallHooks(null, null, myPointcutMock,	myHookParamsMock);

		assertThat(retVal).isTrue();
	}
}
