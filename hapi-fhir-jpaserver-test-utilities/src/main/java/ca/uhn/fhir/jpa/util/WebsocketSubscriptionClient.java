/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.SocketImplementation;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

@ClientEndpoint
public class WebsocketSubscriptionClient implements AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(WebsocketSubscriptionClient.class);
	private final Supplier<RestfulServerExtension> myServerSupplier;
	private final Supplier<SubscriptionSettings> mySubscriptionSettingsSupplier;
	private jakarta.websocket.Session mySession;
	private SocketImplementation mySocketImplementation;

	/**
	 * Constructor
	 */
	public WebsocketSubscriptionClient(
			Supplier<RestfulServerExtension> theServerSupplier, Supplier<SubscriptionSettings> theStorageSettings) {
		assert theServerSupplier != null;
		assert theStorageSettings != null;

		myServerSupplier = theServerSupplier;
		mySubscriptionSettingsSupplier = theStorageSettings;
	}

	public void bind(String theSubscriptionId) {
		assert theSubscriptionId != null;
		assert !theSubscriptionId.contains("/");

		RestfulServerExtension server = myServerSupplier.get();
		assert server != null;

		mySocketImplementation = new SocketImplementation(theSubscriptionId, EncodingEnum.JSON);

		try {
			URI echoUri = new URI("ws://localhost:" + server.getPort() + server.getWebsocketContextPath()
					+ mySubscriptionSettingsSupplier.get().getWebsocketContextPath());

			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			mySession = container.connectToServer(mySocketImplementation, echoUri);
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

	public List<String> getMessages() {
		return mySocketImplementation.getMessages();
	}
}
