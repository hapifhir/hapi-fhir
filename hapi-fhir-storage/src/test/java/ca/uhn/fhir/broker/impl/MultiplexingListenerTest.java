package ca.uhn.fhir.broker.impl;

// Created by claude-sonnet-4-5-20250929

import ca.uhn.fhir.broker.api.BrokerListenerClosedException;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for MultiplexingListener.
 */
@MockitoSettings(strictness = Strictness.LENIENT)
class MultiplexingListenerTest {

	@Mock
	private IMessage<String> mockMessage;

	@Mock
	private IMessageDeliveryContext mockDeliveryContext;

	private MultiplexingListener<String> multiplexingListener;

	@BeforeEach
	void setUp() {
		multiplexingListener = new MultiplexingListener<>(String.class);
		when(mockMessage.getPayload()).thenReturn("test-payload");
	}

	@AfterEach
	void tearDown() {
		if (multiplexingListener != null) {
			multiplexingListener.close();
		}
	}

	@Test
	void testConstructor_setsPayloadType() {
		// when
		MultiplexingListener<Integer> listener = new MultiplexingListener<>(Integer.class);

		// then
		assertThat(listener.getPayloadType()).isEqualTo(Integer.class);
		listener.close();
	}

	@Test
	void testGetPayloadType_returnsCorrectType() {
		// when
		Class<String> payloadType = multiplexingListener.getPayloadType();

		// then
		assertThat(payloadType).isEqualTo(String.class);
	}

