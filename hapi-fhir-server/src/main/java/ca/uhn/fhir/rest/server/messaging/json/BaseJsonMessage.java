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
package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.messaging.IHasPayloadMessageKey;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.IMessageDeliveryContext;
import ca.uhn.fhir.rest.server.messaging.RequestPartitionHeaderUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

import static java.util.Objects.isNull;

public abstract class BaseJsonMessage<T> implements IMessage<T>, Message<T>, IModelJson, IMessageDeliveryContext {

	@JsonProperty("headers")
	private HapiMessageHeaders myHeaders;

	/**
	 * Constructor
	 */
	public BaseJsonMessage() {
		super();
		setDefaultRetryHeaders();
	}

	protected void setDefaultRetryHeaders() {
		HapiMessageHeaders messageHeaders = new HapiMessageHeaders();
		setHeaders(messageHeaders);
	}

	@Override
	@Nonnull
	public MessageHeaders getHeaders() {
		return getHapiHeaders().toMessageHeaders();
	}

	public HapiMessageHeaders getHapiHeaders() {
		if (isNull(myHeaders)) {
			setDefaultRetryHeaders();
		}
		return myHeaders;
	}

	public void setHeaders(HapiMessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}

	@Override
	@Nonnull
	public String getMessageKey() {
		T payload = getPayload();
		if (payload instanceof IHasPayloadMessageKey) {
			String payloadMessageKey = ((IHasPayloadMessageKey) payload).getPayloadMessageKey();
			if (payloadMessageKey != null) {
				return payloadMessageKey;
			}
		}
		return IMessage.super.getMessageKey();
	}

	@Override
	public int getRetryCount() {
		return getHapiHeaders().getRetryCount();
	}

	public T getPayloadWithRequestPartitionIdSetFromHeader(IDefaultPartitionSettings theDefaultPartitionSettings) {
		RequestPartitionHeaderUtil.setRequestPartitionIdFromHeaderIfNotAlreadySet(this, theDefaultPartitionSettings);
		return getPayload();
	}

	public static <P> void addCustomHeaders(IMessage<P> theMessage, Map<String, ?> theCustomHeaders) {
		if (theMessage instanceof BaseJsonMessage<P> baseJsonMessage) {
			theCustomHeaders.forEach(
					(key, value) -> baseJsonMessage.getHapiHeaders().addCustomHeader(key, value));
		}
	}
}
