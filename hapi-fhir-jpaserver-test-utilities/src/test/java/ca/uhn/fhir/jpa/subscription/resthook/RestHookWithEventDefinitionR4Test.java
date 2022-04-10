package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.subscription.FhirR4Util;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.EventDefinition;
import org.hl7.fhir.r4.model.Expression;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.TriggerDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

/**
 * Ignored because this feature isn't implemented yet
 */
@Disabled
public class RestHookWithEventDefinitionR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookWithEventDefinitionR4Test.class);
	private static List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	private static List<String> ourHeaders = Collections.synchronizedList(new ArrayList<>());
	private static List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private String myPatientId;
	private String mySubscriptionId;
	private List<IIdType> mySubscriptionIds = Collections.synchronizedList(new ArrayList<>());

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
	}

	@AfterEach
	public void afterUnregisterRestHookListener() {
		for (IIdType next : mySubscriptionIds) {
			myClient.delete().resourceById(next).execute();
		}
		mySubscriptionIds.clear();

		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		myClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		myClient.delete().resourceConditionalByUrl("Observation?code:missing=false").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@Test
	public void testSubscriptionAddedTrigger() {
		/*
		 * Create patient
		 */

		Patient patient = FhirR4Util.getPatient();
		MethodOutcome methodOutcome = myClient.create().resource(patient).execute();
		myPatientId = methodOutcome.getId().getIdPart();

		/*
		 * Create EventDefinition
		 */

		EventDefinition eventDef = new EventDefinition();
		eventDef
			.setPurpose("Monitor all admissions to Emergency")
			.addTrigger(new TriggerDefinition()
				.setType(TriggerDefinition.TriggerType.DATAADDED)
				.setCondition(new Expression()
					.setDescription("Encounter Location = emergency (active/completed encounters, current or previous)")
					.setLanguage(Expression.ExpressionLanguage.TEXT_FHIRPATH.toCode())
					.setExpression("(this | %previous).location.where(location = 'Location/emergency' and status in {'active', 'completed'}).exists()")
				)
			);

		/*
		 * Create subscription
		 */
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.WEBSOCKET);
		channel.setPayload("application/json");
		subscription.setChannel(channel);

		methodOutcome = myClient.create().resource(subscription).execute();
		mySubscriptionId = methodOutcome.getId().getIdPart();

	}

	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerRestHookInterceptor();
	}

	@BeforeEach
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		ourHeaders.clear();
	}


}