	@Test
	void testAddListener_addsListenerSuccessfully() {
		// given
		IMessageListener<String> mockListener = createStringMockListener();

		// when
		boolean result = multiplexingListener.addListener(mockListener);

		// then
		assertThat(result).isTrue();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testAddListener_throwsExceptionWhenPayloadTypeMismatch() {
		// given - listener with incompatible payload type
		IMessageListener<Integer> mockListener = mock(IMessageListener.class);
		when(mockListener.getPayloadType()).thenReturn(Integer.class);

		// when/then
		assertThatThrownBy(() -> multiplexingListener.addListener((IMessageListener) mockListener))
			.isInstanceOf(ConfigurationException.class)
			.hasMessageContaining("Expecting listener of type")
			.hasMessageContaining(String.class.getName())
			.hasMessageContaining(Integer.class.getName());
	}

	@Test
	void testRemoveListener_removesListenerSuccessfully() {
		// given
		IMessageListener<String> mockListener = createStringMockListener();
		multiplexingListener.addListener(mockListener);

		// when
		boolean result = multiplexingListener.removeListener(mockListener);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void testRemoveListener_returnsFalseWhenListenerNotPresent() {
		// given
		IMessageListener<String> mockListener = createStringMockListener();

		// when
		boolean result = multiplexingListener.removeListener(mockListener);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void testHandleMessage_delegatesToAllSubListeners() {
		// given
		IMessageListener<String> listener1 = createStringMockListener();
		IMessageListener<String> listener2 = createStringMockListener();
		IMessageListener<String> listener3 = createStringMockListener();

		multiplexingListener.addListener(listener1);
		multiplexingListener.addListener(listener2);
		multiplexingListener.addListener(listener3);

		// when
		multiplexingListener.handleMessage(mockDeliveryContext, mockMessage);

		// then - all listeners should have been called
		verify(listener1, times(1)).handleMessage(mockMessage);
		verify(listener2, times(1)).handleMessage(mockMessage);
		verify(listener3, times(1)).handleMessage(mockMessage);
	}

	@Test
	void testHandleMessage_withNoSubListeners_doesNotThrow() {
		// when/then - should not throw even with no sub-listeners
		assertDoesNotThrow(() -> multiplexingListener.handleMessage(mockDeliveryContext, mockMessage));
	}

	@SuppressWarnings("unchecked")
	@Test
	void testHandleMessage_skipsMessageWithWrongPayloadType() {
		// given - add a sub-listener
		IMessageListener<String> mockListener = createStringMockListener();
		multiplexingListener.addListener(mockListener);

		// and - message with incompatible payload type (Integer instead of String)
		IMessage wrongMessage = mock(IMessage.class);
		when(wrongMessage.getPayload()).thenReturn(123);

		// when - handle message with wrong type
		multiplexingListener.handleMessage(mockDeliveryContext, wrongMessage);

		// then - listener should NOT have been called (message skipped)
		verify(mockListener, times(0)).handleMessage(wrongMessage);
	}

	@Test
	void testHandleMessage_callsListenersInOrder() {
		// given - track execution order
		java.util.List<Integer> executionOrder = new java.util.ArrayList<>();

		IMessageListener<String> listener1 = createOrderTrackingListener(executionOrder, 1);
		IMessageListener<String> listener2 = createOrderTrackingListener(executionOrder, 2);
		IMessageListener<String> listener3 = createOrderTrackingListener(executionOrder, 3);

		multiplexingListener.addListener(listener1);
		multiplexingListener.addListener(listener2);
		multiplexingListener.addListener(listener3);

		// when
		multiplexingListener.handleMessage(mockDeliveryContext, mockMessage);

		// then - listeners should have been called in order
		assertThat(executionOrder).containsExactly(1, 2, 3);
	}

	@Test
	void testHandleMessage_afterRemovingListener_doesNotCallRemovedListener() {
		// given
		IMessageListener<String> listener1 = createStringMockListener();
		IMessageListener<String> listener2 = createStringMockListener();

		multiplexingListener.addListener(listener1);
		multiplexingListener.addListener(listener2);
		multiplexingListener.removeListener(listener1);

		// when
		multiplexingListener.handleMessage(mockDeliveryContext, mockMessage);

		// then - only listener2 should have been called
		verify(listener1, times(0)).handleMessage(mockMessage);
		verify(listener2, times(1)).handleMessage(mockMessage);
	}

	@Test
	void testClose_closesAllAutoCloseableSubListeners() throws Exception {
		// given
		//noinspection unchecked
		ICloseableListener<String> closeable1 = mock(ICloseableListener.class);
		when(closeable1.getPayloadType()).thenReturn(String.class);
		//noinspection unchecked
		ICloseableListener<String> closeable2 = mock(ICloseableListener.class);
		when(closeable2.getPayloadType()).thenReturn(String.class);

		multiplexingListener.addListener(closeable1);
		multiplexingListener.addListener(closeable2);

		// when
		multiplexingListener.close();

		// then - both should have been closed
		verify(closeable1, times(1)).close();
		verify(closeable2, times(1)).close();
	}

	@Test
	void testClose_clearsSubListenersList() {
		// given
		IMessageListener<String> listener = createStringMockListener();
		multiplexingListener.addListener(listener);

		// when
		multiplexingListener.close();

		// then - listener list should be cleared
		assertThat(multiplexingListener.getSubListeners()).isEmpty();
	}

	@Test
	void testClose_setsClosedFlag() {
		// when
		multiplexingListener.close();

		// then - subsequent operations should throw BrokerListenerClosedException
		IMessageListener<String> listener = createStringMockListener();
		assertThatThrownBy(() -> multiplexingListener.addListener(listener))
			.isInstanceOf(BrokerListenerClosedException.class)
			.hasMessageContaining("Attempted to use a closed");
	}

	@Test
	void testHandleMessage_afterClose_throwsException() {
		// given
		multiplexingListener.close();

		// when/then
		assertThatThrownBy(() -> multiplexingListener.handleMessage(mockDeliveryContext, mockMessage))
			.isInstanceOf(BrokerListenerClosedException.class)
			.hasMessageContaining("Attempted to use a closed");
	}

	@Test
	void testAddListener_afterClose_throwsException() {
		// given
		multiplexingListener.close();
		IMessageListener<String> mockListener = createStringMockListener();

		// when/then
		assertThatThrownBy(() -> multiplexingListener.addListener(mockListener))
			.isInstanceOf(BrokerListenerClosedException.class)
			.hasMessageContaining("Attempted to use a closed");
	}

	@Test
	void testGetListenerOfTypeOrNull_findsListenerOfType() {
		// given
		TestListener listener1 = new TestListener();
		OtherTestListener listener2 = new OtherTestListener();

		multiplexingListener.addListener(listener1);
		multiplexingListener.addListener(listener2);

		// when
		TestListener found = multiplexingListener.getListenerOfTypeOrNull(TestListener.class);

		// then
		assertThat(found).isSameAs(listener1);
	}

	@Test
	void testGetListenerOfTypeOrNull_returnsNullWhenNotFound() {
		// given
		TestListener listener1 = new TestListener();
		multiplexingListener.addListener(listener1);

		// when
		OtherTestListener found = multiplexingListener.getListenerOfTypeOrNull(OtherTestListener.class);

		// then
		assertThat(found).isNull();
	}

	@Test
	void testGetListenerOfTypeOrNull_findsSubclass() {
		// given
		SubTestListener subListener = new SubTestListener();
		multiplexingListener.addListener(subListener);

		// when - search for parent class
		TestListener found = multiplexingListener.getListenerOfTypeOrNull(TestListener.class);

		// then - should find the subclass
		assertThat(found).isSameAs(subListener);
	}

	@Test
	void testClose_doesNotThrowWhenCloseableListenerThrowsException() {
		// given
		//noinspection unchecked
		ICloseableListener<String> failingCloseable = mock(ICloseableListener.class);
		when(failingCloseable.getPayloadType()).thenReturn(String.class);
		try {
			doThrow(new RuntimeException("Close failed")).when(failingCloseable).close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		multiplexingListener.addListener(failingCloseable);

		// when/then - should not propagate exception
		assertDoesNotThrow(() -> multiplexingListener.close());
	}

	// Helper methods and test classes

	private IMessageListener<String> createStringMockListener() {
		//noinspection unchecked
		IMessageListener<String> listener = mock(IMessageListener.class);
		when(listener.getPayloadType()).thenReturn(String.class);
		return listener;
	}

	interface ICloseableListener<T> extends IMessageListener<T>, AutoCloseable {}

	private IMessageListener<String> createOrderTrackingListener(java.util.List<Integer> executionOrder, int id) {
		return new IMessageListener<>() {
			@Override
			public void handleMessage(@NotNull IMessage<String> theMessage) {
				executionOrder.add(id);
			}

			@Override
			public Class<String> getPayloadType() {
				return String.class;
			}
		};
	}

	// Test listener classes for getListenerOfTypeOrNull tests

	private static class TestListener implements IMessageListener<String> {
		@Override
		public void handleMessage(@NotNull IMessage<String> theMessage) {
			// nothing
		}

		@Override
		public Class<String> getPayloadType() {
			return String.class;
		}
	}

	private static class SubTestListener extends TestListener {
	}

	private static class OtherTestListener implements IMessageListener<String> {
		@Override
		public void handleMessage(@NotNull IMessage<String> theMessage) {
			// nothing
		}

		@Override
		public Class<String> getPayloadType() {
			return String.class;
		}
	}
}