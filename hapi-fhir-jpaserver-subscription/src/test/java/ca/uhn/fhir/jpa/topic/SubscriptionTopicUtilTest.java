package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicUtilTest {
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

}
