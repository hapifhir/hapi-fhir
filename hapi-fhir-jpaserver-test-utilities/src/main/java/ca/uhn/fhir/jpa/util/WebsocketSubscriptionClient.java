package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
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
	private final Supplier<ModelConfig> myModelConfig;
	private WebSocketClient myWebSocketClient;
	private SocketImplementation mySocketImplementation;

	/**
	 * Constructor
	 */
	public WebsocketSubscriptionClient(Supplier<RestfulServerExtension> theServerSupplier, Supplier<ModelConfig> theModelConfig) {
		assert theServerSupplier != null;
		assert theModelConfig != null;

		myServerSupplier = theServerSupplier;
		myModelConfig = theModelConfig;
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
			URI echoUri = new URI("ws://localhost:" + server.getPort() + server.getWebsocketContextPath() + myModelConfig.get().getWebsocketContextPath());
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
