/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription.match.config;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

/**
 * A {@link PerConnectionWebSocketHandler} that ignores transport errors reported after the connection closed.
 * <p>
 * When the server closes a connection with a non-normal code (for example {@code PROTOCOL_ERROR} on a bad bind),
 * Jetty 12.1 reports an "Abnormal Close" error once the close completes. By then Spring has already destroyed the
 * connection's handler, so passing the error on fails with "WebSocketHandler not found" and is logged as an ERROR.
 * There is nothing left to handle it, so it is logged at debug instead.
 */
public class ClosedSessionTolerantPerConnectionWebSocketHandler extends PerConnectionWebSocketHandler {
	private static final Logger ourLog =
			LoggerFactory.getLogger(ClosedSessionTolerantPerConnectionWebSocketHandler.class);

	public ClosedSessionTolerantPerConnectionWebSocketHandler(Class<? extends WebSocketHandler> theHandlerType) {
		super(theHandlerType);
	}

	@Override
	public void handleTransportError(WebSocketSession theSession, @Nonnull Throwable theException) throws Exception {
		if (!theSession.isOpen()) {
			ourLog.debug(
					"Ignoring transport error on closed session {}: {}", theSession.getId(), theException.toString());
			return;
		}
		super.handleTransportError(theSession, theException);
	}
}
