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

import org.hl7.fhir.instance.model.api.IIdType;

/**
 * IMessage implementations that deliver a FHIR Resource payload should implement this interface.
 */
public interface IResourceMessage extends IHasPayloadMessageKey {
	/**
	 * @param theResourceId of the resource contained in the payload
	 */
	void setPayloadId(IIdType theResourceId);

	/**
	 * This method is primarily used for logging
	 * @return the id of the resource contained in the payload
	 */
	String getPayloadId();

	/**
	 * In cases where the IMessage extracts the message key from the payload, this method can be used to set
	 * the message key that will be used.
	 *
	 * See {@link IMessage#getMessageKey()} and {@link IHasPayloadMessageKey#getPayloadMessageKey()}
	 * @param thePayloadMessageKey the message key that should be used for the message delivering this payload
	 */
	void setPayloadMessageKey(String thePayloadMessageKey);
}
