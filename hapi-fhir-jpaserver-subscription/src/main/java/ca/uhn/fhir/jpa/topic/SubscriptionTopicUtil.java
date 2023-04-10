package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.List;

public class SubscriptionTopicUtil {
	public static boolean matches(BaseResourceMessage.OperationTypeEnum theOperationType, List<Enumeration<SubscriptionTopic.InteractionTrigger>> theSupportedInteractions) {
		for (Enumeration<SubscriptionTopic.InteractionTrigger> next : theSupportedInteractions) {
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.CREATE && theOperationType == BaseResourceMessage.OperationTypeEnum.CREATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.UPDATE && theOperationType == BaseResourceMessage.OperationTypeEnum.UPDATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.DELETE && theOperationType == BaseResourceMessage.OperationTypeEnum.DELETE) {
				return true;
			}
		}
		return false;
	}
}
