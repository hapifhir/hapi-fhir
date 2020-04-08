package ca.uhn.fhir.jpa.subscription.model;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("WeakerAccess")
public class ResourceDeliveryMessage extends BaseResourceMessage implements IResourceMessage {

	@JsonProperty("canonicalSubscription")
	private CanonicalSubscription mySubscription;
	@JsonProperty("payload")
	private String myPayloadString;
	@JsonIgnore
	private transient IBaseResource myPayload;
	@JsonProperty("payloadId")
	private String myPayloadId;
	@JsonProperty("operationType")
	private ResourceModifiedMessage.OperationTypeEnum myOperationType;

	/**
	 * Constructor
	 */
	public ResourceDeliveryMessage() {
		super();
	}

	public ResourceModifiedMessage.OperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public IBaseResource getPayload(FhirContext theCtx) {
		IBaseResource retVal = myPayload;
		if (retVal == null && isNotBlank(myPayloadString)) {
			retVal = theCtx.newJsonParser().parseResource(myPayloadString);
			myPayload = retVal;
		}
		return retVal;
	}

	public String getPayloadString() {
		if (this.myPayloadString != null) {
			return this.myPayloadString;
		}

		return "";
	}

	public IIdType getPayloadId(FhirContext theCtx) {
		IIdType retVal = null;
		if (myPayloadId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myPayloadId);
		}
		return retVal;
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public void setSubscription(CanonicalSubscription theSubscription) {
		mySubscription = theSubscription;
	}

	public void setPayload(FhirContext theCtx, IBaseResource thePayload, EncodingEnum theEncoding) {
		myPayload = thePayload;
		myPayloadString = theEncoding.newParser(theCtx).encodeResourceToString(thePayload);
		myPayloadId = thePayload.getIdElement().toUnqualified().getValue();
	}

	@Override
	public String getPayloadId() {
		return myPayloadId;
	}

	public void setPayloadId(IIdType thePayloadId) {
		myPayloadId = null;
		if (thePayloadId != null) {
			myPayloadId = thePayloadId.getValue();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("mySubscription", mySubscription)
			.append("myPayloadString", myPayloadString)
			.append("myPayload", myPayload)
			.append("myPayloadId", myPayloadId)
			.append("myOperationType", myOperationType)
			.toString();
	}

	/**
	 * Helper method to fetch the subscription ID
	 */
	public String getSubscriptionId(FhirContext theFhirContext) {
		String retVal = null;
		if (getSubscription() != null) {
			IIdType idElement = getSubscription().getIdElement(theFhirContext);
			if (idElement != null) {
				retVal = idElement.getValue();
			}
		}
		return retVal;
	}
}
