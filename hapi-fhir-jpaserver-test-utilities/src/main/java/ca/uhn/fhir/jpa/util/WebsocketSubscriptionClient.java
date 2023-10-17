/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ClientEndpoint
public class WebsocketSubscriptionClient implements AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(WebsocketSubscriptionClient.class);
	private final Supplier<RestfulServerExtension> myServerSupplier;
	private final Supplier<StorageSettings> myStorageSettings;
	private jakarta.websocket.Session mySession;
	private List<String> myMessages = new ArrayList<>();

	/**
	 * Constructor
	 */
	public WebsocketSubscriptionClient(
			Supplier<RestfulServerExtension> theServerSupplier, Supplier<StorageSettings> theStorageSettings) {
		assert theServerSupplier != null;
		assert theStorageSettings != null;

		myServerSupplier = theServerSupplier;
		myStorageSettings = theStorageSettings;
	}

	public void bind(String theSubscriptionId) {
		assert theSubscriptionId != null;
		assert !theSubscriptionId.contains("/");

		RestfulServerExtension server = myServerSupplier.get();
		assert server != null;

		try {
			URI echoUri = new URI("ws://localhost:" + server.getPort() + server.getWebsocketContextPath()
					+ myStorageSettings.get().getWebsocketContextPath());

			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			mySession = container.connectToServer(this, echoUri);
			ourLog.info("Connected to WS: {}", mySession.isOpen());
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		if (mySession != null) {
			ourLog.info("Shutting down websocket client");
			mySession.close();
		}
	}

	@OnMessage
	public void processMessageFromServer(String message, Session session) {
		myMessages.add(message);
	}

	public List<String> getMessages() {
		return myMessages;
	}
}
