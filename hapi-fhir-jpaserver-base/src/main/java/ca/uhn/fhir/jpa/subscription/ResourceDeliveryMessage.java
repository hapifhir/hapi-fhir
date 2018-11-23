package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import com.fasterxml.jackson.annotation.*;
import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ResourceDeliveryMessage {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private transient CanonicalSubscription mySubscription;
	@JsonProperty("subscription")
	private String mySubscriptionString;
	@JsonProperty("payload")
	private String myPayloadString;
	@JsonIgnore
	private transient IBaseResource myPayload;
	@JsonProperty("payloadId")
	private String myPayloadId;
	@JsonProperty("operationType")
	private ResourceModifiedMessage.OperationTypeEnum myOperationType;

	public ResourceModifiedMessage.OperationTypeEnum getOperationType() {
		return myOperationType;
	}

	/**
	 * Constructor
	 */
	public ResourceDeliveryMessage() {
		super();
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

	public IIdType getPayloadId(FhirContext theCtx) {
		IIdType retVal = null;
		if (myPayloadId != null) {
			retVal = theCtx.getVersion().newIdType().setValue(myPayloadId);
		}
		return retVal;
	}

	public CanonicalSubscription getSubscription() {
		if (mySubscription == null && mySubscriptionString != null) {
			mySubscription = new Gson().fromJson(mySubscriptionString, CanonicalSubscription.class);
		}
		return mySubscription;
	}

	public void setSubscription(CanonicalSubscription theSubscription) {
		mySubscription = theSubscription;
		if (mySubscription != null) {
			mySubscriptionString = new Gson().toJson(mySubscription);
		}
	}

	public void setPayload(FhirContext theCtx, IBaseResource thePayload) {
		myPayload = thePayload;
		myPayloadString = theCtx.newJsonParser().encodeResourceToString(thePayload);
	}

	public void setPayloadId(IIdType thePayloadId) {
		myPayloadId = null;
		if (thePayloadId != null) {
			myPayloadId = thePayloadId.getValue();
		}
	}

}
