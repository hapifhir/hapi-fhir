/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.messaging;

/**
 * If a Message Listener registered with a Channel Consumer is retry aware, then the Channel Consumer can
 * pass an instance of this interface to provide delivery context details to the listener. For example,
 * some listeners may want to handle a request differently if it is being redelivered because the first delivery failed.
 */
public interface IMessageDeliveryContext {
	/**
	 * @return the number of retries for this message delivery. The first delivery has a retry count of 0.
	 */
	int getRetryCount();

	// TODO KHS lastExceptionType could potentially be useful here as well
}
