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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseResourceModifiedMessage extends BaseResourceMessage implements IResourceMessage, IModelJson {

	@JsonProperty("payload")
	protected String myPayload;

	@JsonProperty("payloadId")
	protected String myPayloadId;

	@JsonProperty(value = "partitionId")
	protected RequestPartitionId myPartitionId;

	@JsonProperty(value = "payloadVersion")
	protected String myPayloadVersion;

	@JsonIgnore
	protected transient IBaseResource myPayloadDecoded;

	@JsonIgnore
	protected transient String myPayloadType;

	/**
	 * Constructor
	 */
	public BaseResourceModifiedMessage() {
		super();
	}

	public BaseResourceModifiedMessage(IIdType theIdType, OperationTypeEnum theOperationType) {
		this();
		setOperationType(theOperationType);
		setPayloadId(theIdType);
	}

	public BaseResourceModifiedMessage(
			FhirContext theFhirContext, IBaseResource theResource, OperationTypeEnum theOperationType) {
		this();
		setOperationType(theOperationType);
		setNewPayload(theFhirContext, theResource);
	}

	public BaseResourceModifiedMessage(
			FhirContext theFhirContext,
			IBaseResource theNewResource,
			OperationTypeEnum theOperationType,
			RequestDetails theRequest) {
		this(theFhirContext, theNewResource, theOperationType);
		if (theRequest != null) {
			setTransactionId(theRequest.getTransactionGuid());
		}
	}

	public BaseResourceModifiedMessage(
			FhirContext theFhirContext,
			IBaseResource theNewResource,
			OperationTypeEnum theOperationType,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {
		this(theFhirContext, theNewResource, theOperationType);
		if (theRequest != null) {
			setTransactionId(theRequest.getTransactionGuid());
		}
		myPartitionId = theRequestPartitionId;
	}

	@Override
	public String getPayloadId() {
		return myPayloadId;
	}

	public String getPayloadVersion() {
		return myPayloadVersion;
	}

	/**
	 * @since 5.6.0
	 */
	public void setPayloadId(IIdType thePayloadId) {
		myPayloadId = null;
		if (thePayloadId != null) {
			myPayloadId = thePayloadId.toUnqualifiedVersionless().getValue();
			myPayloadVersion = thePayloadId.getVersionIdPart();
		}
	}

	/**
	 * @deprecated Use {@link #getPayloadId()} instead. Deprecated in 5.6.0 / 2021-10-27
	 */
	public String getId() {
		return myPayloadId;
	}

	/**
	 * @deprecated Use {@link #setPayloadId(IIdType)}. Deprecated in 5.6.0 / 2021-10-27
	 */
	@Deprecated
	public void setId(IIdType theId) {
		setPayloadId(theId);
	}

	/**
	 * @deprecated Use {@link #getPayloadId(FhirContext)}. Deprecated in 5.6.0 / 2021-10-27
	 */
	public IIdType getId(FhirContext theCtx) {
		return getPayloadId(theCtx);
	}

	/**
	 * @since 5.6.0
	 */
	public IIdType getPayloadId(FhirContext theCtx) {
		IIdType retVal = null;

		if (myPayloadId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myPayloadId).withVersion(myPayloadVersion);
		}

		return retVal;
	}

	@Nullable
	public IBaseResource getNewPayload(FhirContext theCtx) {
		if (myPayloadDecoded == null && isNotBlank(myPayload)) {
			myPayloadDecoded = theCtx.newJsonParser().parseResource(myPayload);
		}
		return myPayloadDecoded;
	}

	@Nullable
	public IBaseResource getPayload(FhirContext theCtx) {
		IBaseResource retVal = myPayloadDecoded;
		if (retVal == null && isNotBlank(myPayload)) {
			IParser parser = EncodingEnum.detectEncoding(myPayload).newParser(theCtx);
			retVal = parser.parseResource(myPayload);
			myPayloadDecoded = retVal;
		}
		return retVal;
	}

	@Nonnull
	public String getPayloadString() {
		if (this.myPayload != null) {
			return this.myPayload;
		}

		return "";
	}

	public void setNewPayload(FhirContext theCtx, IBaseResource thePayload) {
		/*
		 * References with placeholders would be invalid by the time we get here, and
		 * would be caught before we even get here. This check is basically a last-ditch
		 * effort to make sure nothing has broken in the various safeguards that
		 * should prevent this from happening (hence it only being an assert as
		 * opposed to something executed all the time).
		 */
		assert payloadContainsNoPlaceholderReferences(theCtx, thePayload);

		/*
		 * Note: Don't set myPayloadDecoded in here- This is a false optimization since
		 * it doesn't actually get used if anyone is doing subscriptions at any
		 * scale using a queue engine, and not going through the serialize/deserialize
		 * as we would in a queue engine can mask bugs.
		 * -JA
		 */
		myPayload = theCtx.newJsonParser().encodeResourceToString(thePayload);

		setPayloadIdFromPayload(theCtx, thePayload);
	}

	private void setPayloadIdFromPayload(FhirContext theCtx, IBaseResource thePayload) {
		IIdType payloadIdType = thePayload.getIdElement().toUnqualified();
		if (!payloadIdType.hasResourceType()) {
			String resourceType = theCtx.getResourceType(thePayload);
			payloadIdType = payloadIdType.withResourceType(resourceType);
		}

		setPayloadId(payloadIdType);
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("operationType", myOperationType)
				.append("partitionId", myPartitionId)
				.append("payloadId", myPayloadId)
				.toString();
	}

	protected static boolean payloadContainsNoPlaceholderReferences(FhirContext theCtx, IBaseResource theNewPayload) {
		List<ResourceReferenceInfo> refs = theCtx.newTerser().getAllResourceReferences(theNewPayload);
		for (ResourceReferenceInfo next : refs) {
			String ref = next.getResourceReference().getReferenceElement().getValue();
			if (isBlank(ref)) {
				IBaseResource resource = next.getResourceReference().getResource();
				if (resource != null) {
					ref = resource.getIdElement().getValue();
				}
			}
			if (isNotBlank(ref)) {
				if (ref.startsWith("#")) {
					continue;
				}
				if (ref.startsWith("urn:uuid:")) {
					throw new AssertionError(Msg.code(320) + "Reference at " + next.getName() + " is invalid: " + ref);
				}
			}
		}
		return true;
	}

	@Nullable
	@Override
	public String getMessageKeyOrDefault() {
		return StringUtils.defaultString(super.getMessageKeyOrNull(), myPayloadId);
	}

	public boolean hasPayloadType(FhirContext theFhirContext, @Nonnull String theResourceName) {
		if (myPayloadType == null) {
			myPayloadType = getPayloadType(theFhirContext);
		}
		return theResourceName.equals(myPayloadType);
	}

	@Nullable
	public String getPayloadType(FhirContext theFhirContext) {
		String retval = null;
		IIdType payloadId = getPayloadId(theFhirContext);
		if (payloadId != null) {
			retval = payloadId.getResourceType();
		}
		if (isBlank(retval)) {
			IBaseResource payload = getNewPayload(theFhirContext);
			if (payload != null) {
				retval = theFhirContext.getResourceType(payload);
			}
		}
		return retval;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		if (!super.equals(theO)) return false;
		BaseResourceModifiedMessage that = (BaseResourceModifiedMessage) theO;
		return Objects.equals(myPayload, that.myPayload) && Objects.equals(getPayloadId(), that.getPayloadId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), myPayload, getPayloadId());
	}
}
