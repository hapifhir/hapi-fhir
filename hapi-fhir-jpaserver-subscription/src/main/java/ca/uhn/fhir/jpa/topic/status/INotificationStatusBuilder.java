/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface INotificationStatusBuilder<T extends IBaseResource> {
	/**
	 * Build a notification status resource to include as the first element in a topic subscription notification bundle
	 * @param theResources The resources to include in the notification bundle.  It should _NOT_ include the
	 *                       notification status resource.  The first resource will be the "focus" resource.
	 * @param theActiveSubscription	The active subscription that triggered the notification
	 * @param theTopicUrl	The topic URL of the topic subscription
	 * @return the notification status resource.  The resource type varies depending on the FHIR version.
	 */
	T buildNotificationStatus(
			List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl);
}
