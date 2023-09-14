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

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.SocketImplementation;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class WebsocketSubscriptionClient implements AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(WebsocketSubscriptionClient.class);
	private final Supplier<RestfulServerExtension> myServerSupplier;
	private final Supplier<StorageSettings> myStorageSettings;
	private WebSocketClient myWebSocketClient;
	private SocketImplementation mySocketImplementation;

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

		myWebSocketClient = new WebSocketClient();
		mySocketImplementation = new SocketImplementation(theSubscriptionId, EncodingEnum.JSON);

		try {
			myWebSocketClient.start();
			URI echoUri = new URI("ws://localhost:" + server.getPort() + server.getWebsocketContextPath()
					+ myStorageSettings.get().getWebsocketContextPath());
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			ourLog.info("Connecting to : {}", echoUri);

			Future<Session> connection;
			connection = myWebSocketClient.connect(mySocketImplementation, echoUri, request);
			Session session = connection.get(20, TimeUnit.SECONDS);
			ourLog.info("Connected to WS: {}", session.isOpen());
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		if (myWebSocketClient != null) {
			ourLog.info("Shutting down websocket client");
			myWebSocketClient.stop();
		}
	}

	public List<String> getMessages() {
		return mySocketImplementation.getMessages();
	}
}
