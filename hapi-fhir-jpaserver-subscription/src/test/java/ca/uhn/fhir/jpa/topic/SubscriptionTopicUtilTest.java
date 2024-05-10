package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicUtilTest {

	private static final String TEST_CHANNEL_NAME = "TEST_CHANNEL";

	private final FhirContext myContext = FhirContext.forR5Cached();

	@Test
	public void testMatch() {
		// I know this is gross.  I haven't found a nicer way to do this
		var create = new Enumeration<>(new SubscriptionTopic.InteractionTriggerEnumFactory());
		create.setValue(SubscriptionTopic.InteractionTrigger.CREATE);
		var delete = new Enumeration<>(new SubscriptionTopic.InteractionTriggerEnumFactory());
		delete.setValue(SubscriptionTopic.InteractionTrigger.DELETE);

		List<Enumeration<SubscriptionTopic.InteractionTrigger>> supportedTypes = List.of(create, delete);

		assertTrue(SubscriptionTopicUtil.matches(BaseResourceMessage.OperationTypeEnum.CREATE, supportedTypes));
		assertFalse(SubscriptionTopicUtil.matches(BaseResourceMessage.OperationTypeEnum.UPDATE, supportedTypes));
		assertTrue(SubscriptionTopicUtil.matches(BaseResourceMessage.OperationTypeEnum.DELETE, supportedTypes));
		assertFalse(SubscriptionTopicUtil.matches(BaseResourceMessage.OperationTypeEnum.MANUALLY_TRIGGERED, supportedTypes));
		assertFalse(SubscriptionTopicUtil.matches(BaseResourceMessage.OperationTypeEnum.TRANSACTION, supportedTypes));
	}

	@Test
	public void testExtractResourceFromBundle_withCorrectBundle_returnsCorrectResource() {
		Patient patient = new Patient();
		patient.setId("Patient/1");
		Bundle bundle = buildSubscriptionStatus(patient);

		IBaseResource extractionResult = SubscriptionTopicUtil.extractResourceFromBundle(myContext, bundle);
		assertEquals(patient, extractionResult);
	}

	@Test
	public void testExtractResourceFromBundle_withoutReferenceResource_returnsNull() {
		Bundle bundle = buildSubscriptionStatus(null);

		IBaseResource extractionResult = SubscriptionTopicUtil.extractResourceFromBundle(myContext, bundle);
		assertNull(extractionResult);
	}

	private Bundle buildSubscriptionStatus(Resource theResource) {
		SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent event =
				subscriptionStatus.addNotificationEvent();
		Reference reference = new Reference();
		reference.setResource(theResource);
		event.setFocus(reference);

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(subscriptionStatus);
		bundle.addEntry().setResource(theResource);
		return bundle;
	}

	@Test
	public void testExtractResourceFromBundle_withoutNotificationEvent_returnsNull() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new SubscriptionStatus());

		IBaseResource extractionResult = SubscriptionTopicUtil.extractResourceFromBundle(myContext, bundle);
		assertNull(extractionResult);
	}

	@Test
	public void testExtractResourceFromBundle_withEmptyBundle_returnsNull() {
		IBaseResource extractionResult = SubscriptionTopicUtil.extractResourceFromBundle(myContext, new Bundle());
		assertNull(extractionResult);
	}

	@Test
	public void testIsEmptyContentTopicSubscription_withEmptySubscription_returnsFalse() {
		CanonicalSubscription canonicalSubscription = new CanonicalSubscription();
		boolean result = SubscriptionTopicUtil.isEmptyContentTopicSubscription(canonicalSubscription);

		assertFalse(result);
	}

	@ParameterizedTest
	@CsvSource({
			"full-resource, false",
			"id-only	  , false",
			"empty        , true",
			"             , false",
	})
	public void testIsEmptyContentTopicSubscription_withContentPayload_returnsExpectedResult(String thePayloadContent,
																							 boolean theExpectedResult) {
		CanonicalTopicSubscription canonicalTopicSubscription = new CanonicalTopicSubscription();
		canonicalTopicSubscription.setContent(Subscription.SubscriptionPayloadContent.fromCode(thePayloadContent));
		CanonicalSubscription canonicalSubscription = new CanonicalSubscription();
		canonicalSubscription.setTopicSubscription(canonicalTopicSubscription);
		canonicalSubscription.setTopicSubscription(true);

		boolean actualResult = SubscriptionTopicUtil.isEmptyContentTopicSubscription(canonicalSubscription);

		assertEquals(theExpectedResult, actualResult);
	}
}
