/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.subscription;

import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.UnsignedIntType;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;

public class SubscriptionTestDataHelper {
	public static final String TEST_TOPIC = "http://test.topic";
	public static final String TEST_FILTER1 = "Encounter?patient=Patient/123";
	public static final String TEST_FILTER2 = "Encounter?status=finished";
	public static final String TEST_ENDPOINT = "http://rest.endpoint/path";
	public static final String TEST_HEADER1 = "X-Foo: FOO";
	public static final String TEST_HEADER2 = "X-Bar: BAR";

	public static Subscription buildR4TopicSubscription() {
		return buildR4TopicSubscriptionWithContent("full-resource");
	}

	public static Subscription buildR4TopicSubscriptionWithContent(String theChannelPayloadContent) {
		Subscription subscription = new Subscription();

		// Standard R4 stuff
		subscription.getMeta().addTag("http://a", "b", "c");
		subscription.getMeta().addTag("http://d", "e", "f");
		subscription.setId("testId");
		subscription.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		subscription.getChannel().setEndpoint(TEST_ENDPOINT);
		subscription.getChannel().setPayload(CT_FHIR_JSON_NEW);
		subscription.getChannel().addHeader(TEST_HEADER1);
		subscription.getChannel().addHeader(TEST_HEADER2);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);

		// Subscription Topic Extensions:

		subscription.getMeta().addProfile(SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL);
		subscription.setCriteria(TEST_TOPIC);
		subscription
				.getCriteriaElement()
				.addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(TEST_FILTER1));
		subscription
				.getCriteriaElement()
				.addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(TEST_FILTER2));
		subscription
				.getChannel()
				.addExtension(
						SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_HEARTBEAT_PERIOD_URL,
						new UnsignedIntType(86400));
		subscription
				.getChannel()
				.addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_TIMEOUT_URL, new UnsignedIntType(60));
		subscription
				.getChannel()
				.addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_MAX_COUNT, new PositiveIntType(20));
		subscription
				.getChannel()
				.getPayloadElement()
				.addExtension(
						SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT,
						new CodeType(theChannelPayloadContent));

		return subscription;
	}
}
