package ca.uhn.fhir.jpa.subscription.websocket;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.subscription.FhirR4Util;
import ca.uhn.fhir.jpa.subscription.SocketImplementation;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Adds a FHIR subscription with criteria through the rest interface. Then creates a websocket with the id of the
 * subscription
 * <p>
 * Note: This test only returns a ping with the subscription id, Check FhirSubscriptionWithSubscriptionIdR4Test for
 * a test that returns the xml of the observation
 * <p>
 * To execute the following test, execute it the following way:
 * 0. execute 'clean' test
 * 1. Execute the 'createPatient' test
 * 2. Update the patient id static variable
 * 3. Execute the 'createSubscription' test
 * 4. Update the subscription id static variable
 * 5. Execute the 'attachWebSocket' test
 * 6. Execute the 'sendObservation' test
 * 7. Look in the 'attachWebSocket' terminal execution and wait for your ping with the subscription id
 */
public class WebsocketWithSubscriptionIdR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(WebsocketWithSubscriptionIdR4Test.class);

	private String myPatientId;
	private String mySubscriptionId;
	private WebSocketClient myWebSocketClient;
	private SocketImplementation mySocketImplementation;

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@AfterEach
	public void afterCloseWebsocket() throws Exception {
		ourLog.info("Shutting down websocket client");
		myWebSocketClient.stop();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		mySubscriptionTestUtil.registerWebSocketInterceptor();

		/*
		 * Create patient
		 */

		Patient patient = FhirR4Util.getPatient();
		MethodOutcome methodOutcome = myClient.create().resource(patient).execute();
		myPatientId = methodOutcome.getId().getIdPart();

		/*
		 * Create subscription
		 */
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		// subscription.setCriteria("Observation?subject=Patient/" + PATIENT_ID);
		subscription.setCriteria("Observation?code=SNOMED-CT|82313006");

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.WEBSOCKET);
		channel.setPayload("application/json");
		subscription.setChannel(channel);

		methodOutcome = myClient.create().resource(subscription).execute();
		mySubscriptionId = methodOutcome.getId().getIdPart();

		/*
		 * Attach websocket
		 */

		myWebSocketClient = new WebSocketClient();
		mySocketImplementation = new SocketImplementation(mySubscriptionId, EncodingEnum.JSON);

		myWebSocketClient.start();
		URI echoUri = new URI("ws://localhost:" + ourPort + myModelConfig.getWebsocketContextPath());
		ClientUpgradeRequest request = new ClientUpgradeRequest();
		ourLog.info("Connecting to : {}", echoUri);
		Future<Session> connection = myWebSocketClient.connect(mySocketImplementation, echoUri, request);
		Session session = connection.get(10, TimeUnit.SECONDS);

		ourLog.info("Connected to WS: {}", session.isOpen());

		await().until(() -> mySubscriptionRegistry.size() == 1);
	}

	@Test
	public void createObservation() {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode("82313006");
		coding.setSystem("SNOMED-CT");
		Reference reference = new Reference();
		reference.setReference("Patient/" + myPatientId);
		observation.setSubject(reference);
		observation.setStatus(Observation.ObservationStatus.FINAL);

		MethodOutcome methodOutcome2 = myClient.create().resource(observation).execute();
		String observationId = methodOutcome2.getId().getIdPart();
		observation.setId(observationId);

		ourLog.info("Observation id generated by server is: " + observationId);

		ourLog.info("WS Messages: {}", mySocketImplementation.getMessages());
		waitForSize(2, mySocketImplementation.getMessages());
		assertThat(mySocketImplementation.getMessages(), contains("bound " + mySubscriptionId, "ping " + mySubscriptionId));
	}

	@Test
	public void createObservationThatDoesNotMatch() {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode("8231");
		coding.setSystem("SNOMED-CT");
		Reference reference = new Reference();
		reference.setReference("Patient/" + myPatientId);
		observation.setSubject(reference);
		observation.setStatus(Observation.ObservationStatus.FINAL);

		MethodOutcome methodOutcome2 = myClient.create().resource(observation).execute();
		String observationId = methodOutcome2.getId().getIdPart();
		observation.setId(observationId);

		ourLog.info("Observation id generated by server is: " + observationId);

		ourLog.info("WS Messages: {}", mySocketImplementation.getMessages());
		waitForSize(1, mySocketImplementation.getMessages());
		assertThat(mySocketImplementation.getMessages(), contains("bound " + mySubscriptionId));
	}
}
