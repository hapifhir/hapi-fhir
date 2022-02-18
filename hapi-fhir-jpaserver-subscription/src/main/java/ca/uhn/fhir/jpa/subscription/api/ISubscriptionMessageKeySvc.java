package ca.uhn.fhir.jpa.subscription.api;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

/**
 * This is used by "message" type subscriptions to provide a key to the message wrapper before submitting it to the channel
 */
public interface ISubscriptionMessageKeySvc {
	/**
	 *  Given an {@link IBaseResource}, return a key that can be used to identify the message. This key will be used to
	 *  partition the message into a queue.
	 *
	 * @param thePayloadResource the payload resource.
	 * @return the key or null.
	 */
	@Nullable
	String getMessageKeyOrNull(IBaseResource thePayloadResource);
}
