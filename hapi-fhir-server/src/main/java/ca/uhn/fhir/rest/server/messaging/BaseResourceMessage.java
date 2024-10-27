/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.messaging;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;

@SuppressWarnings("WeakerAccess")
public abstract class BaseResourceMessage implements IResourceMessage, IModelJson {

	@JsonProperty("operationType")
	protected BaseResourceModifiedMessage.OperationTypeEnum myOperationType;

	@JsonProperty("attributes")
	private Map<String, String> myAttributes;

	@JsonProperty("transactionId")
	private String myTransactionId;

	@JsonProperty("mediaType")
	private String myMediaType;

	/**
	 * This is used by any message going to kafka for topic partition selection purposes.
	 */
	@JsonProperty("messageKey")
	private String myMessageKey;

	/**
	 * Returns an attribute stored in this message.
	 * <p>
	 * Attributes are just a spot for user data of any kind to be
	 * added to the message for pasing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 * <p>
	 * Note that messages are designed to be passed into queueing systems
	 * and serialized as JSON. As a result, only strings are currently allowed
	 * as values.
	 * </p>
	 */
	public Optional<String> getAttribute(String theKey) {
		Validate.notBlank(theKey);
		if (myAttributes == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(myAttributes.get(theKey));
	}

	/**
	 * Sets an attribute stored in this message.
	 * <p>
	 * Attributes are just a spot for user data of any kind to be
	 * added to the message for passing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 * <p>
	 * Note that messages are designed to be passed into queueing systems
	 * and serialized as JSON. As a result, only strings are currently allowed
	 * as values.
	 * </p>
	 *
	 * @param theKey   The key (must not be null or blank)
	 * @param theValue The value (must not be null)
	 */
	public void setAttribute(String theKey, String theValue) {
		Validate.notBlank(theKey);
		Validate.notNull(theValue);
		if (myAttributes == null) {
			myAttributes = new HashMap<>();
		}
		myAttributes.put(theKey, theValue);
	}

	/**
	 * Copies any attributes from the given message into this messsage.
	 *
	 * @see #setAttribute(String, String)
	 * @see #getAttribute(String)
	 */
	public void copyAdditionalPropertiesFrom(BaseResourceMessage theMsg) {
		if (theMsg.myAttributes != null) {
			if (myAttributes == null) {
				myAttributes = new HashMap<>();
			}
			myAttributes.putAll(theMsg.myAttributes);
		}
	}

	/**
	 * Returns the {@link OperationTypeEnum} that is occurring to the Resource of the message
	 *
	 * @return the operation type.
	 */
	public BaseResourceModifiedMessage.OperationTypeEnum getOperationType() {
		return myOperationType;
	}

	/**
	 * Sets the {@link OperationTypeEnum} occuring to the resource of the message.
	 *
	 * @param theOperationType The operation type to set.
	 */
	public void setOperationType(BaseResourceModifiedMessage.OperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	/**
	 * Retrieve the transaction ID related to this message.
	 *
	 * @return the transaction ID, or null.
	 */
	@Nullable
	public String getTransactionId() {
		return myTransactionId;
	}

	/**
	 * Adds a transaction ID to this message. This ID can be used for many purposes. For example, performing tracing
	 * across asynchronous hooks, tying data together, or downstream logging purposes.
	 * <p>
	 * One current internal implementation uses this field to tie back MDM processing results (which are asynchronous)
	 * to the original transaction log that caused the MDM processing to occur.
	 *
	 * @param theTransactionId An ID representing a transaction of relevance to this message.
	 */
	public void setTransactionId(String theTransactionId) {
		myTransactionId = theTransactionId;
	}

	public String getMediaType() {
		return myMediaType;
	}

	public void setMediaType(String theMediaType) {
		myMediaType = theMediaType;
	}

	@Deprecated
	@Nullable
	public String getMessageKeyOrNull() {
		return getMessageKey();
	}

	@Nullable
	public String getMessageKey() {
		return myMessageKey;
	}

	public void setMessageKey(String theMessageKey) {
		myMessageKey = theMessageKey;
	}

	/**
	 * Returns {@link #getMessageKey()} or {@link #getMessageKeyDefaultValue()} when {@link #getMessageKey()} returns <code>null</code>.
	 *
	 * @return the message key value or default
	 */
	@Nullable
	public String getMessageKeyOrDefault() {
		return defaultString(getMessageKey(), getMessageKeyDefaultValue());
	}

	/**
	 * Provides a fallback value when method {@link #getMessageKey()} returns <code>null</code>.
	 *
	 * @return null by default
	 */
	@Nullable
	protected String getMessageKeyDefaultValue() {
		return null;
	}

	public enum OperationTypeEnum {
		CREATE(RestOperationTypeEnum.CREATE),
		UPDATE(RestOperationTypeEnum.UPDATE),
		DELETE(RestOperationTypeEnum.DELETE),
		MANUALLY_TRIGGERED(RestOperationTypeEnum.UPDATE),
		TRANSACTION(RestOperationTypeEnum.UPDATE);

		private final RestOperationTypeEnum myRestOperationTypeEnum;

		OperationTypeEnum(RestOperationTypeEnum theRestOperationTypeEnum) {
			myRestOperationTypeEnum = theRestOperationTypeEnum;
		}

		public static OperationTypeEnum from(RestOperationTypeEnum theRestOperationType) {
			switch (theRestOperationType) {
				case CREATE:
					return CREATE;
				case UPDATE:
					return UPDATE;
				case DELETE:
					return DELETE;
				default:
					throw new IllegalArgumentException(
							Msg.code(2348) + "Unsupported operation type: " + theRestOperationType);
			}
		}

		public RestOperationTypeEnum asRestOperationType() {
			return myRestOperationTypeEnum;
		}
	}

	@VisibleForTesting
	public Map<String, String> getAttributes() {
		return ObjectUtils.defaultIfNull(myAttributes, Collections.emptyMap());
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		BaseResourceMessage that = (BaseResourceMessage) theO;
		return getOperationType() == that.getOperationType()
				&& Objects.equals(getAttributes(), that.getAttributes())
				&& Objects.equals(getTransactionId(), that.getTransactionId())
				&& Objects.equals(getMediaType(), that.getMediaType())
				&& Objects.equals(getMessageKey(), that.getMessageKey());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getOperationType(), getAttributes(), getTransactionId(), getMediaType());
	}
}
