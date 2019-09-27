package ca.uhn.fhir.jpa.subscription.module.subscriber;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("WeakerAccess")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ResourceDeliveryMessage extends BaseResourceMessage implements IResourceMessage {

	@JsonProperty("canonicalSubscription")
	private CanonicalSubscription mySubscription;
	@JsonProperty("resource")
	private IBaseResource myResource;
	@JsonProperty("resourceId")
	private String myResourceId;
	@JsonIgnore()
	private String myResourceString;
	@JsonIgnore()
	private EncodingEnum myResourceEncoding;
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

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public void setSubscription(CanonicalSubscription theSubscription) {
		mySubscription = theSubscription;
	}

	public IBaseResource getResource() { return myResource; }

	public void setResource(IBaseResource theResource, EncodingEnum theEncoding, FhirContext theFhirContext) {
		myResource = theResource;

		if (theEncoding != null && theFhirContext != null) {
			String resourceString = theEncoding.newParser(theFhirContext).encodeResourceToString(theResource);
			myResourceString = resourceString;
			myResourceEncoding = theEncoding;
			myResourceId = theResource.getIdElement().toUnqualified().getValue();
		}
	}

	public String getResourceString() { return myResourceString; }

	@Override
	public String getResourceId() {
		return this.myResourceId;
	}

	public IIdType getResourceId(FhirContext theCtx) {
		IIdType retVal = null;
		if (this.myResourceId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(this.myResourceId);
		}
		return retVal;
	}

	public EncodingEnum getResourceEncoding() { return myResourceEncoding; }

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("mySubscription", mySubscription)
//			.append("mySubscriptionString", mySubscriptionString)
			.append("myPayloadString", myResourceString)
			.append("myPayload", myResource)
			.append("myPayloadId", myResourceId)
			.append("myOperationType", myOperationType)
			.toString();
	}

	/**
	 * Helper method to fetch the subscription ID
	 */
	public String getSubscriptionId(FhirContext theFhirContext) {
		String retVal = null;
		if (getSubscription() != null) {
			retVal = getSubscription().getIdElement(theFhirContext).getValue();
		}
		return retVal;
	}
}
