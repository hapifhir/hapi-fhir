package ca.uhn.fhir.jpa.subscription.match.config;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClosedSessionTolerantPerConnectionWebSocketHandlerTest {

	private final ClosedSessionTolerantPerConnectionWebSocketHandler myHandler =
			new ClosedSessionTolerantPerConnectionWebSocketHandler(RecordingHandler.class);

	@Test
	void handleTransportError_afterTheConnectionClosed_isIgnored() throws Exception {
		// Jetty 12.1 reports an "Abnormal Close" error after the server closes with a non-normal code, by which time
		// the per-connection handler has been destroyed
		WebSocketSession session = mock(WebSocketSession.class);
		myHandler.afterConnectionEstablished(session);
		myHandler.afterConnectionClosed(session, CloseStatus.PROTOCOL_ERROR);
		when(session.isOpen()).thenReturn(false);

		assertThatNoException()
				.isThrownBy(() -> myHandler.handleTransportError(session, new IOException("Abnormal Close")));
	}

	@Test
	void handleTransportError_onAnOpenConnection_reachesTheHandler() throws Exception {
		WebSocketSession session = mock(WebSocketSession.class);
		when(session.isOpen()).thenReturn(true);
		myHandler.afterConnectionEstablished(session);
		IOException error = new IOException("broken pipe");

		assertThatThrownBy(() -> myHandler.handleTransportError(session, error)).isSameAs(error);
	}

	@Test
	void handleTransportError_onAnOpenConnectionWithNoHandler_stillFails() {
		WebSocketSession session = mock(WebSocketSession.class);
		when(session.isOpen()).thenReturn(true);

		assertThatThrownBy(() -> myHandler.handleTransportError(session, new IOException("x")))
				.isInstanceOf(IllegalStateException.class);
	}

	public static class RecordingHandler extends TextWebSocketHandler {
		@Override
		public void handleTransportError(@Nonnull WebSocketSession theSession, @Nonnull Throwable theException) throws Exception {
			assertThat(theException).isNotNull();
			throw (Exception) theException;
		}
	}
}
