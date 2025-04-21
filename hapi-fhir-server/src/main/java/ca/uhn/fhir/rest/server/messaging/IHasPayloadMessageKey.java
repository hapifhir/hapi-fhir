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

import jakarta.annotation.Nullable;

/**
 * Some broker implementations require a message key. An IMessage implementation can implement this interface if it is possible to derive a message
 * key from the message payload.
 */
public interface IHasPayloadMessageKey {
	/**
	 * @return a message key derived from the payload
	 */
	@Nullable
	default String getPayloadMessageKey() {
		return null;
	}
}
