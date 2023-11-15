/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.List;

public class SubscriptionTopicUtil {
	public static boolean matches(
			BaseResourceMessage.OperationTypeEnum theOperationType,
			List<Enumeration<SubscriptionTopic.InteractionTrigger>> theSupportedInteractions) {
		for (Enumeration<SubscriptionTopic.InteractionTrigger> next : theSupportedInteractions) {
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.CREATE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.CREATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.UPDATE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.UPDATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.DELETE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.DELETE) {
				return true;
			}
		}
		return false;
	}
}
